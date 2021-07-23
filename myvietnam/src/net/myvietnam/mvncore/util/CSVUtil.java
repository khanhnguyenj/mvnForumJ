/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/util/CSVUtil.java,v 1.7 2009/12/04 08:57:41 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.7 $
 * $Date: 2009/12/04 08:57:41 $
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
 * @author: MyVietnam.net developers
 */
package net.myvietnam.mvncore.util;
import java.io.*;

// TODO should check this class as a reference: org.hsqldb.util.CSVWriter
public class CSVUtil {

    protected Writer out;
    protected boolean autoFlush = true;
    protected boolean error = false;
    protected char delimiterChar = ',';
    protected char quoteChar = '"';
    protected boolean newLine = true;
    protected char commentStart = '#';

    public CSVUtil(OutputStream out) {
        this.out = new OutputStreamWriter(out);
    }

    public CSVUtil(OutputStream out, String charset) throws UnsupportedEncodingException {
        this.out = new OutputStreamWriter(out, charset);
    }

    private String escapeAndQuote(String value) {
        int count = 2;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch(c) {
                case '\n': case '\r': case '\\': {
                    count ++;
                } break;
                default: {
                    if (c == quoteChar) {
                        count++;
                    }
                } break;
            }
        }
        StringBuffer sb = new StringBuffer(value.length() + count);
        sb.append(quoteChar);
        for (int i=0; i<value.length(); i++) {
            char c = value.charAt(i);
            switch(c) {
                case '\n': {
                    sb.append("\\n");
                } break;
                case '\r': {
                    sb.append("\\r");
                } break;
                case '\\': {
                    sb.append("\\\\");
                } break;
                default: {
                    if (c == quoteChar) {
                        sb.append("\\" + quoteChar);
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        sb.append(quoteChar);
        return (sb.toString());
    }

    //abc, def
    public void write(String value) throws IOException {
        try {
            if (value == null) {
                value = "";
            }
            boolean quote = false;
            if (value.length() > 0) {
                char c = value.charAt(0);
                if (newLine && (c<'0' || (c>'9' && c<'A') || (c>'Z' && c<'a') || (c>'z'))) {
                    quote = true;
                }
                if (c==' ' || c=='\f' || c=='\t') {
                    quote = true;
                }
                for (int i = 0; i < value.length(); i++) {
                    c = value.charAt(i);
                    if ((c==quoteChar) || (c==delimiterChar) || (c=='\n') || (c=='\r')) {
                        quote = true;
                    }
                }
                if ((c == ' ') || (c == '\f') || (c == '\t')) {
                    quote = true;
                }
            } else if (newLine) {
                quote = true;
            }
            if (newLine) {
                newLine = false;
            } else {
                out.write(delimiterChar);
            }
            if (quote) {
                out.write(escapeAndQuote(value));
            } else {
                out.write(value);
            }
            if (autoFlush) {
                flush();
            }
        } catch (IOException iox) {
            error = true;
            throw iox;
        }
    }

    public void writeln(String value) throws IOException {
        try {
            write(value);
            writeln();
        } catch (IOException iox) {
            error = true;
            throw iox;
        }
    }

    public void writeln() throws IOException {
        try {
            out.write("\n");
            if (autoFlush) {
                flush();
            }
            newLine = true;
        } catch (IOException iox) {
            error = true;
            throw iox;
        }
    }

    public void writeln(String[] values) throws IOException {
        try {
            print(values);
            writeln();
        } catch (IOException iox) {
            error = true;
            throw iox;
        }
    }

    public void write(String[] values) throws IOException {
        try {
            for (int i=0; i<values.length; i++) {
                write(values[i]);
            }
        } catch (IOException iox) {
            error = true;
            throw iox;
        }
    }

    public void writeln(String[][] values) throws IOException {
        try {
            for (int i=0; i<values.length; i++) {
                writeln(values[i]);
            }
            if (values.length == 0) {
                writeln();
            }
        } catch (IOException iox) {
            error = true;
            throw iox;
        }
    }
    
    public void writelnComment(String comment) throws IOException {
        try {
            if (comment == null) {
                comment = "";
            }
            if (!newLine) {
                writeln();
            }
            out.write(commentStart);
            out.write(' ');
            for (int i=0; i<comment.length(); i++) {
                char c = comment.charAt(i);
                switch (c) {
                    case '\r': {
                        if (i+1 < comment.length() && comment.charAt(i+1) == '\n') {
                            i++;
                        }
                    } //break intentionally excluded.
                    case '\n': {
                        writeln();
                        out.write(commentStart);
                        out.write(' ');
                    } break;
                    default: {
                        out.write(c);
                    } break;
                }
            }
            writeln();
        } catch (IOException iox) {
            error = true;
            throw iox;
        }
    }
    
    public void println(String value) {
        try {
            writeln(value);
        } catch (IOException iox) {
            error = true;
        }
    }

    public void println() {
        try {
            writeln();
        } catch (IOException iox) {
            error = true;
        }
    }

    public void println(String[] values) {
        try {
            writeln(values);
        } catch (IOException iox) {
            error = true;
        }
    }

    public void print(String[] values) {
        try {
            write(values);
        } catch (IOException iox) {
            error = true;
        }
    }

    public void println(String[][] values) {
        try {
            writeln(values);
        } catch (IOException iox) {
            error = true;
        }
    }

    public void printlnComment(String comment) {
        try {
            writelnComment(comment);
        } catch (IOException iox) {
            error = true;
        }
    }

    public void print(String value) {
        try {
            write(value);
        } catch (IOException iox) {
            error = true;
        }
    }

    public void flush() throws IOException {
        out.flush();
    }
    
    public boolean checkError() {
        try {
            if (error) {
                return true;
            }
            flush();
            if (error) {
                return true;
            }
            if (out instanceof PrintWriter) {
                error = ((PrintWriter)out).checkError();
            }
        } catch (IOException iox) {
            error = true;
        }
        return error;
    }
}