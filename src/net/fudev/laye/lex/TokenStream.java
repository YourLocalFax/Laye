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

import java.util.Vector;

import net.fudev.laye.LayeFile;
import net.fudev.laye.debug.Console;
import net.fudev.laye.parse.Location;
import net.fudev.laye.struct.Identifier;

/**
 * @author Sekai Kyoretsuna
 */
public final class TokenStream
{
   public final LayeFile file;
   
   private final Console console;
   private final Vector<Token> tokens = new Vector<>();
   
   private int position = 0;
   
   public TokenStream(LayeFile file, Console console)
   {
      this.file = file;
      this.console = console;
   }
   
   public void addToken(Token token)
   {
      // TODO at some point add checks for special tokens like 'not typeof'
      tokens.addElement(token);
   }
   
   public Location getLocation()
   {
      Token token = getToken();
      return new Location(token.line, token.column, file);
   }
   
   public boolean endOfStream()
   {
      return position >= tokens.size();
   }
   
   public Token getToken()
   {
      return tokens.elementAt(position);
   }
   
   public Token getNextToken()
   {
      position++;
      if (position >= tokens.size())
      {
         position++;
         return null;
      }
      return tokens.elementAt(position);
   }
   
   public Token peekToken()
   {
      return peekToken(1);
   }
   
   public Token peekToken(int depth)
   {
      if (position + depth >= tokens.size())
      {
         console.warning("TokenStream", null, "could not peek token (depth " + depth + ").");
         return null;
      }
      return tokens.elementAt(position + depth);
   }
   
   public boolean checkTokenType(Token.Type type)
   {
      return getToken().type == type;
   }
   
   public boolean expect(Token.Type type)
   {
      Token token = getToken();
      if (token.type != type)
      {
         console.error("TokenStream", new Location(token.line, token.column, file),
               type + " expected, got " + token.type + ".");
         return false;
      }
      getNextToken();
      return true;
   }
   
   public Identifier expectIdentifier()
   {
      Token token = getToken();
      if (!expect(Token.Type.IDENTIFIER))
      {
         return null;
      }
      return (Identifier) token.data;
   }
}
