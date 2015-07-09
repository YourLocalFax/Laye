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
package net.fudev.laye.vm;

import net.fudev.laye.type.LayeClosure;
import net.fudev.laye.type.LayeValue;

public class StackFrame
{
   final StackFrame parent;
   
   final LayeClosure closure;
   final LayeValue self;
   
   final int localCount;
   final int maxStackCount;
   
   private final LayeValue[] stack;
   private final LayeValue[] locals;
   
   int insnPtr = 0;
   private int stackPtr = 0;
   
   boolean abortExecution = false;
   
   public StackFrame(StackFrame parent, LayeClosure closure, LayeValue self)
   {
      this.parent = parent;
      this.closure = closure;
      this.self = self;
      this.localCount = closure.prototype.localCount;
      this.maxStackCount = closure.prototype.maxStackCount;
      this.locals = new LayeValue[localCount];
      this.stack = new LayeValue[maxStackCount];
   }
   
   void store(int index, LayeValue value)
   {
      locals[index] = value;
   }
   
   LayeValue load(int index)
   {
      return locals[index];
   }
   
   void push(LayeValue value)
   {
      stack[stackPtr++] = value;
   }
   
   LayeValue pop()
   {
      return stack[--stackPtr];
   }
   
   LayeValue get(int idx)
   {
      if (idx < 0)
      {
         idx += stackPtr;
      }
      return stack[idx];
   }

   LayeValue last()
   {
      return stack[stackPtr - 1];
   }
   
   StackFrame duplicate(StackFrame parent)
   {
      return new StackFrame(parent, closure, self);
   }
}