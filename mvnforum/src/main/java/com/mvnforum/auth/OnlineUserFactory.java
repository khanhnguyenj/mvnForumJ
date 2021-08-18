/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/OnlineUserFactory.java,v 1.15
 * 2009/01/06 02:38:35 minhnn Exp $ $Author: minhnn $ $Revision: 1.15 $ $Date: 2009/01/06 02:38:35 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain intact in the scripts and in the outputted
 * HTML. The "powered by" text/logo with a link back to http://www.mvnForum.com and
 * http://www.MyVietnam.net in the footer of the pages MUST remain visible when the pages are viewed
 * on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 *
 * Support can be obtained from support forums at: http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to: info at MyVietnam net
 *
 * @author: Minh Nguyen
 *
 * @author: Mai Nguyen
 */
package com.mvnforum.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

public interface OnlineUserFactory {

  /**
   * Authenticate the user given its login and password (maybe in encrypted form) and returns some
   * basic information about it. Optionally, some information can be stored in the request or
   * session to track that the user has been logged.
   *
   * @param request useful to retrieve additional information to authenticate the user.
   * @param response a <code>HttpServletResponse</code> value
   * @param loginName a <code>String</code> value
   * @param password a <code>String</code> value
   * @param isEncodedPassword indicate if the password to validate is already encoded. Note that
   *        some backends may not support to validate against an encrypted password.
   * @return an <code>OnlineUser</code> value
   * @exception AuthenticationException if the pair login, password is not valid. Note that this
   *            method will call the {@link #validatePassword(String, String, boolean)}
   * @exception DatabaseException if an error occurs
   * @see #validatePassword(String, String, boolean)
   */
  public OnlineUser getAuthenticatedUser(HttpServletRequest request, HttpServletResponse response,
      String loginName, String password, boolean isEncodedPassword)
      throws AuthenticationException, DatabaseException;

  public OnlineUser getAuthenticatedUser(GenericRequest request, GenericResponse response,
      String loginName, String password, boolean isEncodedPassword)
      throws AuthenticationException, DatabaseException;

  /**
   * This method is called after user have logined successfully. This method could be used to
   * implement functions prepared for that user.
   *
   * @param request a <code>HttpServletRequest</code> value
   * @param response a <code>HttpServletResponse</code> value
   * @param onlineUser a <code>OnlineUser</code> that have justed been authenticated
   */
  public void postLogin(HttpServletRequest request, HttpServletResponse response,
      OnlineUser onlineUser) throws DatabaseException;

  /**
   * <code>Logout</code> the user from the system.
   *
   * @param request a <code>HttpServletRequest</code> value
   * @param response a <code>HttpServletResponse</code> value
   */
  public void logout(HttpServletRequest request, HttpServletResponse response);

  /**
   * <code>Logout</code> the user from the system.
   *
   * @param request a <code>GenericRequest</code> value
   * @param response a <code>GenericResponse</code> value
   */
  public void logout(GenericRequest request, GenericResponse response);

  /**
   * Validate if the given login password pair is valid, in that case <code>true</code> will be
   * returned. If the password is not correct, <code>false</code> will be returned. If the user does
   * not exist or in case of any error, an exception will be thrown.
   *
   * @param loginName a <code>String</code> value
   * @param password a <code>String</code> value
   * @param isEncodedPassword indicate if the password to validate is already encoded. Note that
   *        some backends may not support to validate against an encrypted password.
   * @return a <code>boolean</code> value
   * @exception AuthenticationException if an error occurs
   */
  public boolean validatePassword(String loginName, String password, boolean isEncodedPassword)
      throws AuthenticationException;

  /**
   * Ensure the login/password pair is correct. If password is correct, nothing happen. However, if
   * the password is not correct, an AuthenticationException will be thrown
   *
   * @param loginName a <code>String</code> value
   * @param password a <code>String</code> value
   * @param isEncodedPassword indicate if the password to validate is already encoded. Note that
   *        some backends may not support to validate against an encrypted password.
   * @exception AuthenticationException if an error occurs
   */
  public void ensureCorrectPassword(String loginName, String password, boolean isEncodedPassword)
      throws AuthenticationException;

  /**
   * Get an encoded version of the user password. This can be useful to store that password in a
   * cookie, for example. Note that not all backends will return encrypted passwords as this can be
   * considered an insecure practice.
   *
   * @param loginName a <code>String</code> value
   * @param password a <code>String</code> value
   * @return the encoded password of the user or <code>null</code> if the backend does not support
   *         returning stored passwords.
   */
  public String getEncodedPassword(String loginName, String password);

  /**
   * Get the anonymous user of the system. The request is used to check some parameters about the
   * user, as its language.
   *
   * @param request a <code>HttpServletRequest</code> value
   * @return an <code>OnlineUser</code> value
   * @exception DatabaseException if an error occurs
   */
  public OnlineUser getAnonymousUser(HttpServletRequest request, HttpServletResponse response)
      throws DatabaseException;

  public OnlineUser getAnonymousUser(GenericRequest request, GenericResponse response)
      throws DatabaseException;
}
