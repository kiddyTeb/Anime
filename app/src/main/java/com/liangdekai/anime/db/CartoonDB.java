package com.liangdekai.anime.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.liangdekai.anime.bean.CartoonBean;
import com.liangdekai.anime.bean.ChapterBean;
import com.liangdekai.anime.bean.ImageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库，存储数据
 */
public class CartoonDB {
    public static final String DB_NAME = "cartoon" ;
    public static final int VERSION = 1;//数据库版本
    private SQLiteDatabase mDataBase ;
    private static CartoonDB mCartoonDB ;

    private CartoonDB(Context context){
        DBHelper dbHelper = new DBHelper(context , DB_NAME , null , VERSION);
        mDataBase = dbHelper.getWritableDatabase() ;
    }

    public synchronized static CartoonDB getInstance(Context context){
        if ( mCartoonDB == null){
            mCartoonDB = new CartoonDB(context);
        }
        return mCartoonDB;
    }

    /**
     * 保存相关信息内容
     * @param cartoonBean
     */
    public void saveCartoonInfo(CartoonBean cartoonBean){
        ContentValues values = new ContentValues();//使用ContentValues对要添加的数据进行组装
        if (cartoonBean != null){
            values.put("name" , cartoonBean.getName());
            values.put("type" , cartoonBean.getType());
            values.put("area" , cartoonBean.getArea());
            values.put("finish" , cartoonBean.getFinish());
            values.put("introduction" , cartoonBean.getIntroduction());
            values.put("coverImg" , cartoonBean.getCoverImg());
            mDataBase.insert("Cartoon" , null , values) ;
        }
    }

    /**
     * 获取信息内容
     * @param type
     * @return
     */
    public List<CartoonBean> loadCartoonInfo(String type){
        List<CartoonBean> cartoonList = new ArrayList<>();
        Cursor cursor = mDataBase.query("Cartoon" , null , "type = ?" , new String[]{type} , null , null , null);
        if (cursor.moveToFirst()){
            do {
                CartoonBean cartoonBean = new CartoonBean() ;
                cartoonBean.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                cartoonBean.setArea(cursor.getString(cursor.getColumnIndexOrThrow("area")));
                if (cursor.getString(cursor.getColumnIndexOrThrow("finish")).equals(false)){
                    cartoonBean.setFinish("未完结");
                }else {
                    cartoonBean.setFinish("完结");
                }
                if (cursor.getString(cursor.getColumnIndexOrThrow("introduction")).equals("")){
                    cartoonBean.setIntroduction("暂无");
                }else{
                    cartoonBean.setIntroduction(cursor.getString(cursor.getColumnIndexOrThrow("introduction")));
                }
                cartoonBean.setCoverImg(cursor.getString(cursor.getColumnIndexOrThrow("coverImg")));
                cartoonList.add(cartoonBean);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return cartoonList ;
    }

    /**
     * 保存章节列表
     * @param chapterBean
     */
    public void saveChapterInfo(ChapterBean chapterBean){
        ContentValues values = new ContentValues();//使用ContentValues对要添加的数据进行组装
        if (chapterBean != null){
            values.put("comicName" , chapterBean.getComicName());
            values.put("name" , chapterBean.getName());
            values.put("id" , chapterBean.getId());
            mDataBase.insert("Chapter" , null , values) ;
        }
    }

    /**
     * 获取章节列表数据
     * @param comicName
     * @return
     */
    public List<ChapterBean> loadChapterInfo(String comicName){
        List<ChapterBean> chapterList = new ArrayList<>();
        Cursor cursor = mDataBase.query("Chapter" , null , "comicName = ?" , new String[]{comicName} , null , null , null);
        if (cursor.moveToFirst()){
            do {
                ChapterBean chapterBean = new ChapterBean() ;
                chapterBean.setComicName(cursor.getString(cursor.getColumnIndexOrThrow("comicName")));
                chapterBean.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                chapterBean.setId(cursor.getString(cursor.getColumnIndexOrThrow("id")));
                chapterList.add(chapterBean);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return chapterList ;
    }

    public void saveImageInfo(ImageBean imageBean){
        ContentValues values = new ContentValues();//使用ContentValues对要添加的数据进行组装
        if (imageBean != null){
            values.put("name" , imageBean.getName());
            values.put("imageUrl" , imageBean.getImageUrl());
            values.put("id" , imageBean.getId());
            mDataBase.insert("Image" , null , values);
        }
    }

    public List<String> loadImageInfo(String name){
        List<String> imageList = new ArrayList<>();
        Cursor cursor = mDataBase.query("Image" , null , "name = ?" , new String[]{name} , null , null , null);
        if (cursor.moveToFirst()){
            do {
                imageList.add(cursor.getString(cursor.getColumnIndexOrThrow("imageUrl")));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return imageList ;
    }
}
