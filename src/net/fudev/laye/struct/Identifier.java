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
public final class Identifier
{
   private static final Map<String, Identifier> identifiers = new HashMap<String, Identifier>();
   
   public static Identifier get(String image)
   {
      if (!isValidIdentifier(image))
      {
         return null;
      }
      Identifier result = identifiers.get(image);
      if (result == null)
      {
         result = new Identifier(image);
         identifiers.put(image, result);
      }
      return result;
   }
   
   public static boolean isValidIdentifier(String image)
   {
      if (image == null || image.length() == 0 || image.equals("_"))
      {
         return false;
      }
      if (Keyword.isKeyword(image))
      {
         return false;
      }
      return true;
   }
   
   public static boolean isValidIdentifierStart(char token)
   {
      return token == '_' || Character.isLetter(token);
   }
   
   public static boolean isValidIdentifierPart(char token)
   {
      return isValidIdentifierStart(token) || Character.isDigit(token);
   }
   
   public final String image;
   
   private Identifier(String image)
   {
      this.image = image;
   }
   
   @Override
   public String toString()
   {
      return image;
   }
   
   @Override
   public int hashCode()
   {
      return 31 + image.hashCode();
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
      Identifier other = (Identifier) obj;
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
