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

import java.io.IOException;
import java.util.Vector;

import net.fudev.laye.codegen.FunctionPrototype;
import net.fudev.laye.codegen.LayeCompiler;
import net.fudev.laye.debug.AstViewer;
import net.fudev.laye.debug.Console;
import net.fudev.laye.lex.Lexer;
import net.fudev.laye.lex.TokenStream;
import net.fudev.laye.parse.Parser;
import net.fudev.laye.parse.ast.Ast;
import net.fudev.laye.struct.Identifier;
import net.fudev.laye.type.LayeClosure;
import net.fudev.laye.vm.LayeVM;

/**
 * @author Sekai Kyoretsuna
 */
public final class LayeTest
{
   public static void main(String[] args) throws IOException
   {
      LayeFile source = LayeFile.file("./examples/hello_world.laye");
      
      Console console = new Console();
      
      Lexer lexer = new Lexer(console);
      TokenStream tokens = lexer.getTokens(source);
      
      Parser parser = new Parser(console);
      Ast ast = parser.parse(tokens);
      
      if (ast == null)
      {
         console.error("Parse failure.");
         return;
      }
      
      AstViewer viewer = new AstViewer(console);
      viewer.accept(ast);
      
      LayeCompiler compiler = new LayeCompiler(console);
      FunctionPrototype fnProto = compiler.compile(ast);
      
      LayeClosure rootClosure = new LayeClosure(null, fnProto);
      
      Vector<Identifier> globalSymbolNames = compiler.getGlobalSymbolNames();
      GlobalState state = new GlobalState(globalSymbolNames);
      
      LayeIOLibrary.register(state);
      
      LayeVM vm = new LayeVM(state);
      vm.invoke(rootClosure, null);
      vm.invoke(state.load(Identifier.get("Main")), null);
   }
   
   private LayeTest()
   {
   }
}
