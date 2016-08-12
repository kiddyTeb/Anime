package com.liangdekai.anime.util;

import android.util.Log;

import com.google.gson.Gson;
import com.liangdekai.anime.bean.CartoonBean;
import com.liangdekai.anime.bean.ChapterBean;
import com.liangdekai.anime.bean.ImageBean;
import com.liangdekai.anime.bean.Json;
import com.liangdekai.anime.bean.JsonString;
import com.liangdekai.anime.bean.Result;
import com.liangdekai.anime.bean.ResultBean;
import com.liangdekai.anime.db.CartoonDB;

import java.util.ArrayList;
import java.util.List;

public class HandleSaveResponse {

    public static void handleSaveResponse(CartoonDB cartoonDB , String result){
        Gson gson = new Gson() ;
        JsonString jsonString = gson.fromJson(result , JsonString.class) ;
        ResultBean resultBean = jsonString.getResult() ;
        List<CartoonBean> list = resultBean.getBookList();
        for (int i = 0 ; i < list.size() ; i++){
            cartoonDB.saveCartoonInfo(list.get(i));
        }
    }

    public static void handleSaveChapter(CartoonDB cartoonDB  , String result){
        Gson gson = new Gson() ;
        JsonString jsonString = gson.fromJson(result , JsonString.class) ;
        ResultBean resultBean = jsonString.getResult() ;
        List<ChapterBean> list = resultBean.getChapterList() ;
        String comicName = resultBean.getComicName();
        for (int i = 0 ; i<list.size() ; i++){
            list.get(i).setComicName(comicName);
            cartoonDB.saveChapterInfo(list.get(i));
        }
    }

    public static List<String> handleSaveImage(String result){
        Gson gson = new Gson() ;
        Json jsonString = gson.fromJson(result , Json.class) ;
        Result resultBean = jsonString.getResult() ;
        List<ImageBean> list = resultBean.getImageList();
        List<String> imageList = new ArrayList<>();
        for (int i = 0 ; i < list.size() ; i++){
            /*list.get(i).setName(name);
            cartoonDB.saveImageInfo(list.get(i));*/
            imageList.add(list.get(i).getImageUrl());
        }
        return imageList ;
    }
}
