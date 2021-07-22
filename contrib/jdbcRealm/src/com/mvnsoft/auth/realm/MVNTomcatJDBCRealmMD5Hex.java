/*
 * Copyright (c) 2005 Kurt Miller <truk@optonline.net>
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

/* This file rewritten from a sample implementation of JDBCRealm written by Kurt Miller.
 * 
 * We configure realm with no digest, then encode it when authenticating 
 * instead of decoding database credentials. Because We don't know why 
 * MVNTomcatJDBCRealm is never called  
 * 
 * View Thread: http://www.mvnforum.com/mvnforum/mvnforum/viewthread?thread=2782
 */
package com.mvnsoft.auth.realm;

import java.security.MessageDigest;
import java.security.Principal;

public class MVNTomcatJDBCRealmMD5Hex extends MVNTomcatJDBCRealm {

    public String getMD5_Hex(String input) {
        // please note that we don't use digest, because if we
        // cannot get digest, then the second time we have to call it
        // again, which will fail again
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (Exception ex) {
            System.out.println("Cannot get MessageDigest. Application may fail to run correctly.");
        }
        if (digest == null) {
            return input;
        }
        if (input == null) {
            return null;
        }

        // now everything is ok, go ahead
        try {
            digest.update(input.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException ex) {
            System.out.println("Assertion: This should never occur.");
        }
        byte[] rawData = digest.digest();

        char encoded[] = HexEncoder.encodeHex(rawData);
        String retValue = new String(encoded);
        
        return retValue;
    }

    protected String digest(String credentials) {
        System.out.println("MVNTomcatJDBCRealmMD5Hex.digest()");
        //return super.digest(credentials);
        return getMD5_Hex(credentials);
    }

    /**
     * This is a sample implementation of JDBCRealm using password
     */

    public Principal authenticate(String username, String password) {
        String md5_hex = getMD5_Hex(password);
        System.out.println("MVNTomcatJDBCRealmMD5Hex.authenticate(username, password) with username = " + username);
        //System.out.println("Authenticate 2 params " + username + " and " + md5_base64);
        //return super.authenticate(username, md5_base64);
        return super.authenticate(username, password);
    }

}
