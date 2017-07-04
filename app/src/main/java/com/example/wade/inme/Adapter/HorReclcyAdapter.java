package com.example.wade.inme.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.wade.inme.JavaBean.NewsBean;
import com.example.wade.inme.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**

 "channelId": "5572a108b3cdc86cf39001cd",
 "name": "国内焦点"
 },
 {
 "channelId": "5572a108b3cdc86cf39001ce",
 "name": "国际焦点"

 "channelId": "5572a108b3cdc86cf39001cf",
 "name": "军事焦点"

 "channelId": "5572a108b3cdc86cf39001d0",
 "name": "财经焦点"

 "channelId": "5572a108b3cdc86cf39001d1",
 "name": "互联网焦点"

 "channelId": "5572a108b3cdc86cf39001d2",
 "name": "房产焦点"

 "channelId": "5572a108b3cdc86cf39001d3",
 "name": "汽车焦点"

 "channelId": "5572a108b3cdc86cf39001d4",
 "name": "体育焦点"

 "channelId": "5572a108b3cdc86cf39001d5",
 "name": "娱乐焦点"

 "channelId": "5572a108b3cdc86cf39001d6",
 "name": "游戏焦点"

 "channelId": "5572a108b3cdc86cf39001d7",
 "name": "教育焦点"

 "channelId": "5572a108b3cdc86cf39001d8",
 "name": "女人焦点"

 "channelId": "5572a108b3cdc86cf39001d9",
 "name": "科技焦点"

 "channelId": "5572a109b3cdc86cf39001da",
 "name": "社会焦点"

 "channelId": "5572a109b3cdc86cf39001db",
 "name": "国内最新"

 "channelId": "5572a109b3cdc86cf39001dc",
 "name": "台湾最新"

 "channelId": "5572a109b3cdc86cf39001dd",
 "name": "港澳最新"

 "channelId": "5572a109b3cdc86cf39001de",
 "name": "国际最新"

 "channelId": "5572a109b3cdc86cf39001df",
 "name": "军事最新"

 "channelId": "5572a109b3cdc86cf39001e0",
 "name": "财经最新"

 "channelId": "5572a109b3cdc86cf39001e1",
 "name": "理财最新"

 "channelId": "5572a109b3cdc86cf39001e2",
 "name": "宏观经济最新"

 "channelId": "5572a109b3cdc86cf39001e3",
 "name": "互联网最新"

 "channelId": "5572a109b3cdc86cf39001e4",
 "name": "房产最新"

 "channelId": "5572a109b3cdc86cf39001e5",
 "name": "汽车最新"

 "channelId": "5572a109b3cdc86cf39001e6",
 "name": "体育最新"

 "channelId": "5572a10ab3cdc86cf39001e7",
 "name": "国际足球最新"


 }
 ].
 */

public class HorReclcyAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    List<NewsBean> newsList;
    Context mContext;
    int ii=0;
    String[] tit=new String[]{"全部","国内","国际","军事","体育","国际足球","娱乐","财经","社会","教育","互联网","房产","游戏","女人","科技",
            "台湾","港澳"};

    private OnItemClickListener onItemClickListener=null;
    public HorReclcyAdapter(Context context){
        mContext=context;

    }
    @Override
    public void onClick(View v) {
        if(onItemClickListener!=null){
            onItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public static interface OnItemClickListener{
        void onItemClick(View v,int position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_card_news,null);
        view.setOnClickListener(this);
        return new MycardviewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MycardviewHolder mycardviewHolder=(MycardviewHolder)holder;
            //mycardviewHolder.Tv_card_name.setText(title[position]);
            mycardviewHolder.itemView.setTag(position);
            mycardviewHolder.tv.setText(tit[position]);
        if(position==ii){
            mycardviewHolder.tv.setTextColor(Color.RED);
        }else {
            mycardviewHolder.tv.setTextColor(Color.GRAY);
        }
    }

    public void shuaxin(int i){
        ii=i;
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }
    @Override
    public int getItemCount() {
        return tit.length;
    }
    public class MycardviewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public MycardviewHolder(View itemView) {
            super(itemView);
             tv=(TextView) itemView.findViewById(R.id.tv_tt);
        }
    }

}
