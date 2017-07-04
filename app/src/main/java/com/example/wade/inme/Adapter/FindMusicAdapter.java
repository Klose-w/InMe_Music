package com.example.wade.inme.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.GeDanBean;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.R;
import com.example.wade.inme.Service.DownloadService;

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
    PopupWindow popupdown;
    PopupWindow popupWindow;
    int HightWindow;
    WindowManager wm;
    Window mwindow;
    private List<GeDanBean> gd;
    public FindMusicAdapter(Context context,Window window){
        mContext=context;
        mInflater= LayoutInflater.from(mContext);;
        inMeApplicacation=(InMeApplicacation)mContext.getApplicationContext();
        listmusics=inMeApplicacation.getLocalmusicList();
        mwindow=window;
        wm=(WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        HightWindow=wm.getDefaultDisplay().getHeight();
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
    public void initPopwindow(View view){

        popupWindow=new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(255,255,255,255)));
        popupWindow.setAnimationStyle(R.style.take_photo_anim);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=mwindow.getAttributes();
                lp.alpha=1;
                mwindow.setAttributes(lp);
            }
        });
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        holder.Ib_music_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=LayoutInflater.from(mContext).inflate(R.layout.localsongpopwindow,null);
                TextView Tv_title_pop=(TextView)view.findViewById(R.id.tv_title_pop);
                TextView Tv_next_pop=(TextView)view.findViewById(R.id.tv_next_pop);
                TextView Tv_love_pop=(TextView)view.findViewById(R.id.tv_love_pop);
                Tv_love_pop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popDialog(listmusics.get(position));
                        popupWindow.dismiss();
                    }
                });
                TextView Tv_chat_pop=(TextView)view.findViewById(R.id.tv_chat_pop);
                TextView Tv_talk_pop=(TextView)view.findViewById(R.id.tv_talk_pop);
                TextView Tv_songger_pop=(TextView)view.findViewById(R.id.tv_songger_pop);
                TextView Tv_alumb_pop=(TextView)view.findViewById(R.id.tv_alumb_pop);
                TextView Tv_set_pop=(TextView)view.findViewById(R.id.tv_set_pop);
                TextView Tv_del_pop=(TextView)view.findViewById(R.id.tv_del_pop);
                Tv_title_pop.setText(listmusics.get(position).getSongName());
                Tv_songger_pop.setText("歌手:"+listmusics.get(position).getSongArtist());
                Tv_alumb_pop.setText("专辑:"+listmusics.get(position).getSongAlubmid());;
                Tv_talk_pop.setText("评论(0)");
                Tv_next_pop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inMeApplicacation.nextorlast(2);
                        popupWindow.dismiss();
                    }
                });
                initPopwindow(view);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAtLocation(v,0,0,HightWindow);
                WindowManager.LayoutParams lp=mwindow.getAttributes();
                lp.alpha=0.5f;
                mwindow.setAttributes(lp);
            }
        });
        return convertView;
    }
    public void popDialog(final MusicInfor musicInfor){
        View layout=LayoutInflater.from(mContext).inflate(R.layout.gdlistdialog,null);
        ListView Lv_gd_sc=(ListView) layout.findViewById(R.id.lv_gd_sc);
        gdlistAdapter g=new gdlistAdapter();
        Lv_gd_sc.setAdapter(g);
        final AlertDialog dialog= new AlertDialog.Builder(mContext).setTitle("收藏到歌单").setView(layout).create();
        dialog.show();
        Lv_gd_sc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GeDanBean geDanBean=gd.get(position);
                List<MusicInfor> m=geDanBean.getGdlist();
                m.add(musicInfor);
                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
        params.height=800;
        dialog.getWindow().setAttributes(params);
    }
    public class gdlistAdapter extends BaseAdapter{
        public  gdlistAdapter(){
            gd=inMeApplicacation.getGeDanBeanList();
        }
        @Override
        public int getCount() {
            return gd.size();
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
            public ImageView Iv_list_back_pager;
            public ImageView Iv_list_icon_pager;
            public TextView Tv_list_label_pager;
            public TextView Tv_list_num_pager;
            public ImageButton Ib_list_more;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                convertView=mInflater.inflate(R.layout.musicpager_list_local,null);
                holder=new ViewHolder();
                holder.Iv_list_icon_pager=(ImageView)convertView.findViewById(R.id.iv_list_icon_pager);
                holder.Iv_list_back_pager=(ImageView)convertView.findViewById(R.id.iv_list_back_pager);
                holder.Ib_list_more=(ImageButton) convertView.findViewById(R.id.ib_list_more);
                holder.Tv_list_label_pager=(TextView)convertView.findViewById(R.id.tv_list_label_pager);
                holder.Tv_list_num_pager=(TextView)convertView.findViewById(R.id.tv_list_num_pager);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder)convertView.getTag();
            }
            holder.Iv_list_icon_pager.setImageBitmap((Bitmap)gd.get(position).getBitmap());
            holder.Iv_list_back_pager.setBackgroundResource(R.color.colorLove);
            holder.Tv_list_label_pager.setText((String)gd.get(position).getGdname());
            //holder3.Tv_list_num_pager.setText(listgedanBean.get(position-lable.length-1).getGdlist().size()+"首");
            holder.Tv_list_num_pager.setText(gd.get(position).getGdlist().size()+"首");
            holder.Ib_list_more.setBackgroundResource(R.color.wight);;
            return convertView;
        }
    }
}
