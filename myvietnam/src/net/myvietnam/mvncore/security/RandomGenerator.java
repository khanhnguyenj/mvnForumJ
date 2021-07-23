/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/security/RandomGenerator.java,v 1.6 2009/01/21 07:51:29 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.6 $
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

import org.apache.commons.lang.RandomStringUtils;

public final class RandomGenerator {

    // prevent instantiation
    private RandomGenerator() {
    }

    public static String getRandomMD5_Base64() {
        String randomString = getRandomString();
        return Encoder.getMD5_Base64(randomString);
    }

    public static String getRandomMD5_Hex() {
        String randomString = getRandomString();
        return Encoder.getMD5_Hex(randomString);
    }

    private static String getRandomString() {
        // generate a long string, ideally at least 128 characters
        StringBuffer randomString = new StringBuffer(128);
        randomString.append(String.valueOf(System.currentTimeMillis()));
        randomString.append(RandomStringUtils.randomAlphanumeric(30));
        randomString.append(RandomStringUtils.randomNumeric(30));
        randomString.append(String.valueOf(System.currentTimeMillis()));
        return randomString.toString();
    }

    /*
    public static void main (String args[]) {
        System.out.println("getRandomString     = " + getRandomString());
        System.out.println("getRandomMD5_Base64 = " + getRandomMD5_Base64());
        System.out.println("getRandomMD5_Hex    = " + getRandomMD5_Hex());
        
        // sample result:
        // getRandomString     = 1232523240656iiGtmNgB6OIukTyYZiEI71aHHC3IVe3843919296757668724594677173561232523240734
        // getRandomMD5_Base64 = GkQ5ni+wssZ2brdEaxX5rQ==
        // getRandomMD5_Hex    = 921430570519ba12ea5e177320c330bb
    }*/
}
