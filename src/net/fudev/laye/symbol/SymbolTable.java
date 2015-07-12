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

package net.fudev.laye.symbol;

import java.util.Collections;
import java.util.Vector;

import net.fudev.laye.struct.Identifier;

/**
 * @author Sekai Kyoretsuna
 */
public class SymbolTable
{
   private final Scope globalScope = new Scope();
   private Scope currentScope;
   
   private int nextGlobalIndex = 0;
   private int nextLocalIndex = 0;
   
   public SymbolTable()
   {
      currentScope = globalScope;
   }
   
   public void beginScope()
   {
      Scope newScope = new Scope(currentScope);
      currentScope.addScope(newScope);
      currentScope = newScope;
   }
   
   public void endScope()
   {
      currentScope = currentScope.parent;
      if (currentScope == globalScope)
      {
         nextLocalIndex = 0;
      }
   }
   
   public void addSymbol(Identifier name)
   {
      if (currentScope == globalScope)
      {
         currentScope.addSymbol(Symbol.Type.GLOBAL, name, nextGlobalIndex++);
      }
      else
      {
         currentScope.addSymbol(Symbol.Type.LOCAL_VALUE, name, nextLocalIndex++);
      }
   }
   
   public boolean isSymbolDefined(Identifier name)
   {
      return getSymbol(name) != null;
   }
   
   public Symbol getSymbol(Identifier name)
   {
      Scope scope = currentScope;
      while (scope != null)
      {
         Symbol symbol = scope.getSymbol(name);
         if (symbol != null)
         {
            return symbol;
         }
         scope = scope.parent;
      }
      return null;
   }
   
   public Vector<Symbol> getGlobalSymbols()
   {
      Vector<Symbol> result = new Vector<>(globalScope.getGlobalValueSymbols());
      Collections.sort(result);
      return result;
   }

   public Vector<Symbol> getCurrentLocalSymbols()
   {
      return currentScope.getLocalValueSymbols();
   }

   public Vector<Symbol> getCurrentUpValueSymbols()
   {
      return currentScope.getUpValueSymbols();
   }
}
