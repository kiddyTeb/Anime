package com.liangdekai.anime.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.liangdekai.anime.MyApplication;
import com.liangdekai.anime.libcore.io.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 加载图片类
 */
public class ImageLoader {
    private static final int MAX_SIZE = 20*1024*1024 ;
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            ViewHolder viewHolder = (ViewHolder) msg.obj;
            Bitmap bitmap = getImageByCache(viewHolder.path);
            viewHolder.largeImageView.setImageBitmap(bitmap);
        }
    };
    private TaskManager mTaskManager ;
    private static ImageLoader mImageLoader ;
    private DiskLruCache mDiskLruCache ;

    /**
     * 获取该类的实例
     * @return
     */
    public static ImageLoader getInstance(){
        if (mImageLoader == null){
            synchronized (ImageLoader.class){
                if (mImageLoader == null){
                    mImageLoader = new ImageLoader();
                }
            }
        }
        return mImageLoader;
    }

    public ImageLoader(){
        init();
    }

    private void init(){
        mTaskManager = TaskManager.getInstance();
        try {
            File cacheDir  = DiskLruCacheHelper.getDiskLruCacheDir(MyApplication.getContext() , "bitmap");
            if (!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir , DiskLruCacheHelper.getAppVersion(MyApplication.getContext()) , 1 , MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getImageByCache(String url){
        try {
            String key = DiskLruCacheHelper.hashKeyForDisk(url);
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null){
                InputStream inputStream = snapshot.getInputStream(0);
                //Bitmap bitmap = CompressImage.compressImage(inputStream , 100 , 100) ;
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Log.d("test" , "狗狗2"+bitmap.getByteCount());
                return bitmap;
            }else{
                return null ;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }

    public void loadImage(final String url , final ImageView imageView){
        mTaskManager.addLoadTask(new Runnable() {
            @Override
            public void run() {
                try {
                    String key = DiskLruCacheHelper.hashKeyForDisk(url);
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if (editor != null){
                        OutputStream outputStream = editor.newOutputStream(0);
                        if (downLoadUrlToStream(url , outputStream)){
                            editor.commit();
                        }else{
                            editor.abort();
                        }
                    }
                    mDiskLruCache.flush();
                    ViewHolder viewHolder = new ViewHolder();
                    viewHolder.largeImageView = imageView ;
                    viewHolder.path = url ;
                    Message message = Message.obtain();
                    message.obj = viewHolder ;
                    mHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean downLoadUrlToStream(String urlImage  , OutputStream outputStream){
        HttpURLConnection httpURLConnection = null ;
        BufferedOutputStream out = null ;
        BufferedInputStream in = null ;
        try {
            final URL url = new URL(urlImage);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(httpURLConnection.getInputStream() , 12 * 1024) ;
            out = new BufferedOutputStream(outputStream , 12 * 1024) ;
            int judge ;
            while((judge = in.read()) != -1){
                out.write(judge);
            }
            return true ;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
