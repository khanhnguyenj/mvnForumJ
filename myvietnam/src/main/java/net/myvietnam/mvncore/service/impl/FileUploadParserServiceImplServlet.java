/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/impl/FileUploadParserServiceImplServlet.java,v 1.4 2009/01/12 09:10:25 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.4 $
 * $Date: 2009/01/12 09:10:25 $
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
 * @author: Phong Ta
 */
package net.myvietnam.mvncore.service.impl;

import java.io.File;
import java.util.List;

import net.myvietnam.mvncore.service.FileUploadParserService;
import net.myvietnam.mvncore.web.GenericRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploadParserServiceImplServlet implements FileUploadParserService {

    public List parseRequest(GenericRequest request, int sizeMax, int sizeThreshold, String repository, String headerEncoding)
        throws FileUploadException {

        DiskFileItemFactory factory = new DiskFileItemFactory();

        if (repository != null) {
            factory.setRepository(new File(repository));
        }

        factory.setSizeThreshold(sizeThreshold);

        List fileItems = null;

        if (request.isServletRequest()) {
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(sizeMax);
            if (headerEncoding != null) {
                upload.setHeaderEncoding(headerEncoding);
            }
            fileItems = upload.parseRequest(request.getServletRequest());
        } else {
            throw new IllegalStateException("Not implemeneted for portlet.");
        }
        return fileItems;
    }
}
