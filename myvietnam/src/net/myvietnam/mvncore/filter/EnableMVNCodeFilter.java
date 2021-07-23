/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/filter/EnableMVNCodeFilter.java,v 1.40 2009/12/03 08:44:13 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.40 $
 * $Date: 2009/12/03 08:44:13 $
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
package net.myvietnam.mvncore.filter;

import java.util.Locale;

import net.myvietnam.mvncore.MVNCoreResourceBundle;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.util.StringUtil;

public final class EnableMVNCodeFilter {

    private EnableMVNCodeFilter() { //prevent instantiation
    }

    static String[][] mvnCode = {
        { "[b]",    "<span style=\"font-weight:bold\">" },
        { "[/b]",   "</span>" },
        { "[i]",    "<span style=\"font-style:italic\">" },
        { "[/i]",   "</span>" },
        { "[u]",    "<span style=\"text-decoration:underline\">" },
        { "[/u]",   "</span>" },
        { "[s]",    "<span style=\"text-decoration:line-through\">" },
        { "[/s]",   "</span>" },

        { "[hr]",   "<hr>" },
        { "[br]",   "<br>" },
        { "[p]",    "<p>"  },

        { "[h1]",   "<h1>" },
        { "[/h1]",  "</h1>"},
        { "[h2]",   "<h2>" },
        { "[/h2]",  "</h2>"},
        { "[h3]",   "<h3>" },
        { "[/h3]",  "</h3>"},
        { "[h4]",   "<h4>" },
        { "[/h4]",  "</h4>"},
        { "[h5]",   "<h5>" },
        { "[/h5]",  "</h5>"},
        { "[h6]",   "<h6>" },
        { "[/h6]",  "</h6>"},

        { "[/size]",    "</span>" },
        { "[size=+1]",  "<span style=\"font-size: large\">" },
        { "[size=+2]",  "<span style=\"font-size: x-large\">" },
        { "[size=+3]",  "<span style=\"font-size: xx-large\">" },
        { "[size=+4]",  "<span style=\"font-size: 24pt\">" },
        { "[size=+5]",  "<span style=\"font-size: 24pt\">" },
        { "[size=+6]",  "<span style=\"font-size: 24pt\">" },

        { "[size=1]",  "<span style=\"font-size: 8pt\">" },
        { "[size=2]",  "<span style=\"font-size: 10pt\">" },
        { "[size=3]",  "<span style=\"font-size: 12pt\">" },
        { "[size=4]",  "<span style=\"font-size: 14pt\">" },
        { "[size=5]",  "<span style=\"font-size: 18pt\">" },
        { "[size=6]",  "<span style=\"font-size: 24pt\">" },

        { "[size=-1]",  "<span style=\"font-size: small\">" },
        { "[size=-2]",  "<span style=\"font-size: x-small\">" },
        { "[size=-3]",  "<span style=\"font-size: x-small\">" },
        { "[size=-4]",  "<span style=\"font-size: x-small\">" },
        { "[size=-5]",  "<span style=\"font-size: x-small\">" },
        { "[size=-6]",  "<span style=\"font-size: x-small\">" },

        { "[/font]",  "</span>" },
        { "[font=arial]",  "<span style=\"font-family:arial\">" },
        { "[font=times new roman]",  "<span style=\"font-family:times new roman\">" },
        { "[font=courier new]",  "<span style=\"font-family:courier new\">" },
        { "[font=century gothic]",  "<span style=\"font-family:Century Gothic\">" },

        { "[/color]",           "</span>" },
        { "[color=skyblue]",    "<span style=\"color: skyblue\">" },
        { "[color=royalblue]",  "<span style=\"color: royalblue\">" },
        { "[color=blue]",       "<span style=\"color: blue\">" },
        { "[color=darkblue]",   "<span style=\"color: darkblue\">" },
        { "[color=orange]",     "<span style=\"color: orange\">" },
        { "[color=orangered]",  "<span style=\"color: orangered\">" },
        { "[color=crimson]",    "<span style=\"color: crimson\">" },
        { "[color=red]",        "<span style=\"color: red\">" },
        { "[color=firebrick]",  "<span style=\"color: firebrick\">" },
        { "[color=darkred]",    "<span style=\"color: darkred\">" },
        { "[color=green]",      "<span style=\"color: green\">" },
        { "[color=limegreen]",  "<span style=\"color: limegreen\">" },
        { "[color=seagreen]",   "<span style=\"color: seagreen\">" },
        { "[color=deeppink]",   "<span style=\"color: deeppink\">" },
        { "[color=tomato]",     "<span style=\"color: tomato\">" },
        { "[color=coral]",      "<span style=\"color: coral\">" },
        { "[color=purple]",     "<span style=\"color: purple\">" },
        { "[color=indigo]",     "<span style=\"color: indigo\">" },
        { "[color=burlywood]",  "<span style=\"color: burlywood\">" },
        { "[color=sandybrown]", "<span style=\"color: sandybrown\">" },
        { "[color=sienna]",     "<span style=\"color: sienna\">" },
        { "[color=chocolate]",  "<span style=\"color: chocolate\">" },
        { "[color=teal]",       "<span style=\"color: teal\">" },
        { "[color=silver]",     "<span style=\"color: silver\">" },
        { "[color=brown]",      "<span style=\"color: brown\">" },
        { "[color=yellow]",     "<span style=\"color: yellow\">" },
        { "[color=olive]",      "<span style=\"color: olive\">" },
        { "[color=cyan]",       "<span style=\"color: cyan\">" },
        { "[color=violet]",     "<span style=\"color: violet\">" },
        { "[color=white]",      "<span style=\"color: white\">" },
        { "[color=black]",      "<span style=\"color: black\">" },
        { "[color=pink]",       "<span style=\"color: pink\">" },
        { "[color=purple]",     "<span style=\"color: purple\">" },
        { "[color=navy]",       "<span style=\"color: navy\">" },
        { "[color=beige]",      "<span style=\"color: beige\">" },
        
        { "[color=#87ceeb]",    "<span style=\"color: skyblue\">" },
        { "[color=#4169e1]",    "<span style=\"color: royalblue\">" },
        { "[color=#0000ff]",    "<span style=\"color: blue\">" },
        { "[color=#00008b]",    "<span style=\"color: darkblue\">" },
        { "[color=#ffa500]",    "<span style=\"color: orange\">" },
        { "[color=#ff4500]",    "<span style=\"color: orangered\">" },
        { "[color=#dc143c]",    "<span style=\"color: crimson\">" },
        { "[color=#ff0000]",    "<span style=\"color: red\">" },
        { "[color=#b22222]",    "<span style=\"color: firebrick\">" },
        { "[color=#8b0000]",    "<span style=\"color: darkred\">" },
        { "[color=#008000]",    "<span style=\"color: green\">" },
        { "[color=#32cd32]",    "<span style=\"color: limegreen\">" },
        { "[color=#2e8b57]",    "<span style=\"color: seagreen\">" },
        { "[color=#ff1493]",    "<span style=\"color: deeppink\">" },
        { "[color=#ff6347]",    "<span style=\"color: tomato\">" },
        { "[color=#ff7f50]",    "<span style=\"color: cora\">" },
        { "[color=#800080]",    "<span style=\"color: purple\">" },
        { "[color=#4b0082]",    "<span style=\"color: indigo\">" },
        { "[color=#deb887]",    "<span style=\"color: burlywood\">" },
        { "[color=#f4a460]",    "<span style=\"color: sandybrown\">" },
        { "[color=#a0522d]",    "<span style=\"color: sienna\">" },
        { "[color=#d2691e]",    "<span style=\"color: chocolate\">" },
        { "[color=#008080]",    "<span style=\"color: teal\">" },
        { "[color=#c0c0c0]",    "<span style=\"color: silver\">" },
           
        { "[list]",             "<ul>" },
        { "[/list]",            "</ul>" },
        
        { "[list=1]",           "<ul style=\"list-style-type:decimal\">" },
        { "[/list=1]",          "</ul>" },

        { "[list=a]",           "<ul style=\"list-style-type:lower-alpha\">" },
        { "[/list=a]",          "</ul>" },

        { "[list=A]",           "<ul style=\"list-style-type:upper-alpha\">" },
        { "[/list=A]",          "</ul>" },

        { "[list=i]",           "<ul style=\"list-style-type:lower-roman\">" },
        { "[/list=i]",          "</ul>" },

        { "[list=I]",           "<ul style=\"list-style-type:upper-roman\">" },
        { "[/list=I]",          "</ul>" },

        { "[*]",                "<li>" },
        { "[/*]",                "</li>" },

        { "[code]",             "<div class=\"divcode\"><pre class=\"prettyprint\">" },
        { "[/code]",            "</pre></div>" },

        { "[quote]"      ,      "<div class=\"quote\">"},
        { "[/quote]"     ,      "</div>"},
        
        { "[center]"      ,      "<div align=\"center\">"},
        { "[/center]"     ,      "</div>"},
        
        { "[right]"      ,      "<div align=\"right\">"},
        { "[/right]"     ,      "</div>"},
        
        { "[indent]"      ,      "<blockquote>"},
        { "[/indent]"     ,      "</blockquote>"},
        
    };

    public static String filter(String input) {
        
        boolean inCode = false;
        int beginIndex = 0;
        int currentBracketIndex = 0;
        int inputLength = input.length();
        int mvnCodeLength = mvnCode.length;
        StringBuffer output = new StringBuffer(inputLength * 2);// is it the best init value ?

        while(beginIndex < inputLength) {
            currentBracketIndex = input.indexOf('[', beginIndex);
            if (currentBracketIndex == -1) { // cannot find bracket
                String remain = input.substring(beginIndex, inputLength);// slow here !
                output.append(remain);
                break;
            }

            // now it means we found the bracket
            String remain = input.substring(beginIndex, currentBracketIndex);// slow here !
            output.append(remain);
            boolean matchFound = false;

            // try to find if it matches any mvnCode
            for (int i = 0; i < mvnCodeLength; i++) {
                String currentEmotion = mvnCode[i][0];
                int endIndex = currentBracketIndex + currentEmotion.length();
                if (endIndex > inputLength) continue;
                String match = input.substring(currentBracketIndex, endIndex);// too slow here !!!

                if (currentEmotion.equals(match)) {
                    if ("[code]".equalsIgnoreCase(match)) {
                        inCode = true;
                        output.append(mvnCode[i][1]);
                        beginIndex = currentBracketIndex + currentEmotion.length();
                        matchFound = true;
                        break;
                    }
                    if ("[/code]".equalsIgnoreCase(match)) {
                        inCode = false;
                    }
                    if (inCode == false) {
                        output.append(mvnCode[i][1]);
                        beginIndex = currentBracketIndex + currentEmotion.length();
                        matchFound = true;
                        break;
                    }
                }
            }// for

            if (matchFound == false) {
                beginIndex = currentBracketIndex + 1;
                output.append('[');
            }
        }// while
        return output.toString();
    }

    public static void checkValidation(String input, Locale locale) throws BadInputException {
        
        boolean enableValidate = false;
        int beginIndex = 0;
        int currentBracketIndex = 0;
        int inputLength = input.length();
        int mvnCodeLength = mvnCode.length;
        String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.bbcode_not_valid");
        //String messageError = "BBCode input is not valid.";
        
        
        while(beginIndex < inputLength) {
            currentBracketIndex = input.indexOf('[', beginIndex);
            if (currentBracketIndex == -1) { // cannot find bracket
                break;
            }

            // now it means we found the bracket
            boolean matchFound = false;

            // try to find if it matches any mvnCode
            for (int i = 0; i < mvnCodeLength; i++) {
                
                boolean specialTag = false;
                String currentEmotion = mvnCode[i][0];
                int endIndex = currentBracketIndex + currentEmotion.length();
                if (endIndex > inputLength) continue;
                String match = input.substring(currentBracketIndex, endIndex);// too slow here !!!
                String inputExcludeTagCode = StringUtil.getStringExcludeTag(input, "[code]" , "[/code]");
                inputExcludeTagCode = StringUtil.getStringExcludeTag(inputExcludeTagCode, "[code]" , "[/code]");

//                if (match.equalsIgnoreCase("[*]")) {
//                    specialTag = true;
//                }

                if (match.equalsIgnoreCase("[hr]") || match.equalsIgnoreCase("[p]") || match.equalsIgnoreCase("[br]")) {
                  specialTag = true;
                }
                
                //all special tags in mvnCode
                if (i>22 && i<107) {
                    specialTag = true;
                }
                
                if ("[code]".equalsIgnoreCase(match)) {
                    int countCode = StringUtil.countChar(input, match);
                    String endCode= StringUtil.getCloseTag(match);
                    int countEndCode = StringUtil.countChar(input, endCode);
                    
                    int codeIndex = input.indexOf("[code]");
                    int endCodeIndex = input.indexOf("[/code]");
                    
                    if (endCodeIndex < codeIndex) {
                        throw new BadInputException(localizedMessage);
                    }
                    
                    if (countCode != countEndCode) {
                        throw new BadInputException(localizedMessage);
                    }
                    enableValidate = true;
                } else if ("[/code]".equalsIgnoreCase(match)) {
                    enableValidate = false;
                }
                
                if (currentEmotion.equals(match)) {
                    
                    beginIndex = currentBracketIndex + currentEmotion.length();
                    matchFound = true;
                    
                    if (enableValidate == false && specialTag == false) {
                        int countOpenTag = 0;
                        int countCloseTag = 0;
                        String openTag = "";
                        String closeTag = "";
                        int openTagIndex = 0;
                        int closeTagIndex = 0;
                        
                        
                        if (match.indexOf('/') != -1) {
                            if ("[/code]".equalsIgnoreCase(match)) {
                                inputExcludeTagCode = input;  
                            }
                            closeTag = match;
//                            System.out.println(" CLOSE TAG ****************");
//                            System.out.println("close tag : "+closeTag);
                            closeTagIndex = inputExcludeTagCode.indexOf(match);
//                            System.out.println("close tag index : "+closeTagIndex);
                            countCloseTag = StringUtil.countChar(inputExcludeTagCode, closeTag);
//                            System.out.println("count close tag : "+countCloseTag);

                            openTag = StringUtil.getOpenTag(match);
//                            System.out.println("open tag : "+openTag);
                            openTagIndex= inputExcludeTagCode.indexOf(openTag);
//                            System.out.println("open tag index : "+openTagIndex);
                            countOpenTag = StringUtil.countChar(inputExcludeTagCode, openTag);
//                            System.out.println("count open tag : "+countOpenTag);
                        }  else {
//                            System.out.println(" OPEN TAG ****************");
                            openTag = match;
//                            System.out.println("open tag : "+openTag);
                            openTagIndex = inputExcludeTagCode.indexOf(openTag);
//                            System.out.println("open tag index : "+openTagIndex);
                            countOpenTag = StringUtil.countChar(inputExcludeTagCode, openTag);
//                            System.out.println("inputExcludeTagCode "+inputExcludeTagCode);
//                            System.out.println("count open tag : "+countOpenTag);

                            closeTag = StringUtil.getCloseTag(openTag);
//                            System.out.println("close tag : "+closeTag);
                            closeTagIndex = inputExcludeTagCode.indexOf(closeTag);
//                            System.out.println("close tag index : "+closeTagIndex);
                            countCloseTag = StringUtil.countChar(inputExcludeTagCode, closeTag);
//                            System.out.println("count close tag : "+countCloseTag);
                        }
                        
                        if (closeTagIndex < openTagIndex) {
                            throw new BadInputException(localizedMessage);
                        }
                        
                        if (countOpenTag != countCloseTag) {
                            throw new BadInputException(localizedMessage);
                        }
                    }
                }
            }// for

            if (matchFound == false) {
                beginIndex = currentBracketIndex + 1;
            }
        }// while
    }
    
    public static String removeBBCode(String input) {
        
        int beginIndex = 0;
        int currentBracketIndex = 0;
        int inputLength = input.length();
        int mvnCodeLength = mvnCode.length;
        StringBuffer output = new StringBuffer(inputLength * 2);// is it the best init value ?
        while(beginIndex < inputLength) {
            currentBracketIndex = input.indexOf('[', beginIndex);
            if (currentBracketIndex == -1) { // cannot find bracket
                String remain = input.substring(beginIndex, inputLength);// slow here !
                output.append(remain);
                break;
            }

            // now it means we found the bracket
            String remain = input.substring(beginIndex, currentBracketIndex);// slow here !
            output.append(remain);
            boolean matchFound = false;

            // try to find if it matches any mvnCode
            for (int i = 0; i < mvnCodeLength; i++) {
                String currentEmotion = mvnCode[i][0];
                int endIndex = currentBracketIndex + currentEmotion.length();
                if (endIndex > inputLength) continue;
                String match = input.substring(currentBracketIndex, endIndex);// too slow here !!!
                if (currentEmotion.equals(match)) {
                    output.append("");
                    beginIndex = currentBracketIndex + currentEmotion.length();
                    matchFound = true;
                    break;
                }
            }// for

            if (matchFound == false) {
                beginIndex = currentBracketIndex + 1;
                output.append('[');
            }
        }// while
        return output.toString();
    }
    
//    public static void main(String[] args) {
   
//        for (int i=107;i<119;i++) {
//            System.out.println(mvnCode[108][0]);
//        }
//        
//          String input = "[list=1][font=times new roman][b][size=3][color=red]nhan[/color][/size][/b][/font]";
//                 input += "[font=times new roman][b][size=3][color=red][color=skyblue]code[/color]";
//                 input += "[/color][/size][/b][/font]";
//                 input += "[/list=1]";
//        
//          try {
//              checkValidation(input);
//          } catch (BadInputException e) {
//              System.out.println(e.getMessage());
//          }
        
//        String input = "     [b] [/b]  [code]  []  [/code]";
//        System.out.println(filter(input));
//        
//        String tag ="[code]";
//        System.out.println(StringUtil.getCloseTag(tag));
        
//        String input = "  [b] Bold [/b]   [code] [h1]  [/h1] aaaaaaaaaa  [/code]";
//        String output = filter(input);
//        System.out.println(output + "\n");
    
//        String input = " [][b]smile[/b] [ib][/i]]/b]) grin[)) sad = -[(cry[((minh[[>[)bdfdfc[";
//        System.out.println("input = '" + input + "' length = " + input.length());
//        EnableMVNCodeFilter enableMVNCodeFilter = new EnableMVNCodeFilter();
//
//        long start = System.currentTimeMillis();
//        String output = null;
//        for (int i = 0; i <10000; i++) {
//            output = enableMVNCodeFilter.filter(input);
//        }
//        long time = System.currentTimeMillis() - start;
//        System.out.println("total time = " + time);
//
//        System.out.println("output= '" + output + "'");
//    }
    
}
