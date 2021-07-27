/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/filter/EnableEmotionFilter.java,v 1.23 2007/07/08 10:15:32 hau_mvn Exp $
 * $Author: hau_mvn $
 * $Revision: 1.23 $
 * $Date: 2007/07/08 10:15:32 $
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

public final class EnableEmotionFilter {

    private EnableEmotionFilter() { //prevent instantiation
    }

    //@todo : localize me
    static String[][] emotion = {
    // standard emotion
        { "[:))]",      "laughing.gif",         "laughing"},
        { "[:)]",       "smile.gif",            "smile"},
        { "[:-)]",      "smile.gif",            "smile"},
        { "[:((]",      "crying.gif",           "crying"},
        { "[:(]",       "sad.gif",              "sad"},
        { "[:-(]",      "sad.gif",              "sad"},
        { "[;)]",       "wink.gif",             "wink"},
        { "[:D]",       "biggrin.gif",          "biggrin"},
        { "[;;)]",      "batting_eyelashes.gif","batting eyelashes"},
        { "[:-/]",      "confused.gif",         "confused"},
        { "[:x]",       "love.gif",             "love struck"},

        { "[:\">]",     "blushing.gif",         "blushing"},
        { "[:&quot;&gt;]", "blushing.gif",      "blushing"},

        { "[:p]",       "tongue.gif",           "tongue"},
        { "[:*]",       "kiss.gif",             "kiss"},
        { "[:O]",       "shock.gif",            "shock"},
        { "[X-(]",      "angry.gif",            "angry"},

        { "[:>]",       "smug.gif",             "smug"},
        { "[:&gt;]",    "smug.gif",             "smug"},

        { "[B-)]",      "cool.gif",             "cool"},
        { "[:-s]",      "worried.gif",          "worried"},

        { "[>:)]",      "devilish.gif",         "devilish"},
        { "[&gt;:)]",   "devilish.gif",         "devilish"},

        { "[:|]",       "straight_face.gif",    "straight face"},
        { "[/:)]",      "raised_eyebrow.gif",   "raised eyebrow"},
        { "[O:)]",      "angel.gif",            "angel"},
        { "[:-B]",      "nerd.gif",             "nerd"},
        { "[=;]",       "talk_to_the_hand.gif", "talk to the hand"},
        { "[I-)]",      "sleep.gif",            "sleep"},
        { "[8-|]",      "rolling_eyes.gif",     "rolling eyes"},

        { "[:-&]",      "sick.gif",             "sick"},
        { "[:-&amp;]",  "sick.gif",             "sick"},

        { "[:-$]",      "shhh.gif",             "shhh"},
        { "[[-(]",      "not_talking.gif",      "not talking"},
        { "[:o)]",      "clown.gif",            "clown"},
        { "[8-}]",      "silly.gif",            "silly"},
        { "[(:|]",      "tired.gif",            "tired"},
        { "[=P~]",      "drooling.gif",         "drooling"},
        { "[:-?]",      "thinking.gif",         "thinking"},
        { "[#-o]",      "d_oh.gif",             "d oh"},

        { "[=D>]",      "applause.gif",         "applause"},
        { "[=D&gt;]",   "applause.gif",         "applause"},
    // hidden emotion
        { "[:@)]",      "pig.gif",              "pig"},
        { "[3:-O]",     "cow.gif",              "cow"},
        { "[:(|)]",     "monkey.gif",           "monkey"},

        { "[~:>]",      "chicken.gif",          "chicken"},
        { "[~:&gt;]",   "chicken.gif",          "chicken"},

        { "[@};-]",     "rose.gif",             "rose"},
        { "[%%-]",      "good_luck.gif",        "good luck"},
        { "[**==]",     "flag.gif",             "flag"},
        { "[(~~)]",     "pumpkin.gif",          "pumpkin"},
        { "[~o)]",      "coffee.gif",           "coffee"},
        { "[*-:)]",     "idea.gif",             "idea"},
        { "[8-X]",      "skull.gif",            "skull"},
        { "[=:)]",      "alien_1.gif",          "alien 1"},

        { "[>-)]",      "alien_2.gif",          "alien 2"},
        { "[&gt;-)]",   "alien_2.gif",          "alien 2"},

        { "[:-L]",      "frustrated.gif",       "frustrated"},

        { "[<):)]",     "cowboy.gif",           "cowboy"},
        { "[&lt;):)]",  "cowboy.gif",           "cowboy"},

        { "[[-o<]",     "praying.gif",          "praying"},
        { "[[-o&lt;]",  "praying.gif",          "praying"},

        { "[@-)]",      "hypnotized.gif",       "hypnotized"},
        { "[$-)]",      "money_eyes.gif",       "money eyes"},

        { "[:-\"]",     "whistling.gif",        "whistling"},
        { "[:-&quot;]", "whistling.gif",        "whistling"},

        { "[:^o]",      "liar.gif",             "liar"},
        { "[b-(]",      "beat_up.gif",          "beat up"},

        { "[:)>-]",     "peace.gif",            "peace"},
        { "[:)&gt;-]",  "peace.gif",            "peace"},

        { "[[-X]",      "shame_on_you.gif",     "shame on you"},
        { "[\\:D/]",    "dancing.gif",          "dancing"},

        { "[>:D<]",     "hugs.gif",             "hugs"},
        { "[&gt;:D&lt;]", "hugs.gif",           "hugs"},
    };

    public static String filter(String input, String emotionFolder) {
        if (input == null) return null;

        int beginIndex = 0;
        int currentBracketIndex = 0;
        int inputLength = input.length();
        int emotionLength = emotion.length;
        StringBuffer output = new StringBuffer(inputLength * 2);

        if (emotionFolder.endsWith("/") == false) {
            emotionFolder = emotionFolder + "/";
        }

        while(beginIndex < inputLength) {
            currentBracketIndex = input.indexOf('[', beginIndex);
            if (currentBracketIndex == -1) { // cannot find bracket
                String remain = input.substring(beginIndex, inputLength);
                output.append(remain);
                break;
            }

            // now it means we found the bracket
            String remain = input.substring(beginIndex, currentBracketIndex);// too slow here !!!
            output.append(remain);
            boolean matchFound = false;

            // try to find if it matchs any emotion
            for (int i = 0; i < emotionLength; i++) {
                String currentEmotion = emotion[i][0];
                int endIndex = currentBracketIndex + currentEmotion.length();
                if (endIndex > inputLength) continue;
                String match = input.substring(currentBracketIndex, endIndex);
                if (currentEmotion.equals(match)) {
                    String imgTag = "<img src=\"" + emotionFolder + emotion[i][1] + "\" border=\"0\" alt=\"" + emotion[i][2] + "\" title=\"" + emotion[i][2] + "\" />";
                    output.append(imgTag);
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
/*
    public static void main(String[] args) {
        String input = " :smile :) grin:)) sad = -:(cry:((minh::>:)bdfdfc:";
        System.out.println("input = '" + input + "' length = " + input.length());
        EnableEmotionFilter enableEmotionFilter = new EnableEmotionFilter();

        long start = System.currentTimeMillis();
        String output = null;
        for (int i = 0; i <10000; i++) {
            output = enableEmotionFilter.filter(input, null);
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("total time = " + time);

        System.out.println(output);
    }
    */
}
