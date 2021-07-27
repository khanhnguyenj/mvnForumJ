/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/security/Encoder.java,v 1.34 2009/01/21 07:51:29 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.34 $
 * $Date: 2009/01/21 07:51:29 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding MyVietnam and MyVietnam CoreLib
 * MUST remain intact in the scripts and source code.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Minh Nguyen  
 * @author: Mai  Nguyen  
 */
package net.myvietnam.mvncore.security;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import net.myvietnam.mvncore.misc.Base64;
import net.myvietnam.mvncore.util.MailUtil;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Encoder {

    private static final Logger log = LoggerFactory.getLogger(Encoder.class);

    // Please note that 2 below methods are used in #getMD5_Base64 only
    // use them in other methods will make it not thread-safe
    private static MessageDigest digest = null;

    private static boolean isInited = false;

    private static MessageDigest digest_ = null;

    private static boolean isInited_ = false;

    private Encoder() {
    }

    /**
     * This method return a String that has been encrypted as MD5 and then escaped using Base64.
     * This method should be used to encrypt all password for maximum security.
     * @param input String the string that need encrypted
     * @return String the string after encrypted MD5 and base64
     */
    public static String getMD5_Base64(String input) {
        byte[] rawData = getMD5(input);
        byte[] encoded = Base64.encode(rawData);
        String retValue = new String(encoded);
        return retValue;
    }
    
    /**
     * This method return a String that has been encrypted as MD5 and then escaped using Hex.
     * This method should be used to encrypt all password for maximum security.
     * @param input String the string that need encrypted
     * @return String the string after encrypted MD5 and Hex
     */
    public static String getMD5_Hex(String input) {
        byte[] rawData = getMD5(input);
        char[] encoded = Hex.encodeHex(rawData);
        String retValue = new String(encoded);
        return retValue;
    }
    
    public static synchronized byte[] getMD5(String input) {
        // please note that we don't use digest, because if we
        // cannot get digest, then the second time we have to call it
        // again, which will fail again
        if (isInited == false) {
            isInited = true;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (Exception ex) {
                log.error("Cannot get MessageDigest. Application may fail to run correctly.", ex);
            }
        }
        if (digest == null) {
            throw new IllegalStateException("Cannot get the MD5 digest.");
        }

        // now everything is OK, go ahead
        try {
            digest.update(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            log.error("Assertion: This should never occur.");
        }
        byte[] rawData = digest.digest();
        return rawData;
    }

    public static synchronized String getString_Base64(String input, String algorithm) {
        if (isInited_ == false) {
            isInited_ = true;
            try {
                digest_ = MessageDigest.getInstance(algorithm);
            } catch (Exception ex) {
                log.error("Cannot get MessageDigest. Application may fail to run correctly.", ex);
            }
        }
        if (digest_ == null) {
            return input;
        }

        try {
            digest_.update(input.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException ex) {
            log.error("Assertion: This should never occur.");
        }
        byte[] rawData = digest_.digest();
        byte[] encoded = Base64.encode(rawData);
        String retValue = new String(encoded);
        return retValue;
    }

    public static String encrypt_AES_to_HEX(String input, String key)
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
        IllegalBlockSizeException, BadPaddingException {

        byte[] desKeyData = key.getBytes();
    
        SecretKeySpec secretKey = new SecretKeySpec(desKeyData, "AES");
    
        Cipher c = Cipher.getInstance("AES");
    
        c.init(Cipher.ENCRYPT_MODE, secretKey);
    
        byte[] cipherText = c.doFinal(input.getBytes());
        String retValue = new String(Hex.encodeHex(cipherText));
        return retValue;
    }

    public static String decrypt_AES_from_HEX(String input, String key)
        throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, 
        NoSuchPaddingException, InvalidKeyException {
        
        char[] chrs = input.toCharArray();
        byte[] inputBytes;
        try {
            inputBytes = Hex.decodeHex(chrs);
        } catch (Exception e) {
            log.error("Cannot decodeHex", e);
            throw new IllegalArgumentException("The input hex is not valid.");
        }

        byte[] desKeyData = key.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(desKeyData, "AES");
    
        // get cipher object for password-based encryption
        Cipher c = Cipher.getInstance("AES");
    
        c.init(Cipher.DECRYPT_MODE, secretKey);
    
        // Decrypt the ciphertext
        byte[] cleartext = c.doFinal(inputBytes);
        String retValue = new String(cleartext);
        return retValue;
    }

    /**
     * This method just call URLEncoder.encode() with the default encoding is UTF-8
     * @param input String
     * @return String
     */
    public static String encodeURL(String input) {
        try {
            input = input.replaceAll("%", "%25");// fix bug in JDK that does not encode character %
            return URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Assertion, should never happen", e);
            throw new AssertionError("System error invoking URLEncoder.encode()");
        }
    }

    /**
     * This method just call URLDecoder.decode() with the default encoding UTF-8
     * @param input String
     * @return String
     */
    public static String decodeURL(String input) {
        try {
            return URLDecoder.decode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Assertion, should never happen", e);
            throw new AssertionError("System error invoking URLDecoder.decode()");
        }
    }

    /**
     * Filter a URL to make it safe, this method is used in class URLFilter
     * @param url String a URL to be filtered
     * @return String a URL that has been filtered
     */
    public static String filterUrl(String url) {
        String lowerUrl = url.toLowerCase();
        if ((lowerUrl.indexOf("javascript:") >= 0) || lowerUrl.indexOf("file:") >= 0) {
            return "";
        }

        String protocol = "http://";//default protocol
        String name = null;
        if (url.startsWith("http://")) {
            protocol = "http://";
            name = url.substring(protocol.length());// must duplicate it because of the default protocol
        } else if (url.startsWith("https://")) {
            protocol = "https://";
            name = url.substring(protocol.length());// must duplicate it because of the default protocol
        } else if (url.startsWith("ftp://")) {
            protocol = "ftp://";
            name = url.substring(protocol.length());// must duplicate it because of the default protocol
        } else if (url.startsWith("mailto:")) {
            protocol = "mailto:";
            name = url.substring(protocol.length());// must duplicate it because of the default protocol
        } else {
            name = url;
        }
        String ret;
        if (protocol.equals("mailto:")) {
            try {
                MailUtil.checkGoodEmail(name, null);
                ret = protocol + name;
            } catch (Exception ex) {
                ret = "";
            }
        } else {
            ret = protocol + encodePath(name);
        }
        return ret;
    }

    /**
     *
     * @param path the path, something like this localhost:8080/image/index.html
     * @return the path after being encoded
     */
    public static String encodePath(String path) {
        path = removeInvalidUserInURL(path);
        path = removeJSessionIDInURL(path);
        return path;
        /*
         String ret = "";
         int indexFirstSlash = path.indexOf('/');
         if ( indexFirstSlash != -1 ) {
         String hostport = path.substring(0, indexFirstSlash);
         int indexFirstColon = hostport.indexOf(':');
         if (indexFirstColon != -1) {
         String host = hostport.substring(0, indexFirstColon);
         String port = hostport.substring(indexFirstColon + 1);
         hostport = Encoder.encodeURL(host) + ":" + Encoder.encodeURL(port);
         } else {
         hostport = Encoder.encodeURL(hostport);
         }
         String filename = path.substring(indexFirstSlash + 1);
         filename = Encoder.encodeURL(filename);
         ret = hostport + "/" + filename;
         } else {
         ret = Encoder.encodeURL(path);
         }
         return ret;
         */
    }

    /**
     * This method is used to fix IE spoof URL bug:
     * http://originalsite.com % 0 0 @ www.badsite.com
     * <p>(remove the space above, I added spaces because
     * McAfee complain that it is a trojan)
     * @param path String
     * @return String
     */
    private static String removeInvalidUserInURL(String path) {
        // atIndex is the RIGHT most of @
        int atIndex = path.lastIndexOf('@');
        if (atIndex != -1) {
            // has the user info part
            // percentIndex is the LEFT most of %
            int pecentIndex = path.indexOf('%');
            if ((pecentIndex != -1) && (pecentIndex < atIndex)) {
                // user info part has % in it, very likely a spoof URL
                return path.substring(atIndex + 1);// get the char right after @
            }
        }
        return path;
    }

    /**
     * @param path String
     * @return String
     */
    private static String removeJSessionIDInURL(String path) {
        // startIndex is the index of ;jsessionid=
        // endIndex is the index of ?
        int startIndex = path.indexOf(";jsessionid=");
        if (startIndex != -1) {
            int endIndex = path.indexOf('?');
            if (endIndex != -1) {
                return path.substring(0, startIndex) + path.substring(endIndex);
            } 
            return path.substring(0, startIndex);
        }        
        return path;
    }

    /*
     public static void main(String[] args) {
     //test data should be
     //a1            iou3zTQ6oq2Zt9diAwhXog==
     //Hello World   sQqNsWTgdUEFt6mb5y4/5Q==

     //String testString = "a1";
     //String encrypted = getMD5_Base64(testString);
     //System.out.println("encrypted = " + encrypted);
     //System.out.println("length = " + encrypted.length());


     //String encodeString = "Di\u1ec5n \u0111\u00e0n";//Die^~n d-a`n
     //JDK 1.3 result : Di%3Fn+%3F%E0n
     //JDK 1.4 result : Di%E1%BB%85n+%C4%91%C3%A0n
     //System.out.println("encodeURL input '" + encodeString + "' output '" + encodeURL(encodeString) + "'");

     //String decodeString = "Di%E1%BB%85n+%C4%91%C3%A0n";//encoded of "Die^~n d-a`n"
     //System.out.println("decodeURL input '" + decodeString + "' output '" + decodeURL(decodeString) + "'");
     }*/

}
