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
package io.ylf.laye.vm;

import java.util.HashMap;

import io.ylf.laye.struct.Identifier;

/**
 * @author Sekai Kyoretsuna
 */
public class LayeKit extends LayeObject
{
   public final Identifier name;
   
   private final HashMap<Identifier, LayeObject> slots = new HashMap<>();
   
   private boolean initialized = false;
   
   private LayeKit(Identifier name)
   {
      this.name = name;
   }
   
   // ===== Load Slots
   
   public LayeObject loadSlot(Identifier name)
   {
      return slots.get(name);
   }
   
   public LayeObject loadSlot(String name)
   {
      return loadSlot(Identifier.get(name));
   }
   
   // ===== Store Slots
   
   public void storeSlot(Identifier name, LayeObject value)
   {
      slots.put(name, value);
   }
   
   public void storeSlot(String name, LayeObject value)
   {
      storeSlot(Identifier.get(name), value);
   }
   
   // =====
   
   public void initialize(VirtualMachine vm)
   {
      if (initialized)
      {
         return;
      }
      initialized = true;
      // TODO initialize kits
   }
}
