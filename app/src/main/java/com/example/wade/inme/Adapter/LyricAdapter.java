package com.example.wade.inme.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wade.inme.JavaBean.LyricInfo;
import com.example.wade.inme.Network.DiskLruCache;
import com.example.wade.inme.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wade on 2017/6/13.
 */

public class LyricAdapter extends BaseAdapter {
    List<LyricInfo> lyriclist;
    DiskLruCache.Snapshot snapshot;
    LayoutInflater mInflater;
    Context mContext;
    public LyricAdapter(DiskLruCache.Snapshot snapshot, Context context){
        this.snapshot=snapshot;
        this.mContext=context;
        lyriclist=new ArrayList<LyricInfo>();
        mInflater=LayoutInflater.from(context);
        jiexi();
    }
    public LyricAdapter(String lyric, Context context){
        this.mContext=context;
        lyriclist=new ArrayList<LyricInfo>();
        mInflater=LayoutInflater.from(context);
        jiexi1(lyric);
    }
    public void jiexi1(String lyric){
        File file=new File(lyric);
        Log.e("time",lyric);
        if(file.exists()){
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));

                String linr=null;
                int i=0;
                while ((linr=br.readLine())!=null){
                    if(i>4){
                        if(linr.length()>10){
                            Log.e("time",gettime(linr)+"hh");
                            LyricInfo l=new LyricInfo(linr.substring(10,linr.length()),gettime(linr));
                            lyriclist.add(l);
                        }
                    }
                    i++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void jiexi(){
        if(snapshot!=null){
            InputStream is=snapshot.getInputStream(0);
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader r=new BufferedReader(isr);
            try {
                String linr=null;
                int i=0;
                while ((linr=r.readLine())!=null){
                    if(i>4){
                        if(linr.length()>10){
                            Log.e("time",gettime(linr)+"hh");
                            LyricInfo l=new LyricInfo(linr.substring(10,linr.length()),gettime(linr));
                            lyriclist.add(l);
                        }
                    }
                   i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public List<LyricInfo> getLyricInfos(){
        return lyriclist;
    }

    public long gettime(String str){
        String regEx="[^0-9]";
        Pattern p=Pattern.compile(regEx);
        Matcher m=p.matcher(str);
        String str1=m.replaceAll("").trim();
        int f= Integer.parseInt(str1.substring(0,2));
        int s=Integer.parseInt(str1.substring(2,4));
        return f*60+s;
    }

    @Override
    public int getCount() {
        return lyriclist.size();
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
        TextView Tv_lyric_item;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder = null;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.lyric_item,null);
            holder=new ViewHolder();
            holder.Tv_lyric_item=(TextView)convertView.findViewById(R.id.tv_lyric_item);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder) convertView.getTag();
        }
        holder.Tv_lyric_item.setText(lyriclist.get(position).getText());
        return convertView;
    }

}
