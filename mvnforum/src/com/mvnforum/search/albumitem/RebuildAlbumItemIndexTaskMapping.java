/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/search/albumitem/RebuildAlbumItemIndexTaskMapping.java,v 1.2 2009/01/03 18:32:34 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.2 $
 * $Date: 2009/01/03 18:32:34 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * MyVietnam.net PROPRIETARY/CONFIDENTIAL PROPERTIES. Use is subject to license terms.
 * You CANNOT use this software unless you receive a written permission from MyVietnam.net
 *
 * @author: MyVietnam.net developers
 */
package com.mvnforum.search.albumitem;

/**
 * Rebuilding indices task. This task do indexing of all documents
 */
public class RebuildAlbumItemIndexTaskMapping {

    private static boolean isRebuilding = false;
    
    private RebuildAlbumItemIndexTaskMapping() {
        // to prevent creating an instance
    }

    public static boolean isRebuilding() {
        return isRebuilding;
    }

    public static void setRebuilding(boolean rebuilding) {
        isRebuilding = rebuilding;
    }

}
