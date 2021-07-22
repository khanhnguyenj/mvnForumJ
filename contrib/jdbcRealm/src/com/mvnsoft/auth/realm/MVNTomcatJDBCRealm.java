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

import java.security.Principal;
import java.security.cert.X509Certificate;

import org.apache.catalina.realm.JDBCRealm;

public class MVNTomcatJDBCRealm extends JDBCRealm {

    protected String getPassword(String username) {
        // I don't know why this method is never called
        return super.getPassword(username);
    }

    public Principal authenticate(String username, byte[] credentials) {
        System.out.println("Authenticate byte");
        return super.authenticate(username, credentials);
    }

    public Principal authenticate(String username, String clientDigest, String nOnce, String nc, String cnonce,
            String qop, String realm, String md5a2) {
        //System.out.println("Authenticate username, clientDigest, nOnce, nc, cnonce, qop, realm, md5a2");
        return super.authenticate(username, clientDigest, nOnce, nc, cnonce, qop, realm, md5a2);
    }

    public Principal authenticate(X509Certificate[] certs) {
        //System.out.println("Authenticate X509Certificate");
        return super.authenticate(certs);
    }
}
