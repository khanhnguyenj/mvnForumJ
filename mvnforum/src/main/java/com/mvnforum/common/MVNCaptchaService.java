/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/MVNCaptchaService.java,v 1.23
 * 2009/11/25 09:06:31 lexuanttkhtn Exp $ $Author: lexuanttkhtn $ $Revision: 1.23 $ $Date:
 * 2009/11/25 09:06:31 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain intact in the scripts and in the outputted
 * HTML. The "powered by" text/logo with a link back to http://www.mvnForum.com and
 * http://www.MyVietnam.net in the footer of the pages MUST remain visible when the pages are viewed
 * on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 *
 * Support can be obtained from support forums at: http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to: info at MyVietnam net
 *
 * @author: Minh Nguyen
 *
 * @author: Mai Nguyen
 */
package com.mvnforum.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mvnforum.MVNForumConfig;
import com.mvnforum.auth.AuthenticationException;
import com.mvnforum.auth.OnlineUser;
import com.mvnforum.auth.OnlineUserManager;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.EllipseBackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.MultipleShapeBackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.SimpleTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.gimpy.GimpyFactory;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.util.ParamUtil;

public final class MVNCaptchaService extends ListImageCaptchaEngine {

  private static final Integer MIN_WORD_LENGTH =
      new Integer(MVNForumConfig.getCaptchaImageMinWordLength());

  private static final Integer MAX_WORD_LENGTH =
      new Integer(MVNForumConfig.getCaptchaImageMaxWordLength());

  private static final Integer IMAGE_WIDTH = new Integer(380);

  private static final Integer IMAGE_HEIGHT = new Integer(80);

  private static final Integer MIN_FONT_SIZE = new Integer(44);

  private static final Integer MAX_FONT_SIZE = new Integer(50);

  private static final String NUMERIC_CHARS = "123456789";// No numeric 0

  private static final String UPPER_ASCII_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ";// No upper O I

  // we don't use the lower characters because it cause difficult in some case, so that
  // we must always UPPERCASE the input from user (currently in OnlineUserImpl)
  // private static final String LOWER_ASCII_CHARS= "abcdefghjklmnpqrstuvwxyz";// No lower o i

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
  private MVNCaptchaService() {}

  public static MVNCaptchaService getInstance() {
    return instance;
  }

  @Override
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
    backgroundGeneratorList
        .add(new GradientBackgroundGenerator(IMAGE_WIDTH, IMAGE_HEIGHT, Color.white, Color.black));
    backgroundGeneratorList
        .add(new GradientBackgroundGenerator(IMAGE_WIDTH, IMAGE_HEIGHT, Color.black, Color.white));
    backgroundGeneratorList.add(
        new GradientBackgroundGenerator(IMAGE_WIDTH, IMAGE_HEIGHT, Color.orange, Color.magenta));

    Font[] fontsList = new Font[] {new Font("Arial", Font.PLAIN, 10),
        new Font("Tahoma", Font.BOLD, 10), new Font("Verdana", Font.ITALIC, 10),};

    fontGeneratorList.add(new RandomFontGenerator(MIN_FONT_SIZE, MAX_FONT_SIZE, fontsList));

    // no char upper O, char lower o and numeric 0 because user cannot answer
    WordGenerator words = new RandomWordGenerator(NUMERIC_CHARS + UPPER_ASCII_CHARS);

    for (Iterator fontIter = fontGeneratorList.iterator(); fontIter.hasNext();) {
      FontGenerator font = (FontGenerator) fontIter.next();
      for (Iterator backIter = backgroundGeneratorList.iterator(); backIter.hasNext();) {
        BackgroundGenerator back = (BackgroundGenerator) backIter.next();
        for (Iterator textIter = textPasterList.iterator(); textIter.hasNext();) {
          TextPaster parser = (TextPaster) textIter.next();

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
        // TODO check captcha
        // onlineUser.buildNewCaptcha();
      }
    }

    BufferedImage image = null;

    OutputStream outputStream = null;
    try {
      response.setHeader("Cache-Control", "no-store, no-cache"); // HTTP 1.1
      // response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
      response.setHeader("Pragma", "no-cache"); // HTTP 1.0
      response.setDateHeader("Expires", 0);
      response.setContentType("image/jpeg");

      outputStream = response.getOutputStream();

      // JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
      // encoder.encode(image);
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
        } catch (IOException ex) {
        }
      }
    }
  }
}
