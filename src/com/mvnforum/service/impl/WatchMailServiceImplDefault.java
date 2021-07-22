/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/service/impl/WatchMailServiceImplDefault.java,v 1.10 2007/10/09 11:09:21 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.10 $
 * $Date: 2007/10/09 11:09:21 $
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
 * @author: Anh, Dong Thi Lan
 */
package com.mvnforum.service.impl;

import java.io.IOException;

import net.myvietnam.mvncore.util.AssertionUtil;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.db.ThreadBean;
import com.mvnforum.service.WatchMailService;

import freemarker.template.*;

public class WatchMailServiceImplDefault implements WatchMailService {

    private Configuration cfg;
    private static int count;

    public WatchMailServiceImplDefault () {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");

        cfg = MVNForumConfig.getFreeMarkerConfiguration();
    }

    public Template getDigestSubjectTemplate() throws IOException {
        return cfg.getTemplate(MVNForumGlobal.TEMPLATE_WATCHMAIL_DIGEST_SUBJECT, "UTF-8");
    }

    public Template getDigestBodyTemplate() throws IOException {
        return cfg.getTemplate(MVNForumGlobal.TEMPLATE_WATCHMAIL_DIGEST_BODY, "UTF-8");
    }

    public Template getSingleSubjectTemplate() throws IOException {
        return cfg.getTemplate(MVNForumGlobal.TEMPLATE_WATCHMAIL_SINGLE_SUBJECT, "UTF-8");
    }

    public Template getSingleBodyTemplate() throws IOException {
        return cfg.getTemplate(MVNForumGlobal.TEMPLATE_WATCHMAIL_SINGLE_BODY, "UTF-8");
    }

    public void loadCustomizedVariablesForBeanWatchMail(ThreadBean threadBean, String memberName, SimpleHash beanWatchMail) {
    }

}
