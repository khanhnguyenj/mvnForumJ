/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/service/impl/BinaryStorageServiceImplDisk.java,v 1.28 2010/06/17 10:46:02 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.28 $
 * $Date: 2010/06/17 10:46:02 $
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
 * @author: Trong Vo
 */
package com.mvnforum.service.impl;

import java.io.*;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.service.*;
import net.myvietnam.mvncore.service.impl.BinaryStorageServiceImplBase;
import net.myvietnam.mvncore.util.*;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.common.AttachmentUtil;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.MemberBean;

public class BinaryStorageServiceImplDisk extends BinaryStorageServiceImplBase implements BinaryStorageService {

    private static final Logger log = LoggerFactory.getLogger(BinaryStorageServiceImplDisk.class);

    private static int count;

    public BinaryStorageServiceImplDisk() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }

    public BinaryStorageHandle storeData(String category, String nameId, String fileName, InputStream inputStream,
                                         int storageFileSize, int storageOption, int storageStatus, String contentType, String storageIP)
        throws IOException {

        if (inputStream == null) {
            throw new IllegalArgumentException("Cannot store an empty inputStream.");
        }

        if (category.equals(CATEGORY_POST_ATTACHMENT)) {
            int attachID = 0;
            try {
                attachID = Integer.parseInt(nameId);
            } catch (NumberFormatException e) {
                log.error("Cannot parse to an int value " + nameId, e);
                throw new IllegalArgumentException("Cannot parse to an int value " + nameId);
            }

            String filename = AttachmentUtil.getAttachFilenameOnDisk(attachID);
            FileOutputStream fos = null;
            try {
                log.debug("Attachment filename to save to file system = " + filename);

                fos = new FileOutputStream(filename);
                IOUtils.copy(inputStream, fos);// already do the buffering with block = 4096

                BinaryStorageHandle handle = new BinaryStorageHandle(BinaryStorageService.BINARY_STORAGE_TYPE_DISK, 0, filename);
                return handle;
            } catch (IOException e) {
                log.error("Disk Error", e);
                throw e;
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.debug("Cannot close input file", e);
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        log.debug("Cannot close output file", e);
                    }
                }
            }
        } else if (category.equals(CATEGORY_PM_ATTACHMENT)) {
            int attachID = 0;
            try {
                attachID = Integer.parseInt(nameId);
            } catch (NumberFormatException e) {
                log.error("Cannot parse to an int value " + nameId, e);
                throw new IllegalArgumentException("Cannot parse to an int value " + nameId);
            }

            String filename = AttachmentUtil.getPmAttachFilenameOnDisk(attachID);
            FileOutputStream fos = null;
            try {
                log.debug("PmAttachment filename to save to file system = " + filename);

                fos = new FileOutputStream(filename);
                IOUtils.copy(inputStream, fos);// already do the buffering with block = 4096

                BinaryStorageHandle handle = new BinaryStorageHandle(BinaryStorageService.BINARY_STORAGE_TYPE_DISK, 0, filename);
                return handle;
            } catch (IOException e) {
                log.error("Disk Error", e);
                throw e;
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.debug("Cannot close input file", e);
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        log.debug("Cannot close output file", e);
                    }
                }
            }
        } else if (category.equals(CATEGORY_AVATAR)) {
            int memberID = 0;
            try {
                memberID = Integer.parseInt(nameId);
            } catch (NumberFormatException e) {
                log.error("Cannot parse to an int value " + nameId, e);
                throw new IllegalArgumentException("Cannot parse to an int value " + nameId);
            }

            FileOutputStream fos = null;
            try {
                MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
                String memberName = memberBean.getMemberName();

                StringBuffer bufferPicFile = new StringBuffer(128);
                bufferPicFile.append(MVNForumConfig.getAvatarDir());
                log.debug("Upload avatar to the folder " + MVNForumConfig.getAvatarDir());
                bufferPicFile.append(File.separatorChar).append(memberName).append(".jpg");
                String thumbnailFile =  bufferPicFile.toString();
                log.debug("uploaded file = " + thumbnailFile);

                fos = new FileOutputStream(thumbnailFile);
                IOUtils.copy(inputStream, fos);// already do the buffering with block = 4096

                // NOTE: actually we should call FileUtil.deleteFile(thumbnailFile);
                // if the below method failed, however, left it here is also not serious
                DAOFactory.getMemberDAO().updateAvatar(memberID, MemberBean.MEMBER_AVATAR_USING_UPLOAD);

                BinaryStorageHandle handle = new BinaryStorageHandle(BinaryStorageService.BINARY_STORAGE_TYPE_DISK, 0, fileName);
                return handle;
            } catch (ObjectNotFoundException e) {
                log.error("ObjectNotFoundException Error", e);
                throw new FileNotFoundException(e.getMessage());
            } catch (DatabaseException e) {
                log.error("DatabaseException Error", e);
                throw new IOException(e.getMessage());
            } catch (AssertionError e) {
                log.error("AssertionError Error", e);
                throw new IOException(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.debug("Cannot close input file", e);
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        log.debug("Cannot close output file", e);
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Not support category = " + category);
        }
    }
    
    public Object getInputStreamOrInfo(String category, String nameId, BinaryStorageHandle handle, boolean option)
        throws IOException {
        
        if (category.equals(CATEGORY_POST_ATTACHMENT)) {
            int attachID = 0;
            try {
                attachID = Integer.parseInt(nameId);
            } catch (NumberFormatException e) {
                log.error("Cannot parse to an int value " + nameId, e);
                throw new IllegalArgumentException("Cannot parse to an int value " + nameId);
            }

            String attachFilename = AttachmentUtil.getAttachFilenameOnDisk(attachID);
            File attachFile = new File(attachFilename);

            if (attachFile.exists() == false) {
                throw new FileNotFoundException("Can't find the file to be downloaded (" + attachFile + ")");
            }
            if (attachFile.isFile() == false) {
                throw new IOException("The file to download is a directory.");
            }
            if (option == true) {  //get inputStream
                InputStream inputStream = new FileInputStream(attachFile);
                return inputStream;
            } else {  //get info
                return convertFileToBinaryStorageInfo(attachFile, category);
            }
        } else if (category.equals(CATEGORY_PM_ATTACHMENT)) {
            int attachID = 0;
            try {
                attachID = Integer.parseInt(nameId);
            } catch (NumberFormatException e) {
                log.error("Cannot parse to an int value " + nameId, e);
                throw new IllegalArgumentException("Cannot parse to an int value " + nameId);
            }

            String pmAttachFilename = AttachmentUtil.getPmAttachFilenameOnDisk(attachID);
            File attachFile = new File(pmAttachFilename);

            if (attachFile.exists() == false) {
                throw new FileNotFoundException("Can't find the file to be downloaded (" + attachFile + ")");
            }
            if (attachFile.isFile() == false) {
                throw new IOException("The file to download is a directory.");
            }
            if (option == true) {
                InputStream inputStream = new FileInputStream(attachFile);
                return inputStream;
            } else {
                return convertFileToBinaryStorageInfo(attachFile, category);
            }
        } else if (category.equals(CATEGORY_AVATAR)) {
            int memberID = 0;
            try {
                memberID = Integer.parseInt(nameId);
            } catch (NumberFormatException e) {
                log.error("Cannot parse to an int value " + nameId, e);
                throw new IllegalArgumentException("Cannot parse to an int value " + nameId);
            }

            try {
                MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);

                String memberAvatar = memberBean.getMemberAvatar();
                if (memberAvatar.equals(MemberBean.MEMBER_AVATAR_USING_UPLOAD) ||
                    memberAvatar.startsWith(BinaryStorageService.BINARY_STORAGE)||
                    memberAvatar.startsWith(MVNForumGlobal.UPLOADED_AVATAR_DIR)) {
                    memberAvatar = memberBean.getMemberName() + ".jpg";
                } else {
                    throw new IOException("Assertion: Bad request for memberID = " + memberID);
                }
                String avatarFileName = MVNForumConfig.getAvatarDir() + File.separator + memberAvatar;
                File avatarFile = new File(avatarFileName);
                if (avatarFile.exists() == false) {
                    throw new FileNotFoundException("Can't find the file to be downloaded (" + avatarFile + ")");
                }
                if (avatarFile.isFile() == false) {
                    throw new IOException("The file to download is a directory.");
                }
                if (option == true) {
                    InputStream inputStream = new FileInputStream(avatarFile);
                    return inputStream;
                } else { 
                    return convertFileToBinaryStorageInfo(avatarFile, category);
                }
            } catch (ObjectNotFoundException e) {
                log.error("ObjectNotFoundException Error", e);
                throw new FileNotFoundException(e.getMessage());
            } catch (DatabaseException e) {
                log.error("DatabaseException Error", e);
                throw new IOException(e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Not support category = " + category);
        }
    }

    public InputStream getInputStream(String category, String nameId, BinaryStorageHandle handle)
        throws IOException {

        boolean option = true; 
        InputStream inputStream = (InputStream)this.getInputStreamOrInfo(category, nameId, handle, option);
        return inputStream;
    }
    
    public BinaryStorageInfo getBinaryStorageInfo(String category, String nameId, BinaryStorageHandle handle)
        throws IOException  {
    
        boolean option = false; 
        BinaryStorageInfo binaryStorageInfo = (BinaryStorageInfo)this.getInputStreamOrInfo(category, nameId, handle, option);
        return binaryStorageInfo;
    }
    
    public void deleteData(String category, String nameId, BinaryStorageHandle handle)
        throws IOException {

        if (category.equals(CATEGORY_POST_ATTACHMENT)) {
            int attachID = 0;
            try {
                attachID = Integer.parseInt(nameId);
            } catch (NumberFormatException e) {
                log.error("Cannot parse to an int value " + nameId, e);
                throw new IllegalArgumentException("Cannot parse to an int value " + nameId);
            }

            String filename = AttachmentUtil.getAttachFilenameOnDisk(attachID);
            try {
                log.info("About to delete post attachment = " + filename);
                FileUtil.deleteFile(filename);
            } catch (Exception ex) {
                log.warn("Cannot delete post attachment file " + filename, ex);
                //@todo schedule to delete it later
            }
        } else if (category.equals(CATEGORY_PM_ATTACHMENT)) {
            int attachID = 0;
            try {
                attachID = Integer.parseInt(nameId);
            } catch (NumberFormatException e) {
                log.error("Cannot parse to an int value " + nameId, e);
                throw new IllegalArgumentException("Cannot parse to an int value " + nameId);
            }

            String filename = AttachmentUtil.getPmAttachFilenameOnDisk(attachID);
            try {
                log.info("About to delete pmAttachment = " + filename);
                FileUtil.deleteFile(filename);
            } catch (Exception ex) {
                log.warn("Cannot delete pmAttachment file " + filename, ex);
                //@todo schedule to delete it later
            }
        } else if (category.equals(CATEGORY_AVATAR)) {
            int memberID = 0;
            try {
                memberID = Integer.parseInt(nameId);
            } catch (NumberFormatException e) {
                log.error("Cannot parse to an int value " + nameId, e);
                throw new IllegalArgumentException("Cannot parse to an int value " + nameId);
            }

            try {
                MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
                String memberName = memberBean.getMemberName();

                StringBuffer bufferPicFile = new StringBuffer(128);
                bufferPicFile.append(MVNForumConfig.getAvatarDir());
                bufferPicFile.append(File.separatorChar).append(memberName).append(".jpg");
                String picFile =  bufferPicFile.toString();

                log.debug("Delete avatar = " + picFile);
                File file = new File(picFile);
                file.delete();// we don't need to check the returned value
            } catch (Exception ex) {
                log.warn("Cannot delete avatar file ", ex);
                throw new IOException(ex.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Not support category = " + category);
        }
    }

}
