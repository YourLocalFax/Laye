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
import net.fudev.laye.vm.LayeVM;

/**
 * @author Sekai Kyoretsuna
 */
public final class LayeFloat extends LayeNumber
{
   public static final TypeDef TYPEDEF_FLOAT = TypeDef.get(LayeFloat.class);
   
   public static LayeFloat valueOf(double value)
   {
      return new LayeFloat(value);
   }
   
   public final double value;
   
   private LayeFloat(double value)
   {
      super(TYPEDEF_FLOAT);
      this.value = value;
   }
   
   @Override
   public String toString()
   {
      return Double.toString(value);
   }
   
   @Override
   public boolean toBool()
   {
      return value != 0.0f && value != -0.0f;
   }
   
   @Override
   public long longValue()
   {
      return (long) value;
   }
   
   @Override
   public double doubleValue()
   {
      return value;
   }
   
   @Override
   public LayeValue infix(LayeVM vm, Operator operator, LayeValue rightValue)
   {
      // TODO floating point infix operations
      if (rightValue instanceof LayeInt || rightValue instanceof LayeFloat)
      {
         double right = rightValue instanceof LayeInt ? ((LayeInt) rightValue).value
               : ((LayeFloat) rightValue).value;
         switch (operator.image)
         {
            case "+":
               return valueOf(value + right);
            case "-":
               return valueOf(value - right);
            case "*":
               return valueOf(value * right);
            case "/":
               return valueOf(value / right);
            case "//":
               return LayeInt.valueOf((long) (value / right));
            case "%":
               return valueOf(value % right);
            case "^":
               return valueOf(FMath.pow(value, right));
         }
      }
      return super.infix(vm, operator, rightValue);
   }
}
