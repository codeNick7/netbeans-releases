/*

 ============================================================================
                   The Apache Software License, Version 1.1
 ============================================================================

 Copyright (C) 1999-2003 The Apache Software Foundation. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of  source code must  retain the above copyright  notice,
    this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 3. The end-user documentation included with the redistribution, if any, must
    include  the following  acknowledgment:  "This product includes  software
    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
    Alternately, this  acknowledgment may  appear in the software itself,  if
    and wherever such third-party acknowledgments normally appear.

 4. The names "Batik" and  "Apache Software Foundation" must  not  be
    used to  endorse or promote  products derived from  this software without
    prior written permission. For written permission, please contact
    apache@apache.org.

 5. Products  derived from this software may not  be called "Apache", nor may
    "Apache" appear  in their name,  without prior written permission  of the
    Apache Software Foundation.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Apache Software  Foundation. For more  information on the
 Apache Software Foundation, please see <http://www.apache.org/>.

*/

package org.apache.batik.util.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class represents an object which decodes UTF-8 characters from
 * a stream of bytes.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class UTF8Decoder extends AbstractCharDecoder {
    
    /**
     * The number of bytes of a UTF-8 sequence indexed by the first
     * byte of the sequence.
     */
    protected final static byte[] UTF8_BYTES = {
        1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
        1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
        1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
        1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
        2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,
        3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0,0,
    };

    /**
     * The next char, in case of a 4 bytes sequence.
     */
    protected int nextChar = -1;

    /**
     * Creates a new UTF8Decoder.
     */
    public UTF8Decoder(InputStream is) {
        super(is);
    }

    /**
     * Reads the next character.
     * @return a character or END_OF_STREAM.
     */
    public int readChar() throws IOException {
        if (nextChar != -1) {
            int result = nextChar;
            nextChar = -1;
            return result;
        }
        if (position == count) {
            fillBuffer();
        }
        if (count == -1) {
            return END_OF_STREAM;
        }
        int b1 = buffer[position++] & 0xff;
        switch (UTF8_BYTES[b1]) {
        default:
            charError("UTF-8");

        case 1:
            return b1;

        case 2:
            if (position == count) {
                fillBuffer();
            }
            if (count == -1) {
                endOfStreamError("UTF-8");
            }
            return ((b1 & 0x1f) << 6) | (buffer[position++] & 0x3f);

        case 3:
            if (position == count) {
                fillBuffer();
            }
            if (count == -1) {
                endOfStreamError("UTF-8");
            }
            int b2 = buffer[position++];
            if (position == count) {
                fillBuffer();
            }
            if (count == -1) {
                endOfStreamError("UTF-8");
            }
            int b3 = buffer[position++];
            if ((b2 & 0xc0) != 0x80 || (b3 & 0xc0) != 0x80) {
                charError("UTF-8");
            }
            return ((b1 & 0x1f) << 12) | ((b2 & 0x3f) << 6) | (b3 & 0x1f);

        case 4:
            if (position == count) {
                fillBuffer();
            }
            if (count == -1) {
                endOfStreamError("UTF-8");
            }
            b2 = buffer[position++];
            if (position == count) {
                fillBuffer();
            }
            if (count == -1) {
                endOfStreamError("UTF-8");
            }
            b3 = buffer[position++];
            if (position == count) {
                fillBuffer();
            }
            if (count == -1) {
                endOfStreamError("UTF-8");
            }
            int b4 = buffer[position++];
            if ((b2 & 0xc0) != 0x80 ||
                (b3 & 0xc0) != 0x80 ||
                (b4 & 0xc0) != 0x80) {
                charError("UTF-8");
            }
            int c = ((b1 & 0x1f) << 18)
                | ((b2 & 0x3f) << 12)
                | ((b3 & 0x1f) << 6)
                | (b4 & 0x1f);
            nextChar = (c - 0x10000) % 0x400 + 0xdc00;            
            return (c - 0x10000) / 0x400 + 0xd800;
        }
    }
}
