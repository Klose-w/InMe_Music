package com.example.wade.inme.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
 * Created by wade on 2017/6/21.
 */

public class NewsAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    List<NewsBean> newsList;
    Context mContext;
    private String showapi_appid = "39401";
    private String showapi_sign = "3e47b57274ff414f81e76f624a6a1231";
    String[] AllId=new String[]{"","5572a108b3cdc86cf39001cd","5572a108b3cdc86cf39001ce",
            "5572a108b3cdc86cf39001cf","5572a108b3cdc86cf39001d4",
            "5572a10ab3cdc86cf39001e7","5572a108b3cdc86cf39001d5",
            "5572a108b3cdc86cf39001d0","5572a109b3cdc86cf39001da","5572a108b3cdc86cf39001d7",
            "5572a108b3cdc86cf39001d1","5572a108b3cdc86cf39001d2","5572a108b3cdc86cf39001d6",
            "5572a108b3cdc86cf39001d8","5572a108b3cdc86cf39001d9",
            "5572a109b3cdc86cf39001dc","5572a109b3cdc86cf39001dd"};
    private OnItemClickListener onItemClickListener=null;
    int cc=0;
    ViewnewsPager viewnewsPager;
    Handler handler1;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==1&&viewnewsPager!=null){
                Vr_news_title.setCurrentItem(cc%100);
                cc++;
            }
        }
    };
    public NewsAdapter(Context context,Handler handler1){
        this.handler1=handler1;
        mContext=context;
        newsList=new ArrayList<>();
        getnews("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    Message msg=new Message();
                    msg.arg1=1;
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public void getnews(String id){
        String url1="http://route.showapi.com/109-35?showapi_appid="+showapi_appid+"&channelId="+id+"&channelName=&title=&page=1&needContent=1&needHtml=1&needAllList=1&maxResult=50&showapi_sign="+showapi_sign;
        RequestQueue queue = Volley.newRequestQueue(mContext);
        newsList.clear();
        JsonObjectRequest sr = new JsonObjectRequest(url1, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("news", jsonObject.toString());
                try {
                    jsonObject=jsonObject.getJSONObject("showapi_res_body");
                    Log.e("news", jsonObject.toString());
                    jsonObject=jsonObject.getJSONObject("pagebean");
                    Log.e("news", jsonObject.toString());
                    JSONArray jsonArray=jsonObject.getJSONArray("contentlist");
                    for(int i=0;i<jsonArray.length();i++){
                        NewsBean newsBean=new NewsBean();
                        JSONObject j=(JSONObject)jsonArray.get(i);
                        newsBean.setId(j.getString("id"));
                        newsBean.setTitle(j.getString("title"));
                        newsBean.setDesc(j.getString("html"));
                        newsBean.setComeform(j.getString("source"));
                        newsBean.setPubDate(j.getString("pubDate"));
                        newsBean.setChannelName(j.getString("channelName"));
                        newsBean.setChannelId(j.getString("channelId"));
                        newsBean.setLink(j.getString("link"));
                        JSONArray jj=j.getJSONArray("imageurls");
                        String[] Image=new String[10];
                        for(int k=0;k<jj.length();k++){
                            JSONObject jjj=(JSONObject)jj.get(k);
                            Image[k]=jjj.getString("url").toString().replaceAll("////","");
                            if(k==9){
                                break;
                            }
                        }
                        newsBean.setImage(Image);
                        newsList.add(newsBean);
                    }
                    notifyDataSetChanged();
                    Message msg=new Message();
                    msg.arg1=11;
                    handler1.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("newsnews", e.toString());
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
        if(viewType==0){
            View view= LayoutInflater.from(mContext).inflate(R.layout.newsviewtitle,null);
            view.setOnClickListener(this);
            return new MycardviewHolder(view);
        }else {
            View view= LayoutInflater.from(mContext).inflate(R.layout.newsitem,null);
            view.setOnClickListener(this);
            return new MycardviewHolder1(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position>0){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager=recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager){
            GridLayoutManager gridLayoutManager=((GridLayoutManager)manager);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
                @Override
                public int getSpanSize(int position) {
                    int type=getItemViewType(position);
                    switch (type){
                        case 0:
                            return 2;
                        case 1:
                            return 1;
                        default:
                            return 1;
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MycardviewHolder){
            MycardviewHolder mycardviewHolder=(MycardviewHolder)holder;
            //mycardviewHolder.Tv_card_name.setText(title[position]);
            mycardviewHolder.itemView.setTag(position);
        }else {
            MycardviewHolder1 mycardviewHolder1=(MycardviewHolder1)holder;
            mycardviewHolder1.Tv_news_tv.setText(newsList.get(position-1).getTitle());
            Glide.with(mContext).load(newsList.get(position-1).getImage()[0]).into(mycardviewHolder1.Iv_news_ima);
            mycardviewHolder1.itemView.setTag(position);
        }

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }
    @Override
    public int getItemCount() {
        return newsList.size()+1;
    }
    ViewPager Vr_news_title;
    RecyclerView Rv_list_yl;
    public class MycardviewHolder extends RecyclerView.ViewHolder{
        public MycardviewHolder(View itemView) {
            super(itemView);
            Vr_news_title=(ViewPager) itemView.findViewById(R.id.vr_news_title);
            Rv_list_yl=(RecyclerView)itemView.findViewById(R.id.rv_list_yl);
            LinearLayoutManager mm=new LinearLayoutManager(mContext);
            mm.setOrientation(LinearLayoutManager.HORIZONTAL);
            Rv_list_yl.setLayoutManager(mm);
            final HorReclcyAdapter ha=new HorReclcyAdapter(mContext);
            ha.setOnItemClickListener(new HorReclcyAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    ha.shuaxin(position);
                    getnews(AllId[position]);
                }
            });
            Rv_list_yl.setAdapter(ha);
            viewnewsPager=new ViewnewsPager();
            Vr_news_title.setAdapter(viewnewsPager);

        }
    }

    public NewsBean getBean(int i){
        return newsList.get(i);
    }
    public class MycardviewHolder1 extends RecyclerView.ViewHolder{
        ImageView Iv_news_ima;
        TextView Tv_news_tv;
        public MycardviewHolder1(View itemView) {
            super(itemView);
            Tv_news_tv=(TextView)itemView.findViewById(R.id.tv_news_tv);
            Iv_news_ima=(ImageView)itemView.findViewById(R.id.iv_news_ima);
        }
    }

    public class ViewnewsPager extends PagerAdapter{
        int[] Im=new int[]{R.drawable.tp_sh,R.drawable.tp_cj,R.drawable.tp_js,R.drawable.tp_ty,R.drawable.tp_yl};
        String[] st=new String[]{"聚焦最新社会动态","把握当前热门经济","军事专家为你解读国际形势","了解最新比赛资讯","娱乐八卦抢先看"};
        View[] image=new View[5];
        public ViewnewsPager(){
            for(int i=0;i<5;i++){
                image[i]=LayoutInflater.from(mContext).inflate(R.layout.news_pager_item,null);
                ImageView iv=(ImageView)image[i].findViewById(R.id.iv_news_tit);
                TextView tv=(TextView)image[i].findViewById(R.id.tv_jiesao_news);
                Glide.with(mContext).load(Im[i]).into(iv);
                tv.setText(st[i]);
            }
        }
        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(image[position%5]);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //container.addView(listplay.get(position));
            ViewGroup vv=(ViewGroup)image[position%5].getParent();
            if(vv!=null){
                vv.removeAllViews();
            }
            container.addView(image[position%5]);
            return image[position%5];
        }
    }

}
