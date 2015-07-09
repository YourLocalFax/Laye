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

/**
 * @author Sekai Kyoretsuna
 */
public final class Instruction
{
   // 0x AA AA BB OO
   // 0x CC CC CC OO
   
   /** Laye's max stack size. */
   public static final int MAX_STACK_SIZE = 0x10000;
   
   /** The bit width of an op code */
   public static final int SIZE_OP = 8;
   /** The bit width of argument A and its signed counterpart */
   public static final int SIZE_A = 16;
   /** The bit width of argument B and its signed counterpart */
   public static final int SIZE_B = 8;
   /** The bit width of argument C (a combination of A and B) and its signed counterpart */
   public static final int SIZE_C = SIZE_A + SIZE_B;
   
   /** How many bits left, from the right-most bit of the instruction, the op code starts */
   public static final int POS_OP = 0;
   /** How many bits left, from the right-most bit of the instruction, argument B and its signed counterpart start */
   public static final int POS_B = SIZE_OP;
   /** How many bits left, from the right-most bit of the instruction, argument A and its signed counterpart start */
   public static final int POS_A = POS_B + SIZE_B;
   /** How many bits left, from the right-most bit of the instruction, argument C and its signed counterpart start */
   public static final int POS_C = POS_B;
   
   /** The maximum value of an op code */
   public static final int MAX_OP = (1 << SIZE_OP) - 1;
   /** The maximum value of argument A */
   public static final int MAX_A = (1 << SIZE_A) - 1;
   /** The maximum value of argument A, signed */
   public static final int MAX_SA = MAX_A >> 1;
   /** The minimum value of argument A, signed */
   public static final int MIN_SA = -MAX_SA - 1;
   /** The maximum value of argument B */
   public static final int MAX_B = (1 << SIZE_B) - 1;
   /** The maximum value of argument B, signed */
   public static final int MAX_SB = MAX_B >> 1;
   /** The minimum value of argument B, signed */
   public static final int MIN_SB = -MAX_SB - 1;
   /** The maximum value of argument C */
   public static final int MAX_C = (1 << SIZE_C) - 1;
   /** The maximum value of argument C, signed */
   public static final int MAX_SC = MAX_C >> 1;
   /** The minimum value of argument C, signed */
   public static final int MIN_SC = -MAX_SC - 1;
   
   /** The bit-mask used to get only the op code */
   public static final int MASK_OP = MAX_OP << POS_OP;
   /** The bit-mask used to get only argument A or its signed counterpart */
   public static final int MASK_A = MAX_A << POS_A;
   /** The bit-mask used to get only argument B or its signed counterpart */
   public static final int MASK_B = MAX_B << POS_B;
   /** The bit-mask used to get only argument C or its signed counterpart */
   public static final int MASK_C = MAX_C << POS_C;
   
   /** The bit-mask used to get everything but the op code */
   public static final int MASK_NOT_OP = ~MASK_OP;
   /** The bit-mask used to get everything but argument A or its signed counterpart */
   public static final int MASK_NOT_A = ~MASK_A;
   /** The bit-mask used to get everything but argument B or its signed counterpart */
   public static final int MASK_NOT_B = ~MASK_B;
   /** The bit-mask used to get everything but argument C or its signed counterpart */
   public static final int MASK_NOT_C = ~MASK_C;
   
   // ---------- Macros ---------- //
   
   /**
    * @return the operation code of the given instruction
    */
   public static int GET_OP(final int i)
   {
      return i >>> POS_OP & MAX_OP;
   }
   
   /**
    * @return argument a of the given instruction
    */
   public static int GET_A(final int i)
   {
      return i >>> POS_A & MAX_A;
   }
   
   /**
    * @return argument a of the given instruction, signed
    */
   public static int GET_SA(final int i)
   {
      return (i >>> POS_A & MAX_A) + MIN_SA;
   }
   
   /**
    * @return argument b of the given instruction
    */
   public static int GET_B(final int i)
   {
      return i >>> POS_B & MAX_B;
   }
   
   /**
    * @return argument b of the given instruction, signed
    */
   public static int GET_SB(final int i)
   {
      return (i >>> POS_B & MAX_B) + MIN_SB;
   }
   
   /**
    * @return argument c of the given instruction
    */
   public static int GET_C(final int i)
   {
      return i >>> POS_C & MAX_C;
   }
   
   /**
    * @return argument c of the given instruction, signed
    */
   public static int GET_SC(final int i)
   {
      return (i >>> POS_C & MAX_C) + MIN_SC;
   }
   
   /**
    * @return the given instruction with the operation code set to the given argument
    */
   public static int SET_OP(final int i, final int arg)
   {
      return i & MASK_NOT_OP | (arg & MAX_OP) << POS_OP;
   }
   
   /**
    * @return the given instruction with argument A set to the given argument
    */
   public static int SET_A(final int i, final int arg)
   {
      return i & MASK_NOT_A | (arg & MAX_A) << POS_A;
   }
   
   /**
    * @return the given instruction with argument A, signed, set to the given argument
    */
   public static int SET_SA(final int i, final int arg)
   {
      return i & MASK_NOT_A | (arg - MIN_SA & MAX_A) << POS_A;
   }
   
   /**
    * @return the given instruction with argument B set to the given argument
    */
   public static int SET_B(final int i, final int arg)
   {
      return i & MASK_NOT_B | (arg & MAX_B) << POS_B;
   }
   
   /**
    * @return the given instruction with argument B, signed, set to the given argument
    */
   public static int SET_SB(final int i, final int arg)
   {
      return i & MASK_NOT_B | (arg - MIN_SB & MAX_B) << POS_B;
   }
   
   /**
    * @return the given instruction with argument C set to the given argument
    */
   public static int SET_C(final int i, final int arg)
   {
      return i & MASK_NOT_C | (arg & MAX_C) << POS_C;
   }
   
   /**
    * @return the given instruction with argument C, signed, set to the given argument
    */
   public static int SET_SC(final int i, final int arg)
   {
      return i & MASK_NOT_C | (arg - MIN_SC & MAX_C) << POS_C;
   }
   
   private Instruction()
   {
   }
}
