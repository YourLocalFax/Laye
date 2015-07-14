/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Sekai Kyoretsuna
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.fudev.laye.lex;

import java.io.IOException;
import java.io.InputStream;

import net.fudev.laye.LayeFile;
import net.fudev.laye.debug.Console;
import net.fudev.laye.struct.Identifier;
import net.fudev.laye.struct.Keyword;

/**
 * @author Sekai Kyoretsuna
 */
public class Lexer
{
   private final Console console;
   
   private InputStream input = null;
   
   private StringBuilder builder = new StringBuilder();
   private char currentChar = '\u0000';
   
   private int line = 1, column = 1;
   private boolean eof;
   
   public Lexer(Console console)
   {
      this.console = console;
   }
   
   public TokenStream getTokens(LayeFile file) throws IOException
   {
      input = file.read();
      readChar();
      TokenStream result = new TokenStream(file, console);
      Token token;
      while ((token = lex()) != null)
      {
         result.addToken(token);
      }
      input.close();
      return result;
   }
   
   private String getTempString()
   {
      String result = builder.toString();
      builder.setLength(0);
      builder.trimToSize();
      return result;
   }
   
   private boolean putChar()
   {
      builder.append(currentChar);
      return readChar();
   }
   
   private void putChar(char c)
   {
      builder.append(c);
   }
   
   private boolean readChar()
   {
      int next;
      try
      {
         next = input.read();
      }
      catch (IOException e)
      {
         eof = true;
         return false;
      }
      if (next == -1)
      {
         eof = true;
         return false;
      }
      currentChar = (char) next;
      if (currentChar == '\n')
      {
         line++;
         column = 1;
      }
      else
      {
         // TODO check other characters that don't have a width
         switch (currentChar)
         {
            case '\r':
               break;
            default:
               column++;
         }
      }
      return true;
   }
   
   private Token lex()
   {
      while (!eof)
      {
         if (Character.isWhitespace(currentChar))
         {
            readChar();
            continue;
         }
         switch (currentChar)
         {
            case '(':
               readChar();
               return new Token(Token.Type.OPEN_BRACE, line, column);
            case ')':
               readChar();
               return new Token(Token.Type.CLOSE_BRACE, line, column);
            case '[':
               readChar();
               return new Token(Token.Type.OPEN_SQUARE_BRACE, line, column);
            case ']':
               readChar();
               return new Token(Token.Type.CLOSE_SQUARE_BRACE, line, column);
            case '{':
               readChar();
               return new Token(Token.Type.OPEN_CURLY_BRACE, line, column);
            case '}':
               readChar();
               return new Token(Token.Type.CLOSE_CURLY_BRACE, line, column);
            case ';':
               readChar();
               return new Token(Token.Type.SEMI_COLON, line, column);
            case ':':
               readChar();
               return new Token(Token.Type.COLON, line, column);
            case ',':
               readChar();
               return new Token(Token.Type.COMMA, line, column);
            case '\'':
            case '"':
               return lexStringLiteral();
            default:
               if (Character.isDigit(currentChar))
               {
                  return lexNumericToken();
               }
               return lexOtherTokens();
         }
      }
      
      return null;
   }
   
   private Token lexStringLiteral()
   {
      final int startLine = line;
      final int startColumn = column;
      final char quoteChar = currentChar;
      // Read quote
      readChar();
      while (currentChar != quoteChar)
      {
         if (currentChar == '\\')
         {
            putChar(lexEscapedCharacter());
         }
         else
         {
            putChar();
         }
      }
      // Read quote
      readChar();
      String result = getTempString();
      return new Token(Token.Type.STRING_LITERAL, result, startLine, startColumn);
   }
   
   private char lexEscapedCharacter()
   {
      // Read '\'
      readChar();
      switch (currentChar)
      {
         case 'u':
         {
            readChar();
            final StringBuilder sb = new StringBuilder();
            int idx = 0;
            for (; !eof && Character.isDigit(currentChar); idx++)
            {
               putChar();
            }
            if (idx > 4)
            {
               console.error("at most 4 digits are expected when defining a unicode char, " + idx
                     + " given.");
               return '\u0000';
            }
            return (char) Integer.parseInt(sb.toString());
         }
         case 'r':
            readChar();
            return '\r';
         case 'n':
            readChar();
            return '\n';
         case 't':
            readChar();
            return '\t';
         case '0':
            readChar();
            return '\0';
         case '"':
            readChar();
            return '\"';
         case '\'':
            readChar();
            return '\'';
         case '\\':
            readChar();
            return '\\';
         default:
            console.error("escape character " + currentChar + " not recognized.");
            return '\u0000';
      }
   }
   
   private Token lexNumericToken()
   {
      final int startColumn = column;
      // TODO this only handles decimal integers. Needs binary, octal, hex, fp, and sci-note.
      // TODO maybe also add unicode? 0uXXXX...?
      char lastChar;
      do
      {
         lastChar = currentChar;
         putChar();
      }
      while (Character.isDigit(currentChar) || currentChar == '_');
      if (lastChar == '_')
      {
         console.error("(Parser) line " + line + " (column " + column + 
               "): numbers cannot end with '_'.");
      }
      String result = getTempString();
      return new Token(Token.Type.INT_LITERAL, Long.parseLong(result), line, startColumn);
   }
   
   private Token lexOtherTokens()
   {
      final int startColumn = column;
      if (!Identifier.isValidIdentifierStart(currentChar))
      {
         console.error("(Lexer) token " + currentChar + " is not a valid identifier start.");
         return null;
      }
      do
      {
         putChar();
      }
      while (Identifier.isValidIdentifierPart(currentChar));
      String image = getTempString();
      if (image.equals("_"))
      {
         return new Token(Token.Type.WILDCARD, line, startColumn);
      }
      else if (Keyword.isKeyword(image))
      {
         return new Token(Token.Type.KEYWORD, Keyword.getKeyword(image), line, startColumn);
      }
      return new Token(Token.Type.IDENTIFIER, Identifier.get(image), line, startColumn);
   }
}
