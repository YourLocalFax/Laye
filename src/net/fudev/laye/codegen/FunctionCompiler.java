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

import net.fudev.laye.debug.Console;
import net.fudev.laye.parse.AstVisitor;
import net.fudev.laye.parse.Location;
import net.fudev.laye.parse.ast.*;
import net.fudev.laye.struct.Identifier;
import net.fudev.laye.sym.Symbol;
import net.fudev.laye.sym.SymbolTable;

/**
 * @author Sekai Kyoretsuna
 */
public class FunctionCompiler implements AstVisitor
{
   private final Console console;
   private final SymbolTable symbolTable;
   private final FunctionPrototypeBuilder builder;
   
   public FunctionCompiler(Console console, SymbolTable symbolTable,
         FunctionPrototypeBuilder builder)
   {
      this.console = console;
      this.symbolTable = symbolTable;
      this.builder = builder;
   }
   
   public FunctionPrototype getFunctionPrototype()
   {
      //builder.addOpLoadNull();
      return builder.build();
   }
   
   private void consoleWarning(Location location, String message)
   {
      console.warning("(FunctionCompiler) line " + location.line + " (column " +
                      location.column +"): " + message);
   }
   
   private void consoleError(Location location, String message)
   {
      console.error("(FunctionCompiler) line " + location.line + " (column " +
                    location.column +"): " + message);
   }

   @Override
   public void accept(Ast node)
   {
      node.forEach(child -> child.visit(this));
   }
   
   @Override
   public void accept(NodeVariableDef node)
   {
      Symbol varSymbol;
      if (symbolTable.isSymbolDefined(node.name))
      {
         // TODO make it so this can never fail anyway
         consoleError(node.location, "symbol '" + node.name + "' is already defined.");
         varSymbol = symbolTable.getSymbol(node.name);
      }
      else
      {
         varSymbol = symbolTable.addSymbol(node.name);
      }
      if (node.value != null)
      {
         node.value.visit(this);
      }
      else
      {
         builder.addOpLoadNull();
      }
      switch (varSymbol.type)
      {
         case GLOBAL:
         {
            builder.addOpStoreGlobal(varSymbol.index);
         } break;
         case LOCAL:
         {
            builder.addLocal(node.name);
            builder.addOpStoreLocal(varSymbol.index);
         } break;
      }
   }
   
   @Override
   public void accept(NodeFunctionDef node)
   {
      Symbol fnSymbol;
      if (symbolTable.isSymbolDefined(node.name))
      {
         // TODO make it so this can never fail anyway.
         consoleError(node.location, "symbol '" + node.name + "' is already defined.");
         fnSymbol = symbolTable.getSymbol(node.name);
      }
      else
      {
         fnSymbol = symbolTable.addSymbol(node.name);
      }
      symbolTable.beginScope();
      FunctionPrototypeBuilder newBuilder = new FunctionPrototypeBuilder(builder);
      for (Identifier name : node.params)
      {
         symbolTable.addSymbol(name);
         newBuilder.addParameter(name);
      }
      if (node.isVariadic)
      {
         newBuilder.setIsVariadic();
      }
      FunctionCompiler newCompiler = new FunctionCompiler(console, symbolTable, newBuilder);
      if (node.body != null)
      {
         node.body.visit(newCompiler);
      }
      symbolTable.endScope();
      FunctionPrototype newProto = newCompiler.getFunctionPrototype();
      int newProtoIndex = builder.addNestedFunction(newProto);
      builder.addOpBuildClosure(newProtoIndex);
      switch (fnSymbol.type)
      {
         case GLOBAL:
         {
            builder.addOpStoreGlobal(fnSymbol.index);
         } break;
         case LOCAL:
         {
            builder.addLocal(node.name);
            builder.addOpStoreLocal(fnSymbol.index);
         } break;
      }
   }

   @Override
   public void accept(NodeInfixExpression node)
   {
   }
   
   @Override
   public void accept(NodePostfixExpression node)
   {
   }
   
   @Override
   public void accept(NodeIdentifier node)
   {
      Identifier name = node.name;
      if (!symbolTable.isSymbolDefined(name))
      {
         consoleWarning(node.location, "possible undefined variable '" + name + "'");
      }
      Symbol symbol = symbolTable.getSymbol(name);
      switch (symbol.type)
      {
         case GLOBAL:
         {
            builder.addOpLoadGlobal(symbol.index);
         } break;
         case LOCAL:
         {
            builder.addOpLoadLocal(symbol.index);
         } break;
         default:
            consoleError(node.location, "missing symbol type " + symbol.type + ".");
      }
   }
   
   @Override
   public void accept(NodeFunctionCall node)
   {
      node.target.visit(this);
      node.arguments.forEach(arg -> arg.visit(this));
      builder.addOpInvoke(node.arguments.size());
   }
   
   @Override
   public void accept(NodeIntLiteral node)
   {
      int constIndex = builder.addConstantInt(node.value);
      builder.addOpLoadConst(constIndex);
   }
   
   @Override
   public void accept(NodeStringLiteral node)
   {
      int constIndex = builder.addConstantString(node.value);
      builder.addOpLoadConst(constIndex);
   }
   
   @Override
   public void accept(NodeExternDecl node)
   {
      // TODO error checking, make sure in global scope
      symbolTable.addSymbol(node.name);
   }
   
   @Override
   public void accept(NodeBlock node)
   {
      builder.beginBlock();
      node.body.forEach(n -> n.visit(this));
      builder.endBlock();
   }
}
