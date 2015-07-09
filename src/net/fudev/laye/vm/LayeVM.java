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

import net.fudev.laye.GlobalState;
import net.fudev.laye.codegen.FunctionPrototype;
import net.fudev.laye.struct.Identifier;
import net.fudev.laye.struct.Operator;
import net.fudev.laye.type.LayeClosure;
import net.fudev.laye.type.LayeException;
import net.fudev.laye.type.LayeExternFunction;
import net.fudev.laye.type.LayeValue;
import net.fudev.laye.util.Assert;

/**
 * @author Sekai Kyoretsuna
 */
public class LayeVM
{
   private GlobalState state;
   private CallStack callStack;
   
   public LayeVM(GlobalState state)
   {
      this.state = state;
      callStack = new CallStack();
   }
   
   public void raiseException(String format, Object... args)
   {
      raiseException(new LayeException(format, args));
   }
   
   public void raiseException(LayeException exception)
   {
      // TODO handle exceptions
   }
   
   public LayeValue invoke(LayeValue value, LayeValue thisObject, LayeValue... args)
   {
      if (value instanceof LayeClosure)
      {
         return invoke((LayeClosure) value, thisObject, args);
      }
      else if (value instanceof LayeExternFunction)
      {
         return ((LayeExternFunction) value).invoke(this, args);
      }
      // TODO throw an error.
      return null;
   }
   
   public LayeValue invoke(LayeClosure closure, LayeValue thisObject, LayeValue... args)
   {
      Assert.notNull(this, closure, Identifier.get("closure"));
      
      if (thisObject == null)
      {
         thisObject = LayeValue.NULL;
      }
      
      callStack.newFrame(closure, thisObject);
      
      final int numArgs = closure.prototype.numArgs;
      final boolean variadic = closure.prototype.variadic;
      
      for (int i = 0; i < args.length; i++)
      {
         // TODO Variadic arguments
         callStack.store(i, args[i]);
      }
      
      // If less params were passed than expected, fill with nulls
      // TODO make variadic args a list instead if applicable
      if (args.length < numArgs)
      {
         for (int i = args.length; i < numArgs; i++)
         {
            callStack.store(i, LayeValue.NULL);
         }
      }
      
      final int[] body = closure.prototype.body;
      final int insnCount = body.length;
      
      StackFrame top = callStack.getTop();
      while (top.insnPtr < insnCount && !top.abortExecution)
      {
         executeInstruction(body[top.insnPtr++]);
      }
      
      if (top.abortExecution)
      {
         return LayeValue.NULL;
      }
      
      final LayeValue result = callStack.peek();
      callStack.endFrame();
      return result;
   }
   
   private void executeInstruction(int insn)
   {
      final int op = Instruction.GET_OP(insn);
      final int a = Instruction.GET_A(insn);
      final int b = Instruction.GET_B(insn);
      final int c = Instruction.GET_C(insn);
      final int sa = Instruction.GET_SA(insn);
      final int sb = Instruction.GET_SB(insn);
      final int sc = Instruction.GET_SC(insn);
      
      final UpValue[] currentUpValues = callStack.getTop().currentUpValues;
      
      switch (op)
      {
         default:
         {
            throw new RuntimeException(String.format("unrecognized op code 0x%02X", op));
         }
            
            /**
             * 
             */
         case OpCode.CLOSE:
         {
            for (int i = currentUpValues.length; --i >= a;)
            {
               if (currentUpValues[i] != null)
               {
                  currentUpValues[i].close();
                  currentUpValues[i] = null;
               }
            }
            return;
         }
            
            /**
             * STACK -1
             * 
             * Pops a value from the stack.
             */
         case OpCode.POP:
         {
            callStack.pop();
            return;
         }
            
            /**
             * STACK +1
             * 
             * Duplicates the top value on the stack.
             */
         case OpCode.DUP:
         {
            callStack.push(callStack.get(-1));
            return;
         }
            
            /**
             * STACK +1
             * 
             * A = local index.
             * 
             * Loads a local variable and pushes it to the stack
             */
         case OpCode.LOAD_LOCAL:
         {
            LayeValue value = callStack.load(a);
            callStack.push(value);
            return;
         }
            
            /**
             * STACK -1
             * 
             * A = local index.
             * 
             * Stores a value from the stack in a local variable.
             */
         case OpCode.STORE_LOCAL:
         {
            LayeValue value = callStack.pop();
            callStack.store(a, value);
            return;
         }
            
            /**
             * STACK +1
             * 
             * A = global index.
             * 
             * Loads a global variable and pushes it to the stack
             */
         case OpCode.LOAD_GLOBAL:
         {
            LayeValue value = state.load(a);
            callStack.push(value);
            return;
         }
            
            /**
             * STACK -1
             * 
             * A = global index.
             * 
             * Stores a value from the stack in a global variable.
             */
         case OpCode.STORE_GLOBAL:
         {
            LayeValue value = callStack.pop();
            state.store(a, value);
            return;
         }
            
            // TODO LOAD_INDEX, STORE_INDEX
            
            // TODO LOAD_BOOL
            
            /**
             * STACK +1
             * 
             * Pushes null onto the stack.
             */
         case OpCode.LOAD_NULL:
         {
            callStack.push(LayeValue.NULL);
            return;
         }
            
            /**
             * STACK +1
             * 
             * A = constant index.
             * 
             * Pushes a constant onto the stack.
             */
         case OpCode.LOAD_CONST:
         {
            LayeValue value = (LayeValue) callStack.getTop().closure.prototype.constants[a];
            callStack.push(value);
            return;
         }
            
            /*
             * STACK +1
             * 
             * A = function prototype index.
             * 
             * Builds a closure using the function prototype at index A.
             */
         case OpCode.BUILD_CLOSURE:
         {
            FunctionPrototype prototype = callStack.getTop().closure.prototype.nestedFunctions[a];
            LayeClosure closure = new LayeClosure(callStack.getTop(), prototype);
            
            callStack.push(closure);
            return;
         }
            
            // TODO BUILD_TYPE
            
            // TODO PREFIX, POSTFIX
            
            /**
             * STACK -1
             * 
             * A = Operator index in constant pool.
             * 
             * Pops two values from the stack, performing a binary operation between the two where
             * stack[top - 2] is the left side, and stack[top - 1] is the right. The result is
             * pushed to the stack.
             */
         case OpCode.INFIX:
         {
            Operator operator = (Operator) callStack.getTop().closure.prototype.constants[a];
            LayeValue right = callStack.pop(), left = callStack.pop();
            callStack.push(left.infix(this, operator, right));
            return;
         }
            
            /**
             * A = new insn index. B = testFor value.
             */
         case OpCode.TEST:
         {
            boolean testFor = b != 0;
            boolean value = callStack.pop().toBool();
            if (value == testFor)
            {
               callStack.getTop().insnPtr = a;
            }
            return;
         }
            
            /**
             * STACK -A
             * 
             * A = Number of arguments.
             * 
             * Pops A arguments from the stack, and uses them as arguments to the next value on the
             * stack. The result of that invocation is pushed to the stack.
             */
         case OpCode.INVOKE:
         {
            LayeValue[] arguments = callStack.popCount(a);
            LayeValue target = callStack.pop();
            LayeValue result = invoke(target, null, arguments);
            callStack.push(result);
         }
      }
   }
   
   public GlobalState getState()
   {
      return state;
   }
   
   public void setState(GlobalState state)
   {
      this.state = state;
   }
}
