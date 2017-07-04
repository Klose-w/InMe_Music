package com.example.wade.inme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.JavaBean.musicbean;
import com.example.wade.inme.R;
import com.example.wade.inme.Service.DownloadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wade on 2017/6/22.
 */

public class SearchMusicAdapter extends BaseAdapter {
    OkHttpClient client = new OkHttpClient();
    private String Key;
    private Context mContext;
    private String showapi_appid = "39401";
    private String showapi_sign = "3e47b57274ff414f81e76f624a6a1231";
    private String url;
    private List<MusicInfor> list;
    PopupWindow popupWindow;
    PopupWindow popupdown;
    int HightWindow;
    WindowManager wm;
    Window mwindow;
    InMeApplicacation inMeApplicacation;
    public SearchMusicAdapter(String key, Context context,Window window){
        Key=key;
        mContext=context;
        list=new ArrayList<MusicInfor>();
        mwindow=window;
        url = "http://route.showapi.com/213-1?showapi_appid="+showapi_appid+"&keyword="+Key+"&showapi_sign="+showapi_sign;
        //url="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.catalogSug&format=json&query=" + Key;
        Log.e("url",url);
        findMusic();
        wm=(WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        HightWindow=wm.getDefaultDisplay().getHeight();
        inMeApplicacation=(InMeApplicacation)mContext.getApplicationContext();
    }
    public List<MusicInfor> getSondid(){
        return list;
    }
    public void findMusic(){
        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest sr = new JsonObjectRequest(url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("jjjj",jsonObject.toString());
                //Log.e("json", jsonObject.toString());
               JSONArray jsonArray = null;
                try {
                    jsonObject=jsonObject.getJSONObject("showapi_res_body");
                    jsonObject=jsonObject.getJSONObject("pagebean");
                    jsonArray = jsonObject.getJSONArray("contentlist");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        jsonObject1.getString("songname");
                        MusicInfor m=new MusicInfor(jsonObject1.getString("songname"),jsonObject1.getString("songid"),220,
                                jsonObject1.getString("singerid"), jsonObject1.getString("albumpic_small"),jsonObject1.getString("downUrl"),
                                jsonObject1.getString("m4a"),jsonObject1.getString("singername"),jsonObject1.getString("albumname"));
                        list.add(m);
                    }
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(sr);
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
    public void initdownPopwindow(View view){

        popupdown=new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupdown.setBackgroundDrawable(new ColorDrawable(Color.argb(255,255,255,255)));
        popupdown.setAnimationStyle(R.style.take_photo_anim);
        popupdown.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=mwindow.getAttributes();
                lp.alpha=1;
                mwindow.setAttributes(lp);
            }
        });
    }
    @Override
    public int getCount() {
        return list.size();
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
        TextView Tv_num_top;
        TextView Tv_musictitle_chars;
        ImageView Iv_musicicon_chars;
        TextView Tv_musicatr_chars;
        TextView Tv_musiczj_chars;
        ImageButton Ib_music_more_chars;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       ViewHolder holder = null;
        if(convertView==null){
            convertView=LayoutInflater.from(mContext).inflate(R.layout.charstop_item,null);
            holder=new ViewHolder();
            holder.Tv_num_top=(TextView)convertView.findViewById(R.id.tv_num_top);
            holder.Tv_musictitle_chars=(TextView)convertView.findViewById(R.id.tv_musictitle_chars);
            holder.Tv_musicatr_chars=(TextView)convertView.findViewById(R.id.tv_musicatr_chars);
            holder.Tv_musiczj_chars=(TextView)convertView.findViewById(R.id.tv_musiczj_chars);
            holder.Iv_musicicon_chars=(ImageView) convertView.findViewById(R.id.iv_musicicon_chars);
            holder.Ib_music_more_chars=(ImageButton)convertView.findViewById(R.id.ib_music_more_chars);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder) convertView.getTag();
        }
        final String named=list.get(position).getSongName();
        holder.Tv_num_top.setText(position+"");
        holder.Tv_musictitle_chars.setText(list.get(position).getSongName());
        holder.Tv_musicatr_chars.setText(list.get(position).getSongArtist());
        holder.Tv_musiczj_chars.setText(list.get(position).getSongAlbum());
        holder.Ib_music_more_chars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=LayoutInflater.from(mContext).inflate(R.layout.downloadpopwindow,null);
                TextView Tv_title_pop=(TextView)view.findViewById(R.id.tv_title_pop);
                TextView Tv_next_pop=(TextView)view.findViewById(R.id.tv_next_pop);
                TextView Tv_songger_pop=(TextView)view.findViewById(R.id.tv_songger_pop);
                TextView Tv_alumb_pop=(TextView)view.findViewById(R.id.tv_alumb_pop);
                TextView Tv_download_pop=(TextView)view.findViewById(R.id.tv_download_pop);
                Tv_download_pop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        View view=LayoutInflater.from(mContext).inflate(R.layout.downloadlist,null);
                        ListView Lv_down=(ListView)view.findViewById(R.id.lv_down);
                        TextView Tv_num_ti=(TextView)view.findViewById(R.id.tv_num_ti);
                        final DownloadAdapter adapter=new DownloadAdapter(named,mContext,Tv_num_ti);
                        Lv_down.setAdapter(adapter);
                        Lv_down.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String songid=adapter.songid(position);
                                String songna=adapter.songname(position);
                                Intent intent=new Intent(mContext, DownloadService.class);
                                intent.putExtra("songid",songid);
                                intent.putExtra("songname",songna);
                                mContext.startService(intent);
                                Toast.makeText(mContext,"正在下载。。",Toast.LENGTH_LONG).show();
                                popupdown.dismiss();
                            }
                        });
                        initdownPopwindow(view);
                        popupdown.setFocusable(true);
                        popupdown.setOutsideTouchable(true);
                        popupdown.showAtLocation(v,0,0,HightWindow);
                        WindowManager.LayoutParams lp=mwindow.getAttributes();
                        lp.alpha=0.5f;
                        mwindow.setAttributes(lp);

                    }
                });
                Tv_title_pop.setText(list.get(position).getSongName());
                Tv_songger_pop.setText("歌手"+list.get(position).getSongArtist());
                Tv_alumb_pop.setText("专辑"+list.get(position).getSongAlubmid());;
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
}
