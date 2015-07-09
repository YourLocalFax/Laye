/**
 * Copyright (C) 2015 Sekai Kyoretsuna
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package net.fudev.laye.vm;

/**
 * @author Sekai Kyoretsuna
 */
public final class OpCode
{
   public static final int CLOSE_UP_VALUES = 0x00;
   public static final int POP = 0x01;
   public static final int DUP = 0x02;
   
   public static final int LOAD_LOCAL = 0x03;
   public static final int STORE_LOCAL = 0x04;
   public static final int LOAD_GLOBAL = 0x05;
   public static final int STORE_GLOBAL = 0x06;
   public static final int LOAD_UPVAL = 0x07;
   public static final int STORE_UPVAL = 0x08;
   public static final int LOAD_INDEX = 0x09;
   public static final int STORE_INDEX = 0x0A;
   
   public static final int LOAD_BOOL = 0x0B;
   public static final int LOAD_NULL = 0x0C;
   public static final int LOAD_CONST = 0x0D;
   
   public static final int BUILD_CLOSURE = 0x0E;
   public static final int BUILD_TYPE = 0x0F;
   
   public static final int PREFIX = 0x10;
   public static final int POSTFIX = 0x11;
   public static final int INFIX = 0x12;
   public static final int TYPEOF = 0x13;
   
   public static final int AND = 0x14;
   public static final int OR = 0x15;
   public static final int XOR = 0x16;
   public static final int IS_TYPEOF = 0x17;
   
   public static final int TEST = 0x18;
   public static final int JUMP = 0x19;
   
   public static final int INVOKE = 0x1A;
   public static final int INVOKE_METHOD = 0x1B;
   public static final int TAIL_CALL = 0x1C;
   public static final int RETURN = 0x1D;
   public static final int YIELD = 0x1E;
   public static final int RESUME = 0x1F;
   
   public static final int THIS = 0x20;
   public static final int BASE = 0x21;
   public static final int REF = 0x22;
   public static final int DEREF = 0x23;
   public static final int IS = 0x24;
   
   public static final int LIST = 0x25;
   public static final int TUPLE = 0x26;
   public static final int NEW = 0x27;
   
   public static final int FOR_PREP = 0x28;
   public static final int FOR_TEST = 0x29;
   public static final int FOR_EACH = 0x2A;
   public static final int POST_FOR_EACH = 0x2B;
   
   public static final int MATCH = 0x2C;
   
   public static final int TRY_START = 0x2D;
   public static final int TRY_END = 0x2E;

   public static final int NUM_OPCODES = 0x2F;

   private OpCode()
   {
   }
}
