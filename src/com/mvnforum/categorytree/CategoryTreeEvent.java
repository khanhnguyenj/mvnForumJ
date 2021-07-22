/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/categorytree/CategoryTreeEvent.java,v 1.9 2008/06/05 04:43:20 phuongpdd Exp $
 * $Author: phuongpdd $
 * $Revision: 1.9 $
 * $Date: 2008/06/05 04:43:20 $
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
 * @author: Phuong Pham Dinh Duy
 */
package com.mvnforum.categorytree;

import java.util.EventObject;

public class CategoryTreeEvent extends EventObject {

    private static final long serialVersionUID = -4562260586243337741L;
    private static final String DEFAULT_SOURCE = "";
    
    private String idsPath;

    private int level;

    private boolean hasForum;

    private boolean hasSubCategory;

    private int depth;

    public CategoryTreeEvent(Object source) {
        super(source);
    }

    public CategoryTreeEvent() {
        super(DEFAULT_SOURCE);
    }

    public int getLevel() {
        return this.level;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public String getIdsPath() {
        return idsPath;
    }
    public void setIdsPath(String idsPath) {
        this.idsPath = idsPath;
    }

    public boolean hasForum() {
        return hasForum;
    }
    public void setHasForum(boolean hasForum) {
        this.hasForum = hasForum;
    }

    public boolean hasSubCategory() {
        return hasSubCategory;
    }
    public void setHasSubCategory(boolean hasSubCategory) {
        this.hasSubCategory = hasSubCategory;
    }

    public int getDepth() {
        return this.depth;
    }
    public void setDepth(int depth) {
        this.depth = depth;
    }
}
