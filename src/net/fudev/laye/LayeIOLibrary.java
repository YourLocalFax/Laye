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

import net.fudev.laye.struct.Identifier;
import net.fudev.laye.type.LayeExternFunction;
import net.fudev.laye.type.LayeValue;

/**
 * @author Sekai Kyoretsuna
 */
public final class LayeIOLibrary
{
   public static void register(GlobalState state)
   {
      state.store(Identifier.get("Print"), new LayeExternFunction((vm, args) ->
      {
         StringBuilder builder = new StringBuilder();
         for (int i = 0; i < args.length; i++)
         {
            if (i > 0)
            {
               builder.append(' ');
            }
            builder.append(args[i]);
         }
         vm.getState().getPrintStream().println(builder.toString());
         return LayeValue.NULL;
      }));
   }
   
   private LayeIOLibrary()
   {
   }
}
