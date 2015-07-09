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
package net.fudev.laye.type;

import net.fudev.laye.TypeDef;
import net.fudev.laye.struct.Operator;
import net.fudev.laye.vm.LayeVM;

/**
 * @author Sekai Kyoretsuna
 */
public abstract class LayeValue
{
   public static final LayeValue[] EMPTY_ARRAY = new LayeValue[0];

   public static final LayeNull NULL = LayeNull.INSTANCE;

   public final TypeDef type;
   
   public LayeValue(TypeDef type)
   {
      this.type = type;
   }
   
   @Override
   public abstract String toString();
   
   public boolean toBool()
   {
      return true;
   }
   
   public LayeValue invoke(LayeVM vm, LayeValue[] args)
   {
      vm.raiseException("attempt to invoke type %s.", type.toString());
      return NULL;
   }
   
   public LayeValue infix(LayeVM vm, Operator operator, LayeValue rightValue)
   {
      // TODO handle concatenation here, all objects can be concatenated if the operator hasn't been overloaded.
      vm.raiseException("attempt to perform %s operation on type %s.", operator.image, type.toString());
      return NULL;
   }
}
