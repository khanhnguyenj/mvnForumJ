/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/util/StringUtil.java,v 1.83
 * 2009/12/23 10:08:44 lexuanttkhtn Exp $ $Author: lexuanttkhtn $ $Revision: 1.83 $ $Date:
 * 2009/12/23 10:08:44 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding MyVietnam and MyVietnam CoreLib MUST remain intact in the scripts
 * and source code.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 *
 * Correspondence and Marketing Questions can be sent to: info at MyVietnam net
 *
 * @author: Minh Nguyen
 *
 * @author: Mai Nguyen
 */
package net.myvietnam.mvncore.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.myvietnam.mvncore.MVNCoreConfig;
import net.myvietnam.mvncore.MVNCoreResourceBundle;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.service.EncoderService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;

/**
 * TODO add option for SHORT_STRING_LENGTH
 */
public final class StringUtil {

  private static final Logger log = LoggerFactory.getLogger(StringUtil.class);

  private static EncoderService encoderService = null;

  private static final int SHORT_STRING_LENGTH = 100;

  private StringUtil() {// prevent instantiation
  }

  /**
   * This method trim the input variable, so if it contains only spaces, then it will be empty
   * string, then we have 0 token :-) The returned value is never null. If the input String is null,
   * an empty String array will be returned All tokens are trimmed before returning
   */
  public static String[] getStringArray(String inputValue, String delim) {
    if (inputValue == null) {
      inputValue = "";
    }
    inputValue = inputValue.trim();// very important
    StringTokenizer t = new StringTokenizer(inputValue, delim);
    String[] ret = new String[t.countTokens()];
    int index = 0;
    while (t.hasMoreTokens()) {
      String token = t.nextToken().trim();
      // check for valid value here if needed
      ret[index] = token;
      index++;
    }
    return ret;
  }

  public static String getCloseTag(String input) {
    char[] ch = input.toCharArray();
    String output = "";
    for (int i = 0; i < ch.length; i++) {
      if (ch[i] == '[') {
        output += ch[i];
        output += '/';
      } else {
        output += ch[i];
      }
    }
    return output;
  }

  public static String getOpenTag(String input) {
    char[] ch = input.toCharArray();
    String output = "";
    for (int i = 0; i < ch.length; i++) {
      if (ch[i] == '/') {
        continue;
      }
      output += ch[i];
    }
    return output;
  }

  public static String getStringExcludeTag(String input, String openTag, String closeTag) {
    int codeIndex = input.indexOf(openTag);
    int endCodeIndex = input.indexOf(closeTag);
    int closeTagLength = closeTag.length();
    int length = input.length();
    int beginIndex = 0;
    String output = "";

    if (codeIndex == -1) {
      codeIndex = length;
    }
    output += input.substring(beginIndex, codeIndex);

    if (endCodeIndex != -1) {
      output += input.substring(endCodeIndex + closeTagLength);
    }
    return output;

  }

  public static List getTagNames(String input) {
    input = input.trim();
    ArrayList tagNameArray = new ArrayList();
    char[] chTagNames = input.toCharArray();
    String tagName = "";

    boolean inQuote = false;

    System.out.println("length: " + chTagNames.length);
    for (int i = 0; i < chTagNames.length; i++) {
      if (Character.isSpaceChar(chTagNames[i])) {
        if (inQuote == false) {

          if (Character.isSpaceChar(chTagNames[i + 1]) || chTagNames[i - 1] == '"'
              || chTagNames[i + 1] == '"' || Character.isSpaceChar(chTagNames[i - 1])
              || Character.isSpaceChar(chTagNames[i - 1]) == false) {
            continue;
          }

          tagNameArray.add(tagName);
          tagName = "";

        } else {
          if (Character.isSpaceChar(chTagNames[i + 1]) || chTagNames[i - 1] == '"'
              || chTagNames[i + 1] == '"') {
            continue;
          }

          if (Character.isSpaceChar(chTagNames[i - 1])) {
            if (tagName.length() < 1) {
              continue;
            } else {
              // do nothing
            }
          }

          tagName += chTagNames[i];
        }

      } else if (chTagNames[i] == '"') {
        inQuote = !inQuote;
        if (inQuote == true) {
          continue;
        } else {
          tagNameArray.add(tagName);
          tagName = "";
        }
      } else {
        if (inQuote == false) {
          tagName += chTagNames[i];
          if (i == chTagNames.length - 1 || Character.isSpaceChar(chTagNames[i + 1])) {
            if (tagName.length() > 0) {
              tagNameArray.add(tagName);
              tagName = "";
            }
          }
        } else {
          tagName += chTagNames[i];
        }
      }
    }

    return tagNameArray;
  }

  public static int countChar(String str, String value) {
    int counter = 0;
    int i = 0;
    if (str.indexOf(value) == -1) {
      return 0;
    }
    while (str.length() != 0) {
      if (str.trim().equals("")) {
        break;
      }
      int idx = str.indexOf(value);
      if (idx == -1) {
        break;
      }
      counter++;
      if (counter % 2 == 1) {
        i = counter;
      } else {
        if (counter > i) {
          String tag = str.substring(0, idx);
          if (tag.trim().equals("")) {
            return 1;
          } else {
          }
        }
      }
      str = str.substring(idx + 1);
    }
    return counter;
  }

  public static String[] getStringArrays(String to, String cc, String bcc, String delim) {
    String[] toMail = getStringArray(to, delim);
    String[] ccMail = getStringArray(cc, delim);
    String[] bccMail = getStringArray(bcc, delim);
    String[] ret = new String[toMail.length + ccMail.length + bccMail.length];
    int index = 0;
    for (int i = 0; i < toMail.length; i++) {
      ret[index] = toMail[i];
      index++;
    }
    for (int i = 0; i < ccMail.length; i++) {
      ret[index] = ccMail[i];
      index++;
    }
    for (int i = 0; i < bccMail.length; i++) {
      ret[index] = bccMail[i];
      index++;
    }
    return ret;
  }

  public static String[] getDiffStringArrays(String to, String cc, String bcc, String delim) {
    String[] toMail = getStringArray(to, delim);
    String[] ccMail = getStringArray(cc, delim);
    String[] bccMail = getStringArray(bcc, delim);
    // String[] ret = new String[t.countTokens()];
    Set set = new LinkedHashSet();
    // int index = 0;
    for (int i = 0; i < toMail.length; i++) {
      set.add(toMail[i]);
    }
    for (int i = 0; i < ccMail.length; i++) {
      set.add(ccMail[i]);
    }
    for (int i = 0; i < bccMail.length; i++) {
      set.add(bccMail[i]);
    }
    return (String[]) set.toArray(new String[0]);
  }

  /**
   * Return an empty string if the input is null, otherwise return the same string
   * 
   * @param str the input
   * @return a never null string
   */
  public static String getEmptyStringIfNull(String str) {
    if (str == null) {
      return "";
    }
    return str;
  }

  /**
   * Return an empty string if the input is null, otherwise return the string that has been trimmed
   * 
   * @param str the input
   * @return a never null string, and the result is trimmed before returned
   */
  public static String getEmptyStringIfNullAndTrim(String str) {
    if (str == null) {
      return "";
    }
    return str.trim();
  }

  /**
   * Return true if the input is null or empty string
   * 
   * @param s the input to check
   * @return true if the input is null or empty string
   */
  public static boolean isNullOrEmpty(String s) {
    if ((s == null) || (s.length() == 0)) {
      return true;
    }
    return false;
  }

  /**
   * Return true if the imput is not null and not an empty string
   * 
   * @param s the input to check
   * @return true if the imput is not null and not an empty string
   */
  public static boolean isNotEmpty(String s) {
    return !isNullOrEmpty(s);
  }

  /**
   * This method accepts name with char, number or '_' or '.'
   * <p>
   * This method should be used to check all LoginName input from user for security.
   *
   * @todo: use StringBuffer
   * @param locale TODO
   */
  public static void checkGoodName(String str, Locale locale) throws BadInputException {
    checkGoodName(str, false, locale);
  }

  /**
   * This method accepts name with char, number or '_' or '.' or ' ' if allowSpace = true
   * <p>
   * This method should be used to check all LoginName input from user for security.
   *
   * @todo: use StringBuffer
   * @param locale TODO
   */
  public static void checkGoodName(String str, boolean allowSpace, Locale locale)
      throws BadInputException {
    int length = str.length();
    char c = 0;

    for (int i = 0; i < length; i++) {
      c = str.charAt(i);
      if ((c >= 'a') && (c <= 'z')) {
        // lower char
      } else if ((c >= 'A') && (c <= 'Z')) {
        // upper char
      } else if ((c >= '0') && (c <= '9')/* && (i != 0) */) {
        // minhnn: as of 31 Jan 2004, i relax the LoginName checking
        // so that the LoginName can start with an numeric char
        // hopefully it does not introduce a security bug
        // because this value will be inserted into sql script

        // numeric char
      } else if (((c == '_') || (c == '.') || (c == '@')) && (i != 0)) {
        // minhnn: as of 12 Jan 2005, I relax the LoginName checking
        // so that it can have '@' because many one use email as a LoginName

        // _ char

        // If you need to allow non-ASCII chars, please uncomment the below "else if"
        // However, this is not recommended since there will be potential security
        // } else if (c >= 0x80) {
        // by huxn allow NON-ASCII char
      } else if ((c == ' ') && allowSpace) {
        // space
      } else {
        // not good char, throw an BadInputException
        // @todo : localize me
        String localizedMessage = MVNCoreResourceBundle.getString(locale,
            "mvncore.exception.BadInputException.not_good_name",
            new Object[] {DisableHtmlTagFilter.filter(str), new Character(c)});
        throw new BadInputException(localizedMessage);
      }
    } // for
  }

  /**
   * Get the shorter string, the max length is defined as SHORT_STRING_LENGTH
   *
   * @param str String the input string
   * @return String the sorter string, with the max length = SHORT_STRING_LENGTH
   */
  public static String getShorterString(String str) {
    return getShorterString(str, SHORT_STRING_LENGTH);
  }

  /**
   * Get the shorter string, the current implementation check the last occurrence of
   * Character.isWhitespace(currentChar) as the break
   *
   * @param str String the input string
   * @param maxLength int the max length of the shorter string
   * @return String the string after being making shorter
   */
  public static String getShorterString(String str, int maxLength) {

    if (maxLength < 0) {
      throw new IllegalArgumentException("The maxLength < 0 is not allowed.");
    }
    if (str == null) {
      return "";
    }
    if (str.length() <= maxLength) {
      return str;
    }
    String s = str.substring(0, maxLength);
    char currentChar;
    int index;
    for (index = s.length() - 1; index >= 0; index--) {
      currentChar = s.charAt(index);
      if (Character.isWhitespace(currentChar)) {
        break;
      }
    }
    String shortString = s.substring(0, index + 1);
    return shortString + "...";
  }

  /**
   * Get the shorter string, this is the old implementation before 4 Otc, 2004. This implementation
   * does not check the space as the break one.
   *
   * @param str String the input string
   * @param maxLength int the max length of the shorter string
   * @return String the string after being making shorter
   */
  public static String getShorterStringIgnoreSpace(String str, int maxLength) {
    if (maxLength < 0) {
      throw new IllegalArgumentException("The maxLength < 0 is not allowed.");
    }
    if (str == null) {
      return "";
    }
    if (str.length() <= maxLength) {
      return str;
    }
    return str.substring(0, maxLength) + "...";
  }

  /**
   * Replace the occurred char to a String
   *
   * @param input String the input string
   * @param from char the char that is used to search
   * @param to String the string that will replace the char
   * @return String the string after being replaced
   */
  public static String replace(String input, char from, String to) {
    if (input == null) {
      return null;
    }

    char[] s = input.toCharArray();
    int length = s.length;
    StringBuffer ret = new StringBuffer(length * 2);

    for (int i = 0; i < length; i++) {
      if (s[i] == from) {
        ret.append(to);
      } else {
        ret.append(s[i]);
      }
    } // for
    return ret.toString();
  }

  /**
   * This method can be replaced by getStringArray
   */
  public static Collection getSeparateString(String strContent, String pattern) {
    int beginIndex = 0;
    Collection coResult = new ArrayList();
    String result;
    int position = strContent.indexOf(pattern, beginIndex); // Get the first position
    while (position != -1) {
      result = strContent.substring(beginIndex, position);
      if (!result.trim().equals("")) {
        coResult.add(result);
      }
      beginIndex = position + pattern.length(); // we add the length of the partern such as ';'
      position = strContent.indexOf(pattern, beginIndex);
    }

    return coResult;
  }

  /**
   * Convert a password to a hidden format, usually the asterisk character is used, such as 'secret'
   * is converted to '******'
   *
   * @param password String the input password that need to convert to hidden format
   * @return String the password after being converted to the hidden format
   */
  public static String getHiddenPassword(String password) {
    password = getEmptyStringIfNull(password);
    int length = password.length();
    if (length == 0) {
      return password;
    }
    StringBuffer hiddenPassword = new StringBuffer(length);
    for (int i = 0; i < length; i++) {
      hiddenPassword.append('*');
    }
    return hiddenPassword.toString();
  }

  // fix bug for pluto 1.1
  public static String escapeBackSlash(String str) {
    if (str == null) {
      return null;
    }
    // I didn't use 0x92 because pluto will use 0x9 as # , see PortalURLParserImpl
    return str.replaceAll("\\\\", "0y92");
  }

  // fix bug for pluto 1.1
  public static String unEscapeBackSlash(String str) {
    if (str == null) {
      return null;
    }
    return str.replaceAll("0y92", "\\\\");
  }

  public static String escapeQuote(String input) {
    input = StringEscapeUtils.escapeJavaScript(input);
    char[] chars = input.toCharArray();
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < chars.length; i++) {
      // if (isQuote(chars[i])) {
      // buffer.append('\\').append('"'); // append "\'"
      // } else if (chars[i] == '\n' || chars[i] == '\r') {
      // // do nothing
      // } else if (chars[i] == '\\') {
      // buffer.append('\\').append('\\'); // append "\'"
      // } else
      if (i + 5 < chars.length && // find '&apos;' ~ single quote
          chars[i] == '&' && chars[i + 1] == 'a' && chars[i + 2] == 'p' && chars[i + 3] == 'o'
          && chars[i + 4] == 's' && chars[i + 5] == ';') {
        buffer.append('\\').append('\'');
        i += 5;
        // do nothing
      } else if (i + 4 < chars.length && // find '&#39;' ~ single quote
          chars[i] == '&' && chars[i + 1] == '#' && chars[i + 2] == '3' && chars[i + 3] == '9'
          && chars[i + 4] == ';') {
        buffer.append('\\').append('\'');
        i += 4;
      } else {
        buffer.append(chars[i]);
      }
    }

    String result = DisableHtmlTagFilter.filter(buffer.toString());
    return result;
  }

  public static String displayMediaContent(String url, int width, int height) {

    if ("".equals(url) || url == null) {
      return "";
    }
    if (encoderService == null) {
      encoderService = MvnCoreServiceFactory.getMvnCoreService().getEncoderService();
    }
    url = encoderService.filterUrl(url);

    StringBuffer result = new StringBuffer(128);
    String size = "";
    if (width > 0) {
      size += " width='" + width + "'";
    }
    if (height > 0) {
      size += " height='" + height + "'";
    }

    String lowerURL = url.toLowerCase();

    if (lowerURL.endsWith(".swf")) {
      result.append("<embed src='").append(url)
          .append("' quality='high' scale='noborder' wmode='transparent'")
          .append(" bgcolor='#000000'").append(size).append(" type='application/x-shockwave-flash'")
          .append(
              " pluginspage='http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash'></embed>");
    } else if (lowerURL.endsWith(".jpeg") || lowerURL.endsWith(".jpg") || lowerURL.endsWith(".png")
        || lowerURL.endsWith(".gif")) {
      result.append("<img src='").append(url).append("'").append(size)
          .append(" border='0' alt='' />");
    } else if (lowerURL.endsWith(".mov")) {
      result.append("<object classid='clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B'").append(size)
          .append(
              " codebase='http://www.apple.com/qtactivex/qtplugin.cab'><param name='src' value='")
          .append(url).append(
              "'><param name='autoplay' value='true'><param name='controller' value='false'><param name='loop' value='true'><embed src='"
                  + url
                  + "' width='320' height='255' autoplay='true' controller='false' loop='true' pluginspage='http://www.apple.com/quicktime/download/'></embed></object>");
    } else if (lowerURL.endsWith(".mpg") || lowerURL.endsWith(".wmv")
        || lowerURL.endsWith(".avi")) {
      result.append("<embed src='").append(url).append("'").append(size).append(
          " type='application/x-mplayer2' EnableContextMenu='0' AutoStart='1' loop='1' ShowStatusBar='0' ShowControls='0'></embed>");
    } else {
      // do nothing
    }

    return result.toString();
  }

  public static int parseIntSizeValue(String propertyValue, int defaultValue) {

    try {
      String temp = propertyValue.trim();
      if (temp.endsWith("B") || temp.endsWith("b")) {
        temp = temp.substring(0, temp.length() - 1);
      }
      switch (temp.charAt(temp.length() - 1)) {
        case 'K':
        case 'k':
          // examples (ending 'B' was cut before): "1K", "1KB", "1k", "1kB", "1 K", "1 KB", "1 k",
          // "1 kB"
          return 1024 * Integer.parseInt(temp.substring(0, temp.length() - 1).trim());
        case 'M':
        case 'm':
          // examples (ending 'B' was cut before): "1M", "1MB", "1m", "1mB", "1 M", "1 MB", "1 m",
          // "1 mB"
          return 1024 * 1024 * Integer.parseInt(temp.substring(0, temp.length() - 1).trim());
        default:
          // examples (ending 'B' was cut before): "1", "1B", "1 B"
          return Integer.parseInt(temp.trim());
      }
    } catch (Exception e) {
      log.warn("Error while parsing the value " + propertyValue, e);
      return defaultValue;
    }

  }

  public static String getReferer(HttpServletRequest request) {
    String host = request.getHeader("Host");
    host = (host != null) ? host : MVNCoreConfig.getServerPath();
    String referer = request.getHeader("Referer");
    referer = StringUtil.getEmptyStringIfNull(referer);
    int indexOfHost = referer.indexOf("://" + host);
    // check this again
    if ((indexOfHost == -1) || (indexOfHost > 10) || // 10 is the maximum position when search for
                                                     // the host
        (referer.indexOf("/logout") != -1) || (referer.indexOf("/login") != -1)
        || (referer.indexOf("/register") != -1) || (referer.indexOf("process") != -1)) {
      referer = "";
    }
    return referer;
  }

  public static String getSelectionModel(int[] indexes, String[] values, String name,
      int selected) {
    StringBuffer stringBuffer = new StringBuffer(512);

    stringBuffer.append("<select name=\"" + name + "\" id=\"" + name + "\">\n");
    for (int i = 0; i < indexes.length; i++) {
      stringBuffer.append("<option value=\"" + indexes[i] + "\"");
      if (selected == indexes[i]) {
        stringBuffer.append(" selected=\"selected\"");
      }
      stringBuffer.append(">" + values[i] + "</option>\n");
    }
    stringBuffer.append("</select>\n");

    return stringBuffer.toString();
  }

  public static String removeNonDigits(String string) {

    if (string == null) {
      return string;
    }

    char[] carray = string.toCharArray();
    StringBuffer sb = new StringBuffer(carray.length);

    for (int i = 0; i < carray.length; i++) {
      if ((carray[i] == '.') || (Character.isDigit(carray[i]))) {
        sb.append(carray[i]);
      }
    }

    return sb.toString();
  }

  public static String getDomainFromURL(String url) {
    if (url == null || url.length() == 0) {
      return "";
    }
    String domain = "";
    try {
      domain = new URL(url).getHost();
    } catch (MalformedURLException e) {
      // do nothing
    }
    return domain;
  }

  public static String escapeJavaScript(String str) {
    if (str == null) {
      return null;
    }
    try {
      StringWriter writer = new StringWriter(str.length() * 2);
      escapeJavaStyleString(writer, str);
      return writer.toString();
    } catch (IOException ioe) {
      // this should never ever happen while writing to a StringWriter
      ioe.printStackTrace();
      return null;
    }
  }

  private static void escapeJavaStyleString(Writer out, String str) throws IOException {
    if (str == null) {
      return;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      char ch = str.charAt(i);

      if (ch < 32) {
        switch (ch) {
          case '\b':
            out.write('\\');
            out.write('b');
            break;
          case '\n':
            out.write('\\');
            out.write('n');
            break;
          case '\t':
            out.write('\\');
            out.write('t');
            break;
          case '\f':
            out.write('\\');
            out.write('f');
            break;
          case '\r':
            out.write('\\');
            out.write('r');
            break;
          default:
            out.write(ch);
            break;
        }
      } else {
        switch (ch) {
          case '\'':
            out.write('\\');
            out.write('\'');
            break;
          case '"':
            out.write('\\');
            out.write('"');
            break;
          case '\\':
            out.write('\\');
            out.write('\\');
            break;
          case '/':
            out.write('\\');
            out.write('/');
            break;
          default:
            out.write(ch);
            break;
        }
      }
    }
  }

  public static String escapeLuceneSpecialCharacters(String str) {
    if (str == null) {
      return null;
    }
    try {
      StringWriter writer = new StringWriter(str.length() * 2);
      escapeLuceneSpecialCharacterString(writer, str);
      return writer.toString();
    } catch (IOException ioe) {
      // this should never ever happen while writing to a StringWriter
      ioe.printStackTrace();
      return null;
    }
  }

  private static void escapeLuceneSpecialCharacterString(Writer out, String str)
      throws IOException {
    if (str == null) {
      return;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      char ch = str.charAt(i);
      /*
       * Reference link: http://lucene.apache.org/java/docs/queryparsersyntax.html Lucene supports
       * escaping special characters that are part of the query syntax. The current list special
       * characters are: + - && || ! ( ) { } [ ] ^ " ~ * ? : \ To escape these character use the \
       * before the character.
       */

      switch (ch) {
        case '+':
        case '-':
        case '!':
        case '(':
        case ')':
        case '{':
        case '}':
        case '[':
        case ']':
        case '^':
        case '\"':
        case '~':
        case '*':
        case '?':
        case ':':
        case '\\':
          out.write('\\');
          out.write(ch);
          break;
        case '&':
        case '|':
          // check if it is '&&' or '||' ~ check if the next char is '&' or '|'
          if ((i + 1) < sz && str.charAt(i + 1) == ch) {
            out.write('\\');
            out.write(ch);
            i++;
          }
          out.write(ch);
          break;
        default:
          out.write(ch);
          break;
      }
    }
  }

  public static String wordwrap(String text, int characters, String replace) {
    if (text == null) {
      return null;
    }
    if (text.length() < characters) {
      return text;
    }
    Pattern pattern = Pattern.compile("([^\\s<>\'\"/\\-@$=?&\n\r%]{" + characters + "})");
    Matcher m = pattern.matcher(text);
    StringBuffer sb = new StringBuffer();
    String[] strings = getUnreplacableMatcher(text);
    while (m.find()) {
      if (isReplacableMatcher(strings, m.group())) {
        m.appendReplacement(sb, m.group().concat(replace));
      } else {
        m.appendReplacement(sb, m.group());
      }
    }
    m.appendTail(sb);
    return sb.toString();
  }

  private static boolean isReplacableMatcher(String[] strings, String replace) {
    for (int i = 0; i < strings.length; i++) {
      if (strings[i].indexOf(replace) > -1) {
        return false;
      }
    }
    return true;
  }

  private static String[] getUnreplacableMatcher(String text) {
    Pattern pattern = Pattern.compile("(ht|f)tp(s?)://(\\p{Graph})*");
    Matcher m = pattern.matcher(text);
    Collection collection = new ArrayList();
    while (m.find()) {
      collection.add(m.group());
    }
    return (String[]) collection.toArray(new String[0]);
  }

  // for test only
  public static void main(String[] args) throws Exception {
    // String[] s = getStringArray(" fasg;, zdgsag, ,,", ",");
    // String[] s = getStringArray(" fasg ", ",");
    // System.out.println("length = " + s.length);
    // for (int i = 0; i < s.length; i++) {
    // System.out.println("" + i + " : " + s[i]);
    // }

    // String s1 = " abc das\n\n\n\n\ndasd asd adad as das as da adas da sd ad as sa das das d a .";
    // System.out.println("r = [" + StringUtil.getShorterString(s1, 22) + "]");
  }

}
