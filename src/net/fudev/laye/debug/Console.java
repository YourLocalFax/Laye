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

import java.io.InputStream;
import java.util.Objects;

/**
 * @author Sekai Kyoretsuna
 */
public class Console
{
   public static final ConsoleOutput DEFAULT_OUTPUT = (level, message) ->
   {
      switch (level)
      {
         case INFO:
            System.out.println("[INFO] " + message);
            break;
         case WARNING:
            System.out.println("[WARNING] " + message);
            break;
         case ERROR:
            System.err.println("[ERROR] " + message);
            break;
      }
   };
   
   private ConsoleOutput output = DEFAULT_OUTPUT;
   private InputStream input = System.in;
   
   public Console()
   {
   }
   
   public void write(OutputLevel level, String message)
   {
      output.write(level, message);
   }
   
   public void info(Object object)
   {
      info(Objects.toString(object));
   }
   
   public void info(String message)
   {
      write(OutputLevel.INFO, message);
   }
   
   public void warning(String message)
   {
      write(OutputLevel.WARNING, message);
   }
   
   public void error(String message)
   {
      write(OutputLevel.ERROR, message);
   }
}
