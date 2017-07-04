package com.example.wade.inme.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.wade.inme.Adapter.DiantaiAdapter;
import com.example.wade.inme.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dt_four_ extends Fragment {
    SwipeRefreshLayout Sr_dt1;
    int lev;
    String title;
    String Url;
    GridView Gv_dt_4;
    ImageButton ib_backmain_dt;
    TextView tv_dt_tt;
    List<Dtbean> dtbeenlist;
    String Id;
    DtPager dtPager;
    String name;
    FragmentManager mfragmentManager;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==11){
                if(Sr_dt1.isRefreshing()==true){
                    Sr_dt1.setRefreshing(false);
                }
                if(msg.arg2==17){
                    //Toast.makeText(getContext(),"刷新失败,请检查网络",Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    public Dt_four_(int i,String title,String Id,String name) {
        // Required empty public constructor
        lev=i;
        this.title=title;
        this.Id=Id;
        this.name=name;
        if(lev==3){
            Url="http://mobile.ximalaya.com/m/explore_album_list?category_name="+name+"&condition=hot&device=iPhone&page=1&per_page=30&status=0&tag_name=";
        }else if(lev==0){
            Url="http://mobile.ximalaya.com/m/subject_detail?device=android&id="+Id;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_dt_four_, container, false);
        Gv_dt_4=(GridView)view.findViewById(R.id.gv_dt_4);
        Sr_dt1=(SwipeRefreshLayout)view.findViewById(R.id.sr_dt1);
        Sr_dt1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg=new Message();
                        msg.arg1=11;
                        msg.arg2=17;
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        });
        ib_backmain_dt=(ImageButton)view.findViewById(R.id.ib_backmain_dt);
        tv_dt_tt=(TextView)view.findViewById(R.id.tv_dt_tt);
        tv_dt_tt.setText(title);
        mfragmentManager=getActivity().getSupportFragmentManager();
        ib_backmain_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fm_main, new MainFragment(1,2));
                transaction.commit();
            }
        });
        initData();
        return view;
    }
    public void initData(){
        Log.e("urld",Url);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest sr = new JsonObjectRequest(Url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(lev==3){
                    Onejiexi(jsonObject);
                }else if(lev==0){
                    Onejiexi1(jsonObject);
                }
                dtPager=new DtPager();
                Gv_dt_4.setAdapter(dtPager);
                Message msg=new Message();
                msg.arg1=11;
                handler.sendMessage(msg);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(sr);
    }
    public void Onejiexi1(JSONObject jsonObject){
        try {
            JSONArray jsonArray=jsonObject.getJSONArray("list");
            dtbeenlist=new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jj=jsonArray.getJSONObject(i);
                Dtbean dtbean=new Dtbean();
                dtbean.setNicknaem(jj.getString("title"));
                dtbean.setCoverMiddle(jj.getString("albumCoverUrl290"));
                dtbean.setAlbunId(jj.getString("albumId"));
                dtbeenlist.add(dtbean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ee",e.toString());
        }
    }
    public void Onejiexi(JSONObject jsonObject){
        try {
            JSONArray jsonArray=jsonObject.getJSONArray("list");
            dtbeenlist=new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jj=jsonArray.getJSONObject(i);
                Dtbean dtbean=new Dtbean();
                dtbean.setNicknaem(jj.getString("nickname"));
                dtbean.setCoverMiddle(jj.getString("coverMiddle"));
                dtbean.setAlbunId(jj.getString("albumId"));
                dtbeenlist.add(dtbean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ee",e.toString());
        }
    }
    public class Dtbean{
        String Id;
        String Intro;
        String Nicknaem;
        String CoverMiddle;
        String Title;
        String Tags;
        String LastUptrackAt;
        String AlbunId;
        String Uid;

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getIntro() {
            return Intro;
        }

        public void setIntro(String intro) {
            Intro = intro;
        }

        public String getNicknaem() {
            return Nicknaem;
        }

        public void setNicknaem(String nicknaem) {
            Nicknaem = nicknaem;
        }

        public String getCoverMiddle() {
            return CoverMiddle;
        }

        public void setCoverMiddle(String coverMiddle) {
            CoverMiddle = coverMiddle;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getTags() {
            return Tags;
        }

        public void setTags(String tags) {
            Tags = tags;
        }

        public String getLastUptrackAt() {
            return LastUptrackAt;
        }

        public void setLastUptrackAt(String lastUptrackAt) {
            LastUptrackAt = lastUptrackAt;
        }

        public String getAlbunId() {
            return AlbunId;
        }

        public void setAlbunId(String albunId) {
            AlbunId = albunId;
        }

        public String getUid() {
            return Uid;
        }

        public void setUid(String uid) {
            Uid = uid;
        }
    }
    public class DtPager extends BaseAdapter {
        @Override
        public int getCount() {
            return dtbeenlist.size();

        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        class ViewHolder {
            ImageView iv_into_4_;
            TextView tv_into_4;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                convertView=LayoutInflater.from(getContext()).inflate(R.layout.dt_item_into_4,null);
                holder=new ViewHolder();
                holder.iv_into_4_=(ImageView)convertView.findViewById(R.id.iv_into_4_);
                holder.tv_into_4=(TextView) convertView.findViewById(R.id.tv_into_4);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder)convertView.getTag();
            }
            //Glide.with(mContext).load(twoBean.get(position).getCoverMiddle()).into(holder.Iv_dt_list);
            holder.tv_into_4.setText(dtbeenlist.get(position).getNicknaem());
            Glide.with(getContext()).load(dtbeenlist.get(position).getCoverMiddle()).into(holder.iv_into_4_);
            holder.iv_into_4_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction transaction = mfragmentManager.beginTransaction();
                    transaction.replace(R.id.fm_main, new Diantai_into_Fragment(1,dtbeenlist.get(position).getAlbunId()));
                    transaction.commit();
                }
            });
            return convertView;
        }
    }

}
