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

package net.fudev.laye.analyse;

import net.fudev.laye.debug.Console;
import net.fudev.laye.parse.AstVisitor;
import net.fudev.laye.parse.ast.*;
import net.fudev.laye.struct.Identifier;
import net.fudev.laye.sym.SymbolTable;

/**
 * @author Sekai Kyoretsuna
 */
public class RootVisitor implements AstVisitor
{
   private Console console;
   private SymbolTable symbolTable;
   
   public RootVisitor(Console console, SymbolTable symbolTable)
   {
      this.console = console;
      this.symbolTable = symbolTable;
   }
   
   @Override
   public void accept(Ast node)
   {
      node.forEach(child -> child.visit(this));
   }
   
   @Override
   public void accept(NodeVariableDef node)
   {
      symbolTable.addSymbol(node.name);
   }
   
   @Override
   public void accept(NodeFunctionExpr node)
   {
      console.error("RootVisitor", node.location, "Expression not allowed outside function body.");
      FunctionVisitor visitor = new FunctionVisitor(console, symbolTable);
      node.visit(visitor);
   }
   
   @Override
   public void accept(NodeFunctionDef node)
   {
      symbolTable.addSymbol(node.name);

      FunctionVisitor visitor = new FunctionVisitor(console, symbolTable);
      symbolTable.beginScope(true);

      for (Identifier param : node.params)
      {
         symbolTable.addSymbol(param);
      }
      
      node.body.visit(visitor);
      symbolTable.endScope(true);
   }
   
   @Override
   public void accept(NodeInfixExpression node)
   {
      console.error("RootVisitor", node.location, "Expression not allowed outside function body.");
   }
   
   @Override
   public void accept(NodePostfixExpression node)
   {
      console.error("RootVisitor", node.location, "Expression not allowed outside function body.");
   }
   
   @Override
   public void accept(NodeIdentifier node)
   {
      console.error("RootVisitor", node.location, "Expression not allowed outside function body.");
   }
   
   @Override
   public void accept(NodeFunctionCall node)
   {
      console.error("RootVisitor", node.location, "Expression not allowed outside function body.");
   }
   
   @Override
   public void accept(NodeIntLiteral node)
   {
      console.error("RootVisitor", node.location, "Expression not allowed outside function body.");
   }
   
   @Override
   public void accept(NodeStringLiteral node)
   {
      console.error("RootVisitor", node.location, "Expression not allowed outside function body.");
   }
   
   @Override
   public void accept(NodeExternDecl node)
   {
      symbolTable.addSymbol(node.name);
   }
   
   @Override
   public void accept(NodeBlock node)
   {
      console.error("RootVisitor", node.location, "Expression not allowed outside function body.");
   }
}
