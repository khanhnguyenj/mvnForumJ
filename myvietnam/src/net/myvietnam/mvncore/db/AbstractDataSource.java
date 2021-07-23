/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/db/AbstractDataSource.java,v 1.5 2009/01/01 18:30:12 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.5 $
 * $Date: 2009/01/01 18:30:12 $
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
 */
/*
 * Copyright 2004-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.myvietnam.mvncore.db;

import java.io.PrintWriter;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang.NotImplementedException;

/**
 * Abstract class for basic implementation of interface DataSource
 */
public abstract class AbstractDataSource implements DataSource {

    /**
     * Returns 0: means use default system timeout.
     */
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public void setLoginTimeout(int timeout) throws SQLException {
        throw new NotImplementedException("setLoginTimeout");
    }

    /**
     * LogWriter methods are unsupported.
     */
    public PrintWriter getLogWriter() {
        throw new NotImplementedException("getLogWriter");
    }

    /**
     * LogWriter methods are unsupported.
     */
    public void setLogWriter(PrintWriter pw) throws SQLException {
        throw new NotImplementedException("setLogWriter");
    }

    public boolean isWrapperFor(Class iface) throws SQLException {
        throw new NotImplementedException("isWrapperFor");    
    }

    public Object unwrap(Class iface) throws SQLException {
        throw new NotImplementedException("unwrap");    
    }

}
