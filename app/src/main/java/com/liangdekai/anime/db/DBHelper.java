package com.liangdekai.anime.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asus on 2016/8/8.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String CREATE_CARTOON_TABLE = "create table Cartoon(name text , type text , area text , finish text , introduction text , coverImg text)" ;
    public static final String CREATE_CHAPTER_TABLE = "create table Chapter(comicName text , name text , id text )" ;
    public static final String CREATE_IMAGE_TABLE = "create table Image(name text , imageUrl text , id text )" ;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CARTOON_TABLE);
        sqLiteDatabase.execSQL(CREATE_CHAPTER_TABLE);
        sqLiteDatabase.execSQL(CREATE_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
