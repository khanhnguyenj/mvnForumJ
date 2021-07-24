/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/util/ImageUtil.java,v 1.41 2010/06/17 04:51:57 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.41 $
 * $Date: 2010/06/17 04:51:57 $
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
package net.myvietnam.mvncore.util;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import net.myvietnam.mvncore.MVNCoreResourceBundle;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.thirdparty.JpegEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ImageUtil {

    private static final Logger log = LoggerFactory.getLogger(ImageUtil.class);

    private ImageUtil() {// prevent instantiation
    }

    /**
     * @todo: xem lai ham nay, neu kich thuoc nho hon max thi ta luu truc tiep
     *          inputStream xuong thumbnailFile luon
     *
     * This method create a thumbnail and reserve the ratio of the output image
     * NOTE: This method closes the inputStream after it have done its work.
     *
     * @param inputStream     the stream of a jpeg file
     * @param thumbnailFile   the output file, must have the ".jpg" extension
     * @param maxWidth        the maximum width of the image
     * @param maxHeight       the maximum height of the image
     * @param locale TODO
     * @throws IOException
     * @throws BadInputException
     */
    public static void createThumbnail(InputStream inputStream, String thumbnailFile, int maxWidth, int maxHeight, Locale locale)
        throws IOException {
        
        //boolean useSun = false;
        if (thumbnailFile.toLowerCase().endsWith(".jpg") == false) {
            throw new IllegalArgumentException("Cannot create a thumbnail with the extension other than '.jpg'.");
        }
        
        OutputStream outputStream = new FileOutputStream(thumbnailFile);
        createThumbnail(inputStream, outputStream, maxWidth, maxHeight, locale);
    }
    
    /**
     * TODO: could we optimize performance in case  if the size is smaller than max size, 
     * but in format gif? (this method expected to return jpeg)
     *
     * This method create a thumbnail in format jpeg and reserve the ratio of the output image
     * NOTE: This method closes the inputStream after it have done its work.
     *
     * @param inputStream     the stream of a image file, format support is based on ImageIO library
     * @param outputStream    the output stream of a resulted jpeg file
     * @param maxWidth        the maximum width of the image
     * @param maxHeight       the maximum height of the image
     * @param locale TODO
     * @throws IOException
     * @throws IllegalArgumentException if input parameters are invalid
     */
    public static void createThumbnail(InputStream inputStream, OutputStream outputStream, int maxWidth, int maxHeight, Locale locale)
        throws IOException {
        
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream must not be null.");
        }
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream must not be null.");
        }

        //boolean useSun = false;
        if (maxWidth <= 0) {
            throw new IllegalArgumentException("maxWidth must >= 0");
        }
        if (maxHeight <= 0) {
            throw new IllegalArgumentException("maxHeight must >= 0");
        }

        try {
            //JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(inputStream);
            //BufferedImage srcImage = decoder.decodeAsBufferedImage();
            byte[] srcByte = FileUtil.getBytes(inputStream);
            InputStream is = new ByteArrayInputStream(srcByte);
            //ImageIcon imageIcon = new ImageIcon(srcByte);
            //Image srcImage = imageIcon.getImage();
            
            BufferedImage srcImage = ImageIO.read(is);
            if (srcImage == null) {
                String localizedMessage = MVNCoreResourceBundle.getString(locale, "java.io.IOException.cannot_upload_image");
                throw new IOException(localizedMessage);
            }

            int imgWidth  = srcImage.getWidth();
            int imgHeight = srcImage.getHeight();
//          imgWidth or imgHeight could be -1, which is considered as an assertion
            AssertionUtil.doAssert((imgWidth > 0) && (imgHeight > 0), "Assertion: ImageUtil: cannot get the image size.");
            // Set the scale.
            AffineTransform tx = new AffineTransform();
            if ((imgWidth > maxWidth) || (imgHeight > maxHeight)) {
                double scaleX = (double)maxWidth/imgWidth;
                double scaleY = (double)maxHeight/imgHeight;
                double scaleRatio = (scaleX < scaleY) ? scaleX : scaleY;
                imgWidth  = (int)(imgWidth  * scaleRatio); imgWidth = (imgWidth == 0) ? 1 : imgWidth;
                imgHeight = (int)(imgHeight * scaleRatio); imgHeight = (imgHeight == 0) ? 1 : imgHeight;
                // scale as needed
                tx.scale(scaleRatio, scaleRatio);
            } else {// we don't need any transform here, just save it to file and return
                outputStream.write(srcByte);
                return;
            }

            // create thumbnail image
            BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

            Graphics2D g = bufferedImage.createGraphics();
            boolean useTransform = false;
            if (useTransform) {// use transfrom to draw
                //log.trace("use transform");
                g.drawImage(srcImage, tx, null);
            } else {// use java filter
                //log.trace("use filter");
                Image scaleImage = getScaledInstance(srcImage, imgWidth, imgHeight);
                g.drawImage(scaleImage, 0, 0, null);
            }
            g.dispose();// free resource

            // write it to outputStream 
            // JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
            // encoder.encode(bufferedImage);
            
            ImageIO.write(bufferedImage, "jpeg", outputStream);
        } catch (IOException e) {
            log.error("Error", e);
            throw e;
        } finally {// this finally is very important
            try {
                inputStream.close();
            } catch (IOException e) {
                /* ignore */
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {/* ignore */
                e.printStackTrace();
            }
        }
    }

    /**
     * This method returns a fit-sized image for a source image,
     * this method retains the ratio of the source image
     */
    public static Image getFitSizeImage(Image srcImage, int fitWidth, int fitHeight) {
        if ((fitWidth < 100) || (fitHeight < 100)) {
            throw new IllegalArgumentException("Cannot accept values < 100");
        }

        int srcWidth  = srcImage.getWidth(null);//xem lai cho nay vi neu dung BufferedImage thi khong can null
        int srcHeight = srcImage.getHeight(null);
        //log.trace("src w = " + srcWidth + " h = " + srcHeight);

        // don't need any transforms
        if ((srcWidth == fitWidth) && (srcHeight == fitHeight)) {
            return srcImage;
        }

        int newWidth  = srcWidth;
        int newHeight = srcHeight;

        double fitRatio = (double)fitWidth / fitHeight;
        double srcRatio = (double)srcWidth / srcHeight;
        if (srcRatio > fitRatio) {// must cut the width of the source image
            newWidth = (int)(srcHeight * fitRatio);
        } else {// must cut the height of the source image
            newHeight = (int)(srcWidth / fitRatio);
        }
        //log.trace("new w = " + newWidth + " h = " + newHeight);

        ImageFilter cropFilter = new CropImageFilter((srcWidth-newWidth)/2, (srcHeight-newHeight)/2, newWidth, newHeight);
        ImageProducer cropProd = new FilteredImageSource(srcImage.getSource(), cropFilter);
        Image cropImage = Toolkit.getDefaultToolkit().createImage(cropProd);

        Image retImage = new ImageIcon(getScaledInstance(cropImage, fitWidth, fitHeight)).getImage();

        return retImage;
    }

    /**
     * This method returns a fit-sized image for a source image,
     * this method retains the ratio of the source image
     * @param locale TODO
     */
    public static Image getFitSizeImage(InputStream inputStream, int fitWidth, int fitHeight, Locale locale)
        throws IOException {
        try {
            //JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(inputStream);
            //BufferedImage srcImage = decoder.decodeAsBufferedImage();
            BufferedImage srcImage = ImageIO.read(inputStream);
            if (srcImage == null) {
                String localizedMessage = MVNCoreResourceBundle.getString(locale, "java.io.IOException.cannot_upload_image");
                throw new IOException(localizedMessage);
            }

            return getFitSizeImage(srcImage, fitWidth, fitHeight);
        } catch (IOException e) {
            log.error("Cannot run getFitSizeImage", e);
            throw e;
        } finally {// this finally is very important
            inputStream.close();
        }
    }

    /**
     * This method write the image to a stream.
     * It auto detect the image is Image or BufferedImage.
     * This method close the output stream before it return.
     *
     * @param image Image
     * @param outputStream OutputStream
     * @throws IOException
     */
    public static void writeJpegImage_Sun(Image image, OutputStream outputStream) throws IOException {

        if (outputStream == null) {
            throw new IllegalArgumentException("Does not accept null outputStream");
        }

        try {
            BufferedImage bufferedImage = null;
            if (image instanceof BufferedImage) {
                bufferedImage = (BufferedImage)image;
            } else {
                // 30% cpu resource
                bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                                                  BufferedImage.TYPE_INT_RGB);

                // 7.5 cpu
                Graphics2D g = bufferedImage.createGraphics();

                // 50% cpu
                g.drawImage(image, 0, 0, null);
                g.dispose(); // free resource
            }

            // write it to disk
            // 12% cpu
            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
            //encoder.encode(bufferedImage);
            ImageIO.write(bufferedImage, "jpeg", outputStream);
        } finally {
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void writeJpegImage_Sun(Image image, String fileName) throws IOException {
        OutputStream outputStream = new FileOutputStream(fileName);
        writeJpegImage_Sun(image, outputStream);
    }

    /**
     * This method write image to output stream, then it close the stream before return
     *
     * @param image Image
     * @param outputStream OutputStream
     * @throws IOException
     */
    public static void writeJpegImage_nonSun(Image image, OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("Does not accept null outputStream");
        }
        try {
            JpegEncoder encoder = new JpegEncoder(image, 80, outputStream);
            encoder.Compress();
        } finally {
            try {
                outputStream.close();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
        }
    }

    public static void writeJpegImage_nonSun(Image image, String fileName) throws IOException {
        OutputStream outputStream = new FileOutputStream(fileName);

        //this method will close the stream before it returns so no need to close
        writeJpegImage_nonSun(image, outputStream);
    }

    public static Image getScaledInstance(Image srcImage, int width, int height) {
        boolean useSun = false;
        ImageFilter filter;
        if (useSun) {
            //log.trace("use sun scalefilter");
            filter = new java.awt.image.AreaAveragingScaleFilter(width, height);
        } else {
            //log.trace("use nguoimau scalefilter");
            filter = new net.myvietnam.mvncore.util.AreaAveragingScaleFilter(width, height);
        }
        ImageProducer prod = new FilteredImageSource(srcImage.getSource(), filter);
        Image newImage = Toolkit.getDefaultToolkit().createImage(prod);
        ImageIcon imageIcon = new ImageIcon(newImage);
        return imageIcon.getImage();
    }
    
    public static BufferedImage getProductionImage(String productVersion, String productRealeaseDate) {
        
        String str = productVersion + " on " + productRealeaseDate;
        int IMAGE_WIDTH  = 250;
        int IMAGE_HEIGHT = 30;

        BufferedImage bufferedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setBackground(Color.blue);
        g.setColor(Color.white);
        g.draw3DRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, false);
        FontMetrics fontMetrics = g.getFontMetrics();
        int strWidth  = fontMetrics.stringWidth(str);
        int strHeight = fontMetrics.getAscent() + fontMetrics.getDescent();
        g.drawString(str, (IMAGE_WIDTH - strWidth) / 2, IMAGE_HEIGHT - ((IMAGE_HEIGHT - strHeight) / 2) - fontMetrics.getDescent());
        g.dispose(); // free resource
        return bufferedImage;
    }
/*
    public static void main(String[] args) {
        try {
            FileInputStream is = new FileInputStream("c:\\PUTTY.RND");
            createThumbnail(is, "c:\\out.jpg", 120, 120);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        log.debug("done");
    }
*/
    public static String removeImageFromText(String input)
    {
        if (input == null || input.length() == 0) {
            return input;
        }
        StringBuffer buf = new StringBuffer(input.length() + 25);
        char[] chars = input.toCharArray();
        int len = input.length();
        int index = -1;
        int i = 0;
        int j = 0;
        int oldend = 0;
        while (++index < len) {
            char cur = chars[i = index];
            j = -1;  
            if (cur == '[' && index < (len - 6) && chars[i = index + 1] == 'i' && chars[++i] == 'm' && chars[++i] == 'g' && chars[++i] == ']' ) {
                //process [img]
                j = ++i;
                int u1 = j-1;
                int u2 = input.indexOf("[/img]", u1 + 1);
                if (u2 < 0) {
                    buf.append(chars, oldend, j - oldend);
                    oldend = j;
                } else {
                    buf.append(chars, oldend, index - oldend);
                    buf.append("");
                    String href = input.substring(u1 + 1, u2).trim();
                    // Add http:// to the front of links if and only if it doesn't have any protocols.
                    // Doing this handles this: "[url=sun.com]SUN[/url]"
                    // Changing it to <a href="http://sun.com">SUN</a>
                    // instead of <a href="http://localhost:8080/mvnforum/sun.com">SUN</a>
                    oldend = u2 + 6; // 6 == length of [/img]
                }
                index = oldend -1;// set to the last char of the tag, that is ']'
            }
        }
        if (oldend < len) {
            buf.append(chars, oldend, len - oldend);
        }
        return buf.toString();
    }
}





    /**
     * This class is taken from Pure Java AWT project
     * and modified to exclude PJAGraphicsManager class
     */
  // From java.awt.image.AreaAveragingScaleFilter
  // but without use of float type
  class AreaAveragingScaleFilter extends ImageFilter {
    private ColorModel rgbModel;
    private long []    alphas;
    private long []    reds;
    private long []    greens;
    private long []    blues;

    protected int      srcWidth;
    protected int      srcHeight;
    private   int []   srcPixels;
    protected int      destWidth;
    protected int      destHeight;
    protected int []   destPixels;

    {
      // Test if the class java.awt.image.ColorModel can be loaded
      //boolean classColorModelAccessible = PJAGraphicsManager.getDefaultGraphicsManager ().isClassAccessible ("java.awt.image.ColorModel");
      // modified by minhnn
      boolean classColorModelAccessible = isClassAccessible("java.awt.image.ColorModel");
      if (classColorModelAccessible) {
        rgbModel = ColorModel.getRGBdefault();
      }
    }

    /**
     * Constructs an AreaAveragingScaleFilter that scales the pixels from
     * its source Image as specified by the width and height parameters.
     * @param width  the target width to scale the image
     * @param height the target height to scale the image
     */
    AreaAveragingScaleFilter(int width, int height) {
        destWidth = width;
        destHeight = height;
    }

    public void setDimensions (int w, int h)
    {
      srcWidth = w;
      srcHeight = h;
      if (destWidth < 0)
      {
        if (destHeight < 0)
        {
          destWidth = srcWidth;
          destHeight = srcHeight;
        }
        else {
          destWidth = srcWidth * destHeight / srcHeight;
        }
      }
      else if (destHeight < 0) {
        destHeight = srcHeight * destWidth / srcWidth;
      }

      consumer.setDimensions (destWidth, destHeight);
    }

    public void setHints (int hints)
    {
      // Images are sent entire frame by entire frame
      consumer.setHints (  (hints & (SINGLEPASS | SINGLEFRAME))
                         | TOPDOWNLEFTRIGHT);
    }

    public void imageComplete (int status)
    {
      if (   status == STATICIMAGEDONE
          || status == SINGLEFRAMEDONE) {
        accumPixels (0, 0, srcWidth, srcHeight, rgbModel, srcPixels, 0, srcWidth);
      }
      consumer.imageComplete (status);
    }

    public void setPixels (int x, int y, int width, int height,
                           ColorModel model, byte pixels [], int offset, int scansize)
    {
      // Store pixels in srcPixels array
      if (srcPixels == null) {
        srcPixels = new int [srcWidth * srcHeight];
      }
      for (int row = 0, destRow = y * srcWidth;
           row < height;
           row++, destRow += srcWidth)
      {
        int rowOff = offset + row * scansize;
        for (int col = 0; col < width; col++) {
          // v1.2 : Added & 0xFF to disable sign bit
          srcPixels [destRow + x + col] = model.getRGB (pixels [rowOff + col] & 0xFF);
        }
      }
    }

    public void setPixels (int x, int y, int width, int height,
                   ColorModel model, int pixels[], int offset, int scansize)
    {
      // Store pixels in srcPixels array
      if (srcPixels == null) {
        srcPixels = new int [srcWidth * srcHeight];
      }
      for (int row = 0, destRow = y * srcWidth;
           row < height;
           row++, destRow += srcWidth)
      {
        int rowOff = offset + row * scansize;
        for (int col = 0; col < width; col++) {
          // If model == null, consider it's the default RGB model
          srcPixels [destRow + x + col] = (model == null)
                                                     ? pixels [rowOff + col]
                                                     : model.getRGB (pixels [rowOff + col]);
        }
      }
    }

    private int [] calcRow ()
    {
      long mult = (srcWidth * srcHeight) << 32;
      if (destPixels == null)
        destPixels = new int [destWidth];

      for (int x = 0; x < destWidth; x++)
      {
        int a = (int)roundDiv (alphas [x], mult);
        int r = (int)roundDiv (reds   [x], mult);
        int g = (int)roundDiv (greens [x], mult);
        int b = (int)roundDiv (blues  [x], mult);
        a = Math.max (Math.min (a, 255), 0);
        r = Math.max (Math.min (r, 255), 0);
        g = Math.max (Math.min (g, 255), 0);
        b = Math.max (Math.min (b, 255), 0);
        destPixels [x] = (a << 24 | r << 16 | g << 8 | b);
      }

      return destPixels;
    }

    private void accumPixels (int x, int y, int w, int h,
                              ColorModel model, int [] pixels, int off,
                              int scansize)
    {
      reds   = new long [destWidth];
      greens = new long [destWidth];
      blues  = new long [destWidth];
      alphas = new long [destWidth];

      int sy = y;
      int syrem = destHeight;
      int dy = 0;
      int dyrem = 0;
      while (sy < y + h)
      {
        if (dyrem == 0)
        {
          for (int i = 0; i < destWidth; i++) {
            alphas [i] =
            reds   [i] =
            greens [i] =
            blues  [i] = 0;
          }

          dyrem = srcHeight;
        }

        int amty = Math.min (syrem, dyrem);
        int sx = 0;
        int dx = 0;
        int sxrem = 0;
        int dxrem = srcWidth;
        int a = 0,
            r = 0,
            g = 0,
            b = 0;
        while (sx < w)
        {
          if (sxrem == 0)
          {
            sxrem = destWidth;
            int rgb = pixels [off + sx];
            a = rgb >>> 24;
            r = (rgb >> 16) & 0xFF;
            g = (rgb >>  8) & 0xFF;
            b = rgb & 0xFF;
          }

          int  amtx = Math.min (sxrem, dxrem);
          long mult = (amtx * amty) << 32;
          alphas [dx] += mult * a;
          reds   [dx] += mult * r;
          greens [dx] += mult * g;
          blues  [dx] += mult * b;

          if ((sxrem -= amtx) == 0) {
            sx++;
          }

          if ((dxrem -= amtx) == 0)
          {
            dx++;
            dxrem = srcWidth;
          }
        }

        if ((dyrem -= amty) == 0)
        {
          int[] outpix = calcRow();
          do
          {
            consumer.setPixels (0, dy, destWidth, 1,
                                rgbModel, outpix, 0, destWidth);
            dy++;
          }
          while ((syrem -= amty) >= amty && amty == srcHeight);
        }
        else {
          syrem -= amty;
        }

        if (syrem == 0)
        {
          syrem = destHeight;
          sy++;
          off += scansize;
        }
      }
    }
    ////////////////
    // util method
    ////////////////
    // util method
    /**
     * Returns the rounded result of <code>dividend / divisor</code>, avoiding the use of floating
     * point operation (returns the same as <code>Math.round((float)dividend / divisor)</code>).
     * @param dividend A <code>int</code> number to divide.
     * @param divisor  A <code>int</code> divisor.
     * @return dividend / divisor rounded to the closest <code>int</code> integer.
     */
    public static int roundDiv(int dividend, int divisor) {
        final int remainder = dividend % divisor;
        if (Math.abs(remainder) * 2 <= Math.abs(divisor)) {
            return dividend / divisor;
        } else if (dividend * divisor < 0) {
            return dividend / divisor - 1;
        } else {
            return dividend / divisor + 1;
        }
    }

    /**
     * Returns the rounded result of <code>dividend / divisor</code>, avoiding the use of floating
     * point operation (returns the same as <code>Math.round((double)dividend / divisor)</code>).
     * @param dividend A <code>long</code> number to divide.
     * @param divisor  A <code>long</code> divisor.
     * @return dividend / divisor rounded to the closest <code>long</code> integer.
     */
    public static long roundDiv (long dividend, long divisor) {
        final long remainder = dividend % divisor;
        if (Math.abs (remainder) * 2 <= Math.abs (divisor)) {
          return dividend / divisor;
        } else if (dividend * divisor < 0) {
          return dividend / divisor - 1;
        } else {
          return dividend / divisor + 1;
        }
    }

    /**
     * Returns <code>true</code> if it successes to load the class <code>className</code>.
     * If security manager is too restictive, it is possible that the classes <code>java.awt.Color</code>,
     * <code>java.awt.Rectangle</code>, <code>java.awt.Font</code>, <code>java.awt.FontMetrics</code>
     * and <code>java.awt.image.ColorModel</code> (and also <code>java.awt.Dimension</code> and other classes
     * not required by PJA classes) can't be loaded because they need either the class <code>java.awt.Toolkit</code>
     * or the library awt to be accessible to call their <code>initIDs ()</code> native method.
     * @param  className  the fully qualified class name.
     * @return <code>true</code> if <code>java.awt.Toolkit</code> class could be loaded.
     */
   public static boolean isClassAccessible(String className) {
       // Test if the class className can be loaded
       try {
           Class.forName(className);
           // Class can be loaded
           return true;
       } catch (ClassNotFoundException e) {
           // ignore
       } catch (LinkageError error) {// Thrown by some AWT classes which require AWT library in static initializer.
           // ignore
       } 

       return false;
   }
   
}
