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

package net.fudev.laye.parse.ast;

import java.util.Iterator;
import java.util.Vector;

import net.fudev.laye.parse.AstVisitor;
import net.fudev.laye.parse.Location;

/**
 * @author Sekai Kyoretsuna
 */
public abstract class AstNode implements Iterable<AstNode>
{
   private final Vector<AstNode> children = new Vector<>();
   
   public final Location location;
   
   public boolean hasResult = true;
   
   public AstNode(Location location)
   {
      this.location = location;
   }
   
   public abstract void visit(AstVisitor visitor);
   
   public void addChild(AstNode child)
   {
      children.addElement(child);
   }

   @Override
   public Iterator<AstNode> iterator()
   {
      return children.iterator();
   }
}
