/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/util/MVNCodeFilter.java,v 1.4 2009/10/12 07:00:22 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.4 $
 * $Date: 2009/10/12 07:00:22 $
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
 * @author: Xuan Tran Le  
 */
package com.mvnforum.util;

/**
 * The Class MVNCodeFilter.
 */
public final class MVNCodeFilter {

    /**
     * Instantiates a new mVN code filter.
     */
    private MVNCodeFilter() { //prevent instantiation
    }

    /** The mvn code. */
    static String[][] mvnCode = {
        { "[B]",    "[b]" },
        { "[/B]",   "[/b]" },
        { "[I]",    "[i]" },
        { "[/I]",   "[/i]" },
        { "[U]",    "[u]" },
        { "[/U]",   "[/u]" },
        { "[S]",    "[s]" },
        { "[/S]",   "[/s]" },

        { "[HR]",   "[hr]" },
        { "[BR]",   "[br]" },
        { "[P]",    "[p]"  },

        { "[H1]",   "[h1]" },
        { "[/H1]",  "[/h1]"},
        { "[H2]",   "[h2]" },
        { "[/H2]",  "[/h2]"},
        { "[H3]",   "[h3]" },
        { "[/H3]",  "[/h3]"},
        { "[H4]",   "[h4]" },
        { "[/H4]",  "[/h4]"},
        { "[H5]",   "[h5]" },
        { "[/H5]",  "[/h5]"},
        { "[H6]",   "[h6]" },
        { "[/H6]",  "[/h6]"},

        { "[/SIZE]",    "[/size]" },
        { "[SIZE=+1]",  "[size=+1]" },
        { "[SIZE=+2]",  "[size=+2]" },
        { "[SIZE=+3]",  "[size=+3]" },
        { "[SIZE=+4]",  "[size=+4]" },
        { "[SIZE=+5]",  "[size=+5]" },
        { "[SIZE=+6]",  "[size=+6]" },

        { "[SIZE=1]",  "[size=1]" },
        { "[SIZE=2]",  "[size=2]" },
        { "[SIZE=3]",  "[size=3]" },
        { "[SIZE=4]",  "[size=4]" },
        { "[SIZE=5]",  "[size=5]" },
        { "[SIZE=6]",  "[size=6]" },
        { "[SIZE=7]",  "[size=6]" },

        { "[SIZE=-1]",  "[size=-1]" },
        { "[SIZE=-2]",  "[size=-2]" },
        { "[SIZE=-3]",  "[size=-3]" },
        { "[SIZE=-4]",  "[size=-4]" },
        { "[SIZE=-5]",  "[size=-5]" },
        { "[SIZE=-6]",  "[size=-6]" },

        { "[/FONT]",  "[/font]" },
        { "[FONT=Arial]",  "[font=arial]" },
        { "[FONT=Times New Roman]",  "[font=times new roman]" },
        { "[FONT=Courier New]",  "[font=courier new]" },
        { "[FONT=Century Gothic]",  "[font=century gothic]" },
        
        { "[FONT=Arial Black]",  "[font=arial]" },
        { "[FONT=Arial Narrow]",  "[font=arial]" },
        { "[FONT=Book Antiqua]",  "[font=arial]" },
        { "[FONT=Comic Sans MS]",  "[font=arial]" },
        { "[FONT=Fixedsys]",  "[font=arial]" },
        { "[FONT=Franklin Gothic Medium]",  "[font=arial]" },
        { "[FONT=Garamond]",  "[font=arial]" },
        { "[FONT=Georgia]",  "[font=arial]" },
        { "[FONT=Impact]",  "[font=arial]" },
        { "[FONT=Lucida Console]",  "[font=arial]" },
        { "[FONT=Lucida Sans Unicode]",  "[font=arial]" },
        { "[FONT=Microsoft Sans Serif]",  "[font=arial]" },
        { "[FONT=Palatino Linotype]",  "[font=arial]" },
        { "[FONT=System]",  "[font=arial]" },
        { "[FONT=Tahoma]",  "[font=arial]" },
        { "[FONT=Trebuchet MS]",  "[font=arial]" },
        { "[FONT=Verdana]",  "[font=arial]" },

        { "[/COLOR]",           "[/color]" },
        { "[COLOR=skyblue]",    "[color=skyblue]" },
        { "[COLOR=royalblue]",  "[color=royalblue]" },
        { "[COLOR=blue]",       "[color=blue]" },
        { "[COLOR=darkblue]",   "[color=darkblue]" },
        { "[COLOR=orange]",     "[color=orange]" },
        { "[COLOR=orangered]",  "[color=orangered]" },
        { "[COLOR=crimson]",    "[color=crimson]" },
        { "[COLOR=red]",        "[color=red]" },
        { "[COLOR=firebrick]",  "[color=firebrick]" },
        { "[COLOR=darkred]",    "[color=darkred]" },
        { "[COLOR=green]",      "[color=green]" },
        { "[COLOR=limegreen]",  "[color=limegreen]" },
        { "[COLOR=seagreen]",   "[color=seagreen]" },
        { "[COLOR=deeppink]",   "[color=deeppink]" },
        { "[COLOR=tomato]",     "[color=tomato]" },
        { "[COLOR=coral]",      "[color=coral]" },
        { "[COLOR=purple]",     "[color=purple]" },
        { "[COLOR=indigo]",     "[color=indigo]" },
        { "[COLOR=burlywood]",  "[color=burlywood]" },
        { "[COLOR=sandybrown]", "[color=sandybrown]" },
        { "[COLOR=sienna]",     "[color=sienna]" },
        { "[COLOR=chocolate]",  "[color=chocolate]" },
        { "[COLOR=teal]",       "[color=teal]" },
        { "[COLOR=silver]",     "[color=silver]" },
        { "[COLOR=brown]",      "[color=brown]" },
        { "[COLOR=yellow]",     "[color=yellow]" },
        { "[COLOR=olive]",      "[color=olive]" },
        { "[COLOR=cyan]",       "[color=cyan]" },
        { "[COLOR=violet]",     "[color=violet]" },
        { "[COLOR=white]",      "[color=white]" },
        { "[COLOR=black]",      "[color=black]" },
        { "[COLOR=pink]",       "[color=pink]" },
        { "[COLOR=purple]",     "[color=purple]" },
        { "[COLOR=navy]",       "[color=navy]" },
        { "[COLOR=beige]",      "[color=beige]" },
        
        { "[COLOR=#87ceeb]",    "[color=#87ceeb]" },
        { "[COLOR=#4169e1]",    "[color=#4169e1]" },
        { "[COLOR=#0000ff]",    "[color=#0000ff]" },
        { "[COLOR=#00008b]",    "[color=#00008b]" },
        { "[COLOR=#ffa500]",    "[color=#ffa500]" },
        { "[COLOR=#ff4500]",    "[color=#ff4500]" },
        { "[COLOR=#dc143c]",    "[color=#dc143c]" },
        { "[COLOR=#ff0000]",    "[color=#ff0000]" },
        { "[COLOR=#b22222]",    "[color=#b22222]" },
        { "[COLOR=#8b0000]",    "[color=#8b0000]" },
        { "[COLOR=#8b0000]",    "[color=#8b0000]" },
        { "[COLOR=#32cd32]",    "[color=#32cd32]" },
        { "[COLOR=#2e8b57]",    "[color=#2e8b57]" },
        { "[COLOR=#ff1493]",    "[color=#ff1493]" },
        { "[COLOR=#ff6347]",    "[color=#ff6347]" },
        { "[COLOR=#ff7f50]",    "[color=#ff7f50]" },
        { "[COLOR=#800080]",    "[color=#800080]" },
        { "[COLOR=#4b0082]",    "[color=#4b0082]" },
        { "[COLOR=#deb887]",    "[color=#deb887]" },
        { "[COLOR=#f4a460]",    "[color=#f4a460]" },
        { "[COLOR=#a0522d]",    "[color=#a0522d]" },
        { "[COLOR=#d2691e]",    "[color=#d2691e]" },
        { "[COLOR=#008080]",    "[color=#008080]" },
        { "[COLOR=#c0c0c0]",    "[color=#c0c0c0]" },
           
        { "[LIST]",             "[list]" },
        { "[/LIST]",            "[/list]" },
        
        { "[LIST=1]",           "[list=1]" },
        { "[/LIST=1]",          "[/list=1]" },

        { "[LIST=a]",           "[list=a]" },
        { "[/LIST=a]",          "[/list=a]" },

        { "[LIST=A]",           "[list=A]" },
        { "[/LIST=A]",          "[/list=A]" },

        { "[LIST=i]",           "[list=i]" },
        { "[/LIST=i]",          "[/list=i]" },

        { "[LIST=I]",           "[list=I]" },
        { "[/LIST=I]",          "[/list=I]" },

        { "[*]",                "[*]" },
        { "[/*]",               "[/*]" },

        { "[CODE]",             "[code]" },
        { "[/CODE]",            "[/code]" },

        { "[QUOTE]"      ,      "[quote]"},
        { "[/QUOTE]"     ,      "[/quote]"},

        { "[URL]"     ,         "[url]"},
        { "[/URL]"     ,        "[/url]"},
        
        { "[IMG]"     ,         "[img]"},
        { "[/IMG]"     ,        "[/img]"},

        { "[CENTER]"     ,        "[center]"},
        { "[/CENTER]"     ,       "[/center]"},
        
        { "[RIGHT]"     ,        "[right]"},
        { "[/RIGHT]"     ,       "[/right]"},
        
        { "[INDENT]"     ,        "[indent]"},
        { "[/INDENT]"     ,       "[/indent]"},
        
    };

    /**
     * Filter.
     * 
     * @param input the input
     * 
     * @return the string
     */
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

}
