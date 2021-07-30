/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/service/impl/MvnForumInfoServiceImplDefault.java,v 1.63 2010/08/20 04:53:46 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.63 $
 * $Date: 2010/08/20 04:53:46 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain
 * intact in the scripts and in the outputted HTML.
 * The "powered by" text/logo with a link back to
 * http://www.mvnForum.com and http://www.MyVietnam.net in
 * the footer of the pages MUST remain visible when the pages
 * are viewed on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Support can be obtained from support forums at:
 * http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Minh Nguyen
 * @author: Mai  Nguyen
 */
package com.mvnforum.service.impl;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.util.ImageUtil;

import com.mvnforum.service.MvnForumInfoService;

public class MvnForumInfoServiceImplDefault implements MvnForumInfoService {

    private static int count;

    public MvnForumInfoServiceImplDefault() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }

    private static String PRODUCT_NAME         = "mvnForum";

    private static String PRODUCT_DESC         = "mvnForum ";

    private static String PRODUCT_VERSION      = "1.3-dev";

    private static String PRODUCT_RELEASE_DATE = "20 August 2010";

    private static String PRODUCT_HOMEPAGE     = "http://www.mvnForum.com";

    public String getProductName() {
        return PRODUCT_NAME;
    }

    public String getProductDesc() {
        return PRODUCT_DESC + getVersion();
    }

    public String getProductVersion() {
        return getVersion();
    }

    public String getProductReleaseDate() {
        return PRODUCT_RELEASE_DATE;
    }

    public String getProductHomepage() {
        return PRODUCT_HOMEPAGE;
    }

    public BufferedImage getImage() {
        return ImageUtil.getProductionImage(PRODUCT_VERSION, PRODUCT_RELEASE_DATE);
    }
    
	private String getVersion() {
		String version = null;

		try {
			Properties p = new Properties();
			InputStream is = getClass().getResourceAsStream("/META-INF/maven/com.huongdanjava/mvnforum/pom.properties");
			if (is != null) {
				p.load(is);
				version = p.getProperty("version", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (version == null) {
			version = "SNAPSHOT";
		}

		return version;
	}

}
