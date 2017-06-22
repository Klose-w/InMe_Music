package com.example.wade.inme.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wade.inme.JavaBean.musicbean;
import com.example.wade.inme.R;
import com.example.wade.inme.Service.DownloadMusicService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wade on 2017/6/15.
 */

public class DownloadAdapter extends BaseAdapter {
    List<musicbean> list=new ArrayList<musicbean>();
    Context mContext;

    public List<musicbean> getList() {
        return list;
    }

    public void setList(List<musicbean> list) {
        this.list = list;
    }

    TextView textView;

    public DownloadAdapter(String name, Context context,TextView textView){
        mContext=context;
        this.textView=textView;
        downmusic(name);
    }
    public String songid(int i){
        return list.get(i).getSingid();
    }
    public String songname(int i){
        return list.get(i).getSname();
    }
    public void downmusic(final String name) {
        String url1;
        url1 = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.catalogSug&format=json&query=" + name;
       // Log.e("kk", url1);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest sr = new JsonObjectRequest(url1, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //Log.e("json", jsonObject.toString());
                JSONArray jsonArray = null;
                try {
                    String songid;
                    jsonArray = jsonObject.getJSONArray("song");
                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        jsonObject1.getString("songid");
                        musicbean m=new musicbean(jsonObject1.getString("songid"),jsonObject1.getString("songname"),jsonObject1.getString("artistname"));
                        Log.e("yyyty",jsonObject1.getString("songid")+jsonObject1.getString("songname")+jsonObject1.getString("artistname"));
                        list.add(m);
                    }
                    textView.setText("相似歌曲:共"+list.size()+"首");
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
        TextView Tv_downname_pop;
        TextView Tv_downart_pop;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder = null;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.downpop_item,null);
            holder=new ViewHolder();
            holder.Tv_downname_pop=(TextView)convertView.findViewById(R.id.tv_downname_pop);
            holder.Tv_downart_pop=(TextView)convertView.findViewById(R.id.tv_downart_pop);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder) convertView.getTag();
        }
        if(list.size()!=0){
            holder.Tv_downname_pop.setText(list.get(position).getSname());
            holder.Tv_downart_pop.setText(list.get(position).getArtistname());
        }
        return convertView;
    }

}
