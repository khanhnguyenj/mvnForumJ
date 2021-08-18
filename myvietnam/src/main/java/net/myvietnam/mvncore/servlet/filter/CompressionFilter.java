/*
 * CompressionFilter.java $Header:
 * /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/servlet/filter/CompressionFilter.java,v 1.8
 * 2008/12/30 10:46:29 trungth Exp $ $Revision: 1.8 $ $Date: 2008/12/30 10:46:29 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 *
 * 3. The end-user documentation included with the redistribution, if any, must include the
 * following acknowlegement: "This product includes software developed by the Apache Software
 * Foundation (http://www.apache.org/)." Alternately, this acknowlegement may appear in the software
 * itself, if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software Foundation" must not be used
 * to endorse or promote products derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" nor may "Apache" appear in
 * their names without prior written permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR ITS CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many individuals on behalf of the
 * Apache Software Foundation. For more information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * [Additional notices, if required by prior licensing conditions]
 *
 */

package net.myvietnam.mvncore.servlet.filter;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation of <code>javax.servlet.Filter</code> used to compress the ServletResponse if it is
 * bigger than a threshold.
 *
 * @author Amy Roh
 * @author Dmitri Valdin
 * @version $Revision: 1.8 $, $Date: 2008/12/30 10:46:29 $
 */

public class CompressionFilter implements Filter {

  private static final Logger log = LoggerFactory.getLogger(CompressionFilter.class);

  /**
   * The filter configuration object we are associated with. If this value is null, this filter
   * instance is not currently configured.
   */
  private FilterConfig config = null;

  /**
   * Minimal reasonable threshold
   */
  private int minThreshold = 128;


  /**
   * The threshold number to compress
   */
  protected int compressionThreshold;

  /**
   * Debug level for this filter
   */
  // private int debug = 0;

  /**
   * Place this filter into service.
   *
   * @param filterConfig The filter configuration object
   */

  @Override
  public void init(FilterConfig filterConfig) {

    config = filterConfig;
    if (filterConfig != null) {
      /*
       * String value = filterConfig.getInitParameter("debug"); if (value!=null) { debug =
       * Integer.parseInt(value); } else { debug = 0; }
       */
      String str = filterConfig.getInitParameter("compressionThreshold");
      if (str != null) {
        compressionThreshold = Integer.parseInt(str);
        if (compressionThreshold != 0 && compressionThreshold < minThreshold) {
          log.info(
              "compressionThreshold should be either 0 - no compression or >= " + minThreshold);
          log.info("compressionThreshold set to " + minThreshold);
          compressionThreshold = minThreshold;
        }
      } else {
        compressionThreshold = 0;
      }

    } else {
      compressionThreshold = 0;
    }

  }

  /**
   * Take this filter out of service.
   */
  @Override
  public void destroy() {

    this.config = null;

  }

  /**
   * The <code>doFilter</code> method of the Filter is called by the container each time a
   * request/response pair is passed through the chain due to a client request for a resource at the
   * end of the chain. The FilterChain passed into this method allows the Filter to pass on the
   * request and response to the next entity in the chain.
   * <p>
   * This method first examines the request to check whether the client support compression. <br>
   * It simply just pass the request and response if there is no support for compression.<br>
   * If the compression support is available, it creates a CompressionServletResponseWrapper object
   * which compresses the content and modifies the header if the content length is big enough. It
   * then invokes the next entity in the chain using the FilterChain object
   * (<code>chain.doFilter()</code>), <br>
   **/

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    /*
     * Currently, many browsers do not send character encoding information in the Content-Type
     * header of an HTTP request. If an encoding has not been specified by the client request, the
     * container uses a default encoding to parse request parameters. If the client hasn't set
     * character encoding and the request parameters are encoded with a different encoding than the
     * default, the parameters will be parsed incorrectly. You can use the method
     * setCharacterEncoding in ServletRequest interface to set the encoding. Since this method must
     * be called prior to parsing any post data or reading any input from the request, this function
     * is a prime application for filters(http://java.sun.com/products/servlet/Filters.pdf.) In this
     * case, it may be caused by parsing parameter "gzip". And charset should be UTF-8. You can use
     * another filter for the encoding.(see Tomcat's examples)
     *
     */
    request.setCharacterEncoding("utf-8");
    if (compressionThreshold == 0) {
      // log.trace("doFilter gets called, but compressionTreshold is set to 0 - no compression");
      chain.doFilter(request, response);
      return;
    }
    boolean supportCompression = false;
    if (request instanceof HttpServletRequest) {
      // log.trace("requestURI = " + ((HttpServletRequest)request).getRequestURI());

      // Are we allowed to compress ?
      String s = request.getParameter("gzip");
      if ("false".equals(s)) {
        // log.trace("got parameter gzip=false --> don't compress, just chain filter");
        chain.doFilter(request, response);
        return;
      }

      Enumeration e = ((HttpServletRequest) request).getHeaders("Accept-Encoding");
      while (e.hasMoreElements()) {
        String name = (String) e.nextElement();
        if (name.indexOf("gzip") != -1) {
          // log.trace("supports compression");
          supportCompression = true;
        } else {
          // log.trace("no support for compresion");
        }
      }
    }

    if (!supportCompression) {
      // log.trace("doFilter gets called wo compression");
      chain.doFilter(request, response);
      return;
    }

    if (response instanceof HttpServletResponse) {
      CompressionServletResponseWrapper wrappedResponse =
          new CompressionServletResponseWrapper((HttpServletResponse) response);
      // minhnn
      // wrappedResponse.setDebugLevel(debug);
      wrappedResponse.setCompressionThreshold(compressionThreshold);
      // log.trace("doFilter gets called with compression");
      try {
        chain.doFilter(request, wrappedResponse);
      } finally {
        wrappedResponse.finishResponse();
      }
      return;
    }
  }

  /**
   * Set filter config This function is equivalent to init. Required by Weblogic 6.1
   *
   * @param filterConfig The filter configuration object
   */
  public void setFilterConfig(FilterConfig filterConfig) {
    init(filterConfig);
  }

  /**
   * Return filter config Required by Weblogic 6.1
   */
  public FilterConfig getFilterConfig() {
    return config;
  }

}

