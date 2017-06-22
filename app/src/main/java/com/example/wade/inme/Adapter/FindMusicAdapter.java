package com.example.wade.inme.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by wade on 2017/6/1.
 */

public class FindMusicAdapter extends BaseAdapter {
    List<MusicInfor> listmusics;
    Context mContext;
    String sdpath;
    String musicpath;
    LayoutInflater mInflater;
    InMeApplicacation inMeApplicacation;
    public FindMusicAdapter(Context context){
        mContext=context;
        mInflater= LayoutInflater.from(mContext);;
        inMeApplicacation=(InMeApplicacation)mContext.getApplicationContext();
        ;listmusics=inMeApplicacation.getLocalmusicList();
        /*sdpath= Environment.getExternalStorageDirectory().getAbsolutePath();
        musicpath=sdpath+"/netease/cloudmusic/Music/";
        byte[] buf=new byte[128];
        try {
            RandomAccessFile raf=new RandomAccessFile(sdpath,"r");
            raf.seek(raf.length()-128);
            raf.read(buf);
            raf.close();
            String SongName=new String(buf,3,30,"utf-8").trim();
            Log.e("song",SongName);
        } catch (FileNotFoundException e) {
            Log.e("ja",sdpath+"");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    public List<MusicInfor> getSong(){
        return listmusics;
    }


    @Override
    public int getCount() {
        return listmusics.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        TextView Tv_musictitle;
        ImageView Iv_musicicon;
        TextView Tv_musicatr;
        TextView Tv_musiczj;
        ImageButton Ib_music_more;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.music_list_xianshi,null);
            holder=new ViewHolder();
            holder.Tv_musictitle=(TextView) convertView.findViewById(R.id.tv_musictitle);
            holder.Iv_musicicon=(ImageView) convertView.findViewById(R.id.iv_musicicon);
            holder.Tv_musicatr=(TextView)convertView.findViewById(R.id.tv_musicatr);
            holder.Tv_musiczj=(TextView)convertView.findViewById(R.id.tv_musiczj);
            holder.Ib_music_more=(ImageButton) convertView.findViewById(R.id.ib_music_more);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        MusicInfor musicInfor;
        musicInfor=listmusics.get(position);
        holder.Tv_musictitle.setText(musicInfor.getSongName());
        holder.Tv_musicatr.setText(musicInfor.getSongArtist());
        holder.Tv_musiczj.setText(musicInfor.getSongAlbum());
        return convertView;
    }
}
