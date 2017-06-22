package com.example.wade.inme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.JavaBean.musicbean;
import com.example.wade.inme.R;
import com.example.wade.inme.Service.DownloadMusicService;
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
 * Created by wade on 2017/6/12.
 */

public class CharstopAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    Context mContext;
    int Num;
    List<MusicInfor> musiclist;
    PopupWindow popupWindow;
    PopupWindow popupdown;
    int HightWindow;
    WindowManager wm;
    private String url;
    OkHttpClient client = new OkHttpClient();
    private JSONObject jsonObject1;
    private String showapi_appid = "39401";
    private String showapi_sign = "3e47b57274ff414f81e76f624a6a1231";
    int[] ints = {4,27,32,26,36,5,6,3,17,16,28};
    Window mwindow;
    InMeApplicacation inMeApplicacation;
    public CharstopAdapter(Context context, int Num, final Handler handler, Window window){
        mContext=context;
        mwindow=window;
        url = "http://route.showapi.com/213-4?showapi_appid="+showapi_appid+"&topid="+ints[Num-1]+"&showapi_sign="+showapi_sign;
        mInflater= LayoutInflater.from(mContext);;
        this.Num=Num;
        musiclist=new ArrayList<MusicInfor>();
        wm=(WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        HightWindow=wm.getDefaultDisplay().getHeight();
        inMeApplicacation=(InMeApplicacation)mContext.getApplicationContext();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    jsonObject1 = new JSONObject(jsonObject.toString());
                    //System.out.println(jsonObject);
                    JSONArray jsonArray = jsonObject1.getJSONObject("showapi_res_body").getJSONObject("pagebean").getJSONArray("songlist");
                    System.out.println(jsonArray);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsona=(JSONObject) jsonArray.get(i);
                        MusicInfor onlinSongs = new MusicInfor(
                                jsona.getString("songname"),
                                jsona.getString("songid"),
                                jsona.getInt("seconds"),
                                jsona.getString("singerid"),
                                jsona.getString("albumpic_small"),
                                jsona.getString("downUrl"),
                                jsona.getString("url"),
                                jsona.getString("singername"),
                                jsona.getString("singerid")
                        );

                        musiclist.add(onlinSongs);
                    }
                    Message msg=new Message();
                    msg.arg1=11;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
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
    public int getnum(){
        return musiclist.size();
    }
    public List<MusicInfor> getMusiclist(){
        return musiclist;
    }
    @Override
    public int getCount() {
        return musiclist.size();
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
            convertView=mInflater.inflate(R.layout.charstop_item,null);
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
        if(musiclist.size()!=0){
            holder.Tv_num_top.setText(position+1+"");
            if(position<3){
                holder.Tv_num_top.setTextColor(Color.RED);
            }else {
                holder.Tv_num_top.setTextColor(Color.GRAY);
            }
            final String named=musiclist.get(position).getSongName();
            holder.Tv_musictitle_chars.setText(musiclist.get(position).getSongName());
            holder.Tv_musicatr_chars.setText(musiclist.get(position).getSongArtist());
            holder.Tv_musiczj_chars.setText(musiclist.get(position).getSongAlubmid());
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
                    Tv_title_pop.setText(musiclist.get(position).getSongName());
                    Tv_songger_pop.setText("歌手"+musiclist.get(position).getSongArtist());
                    Tv_alumb_pop.setText("专辑"+musiclist.get(position).getSongAlubmid());;
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
        }

        return convertView;
    }

}
