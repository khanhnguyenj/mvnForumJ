/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/MVNCaptchaService.java,v 1.23 2009/11/25 09:06:31 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.23 $
 * $Date: 2009/11/25 09:06:31 $
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
package com.mvnforum.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.util.ParamUtil;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.auth.*;
import com.octo.captcha.component.image.backgroundgenerator.*;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.*;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.gimpy.GimpyFactory;

public final class MVNCaptchaService extends ListImageCaptchaEngine {

    private static final Integer MIN_WORD_LENGTH = new Integer(MVNForumConfig.getCaptchaImageMinWordLength());

    private static final Integer MAX_WORD_LENGTH = new Integer(MVNForumConfig.getCaptchaImageMaxWordLength());

    private static final Integer IMAGE_WIDTH     = new Integer(380);

    private static final Integer IMAGE_HEIGHT    = new Integer(80);

    private static final Integer MIN_FONT_SIZE   = new Integer(44);

    private static final Integer MAX_FONT_SIZE   = new Integer(50);

    private static final String NUMERIC_CHARS    = "123456789";// No numeric 0

    private static final String UPPER_ASCII_CHARS= "ABCDEFGHJKLMNPQRSTUVWXYZ";// No upper O I

    // we don't use the lower characters because it cause difficult in some case, so that
    // we must always UPPERCASE the input from user (currently in OnlineUserImpl)
    //private static final String LOWER_ASCII_CHARS= "abcdefghjklmnpqrstuvwxyz";// No lower o i

    /**
     * Singleton instance of this class
     */
    private static MVNCaptchaService instance = new MVNCaptchaService();

    private List textPasterList;

    private List backgroundGeneratorList;

    private List fontGeneratorList;

    /**
     * Private constructor to prevent instantiation
     */
    private MVNCaptchaService() {
    }

    public static MVNCaptchaService getInstance() {
        return instance;
    }

    protected void buildInitialFactories() {
        textPasterList = new ArrayList();
        backgroundGeneratorList = new ArrayList();
        fontGeneratorList = new ArrayList();

        textPasterList.add(new SimpleTextPaster(MIN_WORD_LENGTH, MAX_WORD_LENGTH, Color.green));
        textPasterList.add(new RandomTextPaster(MIN_WORD_LENGTH, MAX_WORD_LENGTH, Color.green));
        textPasterList.add(new SimpleTextPaster(MIN_WORD_LENGTH, MAX_WORD_LENGTH, Color.red));
        textPasterList.add(new RandomTextPaster(MIN_WORD_LENGTH, MAX_WORD_LENGTH, Color.red));
        textPasterList.add(new SimpleTextPaster(MIN_WORD_LENGTH, MAX_WORD_LENGTH, Color.blue));
        textPasterList.add(new RandomTextPaster(MIN_WORD_LENGTH, MAX_WORD_LENGTH, Color.blue));

        backgroundGeneratorList.add(new EllipseBackgroundGenerator(IMAGE_WIDTH, IMAGE_HEIGHT));
        backgroundGeneratorList.add(new UniColorBackgroundGenerator(IMAGE_WIDTH, IMAGE_HEIGHT));
        backgroundGeneratorList.add(new MultipleShapeBackgroundGenerator(IMAGE_WIDTH, IMAGE_HEIGHT));
        backgroundGeneratorList.add(new FunkyBackgroundGenerator(IMAGE_WIDTH, IMAGE_HEIGHT));
        backgroundGeneratorList.add(new GradientBackgroundGenerator(IMAGE_WIDTH, IMAGE_HEIGHT, Color.white, Color.black));
        backgroundGeneratorList.add(new GradientBackgroundGenerator(IMAGE_WIDTH, IMAGE_HEIGHT, Color.black, Color.white));
        backgroundGeneratorList.add(new GradientBackgroundGenerator(IMAGE_WIDTH, IMAGE_HEIGHT, Color.orange, Color.magenta));
        /*
        try {
            // minhnn: In Resin, it loads as 'C:\soft\resin-3.0.8\file:\C:\soft\resin-3.0.8\webapps\mvnforum\WEB-INF\lib\jcaptcha-engine-1.0-RC1.jar!\gimpybackgrounds'
            // so it cannot load this directory, I guess this is Resin bugs
            //backgroundGeneratorList.add(new FileReaderRandomBackgroundGenerator(IMAGE_WIDTH, IMAGE_HEIGHT, "/gimpybackgrounds/"));
        } catch (CaptchaException ex) {
            // should use common-logging
            // Cannot call this because of NPE (don't know why): ex.printStackTrace();
        }*/

        Font[] fontsList = new Font[] {
                new Font("Arial", Font.PLAIN, 10),
                new Font("Tahoma", Font.BOLD, 10),
                new Font("Verdana", Font.ITALIC, 10),
             };

        fontGeneratorList.add(new RandomFontGenerator(MIN_FONT_SIZE, MAX_FONT_SIZE, fontsList));
        //fontGeneratorList.add(new TwistedAndShearedRandomFontGenerator(MIN_FONT_SIZE, MAX_FONT_SIZE));// use too many fonts in the Ubuntu
        //fontGeneratorList.add(new TwistedRandomFontGenerator(MIN_FONT_SIZE, MAX_FONT_SIZE));// link character too much
        //fontGeneratorList.add(new RandomFontGenerator(MIN_FONT_SIZE, MAX_FONT_SIZE));// to easy to read
        //fontGeneratorList.add(new DeformedRandomFontGenerator(MIN_FONT_SIZE, MAX_FONT_SIZE));// to small font

        // no char upper O, char lower o and numeric 0 because user cannot answer
        WordGenerator words = new RandomWordGenerator(NUMERIC_CHARS + UPPER_ASCII_CHARS);

        for (Iterator fontIter = fontGeneratorList.iterator(); fontIter.hasNext(); ) {
            FontGenerator font = (FontGenerator)fontIter.next();
            for (Iterator backIter = backgroundGeneratorList.iterator(); backIter.hasNext(); ) {
                BackgroundGenerator back = (BackgroundGenerator)backIter.next();
                for (Iterator textIter = textPasterList.iterator(); textIter.hasNext(); ) {
                    TextPaster parser = (TextPaster)textIter.next();

                    WordToImage word2image = new ComposedWordToImage(font, back, parser);
                    ImageCaptchaFactory factory = new GimpyFactory(words, word2image);
                    addFactory(factory);
                }
            }
        }
    }

    /**
     * Write the captcha image of current user to the servlet response
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException
     */
    public void writeCaptchaImage(HttpServletRequest request, HttpServletResponse response)
        throws IOException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        
        String reload = ParamUtil.getParameter(request, "reload");
        if ("true".equals(reload)) {
            if (MVNForumConfig.getEnableCaptcha()) {
                onlineUser.buildNewCaptcha();
            }
        }
        
        BufferedImage image = onlineUser.getCurrentCaptchaImage();
        if (image == null) {
            return;
        }

        OutputStream outputStream = null;
        try {
            response.setHeader("Cache-Control", "no-store, no-cache"); // HTTP 1.1
            //response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
            response.setHeader("Pragma", "no-cache"); // HTTP 1.0
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");

            outputStream = response.getOutputStream();

            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
            //encoder.encode(image);
            ImageIO.write(image, "jpeg", outputStream);

            outputStream.flush();
            outputStream.close();
            outputStream = null;// no close twice
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) { }
            }
        }
    }

    /*
    public void writeTestCaptchaImage(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        ImageCaptcha imageCaptcha = getNextImageCaptcha();
        BufferedImage image = (BufferedImage)imageCaptcha.getChallenge();

        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
            encoder.encode(image);

            outputStream.flush();
            outputStream.close();
            outputStream = null;// no close twice
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) { }
            }
            imageCaptcha.disposeChallenge();
        }
    }

    private void testCaptchaImage(String folder) {
        if (folder == null) {
            folder = "c:\\";
        }
        if (folder.endsWith("\\") == false) {
            folder = folder + "\\";
        }

        WordGenerator words = new RandomWordGenerator("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");

        for (Iterator fontIter = fontGeneratorList.iterator(); fontIter.hasNext(); ) {
            FontGenerator font = (FontGenerator)fontIter.next();
            for (Iterator backIter = backgroundGeneratorList.iterator(); backIter.hasNext(); ) {
                BackgroundGenerator back = (BackgroundGenerator)backIter.next();
                for (Iterator textIter = textPasterList.iterator(); textIter.hasNext(); ) {
                    TextPaster parser = (TextPaster)textIter.next();

                    WordToImage word2image = new ComposedWordToImage(font, back, parser);
                    ImageCaptchaFactory factory = new GimpyFactory(words, word2image);

                    ImageCaptcha imageCaptcha = factory.getImageCaptcha();
                    String filename = "TestCaptcha_" + getClassName(font) + "_" + getClassName(back) + "_" + getClassName(parser) + ".jpg";
                    try {
                        ImageUtil.writeJpegImage_Sun((BufferedImage) imageCaptcha.getChallenge(), folder + filename);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        return;
                    }
                }
            }
        }
    }

    private String getClassName(Object obj) {
        String fullName = obj.getClass().getName();
        int dotIndex = fullName.lastIndexOf('.');
        if (dotIndex == -1) {
            return fullName;
        }
        return fullName.substring(dotIndex + 1);
    }

    public static void main(String[] args) throws IOException {
        MVNCaptchaService.getInstance().testCaptchaImage("c:\\temp\\testcaptcha");
    }
    */
}
