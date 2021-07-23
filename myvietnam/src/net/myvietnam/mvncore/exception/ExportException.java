/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/exception/ExportException.java,v 1.14 2007/01/15 10:31:06 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.14 $
 * $Date: 2007/01/15 10:31:06 $
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
 * @author: Igor Manic  
 */
package net.myvietnam.mvncore.exception;

/**
 * @author Igor Manic
 * @version $Revision: 1.14 $, $Date: 2007/01/15 10:31:06 $
 * <br/>
 * <code>ExportException</code> represents the error that could be raised during
 * the database export process. This class encapsulates detailed message that
 * describes the error in user-friendly language, and the root exception that
 * caused the export to stop.
 */
public class ExportException extends Exception {

    private Exception exception;

    public ExportException() {
        super();
        this.exception = null;
    }

    public ExportException(String message) {
        super(message);
        this.exception = null;
    }

    public ExportException(Exception e) {
        super();
        this.exception = e;
    }

    public ExportException(String message, Exception e) {
        super(message);
        this.exception = e;
    }

    public String getMessage() {
        String message = super.getMessage();

        if (exception != null) {
            return message + " Detail: " + exception.getMessage();
        }
        return message;
    }

    public Exception getException() {
        return exception;
    }

    public String toString() {
        if (exception != null) {
            return exception.toString();
        }
        return super.toString();
    }

}
