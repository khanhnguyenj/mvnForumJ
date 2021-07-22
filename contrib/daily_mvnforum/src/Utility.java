// $RCSfile: Utility.java,v $
//
// Project "Principia"
//
// Michael Meyling
// Stoltenstrasse 38
// 22119 Hamburg
// Germany

import java.io.*;

/**
 * A collection of usefull static methods.
 * @version $Revision: 1.3 $
 * @author  Michael Meyling
 */
public class Utility {

    /**
     * Constructor, should never be called.
     */
    private Utility() {
        super();
    }

    /**
     * Reads a file and returns the contents as a <code>String</code>.
     *
     * @param   filename name of the file (could include path)
     * @return  contents of file
     * @throws  IOException if a file exception occured
     */
    public static final String loadFile(final String filename) throws IOException {

        String text = null;

        final File f = new File(filename);
        final int size = (int) f.length();
        final FileReader in = new FileReader(filename);
        final char[] data = new char[size];
        int charsread = 0;
        while (charsread < size) {
            charsread += in.read(data, charsread, size - charsread);
        }
        in.close();
        text = new String(data);
        return text;
    }

    /**
     * Reads contents of a file into a string buffer.
     *
     * @param   filename    name of the file (could include path)
     * @param   buffer      buffer to fill with file contents
     * @throws  IOException if a file exception occured
     */
    public static final void loadFile(final String filename, final StringBuffer buffer) throws IOException {

        final File f = new File(filename);
        final int size = (int) f.length();
        buffer.setLength(0);
        final FileReader in = new FileReader(filename);
        final char[] data = new char[size];
        int charsread = 0;
        while (charsread < size) {
            charsread += in.read(data, charsread, size - charsread);
        }
        in.close();
        buffer.insert(0, data);
    }

    /**
     * Reads contents of a file into a string buffer.
     *
     * @param   file        this file will be loaded
     * @param   buffer      buffer to fill with file contents
     * @throws  IOException if a file exception occured
     */
    public static final void loadFile(final File file, final StringBuffer buffer) throws IOException {

        final int size = (int) file.length();
        buffer.setLength(0);
        final FileReader in = new FileReader(file);
        final char[] data = new char[size];
        int charsread = 0;
        while (charsread < size) {
            charsread += in.read(data, charsread, size - charsread);
        }
        in.close();
        buffer.insert(0, data);
    }

    /**
     * Saves a <code>String</code> in a file. In case of an IO error
     * a message is written to <code>System.out</code>.
     *
     * @param   filename    name of the file (could include path)
     * @param   text        data to save in the file
     * @throws  IOException if a file exception occured
     */
    public static final void saveFile(String filename, String text) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
        out.write(text);
        out.close();
    }

    /**
     * Saves a <code>String</code> in a file. In case of an IO error
     * a message is written to <code>System.out</code>.
     *
     * @param   filename    name of the file (could include path)
     * @param   text        data to save in the file
     * @throws  IOException if a file exception occured
     */
    public static final void saveFile(String filename, StringBuffer text) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
        out.write(text.toString());
        out.close();
    }

    /**
     * Quotes a <code>String</code>. If no quotes exist in the
     * <code>String</code>, a quote character is appended at the
     * beginning and the end of the <code>String</code>.
     *
     * @param   unquoted    the ungequoted" <code>String</code>
     * @return  quoted <code>String</code>
     * @throws  NullPointerException if <code>unquoted == null</code>
     */
    public static final String quote(String unquoted) {

        String result = new String("\"");

        for (int i = 0; i < unquoted.length(); i++) {
            if (unquoted.charAt(i) == '\"') {
                result += "\"\"";
            } else {
                result += unquoted.charAt(i);
            }
        }
        result += '\"';
        return result;
    }

    /**
     * Tests if given <code>String</code> begins with a letter and contains
     * only letters and digits.
     *
     * @param   text    test this
     * @return  is <code>text</code> only made of letters and digits and has
     *          a leading letter?
     * @throws  NullPointerException if <code>text == null</code>
     */
    public static final boolean isLetterDigitString(final String text) {
        if (text.length() <= 0) {
            return false;
        }
        if (!Character.isLetter(text.charAt(0))) {
            return false;
        }
        for (int i = 1; i < text.length(); i++) {
            if (!Character.isLetterOrDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Replaces all occurences of <code>search</code> in <code>text</code>
     * by <code>replace</code> and returns the result.
     *
     * @param   text    text to work on
     * @param   search  replace this text by <code>replace</code>
     * @param   replace replacement for <code>search</code>
     * @return  resulting string
     */
    public static final String replace(final String text, final String search, final String replace) {

        final StringBuffer result = new StringBuffer();
        int pos1 = 0;
        int pos2;
        final int len = search.length();
        while (0 <= (pos2 = text.indexOf(search, pos1))) {
            result.append(text.substring(pos1, pos2));
            result.append(replace);
            pos1 = pos2 + len;
        }
        if (pos1 < text.length()) {
            result.append(text.substring(pos1));
        }
        return result.toString();
    }

    /**
     * Waits til a '\n' was read from System.in.
     */
    public static final void waitln() {
        System.out.println("\n..press <return> to continue");
        try {
            (new java.io.BufferedReader(new java.io.InputStreamReader(System.in))).readLine();
        } catch (IOException e) {
        }
    }

    /**
     * Get amount of spaces.
     *
     * @param   length  number of spaces
     * @return  String containig exactly <code>number</code> spaces
     */
    public static final StringBuffer getSpaces(int length) {
        final StringBuffer buffer = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            buffer.append(' ');
        }
        return buffer;
    }

}