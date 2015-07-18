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

package io.ylf.laye.lexical;

import java.util.Objects;

/**
 * @author Sekai Kyoretsuna
 */
public class Token
{
   public static enum Type
   {
      KEYWORD,
      IDENTIFIER,
      OPERATOR,
      ASSIGN,
      WILDCARD("_"),
      OPEN_BRACE("("),
      CLOSE_BRACE(")"),
      OPEN_SQUARE_BRACE("["),
      CLOSE_SQUARE_BRACE(")"),
      OPEN_CURLY_BRACE("{"),
      CLOSE_CURLY_BRACE("}"),
      SEMI_COLON(";"),
      COLON(":"),
      COMMA(","),
      INT_LITERAL,
      STRING_LITERAL;
      
      public final String image;
      
      private Type()
      {
         this.image = null;
      }
      
      private Type(String image)
      {
         this.image = image;
      }
   }
   
   public final Type type;
   
   /**
    * Data stored for each token. In the case of keywords and identifiers, this is an instance
    * of Identifier representing the image of the token. In the case of literals, this represents
    * the literal object (string, integer, etc.) This should never be null.
    */
   public final Object data;
   public final Location location;
   
   public Token(Type type, Location location)
   {
      assert(type != null);
      assert(type.image != null);
      assert(location != null);
      this.type = type;
      this.data = type.image;
      this.location = location;
   }
   
   public Token(Type type, Object data, Location location)
   {
      assert(type != null);
      assert(data != null);
      assert(location != null);
      this.type = type;
      this.data = data;
      this.location = location;
   }
   
   @Override
   public String toString()
   {
      return Objects.toString(data);
   }
}
