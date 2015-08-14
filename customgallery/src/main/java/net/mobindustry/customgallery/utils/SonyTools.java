/*
 * Copyright (c) 2010, Sony Ericsson Mobile Communication AB. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, this
 *      list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *    * Neither the name of the Sony Ericsson Mobile Communication AB nor the names
 *      of its contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.mobindustry.customgallery.utils;

import android.content.res.Resources;
import android.graphics.*;
import android.media.ThumbnailUtils;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * These are tools by SonyEricsson company devs to use in their projects
 * <br />
 * Source: <br />
 * http://developer.sonymobile.com/2011/06/27/how-to-scale-images-for-your-android-application/
 * <br />
 * http://developer.sonymobile.com/downloads/code-example-module/image-scaling-code-example-for-android/
 * <br />
 * http://stackoverflow.com/a/23189682/2957893
 * <br /><br />
 * Class containing static utility methods for bitmap decoding and scaling
 *
 * @author Andreas Agvard (andreas.agvard@sonyericsson.com)<br />
 *         NOTE: class was enhanced with some of our own methods.
 */
public class SonyTools {

   /**
    * Utility function for decoding an image resource. The decoded bitmap will
    * be optimized for further scaling to the requested destination dimensions
    * and scaling logic.
    *
    * @param res          The resources object containing the image data
    * @param resId        The resource id of the image data
    * @param dstWidth     Width of destination area
    * @param dstHeight    Height of destination area
    * @param scalingLogic Logic to use to avoid image stretching
    *
    * @return Decoded bitmap
    */
   public static Bitmap decodeResource(Resources res, int resId, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeResource(res, resId, options);
      options.inJustDecodeBounds = false;
      options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight, scalingLogic);
      return BitmapFactory.decodeResource(res, resId, options);
   }

   /**
    * Utility function for decoding an image resource. The decoded bitmap will
    * be optimized for further scaling to the requested destination dimensions
    * and scaling logic.
    *
    * @param res       The resources object containing the image data
    * @param resId     The resource id of the image data
    * @param dstWidth  Width of destination area
    * @param dstHeight Height of destination area
    *
    * @return Decoded bitmap
    * <p/>
    * Scaling logic FIT is used by default.
    */
   public static Bitmap decodeResource(Resources res, int resId, int dstWidth, int dstHeight) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeResource(res, resId, options);
      options.inJustDecodeBounds = false;
      options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight, ScalingLogic.FIT);
      return BitmapFactory.decodeResource(res, resId, options);
   }

   /**
    * Utility function for decoding an image resource. The decoded bitmap will
    * be optimized for further scaling to the requested destination dimensions
    * and scaling logic.
    *
    * @param path         Path to file
    * @param dstWidth     Width of destination area
    * @param dstHeight    Height of destination area
    * @param scalingLogic Logic to use to avoid image stretching
    *
    * @return Decoded bitmap
    */
   public static Bitmap decodePath(String path, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(path, options);
      options.inJustDecodeBounds = false;
      options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight, scalingLogic);
      return BitmapFactory.decodeFile(path, options);
   }

   /**
    * Utility function for decoding an image resource. The decoded bitmap will
    * be optimized for further scaling to the requested destination dimensions
    * and scaling logic.
    *
    * @param path      Path to file
    * @param dstWidth  Width of destination area
    * @param dstHeight Height of destination area
    *
    * @return Decoded bitmap
    * <p/>
    * Scaling logic FIT is used by default.
    */
   public static Bitmap decodePath(String path, int dstWidth, int dstHeight) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(path, options);
      options.inJustDecodeBounds = false;
      options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight, ScalingLogic.FIT);
      return BitmapFactory.decodeFile(path, options);
   }

   /**
    * Utility function for decoding an image resource. The decoded bitmap will
    * be optimized for further scaling to the requested destination dimensions
    * and scaling logic.
    *
    * @param path Path to file
    * @param size Wanted size (of bigger side if image is not square) of destination bitmap
    *
    * @return Decoded bitmap
    * <p/>
    * Scaling logic FIT is used by default.
    */
   public static Bitmap decodePath(String path, int size) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(path, options);
      options.inJustDecodeBounds = false;

      int[] desiredSizes = calculateDesiredSizes(size, options.outWidth, options.outHeight);

      options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, desiredSizes[0], desiredSizes[1], ScalingLogic.FIT);
      return BitmapFactory.decodeFile(path, options);
   }

   /**
    * Utility function for decoding an image resource. The decoded bitmap will
    * be optimized for further scaling to the requested destination dimensions
    * and scaling logic.
    *
    * @param streamDecodeBounds InputStream to file - this one to read parameters, but not load bytes to memory
    * @param streamDecode       InputStream to file - this to actually decode and load bytes
    * @param size               Wanted size (of bigger side if image is not square) of destination bitmap
    *
    * @return Decoded bitmap
    * <p/>
    * Scaling logic FIT is used by default.
    */
   public static Bitmap decodeStream(InputStream streamDecodeBounds, InputStream streamDecode, int size) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;

      BitmapFactory.decodeStream(streamDecodeBounds, null, options);
      options.inJustDecodeBounds = false;

      int[] desiredSizes = calculateDesiredSizes(size, options.outWidth, options.outHeight);

      options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, desiredSizes[0], desiredSizes[1], ScalingLogic.FIT);
      return BitmapFactory.decodeStream(streamDecode, null, options);
   }

   /**
    * Utility function for creating a scaled version of an existing bitmap
    *
    * @param source    Bitmap to scale
    * @param dstWidth  Wanted width of destination bitmap
    * @param dstHeight Wanted height of destination bitmap
    *
    * @return New scaled bitmap object
    * <p/>
    * Scaling logic FIT is used by default.
    */
   public static Bitmap createScaledBitmap(Bitmap source, int dstWidth, int dstHeight) {
      return createScaledBitmap(source, dstWidth, dstHeight, ScalingLogic.FIT);
   }

   /**
    * Utility function for creating a scaled version of an existing bitmap
    *
    * @param source Bitmap to scale
    * @param size   Wanted size (of bigger side if image is not square) of destination bitmap
    *
    * @return New scaled bitmap object
    * <p/>
    * Scaling logic FIT is used by default. Proportions will be saved.
    */
   public static Bitmap createScaledBitmap(Bitmap source, int size) {
      int width = source.getWidth(), height = source.getHeight();

      if (width == height) {
         return createScaledBitmap(source, size, size, ScalingLogic.FIT);
      }

      int[] desiredSizes = calculateDesiredSizes(size, width, height);

      return createScaledBitmap(source, desiredSizes[0], desiredSizes[1], ScalingLogic.FIT);
   }

   /**
    * Utility function for creating a scaled square version of an existing bitmap
    *
    * @param source Bitmap to scale
    * @param size   Wanted size of destination bitmap
    *
    * @return New scaled bitmap object
    * <p/>
    * If image not square - redundant parts will be cropped
    */
   public static Bitmap createScaledSquareBitmap(Bitmap source, int size) {
      return createScaledBitmap(source, size, size, ScalingLogic.CROP);
   }

   /**
    * Utility function for creating a scaled version of an existing bitmap
    *
    * @param source       Bitmap to scale
    * @param dstWidth     Wanted width of destination bitmap
    * @param dstHeight    Wanted height of destination bitmap
    * @param scalingLogic Logic to use to avoid image stretching
    *
    * @return New scaled bitmap object
    */
   public static Bitmap createScaledBitmap(Bitmap source, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
      Rect srcRect = calculateSrcRect(source.getWidth(), source.getHeight(), dstWidth, dstHeight, scalingLogic);
      Rect dstRect = calculateDstRect(source.getWidth(), source.getHeight(), dstWidth, dstHeight, scalingLogic);

      Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(scaledBitmap);
      canvas.drawBitmap(source, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG));

      return scaledBitmap;
   }

   /**
    * ScalingLogic defines how scaling should be carried out if source and
    * destination image has different aspect ratio.
    * <p/>
    * CROP: Scales the image the minimum amount while making sure that at least
    * one of the two dimensions fit inside the requested destination area.
    * Parts of the source image will be cropped to realize this.
    * <p/>
    * FIT: Scales the image the minimum amount while making sure both
    * dimensions fit inside the requested destination area. The resulting
    * destination dimensions might be adjusted to a smaller size than
    * requested.
    */
   public enum ScalingLogic {
      CROP, FIT, EXACT
   }

   /**
    * Calculate optimal down-sampling factor given the dimensions of a source
    * image, the dimensions of a destination area and a scaling logic.
    *
    * @param srcWidth     Width of source image
    * @param srcHeight    Height of source image
    * @param dstWidth     Width of destination area
    * @param dstHeight    Height of destination area
    * @param scalingLogic Logic to use to avoid image stretching
    *
    * @return Optimal down scaling sample size for decoding
    */
   public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
      if (scalingLogic == ScalingLogic.FIT) {
         final float srcAspect = (float) srcWidth / (float) srcHeight;
         final float dstAspect = (float) dstWidth / (float) dstHeight;

         if (srcAspect > dstAspect) {
            return srcWidth / dstWidth;
         } else {
            return srcHeight / dstHeight;
         }
      } else {
         final float srcAspect = (float) srcWidth / (float) srcHeight;
         final float dstAspect = (float) dstWidth / (float) dstHeight;

         if (srcAspect > dstAspect) {
            return srcHeight / dstHeight;
         } else {
            return srcWidth / dstWidth;
         }
      }
   }

   /**
    * Calculates source rectangle for scaling bitmap
    *
    * @param srcWidth     Width of source image
    * @param srcHeight    Height of source image
    * @param dstWidth     Width of destination area
    * @param dstHeight    Height of destination area
    * @param scalingLogic Logic to use to avoid image stretching
    *
    * @return Optimal source rectangle
    */
   public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
      if (scalingLogic == ScalingLogic.CROP) {
         final float srcAspect = (float) srcWidth / (float) srcHeight;
         final float dstAspect = (float) dstWidth / (float) dstHeight;

         if (srcAspect > dstAspect) {
            final int srcRectWidth = (int) (srcHeight * dstAspect);
            final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
            return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
         } else {
            final int srcRectHeight = (int) (srcWidth / dstAspect);
            final int scrRectTop = (srcHeight - srcRectHeight) / 2;
            return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
         }
      } else {
         return new Rect(0, 0, srcWidth, srcHeight);
      }
   }

   /**
    * Calculates destination rectangle for scaling bitmap
    *
    * @param srcWidth     Width of source image
    * @param srcHeight    Height of source image
    * @param dstWidth     Width of destination area
    * @param dstHeight    Height of destination area
    * @param scalingLogic Logic to use to avoid image stretching
    *
    * @return Optimal destination rectangle
    */
   public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
      if (scalingLogic == ScalingLogic.FIT) {
         final float srcAspect = (float) srcWidth / (float) srcHeight;
         final float dstAspect = (float) dstWidth / (float) dstHeight;

         if (srcAspect > dstAspect) {
            return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
         } else {
            return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
         }
      } else {
         return new Rect(0, 0, dstWidth, dstHeight);
      }
   }

   /**
    * Calculates destination sized based on given source sizes and desired bigger side size
    *
    * @param desiredSize  Wanted size (of bigger side if image is not square) of destination bitmap
    * @param sourceWidth  Height of source image
    * @param sourceHeight Width of destination area
    *
    * @return array with width and height corresponding
    */
   private static int[] calculateDesiredSizes(int desiredSize, int sourceWidth, int sourceHeight) {
      if (sourceWidth == sourceHeight) {
         return new int[] { desiredSize, desiredSize };
      }

      float ratio;
      int desiredWidth, desiredHeight;
      boolean vertical = sourceHeight > sourceWidth;
      if (vertical) {
         ratio = sourceHeight / sourceWidth;
         desiredHeight = desiredSize;
         desiredWidth = Math.round(desiredSize / ratio);
      } else {
         ratio = sourceWidth / sourceHeight;
         desiredWidth = desiredSize;
         desiredHeight = Math.round(desiredSize / ratio);
      }

      return new int[] { desiredWidth, desiredHeight };
   }

   /**
    * Crops image. Selection area for cropping is a square with size equal to smaller side, centered.
    *
    * @param filePath path to source bitmap
    */
   public static Bitmap cropCenter(String filePath) {
      Bitmap source, croppedBitmap;
      InputStream is;

      try {
         is = new FileInputStream(filePath);
      } catch (IOException e) {
         Log.e("Log", e.getClass().getSimpleName() + " during getting photo by filepath (" + filePath + "): "
                 + e.getLocalizedMessage());
         return null;
      }

      source = BitmapFactory.decodeStream(is);
      int size = source.getHeight() > source.getWidth() ? source.getWidth() : source.getHeight();
      croppedBitmap = ThumbnailUtils.extractThumbnail(source, size, size);
      return croppedBitmap;
   }

   public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
      return stream.toByteArray();
   }
}
