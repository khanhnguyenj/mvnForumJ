/*
 * $Header:
 * /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/web/impl/GenericRequestServletImpl.java,v
 * 1.14 2010/06/15 11:27:41 minhnn Exp $ $Author: minhnn $ $Revision: 1.14 $ $Date: 2010/06/15
 * 11:27:41 $
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
 * @author: Phong Ta Quoc
 */
package net.myvietnam.mvncore.web.impl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import net.myvietnam.mvncore.web.GenericRequest;

public class GenericRequestServletImpl extends HttpServletRequestWrapper implements GenericRequest {

  private ServletContext context;

  public GenericRequestServletImpl(HttpServletRequest request) {
    super(request);
    this.context = request.getSession().getServletContext();
  }

  public GenericRequestServletImpl(HttpServletRequest request, ServletContext context) {
    super(request);

    if (context == null) {
      throw new IllegalArgumentException("variable context must not be null.");
    }
    this.context = context;
  }

  @Override
  public HttpServletRequest getServletRequest() {
    return (HttpServletRequest) getRequest();
  }

  @Override
  public Object getPortletRequest() {
    return null;
  }

  @Override
  public Object getContext() {
    return context;
  }

  @Override
  public boolean isServletRequest() {
    return true;
  }

  @Override
  public boolean isPortletRequest() {
    return false;
  }

  @Override
  public String getSessionId() {
    return this.getSession().getId();
  }

  @Override
  public String getRealPath(String path) {
    if (context == null) {
      throw new IllegalStateException("Cannot getRealPath with a null context.");
    }

    return context.getRealPath(path);
  }

  @Override
  public void setSessionAttribute(String name, Object value) {
    this.getSession().setAttribute(name, value);
  }

  @Override
  public Object getSessionAttribute(String name) {
    return this.getSession().getAttribute(name);
  }

  @Override
  public void setSessionAttribute(String name, Object value, int scope) {
    this.getSession().setAttribute(name, value);
  }

  @Override
  public Object getSessionAttribute(String name, int scope) {
    return this.getSession().getAttribute(name);
  }

  @Override
  public String getMethod() {
    return this.getServletRequest().getMethod();
  }

  @Override
  public String getUserAgent() {
    return getHeader("User-Agent");
  }

  @Override
  public String getReferer() {
    return getHeader("Referer");
  }

  @Override
  public String getPortalInfo() {
    return null;
  }

}
