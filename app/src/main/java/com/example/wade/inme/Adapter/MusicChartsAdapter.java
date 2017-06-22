package com.example.wade.inme.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.Network.OkHttpRequest;
import com.example.wade.inme.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.Inflater;


/**
 * Created by Lenovo on 2017/6/6.
 */

public class MusicChartsAdapter extends BaseAdapter{
    private String showapi_appid = "39401";
    private String showapi_sign = "3e47b57274ff414f81e76f624a6a1231";
    private String url;
    int[] ints = {4,27,28,26,36};
    LayoutInflater mInflater;
    private Context context;
    JSONObject jsonObject;
    private ArrayList<String> strings;
    int Image[]={R.drawable.ranklist_first,R.drawable.ranklist_second,R.drawable.ranklist_third,R.drawable.ranklist_fourth,R.drawable.ranklist_five};
    List<Map<String,Object>> mapList;
    Handler handler1;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String message;
            Log.e("gg","message");
           //Log.e("ok",message);
            try {
                for(int k=0;k<5;k++){
                    message = bundle.getString("message"+k);
                    jsonObject = new JSONObject(message);
                    //System.out.println(jsonObject);
                    JSONArray jsonArray = jsonObject.getJSONObject("showapi_res_body").getJSONObject("pagebean").getJSONArray("songlist");
                    Log.e("gg",message);
                    Map<String,Object> map=new HashMap<String,Object>();
                    for (int i = 0; i < 3; i++) {
                        JSONObject jsona = (JSONObject) jsonArray.get(i);
                        map.put(i+"",jsona.getString("singername")+"-"+jsona.getString("songname"));
                    }
                    mapList.add(map);
                }
                notifyDataSetChanged();
                Message messag1=new Message();
                messag1.arg1=11;
                handler1.sendMessage(messag1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    public MusicChartsAdapter(Context context,Handler handler1){
        super();
        this.context = context;
        this.handler1=handler1;
        mInflater=LayoutInflater.from(context);
        mapList=new ArrayList<Map<String,Object>>();
        //url = "http://route.showapi.com/213-4?showapi_appid="+showapi_appid+"&topid="+ints[i]+"&showapi_sign="+showapi_sign;
        new OkHttpRequest(handler).start();
       // Log.e("ok",url);
        //}


    }


    public MusicChartsAdapter(Context context, ArrayList<String> strings){
        super();
        this.context = context;
        this.strings = strings;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 5;
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
        TextView Tv_top1_music;
        TextView Tv_top2_music;
        TextView Tv_top3_music;
        ImageView Iv_title_ima;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.item_musiccharts,null);
            holder=new ViewHolder();
            holder.Tv_top1_music=(TextView) convertView.findViewById(R.id.tv_top1_music);
            holder.Tv_top2_music=(TextView) convertView.findViewById(R.id.tv_top2_music);
            holder.Tv_top3_music=(TextView) convertView.findViewById(R.id.tv_top3_music);
            holder.Iv_title_ima=(ImageView) convertView.findViewById(R.id.iv_title_ima);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.Iv_title_ima.setBackgroundResource(Image[position]);
        if(mapList.size()!=0){
            holder.Tv_top1_music.setText("1 "+mapList.get(position).get("0"));
            holder.Tv_top2_music.setText("2 "+mapList.get(position).get("1"));
            holder.Tv_top3_music.setText("3 "+mapList.get(position).get("2"));
        }

        return convertView;
    }
        //TextView textView = new TextView(context);
        //textView.setTextSize(50);
        //textView.setText(strings.get(position));

}
