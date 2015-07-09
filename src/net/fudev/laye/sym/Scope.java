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

package net.fudev.laye.sym;

import java.util.Vector;

import net.fudev.laye.struct.Identifier;

/**
 * @author Sekai Kyoretsuna
 */
public class Scope
{
   private final Vector<Symbol> symbols = new Vector<>();
   private final Vector<Scope> children = new Vector<>();
   
   public final Scope parent;
   
   public Scope()
   {
      parent = null;
   }
   
   public Scope(Scope parent)
   {
      this.parent = parent;
   }
   
   public int addSymbol(Symbol.Type type, Identifier name, int index)
   {
      symbols.addElement(new Symbol(type, name, index));
      return index;
   }
   
   public void addScope(Scope child)
   {
      children.addElement(child);
   }
   
   public Symbol getSymbol(Identifier name)
   {
      for (Symbol symbol : symbols)
      {
         if (symbol.name.equals(name))
         {
            return symbol;
         }
      }
      return null;
   }

   Vector<Symbol> getSymbols()
   {
      return symbols;
   }
}
