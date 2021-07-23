/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/util/MD5.java,v 1.6 2007/01/15 10:27:31 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.6 $
 * $Date: 2007/01/15 10:27:31 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain 
 * intact in the scripts and in the outputted HTML.
 * The "powered by" text/logo with a link back to
 * http://www.mvnForum.com and http://www.MyVietnam.net in 
 * the footer of the pages MUST remain visible when the pages
 * are viewed on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Support can be obtained from support forums at:
 * http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: 
 */
package org.mvnforum.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

//==============================================================================
//  The JavaReference.com Software License, Version 1.0 
//  Copyright (c) 2002-2005  JavaReference.com. All rights reserved.
//
//  
//  Redistribution and use in source and binary forms, with or without 
//  modification, are permitted provided that the following conditions 
//  are met: 
//  
//  1. Redistributions of source code must retain the above copyright notice, 
//     this list of conditions and the following disclaimer. 
//  
//  2. Redistributions in binary form must reproduce the above copyright notice, 
//     this list of conditions and the following disclaimer in the documentation 
//     and/or other materials provided with the distribution. 
//     
//  3. The end-user documentation included with the redistribution, if any, must 
//     include the following acknowlegement: 
//     
//     "This product includes software developed by the Javareference.com 
//     (http://www.javareference.com/)." 
//     
//     Alternately, this acknowlegement may appear in the software itself, if and 
//     wherever such third-party acknowlegements normally appear. 
//     
//  4. The names "JavaReference" and "Javareference.com", must not be used to 
//     endorse or promote products derived from this software without prior written 
//     permission. For written permission, please contact webmaster@javareference.com. 
//     
//  5. Products derived from this software may not be called "Javareference" nor may 
//     "Javareference" appear in their names without prior written permission of  
//     Javareference.com. 
//     
//  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
//  INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
//  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
//  JAVAREFERENCE.COM OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
//  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
//  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
//  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
//  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
//  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
//  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
//  
//================================================================================ 
//  Software from this site consists of contributions made by various individuals 
//  on behalf of Javareference.com. For more information on Javareference.com, 
//  please see http://www.javareference.com 
//================================================================================

/**
 * @author anandh
 */
public class MD5 {

    static char[] carr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String getBase64FromHEX(String input) {
        
        byte barr[] = new byte[16];
        int bcnt = 0;
        for (int i = 0; i < 32; i += 2) {
            char c1 = input.charAt(i);
            char c2 = input.charAt(i + 1);
            int i1 = intFromChar(c1);
            int i2 = intFromChar(c2);

            barr[bcnt] = 0;
            barr[bcnt] |= (byte) ((i1 & 0x0F) << 4);
            barr[bcnt] |= (byte) (i2 & 0x0F);
            bcnt++;
        }

        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(barr);
    }

    public static synchronized String getMD5_Base64(String input) {
        // please note that we dont use digest, because if we
        // cannot get digest, then the second time we have to call it
        // again, which will fail again
        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (digest == null)
            return input;

        // now everything is ok, go ahead
        try {
            digest.update(input.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        byte[] rawData = digest.digest();
        BASE64Encoder bencoder = new BASE64Encoder();
        return bencoder.encode(rawData);
    }

    private static int intFromChar(char c) {
        char clower = Character.toLowerCase(c);
        for (int i = 0; i < carr.length; i++) {
            if (clower == carr[i]) {
                return i;
            }
        }

        return 0;
    }

    public static void main(String[] args) {
        
        //String password = args[0];
        String password = "test";

        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            digest.update(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        byte[] rawData = digest.digest();
        StringBuffer printable = new StringBuffer();

        for (int i = 0; i < rawData.length; i++) {
            printable.append(carr[((rawData[i] & 0xF0) >> 4)]);
            printable.append(carr[(rawData[i] & 0x0F)]);
        }
        String phpbbPassword = printable.toString();

        System.out.println("PHPBB           : " + phpbbPassword);
        System.out.println("MVNFORUM        : " + getMD5_Base64(password));
        System.out.println("PHPBB->MVNFORUM : " + getBase64FromHEX(phpbbPassword));
    }

}
