/*
 * $Header: /cvsroot/mvnforum/mvnforum/i18n/tool/SynchronizeI18Files.java,v 1.9 2009/01/06 05:00:24 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.9 $
 * $Date: 2009/01/06 05:00:24 $
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
 * @author: Vo Dinh Anh
 */

import java.io.*;
import java.util.*;

public class SynchronizeI18Files {

    public static ArrayList getContent(String fileName) {
        ArrayList arrLine = new ArrayList();
        File file = new File(fileName);
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String inLine = reader.readLine();
                while (inLine != null) {
                    arrLine.add(inLine + "\n");
                    inLine = reader.readLine();
                }
                reader.close();
                return arrLine;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } 
        return null;
    }
    
    public static void makeFile(String srcFilename1, String srcFilename2, Writer out)
        throws IOException {
        
        ArrayList srcArr = getContent(srcFilename1);
        
        FileInputStream inputStreamSrc1 = new FileInputStream(srcFilename1);
        FileInputStream inputStreamSrc2 = new FileInputStream(srcFilename2);

        PropertyResourceBundle bundle1 = new PropertyResourceBundle(inputStreamSrc1);
        PropertyResourceBundle bundle2 = new PropertyResourceBundle(inputStreamSrc2);

        for (Iterator iterator = srcArr.iterator(); iterator.hasNext(); ) {
            String strLine = (String) iterator.next();
            
            if ( (strLine.trim().startsWith("#")) || (strLine.trim().length() == 0) ) {
                out.write(strLine);
            } else {
                int endIndex = strLine.indexOf("=");
                if (endIndex != -1) {
                    
                    String strKey = strLine.substring(0, endIndex).trim();
                    String strLocale2 = null;
                    try {
                        strLocale2 = bundle2.getString(strKey).trim();
                        out.write(strKey + " = " + strLocale2 + "\n");
                    } catch (MissingResourceException ex) {
                        try {
                            String strLocale1 =  bundle1.getString(strKey);
                            out.write(strKey + " = " + strLocale1 + "\n");
                        } catch (MissingResourceException e) {
                            // ignore
                        }
                    }
                } else {
                    // do nothing
                }
            } // else
        } // for
    }

    public static void main(String[] args) throws Exception {
        
        System.out.println("SynchronizeI18Files.main()");
        
        String userDir = System.getProperty("user.dir") + File.separatorChar; //get current directory
        
        String resultEncode = "utf-8";
        
        String srcFilename1 = userDir + "mvnForum_i18n.properties";
        String srcFilename2 = userDir + "mvnForum_i18n_hu.properties";
        
        OutputStream os = new FileOutputStream(userDir + "result.properties"); 
        Writer fileCreate = new OutputStreamWriter(os, resultEncode);
        
        try {
            BufferedWriter out = new BufferedWriter(fileCreate);
            makeFile(srcFilename1, srcFilename2, out);
            out.close();
            System.out.println("Converted successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
