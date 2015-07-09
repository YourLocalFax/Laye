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
import net.fudev.laye.codegen.FunctionPrototype;
import net.fudev.laye.vm.StackFrame;
import net.fudev.laye.vm.UpValue;

/**
 * @author Sekai Kyoretsuna
 */
public class LayeClosure extends LayeValue
{
   public static final TypeDef TYPEDEF_CLOSURE = TypeDef.get(LayeClosure.class);
   
   public final StackFrame definedFrame;
   public final FunctionPrototype prototype;
   
   // TODO make NOT public, eventually move the VM to a place where it can see this.
   public final UpValue[] capturedUpValues;
   
   public LayeClosure(StackFrame definedFrame, FunctionPrototype prototype)
   {
      super(TYPEDEF_CLOSURE);
      this.definedFrame = definedFrame;
      this.prototype = prototype;
      this.capturedUpValues = new UpValue[prototype.maxStackCount];
   }
   
   @Override
   public String toString()
   {
      return "closure:" + hashCode();
   }
}
