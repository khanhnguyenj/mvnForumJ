/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/MvnCoreService.java,v 1.19 2009/05/14 02:57:52 nguyendnc Exp $
 * $Author: nguyendnc $
 * $Revision: 1.19 $
 * $Date: 2009/05/14 02:57:52 $
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
 * @author: Trong Vo
 */
package net.myvietnam.mvncore.service;

public interface MvnCoreService {
    
    public EncoderService getEncoderService();
    
    public URLResolverService getURLResolverService();

    public EnvironmentService getEnvironmentService();
    
    public MvnCoreInfoService getMvnCoreInfoService();
    
    public BinaryStorageService getBinaryStorageService();
    
    public MvnCoreLifeCycleService getMvnCoreLifeCycleService();
    
    public FileUploadParserService getFileUploadParserService();
    
    public EventLogService getEventLogService();
    
    public IPFilterService getIPFilterService();
    
    public SpellCheckerService getSpellCheckerService();
}
