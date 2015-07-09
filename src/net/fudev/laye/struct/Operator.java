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
package net.fudev.laye.struct;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sekai Kyoretsuna
 */
public final class Operator
{
   public static final Operator PLUS = get("+");
   public static final Operator MINUS = get("-");
   public static final Operator TIMES = get("*");
   public static final Operator DIVIDE = get("/");
   public static final Operator INT_DIVIDE = get("//");
   public static final Operator MODULO = get("%");
   public static final Operator POWER = get("^");
   public static final Operator AND = get("&");
   public static final Operator OR = get("|");
   public static final Operator XOR_COMPL = get("~");
   public static final Operator LSHIFT = get("<<");
   public static final Operator RSHIFT = get(">>");
   public static final Operator URSHIFT = get(">>>");
   public static final Operator EQUALTO = get("==");
   public static final Operator NEQUALTO = get("!=");
   public static final Operator LESS = get("<");
   public static final Operator LESSEQ = get("<=");
   public static final Operator GREATER = get(">");
   public static final Operator GREATEREQ = get(">=");
   public static final Operator COMP3 = get("<=>");
   public static final Operator CONCAT = get("<>");
   
   private static final Map<String, Operator> operators = new HashMap<String, Operator>();
   
   public static Operator get(String image)
   {
      if (!isValidOperatorImage(image))
      {
         return null;
      }
      Operator result = operators.get(image);
      if (result == null)
      {
         operators.put(image, result = new Operator(image));
      }
      return result;
   }
   
   public static boolean isValidOperatorImage(String image)
   {
      if (image == null || image.equals("="))
      {
         return false;
      }
      for (char c : image.toCharArray())
      {
         if (!isValidOperatorChar(c))
         {
            return false;
         }
      }
      return true;
   }
   
   public static boolean isValidOperatorChar(char token)
   {
      switch (token)
      {
         case '~':
         case '!':
         case '@':
         case '$':
         case '%':
         case '^':
         case '&':
         case '*':
         case '-':
         case '+':
         case '=':
         case '\\':
         case '|':
         case '<':
         case '>':
         case '/':
         case '?':
         default:
            return false;
      }
   }
   
   public final String image;
   
   private Operator(String image)
   {
      this.image = image;
   }
   
   @Override
   public int hashCode()
   {
      return 37 + image.hashCode();
   }
   
   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      Operator other = (Operator) obj;
      if (image == null)
      {
         if (other.image != null)
         {
            return false;
         }
      }
      else if (!image.equals(other.image))
      {
         return false;
      }
      return true;
   }
}
