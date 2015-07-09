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
import net.fudev.laye.util.FMath;
import net.fudev.laye.util.IMath;
import net.fudev.laye.vm.LayeVM;

/**
 * @author Sekai Kyoretsuna
 */
public final class LayeInt extends LayeNumber
{
   public static final TypeDef TYPEDEF_INT = TypeDef.get(LayeInt.class);
   
   public static LayeInt valueOf(long value)
   {
      // TODO Laye integer cache.
      return new LayeInt(value);
   }
   
   public final long value;
   
   private LayeInt(long value)
   {
      super(TYPEDEF_INT);
      this.value = value;
   }
   
   @Override
   public String toString()
   {
      return Long.toString(value);
   }
   
   @Override
   public boolean toBool()
   {
      return value != 0L;
   }
   
   @Override
   public long longValue()
   {
      return value;
   }
   
   @Override
   public double doubleValue()
   {
      return (double) value;
   }
   
   @Override
   public LayeValue infix(LayeVM vm, Operator operator, LayeValue rightValue)
   {
      // TODO floating point infix operations
      if (rightValue instanceof LayeInt)
      {
         boolean isInteger = true;
         switch (operator.image)
         {
            case "+":
               return isInteger ? valueOf(value + ((LayeInt) rightValue).value)
                     : LayeFloat.valueOf(value + ((LayeFloat) rightValue).value);
            case "-":
               return isInteger ? valueOf(value - ((LayeInt) rightValue).value)
                     : LayeFloat.valueOf(value - ((LayeFloat) rightValue).value);
            case "*":
               return isInteger ? valueOf(value * ((LayeInt) rightValue).value)
                     : LayeFloat.valueOf(value * ((LayeFloat) rightValue).value);
            case "/":
               return isInteger ? valueOf(value / ((LayeInt) rightValue).value)
                     : LayeFloat.valueOf(value / ((LayeFloat) rightValue).value);
            case "//":
               return isInteger ? valueOf(value / ((LayeInt) rightValue).value)
                     : valueOf((long) (value / ((LayeFloat) rightValue).value));
            case "%":
               return isInteger ? valueOf(value % ((LayeInt) rightValue).value)
                     : LayeFloat.valueOf(value % ((LayeFloat) rightValue).value);
            case "^":
               return isInteger ? valueOf(IMath.pow(value, ((LayeInt) rightValue).value))
                     : LayeFloat.valueOf(FMath.pow(value, ((LayeFloat) rightValue).value));
         }
      }
      return super.infix(vm, operator, rightValue);
   }
}
