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

/**
 * @author Sekai Kyoretsuna
 */
public final class Keyword
{
   private static final HashMap<String, Keyword> KEYWORDS = new HashMap<>();
   
   public static final String STR_AND = "and";
   public static final String STR_OR = "or";
   public static final String STR_XOR = "xor";
   public static final String STR_NOT = "not";
   public static final String STR_TYPEOF = "typeof";
   public static final String STR_REF = "ref";
   public static final String STR_DEREF = "deref";

   public static final String STR_TRUE = "true";
   public static final String STR_FALSE = "false";
   public static final String STR_NULL = "null";

   public static final String STR_IF = "if";
   public static final String STR_EL = "el";
   public static final String STR_FOR = "for";
   public static final String STR_EACH = "each";
   public static final String STR_IN = "in";
   public static final String STR_TO = "to";
   public static final String STR_BY = "by";
   public static final String STR_WHILE = "while";
   public static final String STR_WITH = "with";
   public static final String STR_TAKE = "take";
   public static final String STR_MATCH = "match";

   public static final String STR_RET = "ret";
   public static final String STR_CONT = "cont";
   public static final String STR_BREAK = "break";
   public static final String STR_RESUME = "resume";
   public static final String STR_YIELD = "yield";

   public static final String STR_VAR = "var";
   public static final String STR_THIS = "this";
   public static final String STR_SELF = "self";
   public static final String STR_BASE = "base";

   public static final String STR_FN = "fn";
   public static final String STR_TYPE = "type";
   public static final String STR_EXTERN = "extern";
   public static final String STR_USE = "use";
   
   public static final Keyword AND = addKeyword(STR_AND);
   public static final Keyword OR = addKeyword(STR_OR);
   public static final Keyword XOR = addKeyword(STR_XOR);
   public static final Keyword NOT = addKeyword(STR_NOT);
   public static final Keyword TYPEOF = addKeyword(STR_TYPEOF);
   public static final Keyword REF = addKeyword(STR_REF);
   public static final Keyword DEREF = addKeyword(STR_DEREF);

   public static final Keyword TRUE = addKeyword(STR_TRUE);
   public static final Keyword FALSE = addKeyword(STR_FALSE);
   public static final Keyword NULL = addKeyword(STR_NULL);

   public static final Keyword IF = addKeyword(STR_IF);
   public static final Keyword EL = addKeyword(STR_EL);
   public static final Keyword FOR = addKeyword(STR_FOR);
   public static final Keyword EACH = addKeyword(STR_EACH);
   public static final Keyword IN = addKeyword(STR_IN);
   public static final Keyword TO = addKeyword(STR_TO);
   public static final Keyword BY = addKeyword(STR_BY);
   public static final Keyword WHILE = addKeyword(STR_WHILE);
   public static final Keyword WITH = addKeyword(STR_WITH);
   public static final Keyword TAKE = addKeyword(STR_TAKE);
   public static final Keyword MATCH = addKeyword(STR_MATCH);

   public static final Keyword RET = addKeyword(STR_RET);
   public static final Keyword CONT = addKeyword(STR_CONT);
   public static final Keyword BREAK = addKeyword(STR_BREAK);
   public static final Keyword RESUME = addKeyword(STR_RESUME);
   public static final Keyword YIELD = addKeyword(STR_YIELD);

   public static final Keyword VAR = addKeyword(STR_VAR);
   public static final Keyword THIS = addKeyword(STR_THIS);
   public static final Keyword SELF = addKeyword(STR_SELF);
   public static final Keyword BASE = addKeyword(STR_BASE);

   public static final Keyword FN = addKeyword(STR_FN);
   public static final Keyword TYPE = addKeyword(STR_TYPE);
   public static final Keyword EXTERN = addKeyword(STR_EXTERN);
   public static final Keyword USE = addKeyword(STR_USE);
   
   private static Keyword addKeyword(String keyword)
   {
      Keyword result = KEYWORDS.get(keyword);
      if (result == null)
      {
         result = new Keyword(keyword);
         KEYWORDS.put(keyword, result);
      }
      return result;
   }
   
   public static Keyword getKeyword(String keyword)
   {
      return KEYWORDS.get(keyword);
   }
   
   public static boolean isKeyword(String keyword)
   {
      return KEYWORDS.containsKey(keyword);
   }
   
   public final String image;
   
   private Keyword(String image)
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
      return 47 + ((image == null) ? 0 : image.hashCode());
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
      if (!(obj instanceof Keyword))
      {
         return false;
      }
      Keyword other = (Keyword) obj;
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
