/*
 * $Header: /cvsroot/mvnforum/mvnforum/i18n/tool/ConvertURL.java,v 1.15 2009/01/05 06:59:32 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.15 $
 * $Date: 2009/01/05 06:59:32 $
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
//package test;
import java.io.*;
import java.util.*;

public class ConvertURL {

    static String convertURL(String href) {
        StringBuffer output = new StringBuffer();

        if (href.startsWith("http") == false &&
            href.startsWith("javascript:") == false &&
            href.startsWith("mailto:") == false &&
            href.startsWith("aim:") == false &&
            href.startsWith("<%=response.encodeURL") == false) {
            href = href.replaceAll("<%=", "\" + ");
            href = href.replaceAll("%>", " + \"");

            boolean redundantEnd = false;
            if (href.endsWith(" + \"")) {
                redundantEnd = true;
                //System.out.println("redundantEnd = true");
                href = href.substring(0, href.length()-4);
            }

            boolean redundantBegin = false;
            if (href.startsWith("\" + ")) {
                redundantBegin = true;
                //System.out.println("redundantBegin = true");
                href = href.substring(4);
            }

            if (redundantBegin) {
                output.append("<%=response.encodeURL(");
            }
            else {
                output.append("<%=response.encodeURL(\"");
            }

            output.append(href);

            if (redundantEnd) {
                output.append(")%>");
            } else {
                output.append("\")%>");
            }
        } else {
            return href;
        }
        return output.toString();
    }

    static String convertTagA(String tagA) throws IllegalArgumentException {
        String output = "";
        if (tagA.startsWith("<a ") == false) {
            throw new IllegalArgumentException();
        }
        if (tagA.endsWith(">") == false) {
            throw new IllegalArgumentException();
        }

        if (tagA.indexOf('>') != tagA.lastIndexOf('>')) {
            //throw new IllegalArgumentException();
        }
        int firstIndex = tagA.indexOf("href=\"");
        if (firstIndex == -1) {
            System.out.println("WARNING: TagA does not have href: " + tagA);
            return tagA;
        }
        int lastIndex = tagA.indexOf("\"", firstIndex + 6);

        String begin = tagA.substring(0, firstIndex + 6);
        String href = tagA.substring(firstIndex + 6, lastIndex);
        String end = tagA.substring(lastIndex);

//        System.out.println("begin = " + begin);
//        System.out.println("href = " + href);
//        System.out.println("end = " + end);

        output = begin + convertURL(href) + end;

        return output;
    }

    static String convertTagForm(String tagForm) throws IllegalArgumentException {
        String output = "";
        if (tagForm.startsWith("<form ") == false) {
            throw new IllegalArgumentException();
        }
        if (tagForm.endsWith(">") == false) {
            throw new IllegalArgumentException();
        }

        if (tagForm.indexOf('>') != tagForm.lastIndexOf('>')) {
            //throw new IllegalArgumentException();
        }
        int firstIndex = tagForm.indexOf("action=\"");
        if (firstIndex == -1) {
            System.out.println("WARNING: TagForm does not have action: " + tagForm);
            return tagForm;
        }
        int lastIndex = tagForm.indexOf("\"", firstIndex + 8);

        String begin = tagForm.substring(0, firstIndex + 8);
        String href = tagForm.substring(firstIndex + 8, lastIndex);
        String end = tagForm.substring(lastIndex);

//        System.out.println("begin = " + begin);
//        System.out.println("action = " + href);
//        System.out.println("end = " + end);

        output = begin + convertURL(href) + end;

        return output;
    }

    static String convertTagMeta(String tagMeta) throws IllegalArgumentException {
        String output = "";
        if (tagMeta.startsWith("<meta ") == false) {
            throw new IllegalArgumentException();
        }
        if (tagMeta.endsWith(">") == false) {
            throw new IllegalArgumentException();
        }

        if (tagMeta.indexOf('>') != tagMeta.lastIndexOf('>')) {
            //throw new IllegalArgumentException();
        }
        int firstIndex = tagMeta.indexOf("url=");
        if (firstIndex == -1) {
            System.out.println("WARNING: TagMeta does not have url=: " + tagMeta);
            return tagMeta;
        }
        int lastIndex = tagMeta.indexOf("'", firstIndex + 4);

        String begin = tagMeta.substring(0, firstIndex + 4);
        String href = tagMeta.substring(firstIndex + 4, lastIndex);
        String end = tagMeta.substring(lastIndex);

//        System.out.println("begin = " + begin);
//        System.out.println("action = " + href);
//        System.out.println("end = " + end);

        output = begin + convertURL(href) + end;

        return output;
    }

    static String convertLine(String line, String startToken, String endToken) throws IllegalArgumentException {
        int startIndex = -1;
        int endIndex = -1;
outer:
        while (true) {
            startIndex = line.indexOf(startToken, endIndex + 1);
            if (startIndex < 0 || startIndex + 1 >= line.length()) {
                break;
            }
            int beginSearchEndIndex = startIndex + 1;
            while (true) {
                endIndex = line.indexOf(endToken, beginSearchEndIndex);
                if (endIndex < 0) break;
                char charBefore = line.charAt(endIndex - 1);
                if (charBefore == '%') {
                    beginSearchEndIndex = endIndex + 1;
                } else {
                    break;
                }
            }
            if (endIndex < 0) {
                break;
            }
            String matches = line.substring(startIndex, endIndex+1);
            //log("match = " + matches + " resource = " + (String) resourceMap.get(matches), Project.MSG_WARN);
            //If there is a white space or = or :, then
            //it isn't to be treated as a valid key.
            for (int k = 0; k < matches.length(); k++) {
                /*
                char c = matches.charAt(k);
                if (c == ':' ||
                    c == '=' ||
                    Character.isSpaceChar(c)) {
                    endIndex = endIndex - 1;
                    continue outer;
                }
            */
            }
            String replace = null;
            if (startToken.equals("<a ")) {
                replace = (String) convertTagA(matches);
            } else if (startToken.equals("<form ")) {
                replace = (String) convertTagForm(matches);
            } else if (startToken.equals("<meta ")) {
                replace = (String) convertTagMeta(matches);
            } else {
                throw new IllegalArgumentException();
            }

            //If the key hasn't been loaded into resourceMap,
            //use the key itself as the value also.
            if (replace == null) {
                //log("Warning: The key: " + matches + " hasn't been defined.", Project.MSG_WARN);
                replace = matches;
            }
            line = line.substring(0, startIndex)
                + replace
                + line.substring(endIndex + 1);
            // minhnn: I dont know if the original code has bug
            // I changed from "+ 1" to "- 1" and it works well
            endIndex = startIndex + replace.length() - 1;
            if (endIndex + 1 >= line.length()) {
                break;
            }
        }
        return line;
    }

    static String convertLine(String line) throws Exception {
        try {
            String linewithA = convertLine(line, "<a ", ">");
            String linewithAandForm = convertLine(linewithA, "<form ", ">");
            String linewithAandFormAndMeta = convertLine(linewithAandForm, "<meta ", ">");
            return linewithAandFormAndMeta;
        } catch (Exception ex) {
            System.out.println("line = " + line);
            throw ex;
        }
    }

    public static ArrayList getContent(String fileName) {
        ArrayList arrLine = new ArrayList();
        File file = new File(fileName);
        if (file.exists() == false) {
            return null;
        } 
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String inLine = reader.readLine();
            while (inLine != null) {
                arrLine.add(inLine + "\n");
                //content += inLine + "\n";
                inLine = reader.readLine();
            }
            reader.close();
            return arrLine;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void convertFile(String srcFilename, String descFilename)
        throws Exception {
        ArrayList descArr = new ArrayList();
        Collection tempArr = getContent(srcFilename);
        for (Iterator iterator = tempArr.iterator(); iterator.hasNext(); ) {
            descArr.add(convertLine((String) iterator.next()));
        }
        ExportContentToFile(descArr, descFilename);
    }

    public static void ExportContentToFile(ArrayList lines, String fileName) {
        String curDir = System.getProperty("user.dir"); //Get Current dir
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(curDir + "/out/" + fileName));
            for (Iterator iterator = lines.iterator(); iterator.hasNext(); ) {
                //System.out.print(convertLine((String)iterator.next()));
                out.write((String) iterator.next());
            }
            out.close();
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    public static void main(String[] args) throws Exception {
        //String href = "index";
        //String href = "index?forum=<%=forumid%>&a=true";
        //String correctOutput = "<%=response.encodeURL(context + \"index\")%>";

//        String tagA = "<a href=\"index?forum=<%=forumid%>&a=true\" class=\"topmenu\">";
//        String tagA = "<a href=\"listmembers\" class=\"topmenu\">";
//        String tagA = "<a href=\"<%=filename%>\" class=\"topmenu\">";
//        String tagA = "<a href=\"#\">";
//        String tagA = "<a href=\"javascript:smilie('[:&quot;>]')\">";
//        System.out.println("tagA = " + tagA);
//        System.out.println("output = " + convertTagA(tagA));

//        String tagMeta = "<meta http-equiv='refresh' content='3; url=viewthread?thread=<%=   threadID    %>&offset=<%=offset%>'>";
//        System.out.println("tagMeta = " + tagMeta);
//        System.out.println("output = " + convertTagMeta(tagMeta));

//        String tagForm = "<form action=\"addattachmentprocess\" method=\"post\" enctype=\"multipart/form-data\" name=\"submitform\">";
//        System.out.println("tagForm = " + tagForm);
//        System.out.println("output = " + convertTagForm(tagForm));


        //String line = "link 1 <a href=\"listmembers\" class=\"topmenu\"> link 2 <a href=\"index?forum=<%=forumid%>&a=true\" class=\"topmenu\"> form ne` <form action=\"addattachmentprocess\" method=\"post\" enctype=\"multipart/form-data\" name=\"submitform\">";
//        String line = "<td><a href=\"javascript:smilie('[:&quot;&gt;]')\"><img src=\"<%=contextPath%>/mvnplugin/mvnforum/images/emotion/blushing.gif\" alt=\"blushing\" border=\"0\"></a>&nbsp;</td>";
//        System.out.println("line = " + line);
//        System.out.println("output = " + convertLine(line));
        //convertFile("listmembers.jsp", "b.txt");

        //convertFile("listrecentthreads.jsp", "listrecentthreads.jsp");

        try {
            if (args[0].equals("all")) {
                String curDir = System.getProperty("user.dir"); //Get Current dir
                File dir = new File(curDir);
                if (dir.isFile()) throw new IOException("IOException -> BadInputException: not a directory.");
                File[] files = dir.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        File file = files[i];
                        if (file.isFile()) {
                            String absPath = file.getAbsolutePath();
                            if (absPath.endsWith(".jsp")) {
                                int lastIndex = absPath.lastIndexOf('\\');
                                String name = absPath.substring(lastIndex + 1);
                                System.out.println("name = " + name);
                                convertFile(name, name);
                            }
                        } else {
                            System.out.println("get folder = " + file);
                        }
                    }
                }//if
                dir.delete();
            } else {
                convertFile(args[0], args[0]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
