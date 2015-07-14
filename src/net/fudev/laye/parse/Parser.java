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

package net.fudev.laye.parse;

import java.util.Vector;

import net.fudev.laye.debug.Console;
import net.fudev.laye.lex.Token;
import net.fudev.laye.lex.TokenStream;
import net.fudev.laye.parse.ast.*;
import net.fudev.laye.struct.Identifier;
import net.fudev.laye.struct.Keyword;
import net.fudev.laye.struct.Operator;

/**
 * @author Sekai Kyoretsuna
 */
public class Parser
{
   private final Console console;
   
   private TokenStream tokens = null;
   
   public Parser(Console console)
   {
      this.console = console;
   }
   
   private void consoleError(String message)
   {
      Token lastToken = tokens.getToken();
      console.error("(Parser) line " + lastToken.line + " (column " + lastToken.column + "): " + message);
   }
   
   /**
    * @return
    */
   public Ast parse(TokenStream tokens)
   {
      this.tokens = tokens;
      Ast result = new Ast(new Location(1, 1, tokens.file));
      
      while (!tokens.endOfStream())
      {
         AstNode next = parseTopLevel();
         if (next == null)
         {
            Token token = tokens.getToken();
            consoleError("unable to parse at token " + token);
            return null;
         }
         result.addChild(next);
      }
      
      return result;
   }
   
   private AstNode parseTopLevel()
   {
      Token token = tokens.getToken();
      switch (token.type)
      {
         case SEMI_COLON:
         {
            // Lex ';'
            tokens.getNextToken();
            return parseTopLevel();
         }
         case KEYWORD:
         {
            switch (((Keyword) token.data).image)
            {
               case Keyword.STR_EXTERN:
               {
                  return parseExternDecl();
               }
               case Keyword.STR_FN:
               {
                  if (tokens.peekToken().type == Token.Type.OPEN_BRACE)
                  {
                     return parseFunctionExpression();
                  }
                  return parseFunctionDefinition();
               }
               case Keyword.STR_VAR:
               {
                  return parseVariableDefinition();
               }
            }
            break;
         }
         default:
            return parsePrimaryExpression();
      }
      return null;
   }
   
   private Vector<NodeExpression> parseCommaSeparatedExpressions()
   {
      Vector<NodeExpression> result = new Vector<>();
      while (true)
      {
         NodeExpression current = parsePrimaryExpression();
         if (current == null)
         {
            if (result.size() > 0)
            {
               consoleError("(comma expression encountered invalid expression.");
               return null;
            }
            return result;
         }
         result.addElement(current);
         if (!tokens.checkTokenType(Token.Type.COMMA))
         {
            break;
         }
         // Lex ','
         tokens.getNextToken();
      }
      return result;
   }
   
   private NodeExpression parsePrimaryExpression()
   {
      return parsePrimaryExpression(true);
   }
   
   private NodeExpression parsePrimaryExpression(boolean allowPostfixCall)
   {
      switch (tokens.getToken().type)
      {
         case KEYWORD:
         {
            switch (((Keyword) tokens.getToken().data).image)
            {
               case Keyword.STR_FN:
               {
                  return parseFunctionExpression();
               }
               default:
                  consoleError("unexpected keyword " + tokens.getToken());
                  return null;
            }
         }
         case OPEN_CURLY_BRACE:
         {
            return parseBlock();
         }
         case INT_LITERAL:
         {
            long value = ((Long) tokens.getToken().data).longValue();
            // Lex int
            tokens.getNextToken();
            return new NodeIntLiteral(tokens.getLocation(), value);
         }
         case STRING_LITERAL:
         {
            String value = (String) tokens.getToken().data;
            // Lex string
            tokens.getNextToken();
            return new NodeStringLiteral(tokens.getLocation(), value);
         }
         case IDENTIFIER:
         {
            NodeIdentifier node = new NodeIdentifier(tokens.getLocation(),
                  tokens.expectIdentifier());
            return postfix(node, allowPostfixCall);
         }
         default:
            return null;
      }
   }
   
   private NodeExpression postfix(NodeExpression target, boolean allowPostfixCall)
   {
      if (target == null)
      {
         return null;
      }
      switch (tokens.getToken().type)
      {
         case OPEN_BRACE:
         {
            if (!allowPostfixCall)
            {
               return target;
            }
            NodeFunctionCall result = new NodeFunctionCall(tokens.getLocation(), target);
            // Lex '('
            tokens.getNextToken();
            result.arguments = parseCommaSeparatedExpressions();
            if (result.arguments == null)
            {
               consoleError("failed to parse function arguments.");
               return null;
            }
            tokens.expect(Token.Type.CLOSE_BRACE);
            return result;
         }
         default:
            return target;
      }
   }
   
   private NodeExpression factor()
   {
      return parsePrimaryExpression();
      // TODO uncomment operator factoring.
      // final NodeExpression left;
      // if ((left = parsePrimaryExpression()) == null)
      // {
      // return null;
      // }
      // return factorRHS(0, left);
   }
   
   // TODO add precedence
   private NodeExpression factorRHS(NodeExpression left) // final int minp
   {
      while (tokens.checkTokenType(Token.Type.OPERATOR))
      // && tokenData.operator.precedence >= minp)
      {
         final Operator op = (Operator) tokens.getToken().data;
         // Lex Operator
         tokens.getNextToken();
         // Load up the right hand side, if one exists
         NodeExpression right;
         if ((right = parsePrimaryExpression()) != null)
         {
            while (tokens.checkTokenType(Token.Type.OPERATOR))
            // && tokenData.operator.precedence > op.precedence)
            {
               right = factorRHS(right);
            }
            left = new NodeInfixExpression(tokens.getLocation(), left, right, op);
         }
         else
         {
            return new NodePostfixExpression(tokens.getLocation(), left, op);
         }
      }
      return left;
   }
   
   private NodeExternDecl parseExternDecl()
   {
      NodeExternDecl extern = new NodeExternDecl(tokens.getLocation());
      
      // Lex 'fn'
      tokens.getNextToken();
      
      extern.name = tokens.expectIdentifier();
      if (extern.name == null)
      {
         consoleError("extern name expected.");
         return null;
      }
      
      return extern;
   }
   
   // TODO check if these function things can be combined.
   
   private NodeFunctionExpr parseFunctionExpression()
   {
      NodeFunctionExpr fn = new NodeFunctionExpr(tokens.getLocation());
      
      // Lex 'fn'
      tokens.getNextToken();
      
      // Lex '('
      if (!tokens.expect(Token.Type.OPEN_BRACE))
      {
         console.error("'(' expected to start function parameter list.");
         return null;
      }
      while (!tokens.checkTokenType(Token.Type.CLOSE_BRACE))
      {
         Identifier param = tokens.expectIdentifier();
         if (param == null)
         {
            consoleError("no parameter name given.");
            return null;
         }
         fn.addParameter(param);
         // TODO check variadic param and such
         if (!tokens.checkTokenType(Token.Type.COMMA))
         {
            break;
         }
         // Lex ','
         tokens.getNextToken();
      }
      // Lex ')'
      tokens.getNextToken();
      
      if (!tokens.checkTokenType(Token.Type.SEMI_COLON))
      {
         fn.body = parsePrimaryExpression();
         if (fn.body == null)
         {
            consoleError("error parsing function body.");
         }
      }
      else
      {
         // Lex ';'
         tokens.getNextToken();
      }
      
      return fn;
   }
   
   private NodeFunctionDef parseFunctionDefinition()
   {
      NodeFunctionDef fn = new NodeFunctionDef(tokens.getLocation());
      
      // Lex 'fn'
      tokens.getNextToken();
      
      fn.name = tokens.expectIdentifier();
      if (fn.name == null)
      {
         consoleError("function name expected.");
         return null;
      }
      
      // Lex '('
      if (!tokens.expect(Token.Type.OPEN_BRACE))
      {
         console.error("'(' expected to start function parameter list.");
         return null;
      }
      while (!tokens.checkTokenType(Token.Type.CLOSE_BRACE))
      {
         Identifier param = tokens.expectIdentifier();
         if (param == null)
         {
            consoleError("no parameter name given.");
            return null;
         }
         fn.addParameter(param);
         // TODO check variadic param and such
         if (!tokens.checkTokenType(Token.Type.COMMA))
         {
            break;
         }
         // Lex ','
         tokens.getNextToken();
      }
      // Lex ')'
      tokens.getNextToken();
      
      if (!tokens.checkTokenType(Token.Type.SEMI_COLON))
      {
         fn.body = parsePrimaryExpression();
         if (fn.body == null)
         {
            consoleError("error parsing function body.");
         }
      }
      else
      {
         // Lex ';'
         tokens.getNextToken();
      }
      
      return fn;
   }
   
   private NodeVariableDef parseVariableDefinition()
   {
      NodeVariableDef var = new NodeVariableDef(tokens.getLocation());
      
      // Lex 'var'
      tokens.getNextToken();
      
      var.name = tokens.expectIdentifier();
      if (var.name == null)
      {
         consoleError("variable name expected.");
         return null;
      }
      
      if (tokens.checkTokenType(Token.Type.ASSIGN))
      {
         // Lex '='
         tokens.getNextToken();
         
         var.value = parsePrimaryExpression();
         if (var.value == null)
         {
            consoleError("error parsing variable value.");
         }
      }
      // else the value defaults to null.
      
      return var;
   }
   
   private NodeBlock parseBlock()
   {
      Location location = tokens.getLocation();
      Vector<AstNode> result = new Vector<>();
      tokens.getNextToken();
      while (!tokens.checkTokenType(Token.Type.CLOSE_CURLY_BRACE))
      {
         AstNode node = parseTopLevel();
         if (node == null)
         {
            consoleError("block failed to parse.");
            return null;
         }
         result.addElement(node);
      }
      tokens.expect(Token.Type.CLOSE_CURLY_BRACE);
      return new NodeBlock(location, result);
   }
}
