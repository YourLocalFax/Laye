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

/**
 * @author Sekai Kyoretsuna
 */
class CallStack
{
   private int frameCount = 0;
   private StackFrame top = null;
   
   public CallStack()
   {
   }
   
   public int getFrameCount()
   {
      return frameCount;
   }
   
   public StackFrame getTop()
   {
      return top;
   }
   
   public void newFrame(LayeClosure closure, LayeValue self)
   {
      frameCount++;
      top = new StackFrame(top, closure, self);
   }
   
   public void endFrame()
   {
      frameCount--;
      top = top.parent;
   }
   
   public void store(int index, LayeValue value)
   {
      top.store(index, value);
   }
   
   public LayeValue load(int index)
   {
      return top.load(index);
   }
   
   public void push(LayeValue value)
   {
      top.push(value);
   }
   
   public LayeValue pop()
   {
      return top.pop();
   }

   public LayeValue[] popCount(int a)
   {
      LayeValue[] result = new LayeValue[a];
      for (int i = a - 1; i >= 0; i--)
      {
         result[i] = pop();
      }
      return result;
   }
   
   public LayeValue get(int idx)
   {
      return top.get(idx);
   }

   public LayeValue peek()
   {
      return top.peek();
   }
   
   public void unwind(int frames)
   {
      frameCount -= frames;
      while (--frames >= 0)
      {
         top.abortExecution = true;
         top = top.parent;
      }
   }
}
