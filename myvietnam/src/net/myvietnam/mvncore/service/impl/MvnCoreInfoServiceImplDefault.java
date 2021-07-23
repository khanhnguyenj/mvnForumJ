/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/impl/MvnCoreInfoServiceImplDefault.java,v 1.61 2010/08/20 05:01:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.61 $
 * $Date: 2010/08/20 05:01:55 $
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
package net.myvietnam.mvncore.service.impl;

import java.awt.image.BufferedImage;

import net.myvietnam.mvncore.service.MvnCoreInfoService;
import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.util.ImageUtil;

public class MvnCoreInfoServiceImplDefault implements MvnCoreInfoService {

    private static int count;
    
    public MvnCoreInfoServiceImplDefault() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }   

    private static String PRODUCT_NAME         = "MyVietnam WebApp Framework - Core Lib";

    private static String PRODUCT_DESC         = "MyVietnam Web Application Framework - Core Library";

    private static String PRODUCT_VERSION      = "2.3-dev";

    private static String PRODUCT_RELEASE_DATE = "20 August 2010";

    private static String PRODUCT_HOMEPAGE     = "http://www.MyVietnam.net";


    public String getProductName() {
        return PRODUCT_NAME;
    }

    public String getProductDesc() {
        return PRODUCT_DESC;
    }

    public String getProductHomepage() {
        return PRODUCT_HOMEPAGE;
    }

    public String getProductReleaseDate() {
        return PRODUCT_RELEASE_DATE;
    }

    public String getProductVersion() {
        return PRODUCT_VERSION;
    }

    public BufferedImage getImage() {

        return ImageUtil.getProductionImage(PRODUCT_VERSION, PRODUCT_RELEASE_DATE);
    }

}
