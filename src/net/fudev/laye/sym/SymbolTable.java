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

import java.util.Collections;
import java.util.Vector;

import net.fudev.laye.struct.Identifier;

/**
 * @author Sekai Kyoretsuna
 */
public class SymbolTable
{
   private static final class LocalScope
   {
      public final LocalScope parentScope;
      // public int nextLocal = 0;
      
      public LocalScope(LocalScope parentScope)
      {
         this.parentScope = parentScope;
      }
   }
   
   private final Scope globalScope = new Scope();
   private Scope currentScope;
   private Scope lastScope;
   private LocalScope currentLocalScope;
   
   private int nextGlobalIndex = 0;
   private int nextLocalIndex = 0;
   
   // private Stack<Integer> localIndices = new Stack<Integer>();
   
   public SymbolTable()
   {
      currentScope = globalScope;
   }
   
   public Scope nextScope()
   {
      if (currentScope == null)
      {
         currentScope = globalScope;
      }
      currentScope = currentScope.nextScope;
      return currentScope;
   }
   
   public void beginScope(boolean isLocalScope)
   {
      if (isLocalScope)
      {
         currentLocalScope = new LocalScope(currentLocalScope);
      }
      Scope newScope = new Scope(currentScope);
      if (lastScope != null)
      {
         lastScope.nextScope = newScope;
      }
      else
      {
         globalScope.nextScope = newScope;
      }
      currentScope.addScope(newScope);
      currentScope = newScope;
      lastScope = newScope;
   }
   
   public void endScope(boolean isLocalScope)
   {
      if (isLocalScope)
      {
         currentLocalScope = currentLocalScope.parentScope;
      }
      currentScope = currentScope.parent;
      if (currentScope == globalScope)
      {
         nextLocalIndex = 0;
      }
   }
   
   public Symbol addSymbol(Identifier name)
   {
      if (currentScope.parent != null)
      {
         currentScope.addSymbol(Symbol.Type.LOCAL, name, nextLocalIndex++);
      }
      else
      {
         currentScope.addSymbol(Symbol.Type.GLOBAL, name, nextGlobalIndex++);
      }
      return getSymbol(name);
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
      Vector<Symbol> result = new Vector<>(globalScope.getSymbols());
      Collections.sort(result);
      return result;
   }
}
