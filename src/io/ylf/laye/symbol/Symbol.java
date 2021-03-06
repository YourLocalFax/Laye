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
package io.ylf.laye.symbol;

import io.ylf.laye.struct.Identifier;

/**
 * @author Sekai Kyoretsuna
 */
public class Symbol
{
   public static enum Type
   {
      GLOBAL, LOCAL, TYPE_INSTANCE, TYPE_STATIC, ENUM
   }
   
   public final Type type;
   
   /**
    * The defined name of this symbol.
    */
   public final Identifier name;
   
   /**
    * The index of this symbol. This is not used for global symbols, it may be zero.
    */
   public final int index;
   
   public Symbol(Type type, Identifier name, int index)
   {
      assert(type != null && name != null && index >= 0);
      this.type = type;
      this.name = name;
      this.index = index;
   }
}
