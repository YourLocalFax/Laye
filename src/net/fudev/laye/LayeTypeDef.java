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

import java.util.HashMap;
import java.util.Map;

import net.fudev.laye.codegen.TypePrototype;
import net.fudev.laye.err.VariableDoesNotExist;
import net.fudev.laye.struct.Identifier;
import net.fudev.laye.type.LayeException;
import net.fudev.laye.type.LayeValue;
import net.fudev.laye.util.Assert;
import net.fudev.laye.vm.LayeVM;

/**
 * @author Sekai Kyoretsuna
 */
public class LayeTypeDef extends TypeDef
{
   private static Map<TypePrototype, LayeTypeDef> cache = new HashMap<TypePrototype, LayeTypeDef>();
   
   public static LayeTypeDef get(TypePrototype type, LayeTypeDef baseType)
   {
      if (type == null)
      {
         return null;
      }
      LayeTypeDef result = cache.get(type);
      if (result == null)
      {
         result = new LayeTypeDef(type, baseType);
         cache.put(type, result);
      }
      return result;
   }
   
   private final Map<Identifier, LayeValue> fields = new HashMap<Identifier, LayeValue>();
   
   public final TypePrototype type;
   public final LayeTypeDef baseType;
   
   private LayeTypeDef(TypePrototype type, LayeTypeDef baseType)
   {
      this.type = type;
      this.baseType = baseType;
   }
   
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((baseType == null) ? 0 : baseType.hashCode());
      result = prime * result + ((fields == null) ? 0 : fields.hashCode());
      result = prime * result + ((type == null) ? 0 : type.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (!(obj instanceof LayeTypeDef))
      {
         return false;
      }
      LayeTypeDef other = (LayeTypeDef) obj;
      if (baseType == null)
      {
         if (other.baseType != null)
         {
            return false;
         }
      }
      else if (!baseType.equals(other.baseType))
      {
         return false;
      }
      if (fields == null)
      {
         if (other.fields != null)
         {
            return false;
         }
      }
      else if (!fields.equals(other.fields))
      {
         return false;
      }
      if (type == null)
      {
         if (other.type != null)
         {
            return false;
         }
      }
      else if (!type.equals(other.type))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "Type:" + hashCode();
   }

   /* (non-Javadoc)
    * @see net.fudev.laye.TypeDef#newInstance(net.fudev.laye.vm.LayeVM, net.fudev.laye.type.LayeValue[])
    */
   @Override
   public LayeValue newInstance(LayeVM vm, LayeValue[] args)
   {
      return null;
   }

   /* (non-Javadoc)
    * @see net.fudev.laye.TypeDef#newInstance(net.fudev.laye.vm.LayeVM, java.lang.String, net.fudev.laye.type.LayeValue[])
    */
   @Override
   public LayeValue newInstance(LayeVM vm, String ctorName, LayeValue[] args)
   {
      return null;
   }
   
   /**
    * Assigns the given value to the variable id. If the variable does not exist, it is created. Both the value and id
    * cannot be null.
    * 
    * @param id
    * @param value
    */
   public void putField(LayeVM vm, Identifier id, LayeValue value)
   {
      Assert.notNull(vm, id, Identifier.get("id"));
      Assert.notNull(vm, value, Identifier.get("value"));
      fields.put(id, value);
   }
   
   /**
    * Assigns the given value to the variable id if it exists. If the variable does not exist, the method fails and a
    * {@link VariableDoesNotExist} exception is thrown. Both the value and id cannot be null.
    * 
    * @param id
    * @param value
    */
   public void setField(LayeVM vm, Identifier id, LayeValue value)
   {
      Assert.notNull(vm, id, Identifier.get("id"));
      Assert.notNull(vm, value, Identifier.get("value"));
      if (!fields.containsKey(id))
      {
         vm.raiseException(new LayeException.NoSuchField(id));
         return;
      }
      fields.put(id, value);
   }
   
   /**
    * Attempts to get a value at the given variable id. If the variable does not exist, the method fails and a
    * {@link VariableDoesNotExist} exception is thrown. Otherwise the value stored at the id is returned. The id cannot
    * be null.
    * 
    * @param id
    * @return
    */
   public LayeValue getField(LayeVM vm, Identifier id)
   {
      Assert.notNull(vm, id, Identifier.get("id"));
      LayeValue result = fields.get(id);
      if (result == null)
      {
         vm.raiseException(new LayeException.NoSuchField(id));
         return LayeValue.NULL;
      }
      return result;
   }

   /* (non-Javadoc)
    * @see net.fudev.laye.TypeDef#getIndex(net.fudev.laye.vm.LayeVM, net.fudev.laye.struct.Identifier)
    */
   @Override
   public LayeValue getIndex(LayeVM vm, Identifier identifier)
   {
      return null;
   }

   /* (non-Javadoc)
    * @see net.fudev.laye.TypeDef#setIndex(net.fudev.laye.vm.LayeVM, net.fudev.laye.struct.Identifier, net.fudev.laye.type.LayeValue)
    */
   @Override
   public void setIndex(LayeVM vm, Identifier identifier, LayeValue value)
   {
   }
}
