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
package net.fudev.laye.codegen;

import java.util.Vector;

import net.fudev.laye.type.LayeInt;
import net.fudev.laye.type.LayeString;
import net.fudev.laye.type.LayeValue;
import net.fudev.laye.util.Util;
import net.fudev.laye.vm.Instruction;
import net.fudev.laye.vm.OpCode;

/**
 * @author Sekai Kyoretsuna
 */
class FunctionPrototypeBuilder
{
//   private final class Block
//   {
//      public final Block previous;
//      
//      public int initialLocalsSize;
//      public int startPosition;
//      
//      public Block(Block previous)
//      {
//         this.previous = previous;
//         startPosition = getCurrentPosition();
//         initialLocalsSize = getLocalCount();
//      }
//   }
   
   private final Vector<Integer> body = new Vector<>();
   
   public int numParameters = 0;
   private boolean variadic = false;
   
   private int localCount = 0;
   private int upValueCount = 0;
   private int maxStackCount = 0;
   
   private final Vector<FunctionPrototype> nestedFunctions = new Vector<>();
   private final Vector<Object> constants = new Vector<>();
   
   private int currentStackCount = 0;
   
//   private Block currentBlock = null;
   
   public FunctionPrototypeBuilder()
   {
   }
   
   public FunctionPrototype build()
   {
      int[] body = Util.listToIntArray(this.body);
      FunctionPrototype[] nestedFunctions = this.nestedFunctions
            .toArray(new FunctionPrototype[this.nestedFunctions.size()]);
      Object[] constants = this.constants.toArray(new Object[this.constants.size()]);
      
      return new FunctionPrototype(body, numParameters, variadic, localCount, maxStackCount,
            nestedFunctions, constants);
   }
   
   public void setIsVariadic()
   {
      variadic = true;
   }
   
   public int getLocalCount()
   {
      return localCount;
   }
   
   public int getUpValueCount()
   {
      return upValueCount;
   }
   
//   public void beginBlock()
//   {
//      currentBlock = new Block(currentBlock);
//   }
//   
//   public void endBlock()
//   {
//      int endPosition = getCurrentPosition();
//      for (int i = currentBlock.startPosition + 1; i < endPosition; i++)
//      {
//         int c = body.get(i);
//         if (Instruction.GET_OP(c) == OpCode.RETURN)
//         {
//            body.set(i, Instruction.SET_A(OpCode.RETURN, endPosition - i));
//         }
//      }
//      final int oldUpValueCount = getUpValueCount();
//      if (getLocalCount() != currentBlock.initialLocalsSize)
//      {
//         setLocalsSize(currentBlock.initialLocalsSize);
//         if (oldUpValueCount != getUpValueCount())
//         {
//            addOpCloseUpValues(currentBlock.initialLocalsSize);
//         }
//      }
//      currentBlock = currentBlock.previous;
//   }
   
   private int addConstant(LayeValue value)
   {
      int index = constants.indexOf(value);
      if (index == -1)
      {
         constants.add(value);
         return constants.size() - 1;
      }
      return index;
   }
   
   public int addConstantInt(long value)
   {
      LayeValue intValue = LayeInt.valueOf(value);
      return addConstant(intValue);
   }
   
   public int addConstantString(String value)
   {
      LayeValue strValue = new LayeString(value);
      return addConstant(strValue);
   }
   
   public int addNestedFunction(FunctionPrototype proto)
   {
      nestedFunctions.addElement(proto);
      return nestedFunctions.size() - 1;
   }
   
   private void changeStackSize(int amount)
   {
      currentStackCount += amount;
      if (currentStackCount < 0)
      {
         // TODO console plz
         throw new RuntimeException("stack size cannot be negative.");
      }
      if (currentStackCount > maxStackCount)
      {
         maxStackCount = currentStackCount;
      }
   }
   
   public int getCurrentPosition()
   {
      return body.size() - 1;
   }
   
   private int addInsn(int op)
   {
      body.addElement(op);
      return getCurrentPosition();
   }
   
   private int addInsn_A(int op, int a)
   {
      body.addElement(Instruction.SET_A(op, a));
      return getCurrentPosition();
   }
   
   public int addOpCloseUpValues(int toIndex)
   {
      return addInsn_A(OpCode.CLOSE_UP_VALUES, toIndex);
   }
   
   public int addOpLoadLocal(int index)
   {
      changeStackSize(1);
      return addInsn_A(OpCode.LOAD_LOCAL, index);
   }
   
   public int addOpStoreLocal(int index)
   {
      changeStackSize(-1);
      return addInsn_A(OpCode.STORE_LOCAL, index);
   }
   
   public int addOpLoadGlobal(int index)
   {
      changeStackSize(1);
      return addInsn_A(OpCode.LOAD_GLOBAL, index);
   }
   
   public int addOpStoreGlobal(int index)
   {
      changeStackSize(-1);
      return addInsn_A(OpCode.STORE_GLOBAL, index);
   }
   
   public int addOpLoadConst(int index)
   {
      changeStackSize(1);
      return addInsn_A(OpCode.LOAD_CONST, index);
   }
   
   public int addOpLoadNull()
   {
      changeStackSize(1);
      return addInsn(OpCode.LOAD_NULL);
   }
   
   public int addOpBuildClosure(int index)
   {
      changeStackSize(1);
      return addInsn_A(OpCode.BUILD_CLOSURE, index);
   }
   
   public int addOpInvoke(int argCount)
   {
      changeStackSize(-argCount);
      return addInsn_A(OpCode.INVOKE, argCount);
   }
}
