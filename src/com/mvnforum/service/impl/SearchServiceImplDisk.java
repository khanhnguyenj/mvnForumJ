/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/service/impl/SearchServiceImplDisk.java,v 1.23 2009/12/11 08:19:08 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.23 $
 * $Date: 2009/12/11 08:19:08 $
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
 * @author: Phong Ta Quoc
 */
package com.mvnforum.service.impl;

import java.io.File;
import java.io.IOException;

import net.myvietnam.mvncore.util.AssertionUtil;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.service.SearchService;

public class SearchServiceImplDisk implements SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchServiceImplDisk.class);

    private static int count;

    public SearchServiceImplDisk() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
        load();
    }

    public void load() {
        log.info("Using SearchService " + this.getClass().getName());
    }

    public void clearData() {
        //don't need to implement for disk
    }

    public boolean isFailed() {
        return false;
    }

    public void deleteContent(Directory directory) throws IOException {
        //don't need to implement for disk
    }

    public Directory getSearchPostIndexDir() throws IOException {
        return FSDirectory.open(new File(MVNForumConfig.getSearchPostIndexDirName()));
    }

    public Directory getSearchMemberIndexDir() throws IOException {
        return FSDirectory.open(new File(MVNForumConfig.getSearchMemberIndexDirName()));
    }

    public Directory getSearchAttachmentIndexDir() throws IOException {
        return FSDirectory.open(new File(MVNForumConfig.getSearchAttachmentIndexDirName()));
    }

    public Directory getSearchAlbumItemIndexDir() throws IOException {
        return FSDirectory.open(new File(MVNForumConfig.getSearchAlbumItemIndexDirName()));
    }

    public boolean savePostOnDisk() {
        return true;
    }

    public boolean saveMemberOnDisk() {
        return true;
    }

    public boolean saveAttachmentOnDisk() {
        return true;
    }

    public boolean saveAlbumItemOnDisk() {
        return true;
    }

    public int getPostSearchImplType() {
        return MVNForumGlobal.SEARCH_INDEX_TYPE_DISK;
    }

    public int getMemberSearchImplType() {
        return MVNForumGlobal.SEARCH_INDEX_TYPE_DISK;
    }

    public int getAttachmentSearchImplType() {
        return MVNForumGlobal.SEARCH_INDEX_TYPE_DISK;
    }

    public int getAlbumItemSearchImplType() {
        return MVNForumGlobal.SEARCH_INDEX_TYPE_DISK;
    }

}
