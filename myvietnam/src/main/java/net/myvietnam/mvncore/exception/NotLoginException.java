/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/exception/NotLoginException.java,v 1.14 2007/01/15 10:31:09 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.14 $
 * $Date: 2007/01/15 10:31:09 $
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
package net.myvietnam.mvncore.exception;

import java.util.Locale;

import net.myvietnam.mvncore.MVNCoreResourceBundle;

public class NotLoginException extends Exception {

    public static final int NOT_LOGIN           = 0;
    public static final int ILLEGAL_STATE       = 1;
    public static final int WRONG_NAME          = 2;
    public static final int WRONG_PASSWORD      = 3;
    public static final int ACCOUNT_DISABLED    = 4;
    public static final int NOT_ENOUGH_RIGHTS   = 5;
    public static final int NOT_ACTIVATED       = 6;
    public static final int COOKIE_NOT_ALLOWED  = 7;
    public static final int LOGIN_DISABLED      = 8;

    private int exceptionReason = NOT_LOGIN;

    public NotLoginException(String msg) {
        super(msg);
    }

    public NotLoginException(int reason) {
        exceptionReason = reason;
    }

    public NotLoginException(String msg, int reason) {
        super(msg);
        exceptionReason = reason;
    }

    public int getReason() {
        return exceptionReason;
    }

    public String getReasonString() {
        if (exceptionReason == ILLEGAL_STATE) {
            return "reason: ILLEGAL_STATE";
        } else if (exceptionReason == WRONG_NAME) {
            return "reason: WRONG_NAME";
        } else if (exceptionReason == WRONG_PASSWORD) {
            return "reason: WRONG_PASSWORD";
        } else if (exceptionReason == ACCOUNT_DISABLED) {
            return "reason: ACCOUNT_DISABLED";
        } else if (exceptionReason == NOT_ENOUGH_RIGHTS) {
            return "reason: NOT_ENOUGH_RIGHTS";
        } else if (exceptionReason == NOT_ACTIVATED) {
            return "reason: NOT_ACTIVATED";
        } else if (exceptionReason == COOKIE_NOT_ALLOWED) {
            return "reason: COOKIE_NOT_ALLOWED";
        } else if (exceptionReason == LOGIN_DISABLED) {
            return "reason: LOGIN_DISABLED";
        }
        return "reason: NOTLOGIN";
    }

    public String getReasonExplanation() {
        Locale locale = Locale.getDefault();
        return getReasonExplanation(locale);
    }

    public String getReasonExplanation(Locale locale) {
        if (exceptionReason == ILLEGAL_STATE) {
            return MVNCoreResourceBundle.getString(locale, "mvncore.exception.NotLoginException.illegal_state");
        } else if (exceptionReason == WRONG_NAME) {
            return MVNCoreResourceBundle.getString(locale, "mvncore.exception.NotLoginException.wrong_name");
        } else if (exceptionReason == WRONG_PASSWORD) {
            return MVNCoreResourceBundle.getString(locale, "mvncore.exception.NotLoginException.wrong_password");
        } else if (exceptionReason == ACCOUNT_DISABLED) {
            return MVNCoreResourceBundle.getString(locale, "mvncore.exception.NotLoginException.account_disabled");
        } else if (exceptionReason == NOT_ENOUGH_RIGHTS) {
            return MVNCoreResourceBundle.getString(locale, "mvncore.exception.NotLoginException.not_enough_rights");
        } else if (exceptionReason == NOT_ACTIVATED) {
            return MVNCoreResourceBundle.getString(locale, "mvncore.exception.NotLoginException.not_activated");
        } else if (exceptionReason == COOKIE_NOT_ALLOWED) {
            return MVNCoreResourceBundle.getString(locale, "mvncore.exception.NotLoginException.cookie_not_allowed");
        } else if (exceptionReason == LOGIN_DISABLED) {
            return MVNCoreResourceBundle.getString(locale, "mvncore.exception.NotLoginException.login_disabled");
        }
        return MVNCoreResourceBundle.getString(locale, "mvncore.exception.NotLoginException.default");
    }

    public String getMessage() {
        if (super.getMessage() == null) {
            return getReasonExplanation();
        }
        return super.getMessage();
    }
}
