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

package net.fudev.laye.debug;

import java.util.Objects;

import net.fudev.laye.parse.AstVisitor;
import net.fudev.laye.parse.ast.*;

/**
 * @author Sekai Kyoretsuna
 */
public class AstViewer implements AstVisitor
{
   private final Console console;
   
   private int tabs = 0;
   
   public AstViewer(Console console)
   {
      this.console = console;
   }
   
   private void print(Object object)
   {
      print(Objects.toString(object));
   }
   
   private void print(String message)
   {
      StringBuilder output = new StringBuilder();
      for (int i = 0; i < tabs; i++)
      {
         output.append("   ");
      }
      console.info(output.append(message).toString());
   }
   
   @Override
   public void accept(Ast node)
   {
      node.forEach((child) -> child.visit(this));
   }
   
   @Override
   public void accept(NodeFunctionDef node)
   {
      print("Function " + node.name);
      tabs += 2;
      node.params.forEach((param) -> print(param.image));
      tabs--;
      node.body.visit(this);
      tabs--;
   }
   
   @Override
   public void accept(NodeInfixExpression node)
   {
      node.left.visit(this);
      tabs += 2;
      print(node.operator);
      node.right.visit(this);
      tabs -= 2;
   }
   
   @Override
   public void accept(NodePostfixExpression node)
   {
      node.left.visit(this);
      tabs += 2;
      print(node.operator);
      tabs -= 2;
   }
   
   @Override
   public void accept(NodeIdentifier nodeIdentifier)
   {
      print(nodeIdentifier.name);
   }
   
   @Override
   public void accept(NodeFunctionCall node)
   {
      // print("Function Call:");
      node.target.visit(this);
      tabs += 2;
      node.arguments.forEach((child) -> child.visit(this));
      tabs -= 2;
   }
   
   @Override
   public void accept(NodeStringLiteral node)
   {
      print("\"" + node.value + "\"");
   }
   
   @Override
   public void accept(NodeExternDecl node)
   {
      print("Extern " + node.name);
   }
   
   @Override
   public void accept(NodeBlock node)
   {
      for (AstNode n : node.body)
      {
         n.visit(this);
      }
   }
}
