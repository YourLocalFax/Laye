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
package net.fudev.laye;

import net.fudev.laye.codegen.TypePrototype;
import net.fudev.laye.struct.Identifier;
import net.fudev.laye.type.LayeValue;
import net.fudev.laye.vm.LayeVM;

/**
 * @author Sekai Kyoretsuna
 */
public abstract class TypeDef
{
   public static NativeTypeDef get(Class<?> type)
   {
      return NativeTypeDef.get(type);
   }
   
   public static LayeTypeDef get(TypePrototype type, LayeTypeDef baseType)
   {
      return LayeTypeDef.get(type, baseType);
   }
   
   @Override
   public abstract int hashCode();
   
   @Override
   public abstract boolean equals(Object other);
   
   @Override
   public abstract String toString();
   
   public LayeValue newInstance(LayeVM vm)
   {
      return newInstance(vm, new LayeValue[0]);
   }
   
   public abstract LayeValue newInstance(LayeVM vm, LayeValue[] args);
   
   public LayeValue newInstance(LayeVM vm, String ctorName)
   {
      return newInstance(vm, ctorName, new LayeValue[0]);
   }
   
   public abstract LayeValue newInstance(LayeVM vm, String ctorName, LayeValue[] args);
   
   public LayeValue getField(LayeVM vm, String name)
   {
      return getField(vm, Identifier.get(name));
   }
   
   public abstract LayeValue getField(LayeVM vm, Identifier identifier);
   
   public void setField(LayeVM vm, String name, LayeValue value)
   {
      setField(vm, Identifier.get(name), value);
   }
   
   public abstract void setField(LayeVM vm, Identifier identifier, LayeValue value);
   
   public abstract LayeValue getIndex(LayeVM vm, Identifier identifier);
   
   public abstract void setIndex(LayeVM vm, Identifier identifier, LayeValue value);
}
