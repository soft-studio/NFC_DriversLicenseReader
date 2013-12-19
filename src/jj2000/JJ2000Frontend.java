/**
 * @version $Id: JJ2000Frontend.java 169 2012-01-15 18:33:24Z mroland $
 *
 * @author Michael Roland <mi.roland@gmail.com>
 *
 * Copyright (c) 2012 Michael Roland
 *
 * ALL RIGHTS RESERVED.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name Michael Roland nor the names of any contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL MICHAEL ROLAND BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jj2000;

import jj2000.j2k.decoder.Decoder;
import jj2000.j2k.util.ParameterList;
import android.graphics.Bitmap;

/**
 * Frontend for JJ2000 library.
 */
public class JJ2000Frontend {
  /** The parameter info, with all possible options. */
  private static String pinfoDecoder[][] = Decoder.getAllParameters();

  public static Bitmap decode(byte[] input) {
    // Get the dfault parameter values
    ParameterList defpl = new ParameterList();
    for (int i = pinfoDecoder.length - 1; i >= 0; --i) {
      if (pinfoDecoder[i][3] != null) {
        defpl.put(pinfoDecoder[i][0], pinfoDecoder[i][3]);
      }
    }

    ParameterList pl = new ParameterList(defpl);

    //pl.setProperty("rate", "3");

    Decoder dec = new Decoder(pl);
    return dec.run(input);
  }
}
