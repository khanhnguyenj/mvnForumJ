/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/XMLWriter.java,v 1.13 2009/01/12 15:02:34 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.13 $
 * $Date: 2009/01/12 15:02:34 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain 
 * intact in the scripts and in the outputted HTML.
 * The "powered by" text/logo with a link back to
 * http://www.mvnForum.com and http://www.MyVietnam.net in 
 * the footer of the pages MUST remain visible when the pages
 * are viewed on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Support can be obtained from support forums at:
 * http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Igor Manic   
 */
package com.mvnforum.admin.importexport;

import java.io.*;

import net.myvietnam.mvncore.exception.ExportException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Igor Manic
 * @version $Revision: 1.13 $, $Date: 2009/01/12 15:02:34 $
 * <br/>
 * <code>XMLWriter</code> class encapsulates methods for creating and writing
 * data to XML files with automatic indentation and special characters encoding.
 */
public class XMLWriter {

    /** Message log. */
    private static final Logger log = LoggerFactory.getLogger(XMLWriter.class);

    private static String lineSeparator = System.getProperty("line.separator", "\n");
    private StringBuffer textBuffer;
    private int indentLevel = 0; //current XML element level
    private String indentString = ""; //indent string for each next level of XML output
    private OutputStreamWriter outWriter = null;
    private boolean startedNewElement = false;

    private XMLWriter() {
        textBuffer = null;
        indentLevel = 0; 
        indentString = "";
        outWriter = null;
        startedNewElement = false;
    }

    public XMLWriter(String indentString, OutputStreamWriter outWriter) {
        this();
        this.indentString = indentString;
        this.outWriter = outWriter;
    }

    public XMLWriter(String indentString, File outputFile)
        throws ExportException {
        
        this();
        this.indentString = indentString;
        log.debug("Setting output to file \"" + outputFile.getAbsolutePath() + "\"");
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                log.error("XML output could not be created.");
                throw new ExportException("Error on server: "+
                      "Can't make XML output file.", e);
            }
        } else if (!outputFile.isFile()) {
            log.error("XML output is not a file (it's probably directory).");
            throw new ExportException("Error on server: "+
                  "XML output is not a file (it's probably directory).");
        }
        if (!outputFile.canWrite()) {
            log.error("XML output file can't be written to.");
            throw new ExportException("Error on server: "+
                  "XML output file can't be written to.");
        } else {
            try {
                OutputStream outStream = new FileOutputStream(outputFile);
                this.outWriter=new OutputStreamWriter(outStream, "UTF8");
            } catch (FileNotFoundException e) {
                log.error("XML output file can't be found.");
                throw new ExportException("Error on server: "+
                      "XML output file can't be found.", e);
            } catch (UnsupportedEncodingException e) {
                log.error("UTF8 is unsupported encoding for XML output.");
                throw new ExportException("Error on server: "+
                      "Can't make XML output file.", e);
            }
        }
    }

    public XMLWriter(String indentString, String fileName)
    throws ExportException {
        this(indentString, new File(fileName));
    }


    public void close() throws IOException {
        outputFlush();
        if (outWriter != null) {
            outWriter.close();
        }
    }

    public void startDocument(String dtdschemaDecl) throws IOException {
        String xmlDecl="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        startedNewElement = false;
        outputText(xmlDecl); 
        outputNewLine();
        outputText(dtdschemaDecl); 
        outputNewLine();
    }

    public void endDocument() throws IOException {
        processBufferedData();
        outputNewLine();
        outputFlush();
    }

    public void startElement(String elementName)
        throws IOException {
        startElement(elementName, null);
    }

    public void startElement(String elementName, String[] attrNameValue)
        throws IOException {
        processBufferedData();
        outputNewLine();
        outputText("<"+elementName);
        if (attrNameValue!=null) {
            for (int i=0; i<attrNameValue.length-1; i+=2) {
                String attrName = attrNameValue[i];
                String attrValue = attrNameValue[i+1];
                outputText(" ");
                outputText(attrName + "=\"" + attrValue + "\"");
            }
        }
        outputText(">");
        indentLevel++;
        //outputNewLine();
        startedNewElement = true;
    }

    public void endElement(String elementName)
        throws IOException {
        processBufferedData();
        indentLevel--;
        if (!startedNewElement) {
            outputNewLine();
        }
        outputText("</" + elementName + ">");
        //outputNewLine();
        startedNewElement = false;
    }


    public void writeNewLine() throws IOException {
        outputNewLine();
    }

    public void writeComment(String comment) throws IOException {
        processBufferedData();
        /*if (startedNewElement)*/ outputNewLine();
        outputText("<!-- "+comment+" -->");
        //startedNewElement=false; I should leave this variable, otherwise, if
        //I have more comments one behind another, they'll all be output in
        //one row, one to another, which is bad
    }

    public void writeData(String data) throws IOException {
        if (textBuffer == null) {
            textBuffer = new StringBuffer(data);
        } else {
            textBuffer.append(data);
        }
    }

    public void encodeAndWriteData(String data) throws IOException {
        //we should encode all '<' and '&'
        int amp = data.indexOf('&');
        int lt = data.indexOf('<');
        int i = -1;
        if ( (amp>=0) && (lt>=0) ) {
            i = (amp<lt) ? amp : lt;
        } else if (amp>=0) {
            i = amp;
        } else {
            i = lt;
        }
        while ( (i<data.length()) && (i>=0) ) {
            if (i == amp) {
                data = data.substring(0, i) + "&amp;" + data.substring(i+1);
                amp = data.indexOf('&', i+4);
                lt = data.indexOf('<', i+4);
                i += 4; //it should be 5, but never mind
            } else if (i==lt) {
                data = data.substring(0, i) + "&lt;" + data.substring(i+1);
                amp = data.indexOf('&', i+3);
                lt = data.indexOf('<', i+3);
                i += 3; //it should be 5, but never mind
            } else {
                log.error("processBufferedRawText(): i=="+i+
                          " is different than both amp=="+amp+" and lt=="+lt+"?!");
                i++;
                amp = data.indexOf('&', i);
                lt = data.indexOf('<', i);
            }
            if ( (amp>=0) && (lt>=0) ) {
                i = (amp<lt) ? amp : lt;
            } else if (amp >= 0) {
                i = amp;
            } else {
                i = lt;
            }
        }

        writeData(data);
    }



// ========================================================
// =================== RAW TEXT OUTPUT ====================
// ========================================================

    private void processBufferedData() throws IOException {
        if (textBuffer == null) {
            return;
        }
        String s = "" + textBuffer;
        if (!s.trim().equals("")) {
            textBuffer = null;
        }
        if (s.equals("")) {
            return;
        }

        if (startedNewElement) {
            outputNewLine();
        }
        startedNewElement=false;
        String padding="";
        for (int i=0; i<indentLevel; i++) {
            padding = padding + indentString;
        }
        int pos = s.indexOf(lineSeparator);
        while ((pos<s.length()) && (pos>=0)) {
            s = s.substring(0, pos)+
              lineSeparator+ padding+
              s.substring(pos+lineSeparator.length());
            pos = s.indexOf(lineSeparator, pos+lineSeparator.length());
        }
        outputText(s);
    }

    private void outputText(String s) throws IOException {
        if (outWriter == null) {
            log.error("outputText(): outWriter==null.");
        } else {
            outWriter.write(s);
        }
    }

    private void outputFlush() throws IOException {
        if (outWriter == null) {
            log.error("outputFlush(): outWriter==null.");
        } else {
            outWriter.flush();
        }
    }

    private void outputNewLine() throws IOException {
        if (outWriter == null) {
            log.error("outputNewLine(): outWriter==null.");
        } else {
            outWriter.write(lineSeparator);
            for (int i=0; i<indentLevel; i++) {
                outWriter.write(indentString);
            }
        }
    }

}
