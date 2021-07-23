/*
 * CompressionResponseStream.java
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/servlet/filter/CompressionResponseStream.java,v 1.8 2009/01/01 18:30:12 minhnn Exp $
 * $Revision: 1.8 $
 * $Date: 2009/01/01 18:30:12 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * [Additional notices, if required by prior licensing conditions]
 *
 */

package net.myvietnam.mvncore.servlet.filter;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation of <b>ServletOutputStream</b> that works with
 * the CompressionServletResponseWrapper implementation.
 *
 * @author Amy Roh
 * @author Dmitri Valdin
 * @version $Revision: 1.8 $, $Date: 2009/01/01 18:30:12 $
 */

public class CompressionResponseStream extends ServletOutputStream {

    //minhnn : I remove the log because log4j does not support TRACE level
    private static final Logger log = LoggerFactory.getLogger(CompressionResponseStream.class);

    // ----------------------------------------------------------- Constructors


    /**
     * Construct a servlet output stream associated with the specified Response.
     *
     * @param response The associated response
     */
    public CompressionResponseStream(HttpServletResponse response) throws IOException{

        super();
        closed = false;
        this.response = response;
        this.output = response.getOutputStream();

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * The threshold number which decides to compress or not.
     * Users can configure in web.xml to set it to fit their needs.
     */
    protected int compressionThreshold = 0;

    /**
     * Debug level
     */
//    private int debug = 0;

    /**
     * The buffer through which all of our output bytes are passed.
     */
    protected byte[] buffer = null;

    /**
     * The number of data bytes currently in the buffer.
     */
    protected int bufferCount = 0;

    /**
     * The underlying gzip output stream to which we should write data.
     */
    protected GZIPOutputStream gzipstream = null;

    /**
     * Has this stream been closed?
     */
    protected boolean closed = false;

    /**
     * The content length past which we will not write, or -1 if there is
     * no defined content length.
     */
    protected int length = -1;

    /**
     * The response with which this servlet output stream is associated.
     */
    protected HttpServletResponse response = null;

    /**
     * The underlying servket output stream to which we should write data.
     */
    protected ServletOutputStream output = null;


    // --------------------------------------------------------- Public Methods

    /**
     * Set debug level
     */
//    public void setDebugLevel(int debug) {
//        this.debug = debug;
//    }


    /**
     * Set the compressionThreshold number and create buffer for this size
     */
    protected void setBuffer(int threshold) {
        compressionThreshold = threshold;
        buffer = new byte[compressionThreshold];
        //log.trace("buffer is set to " + compressionThreshold);
    }

    /**
     * Close this output stream, causing any buffered data to be flushed and
     * any further output data to throw an IOException.
     */
    public void close() throws IOException {

        //log.trace("close() @ CompressionResponseStream", new IOException("tracing"));
        if (closed) {
            //minhnn : instead of throw IOException, we should log waring or even
            // ignore it so that it does not throw exception when close many time
            //throw new IOException("This output stream has already been closed");
            log.warn("This compression output stream has already been closed");
            
            return;// minhnn: on 1 Otc 2007 I add return to not close it more than one time
        }

        if (gzipstream != null) {
            flushToGZip();
            gzipstream.close();
            gzipstream = null;
        } else {
            if (bufferCount > 0) {
                // minhnn comment
                /*
                if (debug > 2) {
                    System.out.print("output.write(");
                    System.out.write(buffer, 0, bufferCount);
                    System.out.println(")");
                }*/
                output.write(buffer, 0, bufferCount);
                bufferCount = 0;
            }
        }

        output.close();
        closed = true;

    }


    /**
     * Flush any buffered data for this output stream, which also causes the
     * response to be committed.
     */
    public void flush() throws IOException {

        //log.trace("flush() @ CompressionResponseStream");
        if (closed) {
            //throw new IOException("Cannot flush a closed output stream");
            log.warn("Cannot flush a closed output stream");
        }

        if (gzipstream != null) {
            gzipstream.flush();
        }

    }

    public void flushToGZip() throws IOException {

        //log.trace("flushToGZip() @ CompressionResponseStream");
        if (bufferCount > 0) {
            //log.trace("flushing out to GZipStream, bufferCount = " + bufferCount);
            writeToGZip(buffer, 0, bufferCount);
            bufferCount = 0;
        }

    }

    /**
     * Write the specified byte to our output stream.
     *
     * @param b The byte to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(int b) throws IOException {

        //log.trace("write " + b + " in CompressionResponseStream ");
        if (closed) {
            throw new IOException("Cannot write to a closed output stream");
        }

        if (bufferCount >= buffer.length) {
            flushToGZip();
        }

        buffer[bufferCount++] = (byte) b;

    }


    /**
     * Write <code>b.length</code> bytes from the specified byte array
     * to our output stream.
     *
     * @param b The byte array to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(byte b[]) throws IOException {

        write(b, 0, b.length);

    }


    /**
     * Write <code>len</code> bytes from the specified byte array, starting
     * at the specified offset, to our output stream.
     *
     * @param b The byte array containing the bytes to be written
     * @param off Zero-relative starting offset of the bytes to be written
     * @param len The number of bytes to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(byte b[], int off, int len) throws IOException {

        //log.trace("write, bufferCount = " + bufferCount + " len = " + len + " off = " + off);
        /* minhnn comment
        if (debug > 2) {
            System.out.print("write(");
            System.out.write(b, off, len);
            System.out.println(")");
        }*/

        if (closed) {
            throw new IOException("Cannot write to a closed output stream");
        }

        if (len == 0) {
            return;
        }

        // Can we write into buffer ?
        if (len <= (buffer.length - bufferCount)) {
            System.arraycopy(b, off, buffer, bufferCount, len);
            bufferCount += len;
            return;
        }

        // There is not enough space in buffer. Flush it ...
        flushToGZip();

        // ... and try again. Note, that bufferCount = 0 here !
        if (len <= (buffer.length - bufferCount)) {
            System.arraycopy(b, off, buffer, bufferCount, len);
            bufferCount += len;
            return;
        }

        // write direct to gzip
        writeToGZip(b, off, len);
    }

    public void writeToGZip(byte b[], int off, int len) throws IOException {

        //log.trace("writeToGZip, len = " + len);
        /* minhnn comment
        if (debug > 2) {
            System.out.print("writeToGZip(");
            System.out.write(b, off, len);
            System.out.println(")");
        }*/
        if (gzipstream == null) {
            //log.trace("new GZIPOutputStream");
            response.addHeader("Content-Encoding", "gzip");
            gzipstream = new GZIPOutputStream(output);
        }
        gzipstream.write(b, off, len);

    }


    // -------------------------------------------------------- Package Methods


    /**
     * Has this response stream been closed?
     */
    public boolean closed() {

        return (this.closed);

    }

}
