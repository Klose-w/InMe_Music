package com.example.wade.inme.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.wade.inme.JavaBean.GeDanBean;
import com.example.wade.inme.R;

import java.util.List;

/**
 * Created by wade on 2017/6/23.
 */
        /*biaoji=1;
        SongName=son;
        Songid=soi;
        Songsecond=soc;
        Songerid=sori;
        Songbitmap=sob;
        Songdownurl=sod;
        SongUrl=su;
        SongArtist=sa;
        SongAlubmid=soa;
        SongDuration=Songsecond*1000;*/
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String Recntly_Play="create table Reclist("
            +"SongId integer primary key autoincrement,"
            +"SongName text,"
            +"SongAlbum text,"
            +"SongArtist text,"
            +"SongUrl text,"
            +"Songerid text,"
            +"Songbitmap text,"
            +"SongDuration integer,"
            +"Biaoji integer,"
            +"SongSize integer)";
    public static final String Down_Play="create table Downlist("
            +"SongId integer primary key autoincrement,"
            +"SongName text,"
            +"SongAlbum text,"
            +"SongArtist text,"
            +"SongUrl text,"
            +"SongDuration integer,"
            +"SongSize integer)";
    public static final String Gd_list="create table Gdlist("
            +"GdId integer primary key autoincrement,"
            +"Gdlist text)";
    Context mContext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Recntly_Play);
        db.execSQL(Down_Play);
        db.execSQL(Gd_list);
        //Toast.makeText(mContext,"成功",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
