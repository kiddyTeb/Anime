package com.liangdekai.anime.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by asus on 2016/8/10.
 */
public class CompressImage {

    /**
     * 计算采样率
     * @param options
     * @param requireHeight
     * @param requireWidth
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options , int requireHeight , int requireWidth){
        final int height = options.outHeight ;
        final int width = options.outWidth ;
        int inSampleSize = 1 ;
        if (height > requireHeight || width > requireWidth){
            final int heightRatio = (Math.round((float)height / (float) requireHeight));
            final int widthRatio = (Math.round((float)width / (float) requireWidth));
            inSampleSize = heightRatio > widthRatio ? widthRatio : heightRatio ;
        }
        return inSampleSize;
    }

    /**
     * 根据采样率对图片进行压缩
     * @param inputStream
     * @param requireHeight
     * @param requireWidth
     * @return
     */
    public static Bitmap compressImage(InputStream inputStream, int requireHeight , int requireWidth){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true ;
        BitmapFactory.decodeStream(inputStream);
        options.inSampleSize = calculateInSampleSize(options , requireHeight , requireWidth);
        options.inJustDecodeBounds = false ;
        return BitmapFactory.decodeStream(inputStream) ;
    }
}
