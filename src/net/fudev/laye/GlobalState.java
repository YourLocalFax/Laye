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

import java.io.PrintStream;
import java.util.Vector;

import net.fudev.laye.err.VariableUndefined;
import net.fudev.laye.struct.Identifier;
import net.fudev.laye.type.LayeValue;

/**
 * @author Sekai Kyoretsuna
 */
public class GlobalState
{
   private final Vector<Identifier> symbols;
   private final LayeValue[] values;
   
   private PrintStream stdout = System.out;
   
   public GlobalState(Vector<Identifier> symbols)
   {
      this.symbols = symbols;
      values = new LayeValue[symbols.size()];
   }
   
   public LayeValue load(int index)
   {
      return values[index];
   }
   
   public LayeValue load(Identifier name)
   {
      int index = symbols.indexOf(name);
      if (index == -1)
      {
         throw new VariableUndefined(name);
      }
      return values[index];
   }
   
   public void store(int index, LayeValue value)
   {
      values[index] = value;
   }
   
   public void store(Identifier name, LayeValue value)
   {
      int index = symbols.indexOf(name);
      if (index == -1)
      {
         throw new VariableUndefined(name);
      }
      values[index] = value;
   }

   public PrintStream getPrintStream()
   {
      return stdout;
   }
}
