package com.example.wade.inme.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.wade.inme.Fragment.Diantai_into_Fragment;
import com.example.wade.inme.Fragment.Dt_four_;
import com.example.wade.inme.Fragment.LocalMusicFragment;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.MusicInfor;
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

public class DiantaiAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    String[] title = new String[]{"剑魂", "李一桐", "向天再借", "真正的快乐", "周杰伦", "小小天涯", "再见了朋友", "何处不古风", "知了电台", "射雕英雄传"};
    String[] search = new String[]{"洛阳夜雨", "再逢明月照九州", "猪八戒", "射雕英雄传", "李一桐"};
    Context mContext;
    String url1="http://mobile.ximalaya.com/m/super_explore_index2?channel=ios-b1&device=iPhone&includeActivity=true&picVersion=9&scale=3&version=3.1.43";
    private OnItemClickListener onItemClickListener = null;
    List<TitleBean> titleBean =new ArrayList<>();
    ViewdtPager viewdtPager1;
    ListdtPager listdaPager;
    Listdt2Pager listdt2Pager;
    Listdt2Pager listdt2Pager1;
    int finalI=0;
    List<TwoBean> twoBean=new ArrayList<>();
    List<ThreeBean> threeBean=new ArrayList<>();
    List<FourBean> fourBeen=new ArrayList<>();
    List<MusicInfor> threeBean1s=new ArrayList<>();
    InMeApplicacation inMeApplicacation;
    int biaoji3=0;
    int cc=0;
    FragmentManager mfragmentManager;
    MycardviewHolder mycardviewHolder;
    Handler handler1;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==1){
                if(mycardviewHolder!=null){
                    mycardviewHolder.getR().setCurrentItem(cc%100);
                    cc=mycardviewHolder.getR().getCurrentItem();
                    cc++;
                }

            }
        }
    };
    public DiantaiAdapter(Context context,FragmentManager f,Handler handler1) {
        this.handler1=handler1;
        mContext = context;
        mfragmentManager=f;
        inMeApplicacation=(InMeApplicacation)mContext.getApplicationContext();
        initData();
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

    public void initData(){
        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest sr = new JsonObjectRequest(url1, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                initTwoPager(jsonObject);
                initTitlePager(jsonObject);
                initFourPager(jsonObject);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        JsonObjectRequest sr1 = new JsonObjectRequest("http://mobile.ximalaya.com/m/explore_track_list?category_name=all&condition=daily&device=android&page=2&per_page=33&tag_name=", null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                initThreePager(jsonObject);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(sr);
        queue.add(sr1);
    }
    public void initTitlePager(JSONObject jsonObject){
        titleBean.clear();
        try {
            jsonObject=jsonObject.getJSONObject("focusImages");
            Log.e("news", jsonObject.toString());
            JSONArray jj=jsonObject.getJSONArray("list");
            for(int i=0;i<jj.length();i++){
                JSONObject j1=jj.getJSONObject(i);
                TitleBean title=new TitleBean();
                title.setId(j1.getString("id"));
                title.setShortTitle(j1.getString("shortTitle"));
                title.setPic(j1.getString("pic").replaceAll("////",""));
                title.setType(j1.getString("type"));
                if(j1.getString("type").equals("9")){
                    title.setSpecialId(j1.getString("specialId"));
                }
                //title.setUid(j1.getString("uid"));
                //title.setTrackId(j1.getString("trackId"));
                //title.setRoomId(j1.getString("roomId"));
                titleBean.add(title);
            }
            viewdtPager1.notifyDataSetChanged();
            notifyDataSetChanged();
            Message msg=new Message();
            msg.arg1=11;
            handler1.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("newsnews", e.toString());
        }
    }
    public void initTwoPager(JSONObject jsonObject){
        twoBean.clear();
        try {
            jsonObject=jsonObject.getJSONObject("recommendAlbums");
            Log.e("news", jsonObject.toString());
            JSONArray jj=jsonObject.getJSONArray("list");
            for(int i=0;i<jj.length();i++){
                JSONObject j1=jj.getJSONObject(i);
                TwoBean title=new TwoBean();
                title.setId(j1.getString("id"));
                title.setAlbunId(j1.getString("albumId"));
                title.setUid(j1.getString("uid"));
                title.setIntro(j1.getString("intro"));
                title.setNicknaem(j1.getString("nickname"));
                title.setCoverMiddle(j1.getString("coverMiddle"));
                title.setTitle(j1.getString("title"));
                title.setTags(j1.getString("tags"));
                twoBean.add(title);
            }
            listdaPager.notifyDataSetChanged();
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("newsnews", e.toString());
        }
    }
    public void initThreePager(JSONObject jsonObject){
        threeBean1s.clear();
        try {
            Log.e("news", jsonObject.toString());
            JSONArray jj=jsonObject.getJSONArray("list");
            for(int i=0;i<jj.length();i++){
                JSONObject j1=jj.getJSONObject(i);
                MusicInfor m=new MusicInfor(j1.getString("title"),j1.getString("trackId"),j1.getInt("playsCounts")/1000,j1.getString("id"),
                        j1.getString("coverSmall"),"",j1.getString("playPath64"),j1.getString("nickname"),j1.getString("albumId"));

                threeBean1s.add(m);
            }
            //listdaPager.notifyDataSetChanged();
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("newsnews", e.toString());
        }
    }
    public void initFourPager(JSONObject jsonObject){
        fourBeen.clear();
        try {
            jsonObject=jsonObject.getJSONObject("categories");
            Log.e("news", jsonObject.toString());
            JSONArray jj=jsonObject.getJSONArray("data");
            for(int i=1;i<jj.length();i++){
                JSONObject j1=jj.getJSONObject(i);
                FourBean title=new FourBean();
                title.setId(j1.getString("id"));
                title.setName(j1.getString("name"));
                title.setTitle(j1.getString("title"));
                title.setOrderNum(j1.getString("orderNum"));
                title.setCoverPath(j1.getString("coverPath"));
                title.setCategoryType(j1.getString("categoryType"));
                title.setContentType(j1.getString("contentType"));
                fourBeen.add(title);
            }
            if(listdt2Pager!=null){
                listdt2Pager.notifyDataSetChanged();
            }
            if(listdt2Pager1!=null){
                listdt2Pager1.notifyDataSetChanged();
            }
            //listdt2Pager.notifyDataSetChanged();
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("newsnews", e.toString());
        }
    }
    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.diantai_viewpager, null);
            view.setOnClickListener(this);
            return new MycardviewHolder(view);
        } else if(viewType==1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.diantai_item2, null);
            view.setOnClickListener(this);
            return new MycardviewHolder1(view);
        }else if(viewType==2){
            View view = LayoutInflater.from(mContext).inflate(R.layout.dt_three_item, null);
            view.setOnClickListener(this);
            return new MycardviewHolder2(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.dt_four_item, null);
            view.setOnClickListener(this);
            return new MycardviewHolder3(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if(position==1){
            return 1;
        }else if(position<12){
            return  2;
        }else {
            return  3;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = ((GridLayoutManager) manager);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type) {
                        case 0:
                            return 3;
                        case 1:
                            return 3;
                        default:
                            return 3;
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MycardviewHolder) {
            mycardviewHolder = (MycardviewHolder) holder;
            viewdtPager1=new ViewdtPager();
            mycardviewHolder.Vp_title_tp.setAdapter(viewdtPager1);
            mycardviewHolder.ll_dt1_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listdt2Pager.notifyDataSetChanged();
                }
            });
            mycardviewHolder.ll_dt1_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listdaPager.notifyDataSetChanged();
                }
            });
            mycardviewHolder.itemView.setTag(position);
        } else if(holder instanceof MycardviewHolder1){
            MycardviewHolder1 mycardviewHolder1;
            mycardviewHolder1 = (MycardviewHolder1) holder;
            listdaPager=new ListdtPager();
            mycardviewHolder1.Lv_dt_list.setAdapter(listdaPager);
            mycardviewHolder1.Tv_hyh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    biaoji3++;
                    if(biaoji3>=(twoBean.size()/3)){
                        biaoji3=0;
                    }
                    listdaPager.notifyDataSetChanged();
                }
            });
            mycardviewHolder1.itemView.setTag(position);
        }else if(holder instanceof MycardviewHolder2){
            MycardviewHolder2 mycardviewHolder2;
            mycardviewHolder2 = (MycardviewHolder2) holder;
            final int ss=position-2;
            if(threeBean1s.size()!=0){
                mycardviewHolder2.Tv_dt3_title.setText("热门声音");
                mycardviewHolder2.Tv_dt3_1.setText(threeBean1s.get(ss*3).getSongName());
                mycardviewHolder2.Tv_dt3_2.setText(threeBean1s.get(ss*3+1).getSongName());
                mycardviewHolder2.Tv_dt3_3.setText(threeBean1s.get(ss*3+2).getSongName());

                Glide.with(mContext).load(threeBean1s.get(ss*3).getSongbitmap()).into(mycardviewHolder2.Iv_dt3_1);
                Glide.with(mContext).load(threeBean1s.get(ss*3+1).getSongbitmap()).into(mycardviewHolder2.Iv_dt3_2);
                Glide.with(mContext).load(threeBean1s.get(ss*3+2).getSongbitmap()).into(mycardviewHolder2.Iv_dt3_3);
                mycardviewHolder2.Iv_dt3_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inMeApplicacation.setPlaymusicList(threeBean1s);
                        inMeApplicacation.setNowplay(ss*3);
                    }
                });
                mycardviewHolder2.Iv_dt3_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inMeApplicacation.setPlaymusicList(threeBean1s);
                        inMeApplicacation.setNowplay(ss*3+1);
                    }
                });
                mycardviewHolder2.Iv_dt3_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inMeApplicacation.setPlaymusicList(threeBean1s);
                        inMeApplicacation.setNowplay(ss*3+2);
                    }
                });
            }

            mycardviewHolder2.itemView.setTag(position);
        }else {
            MycardviewHolder3 mycardviewHolder3;
            mycardviewHolder3 = (MycardviewHolder3) holder;
            listdt2Pager=new Listdt2Pager();
            mycardviewHolder3.Lv_dt4_1.setAdapter(listdt2Pager);
            listdt2Pager1=new Listdt2Pager(3);
            mycardviewHolder3.Lv_dt4_2.setAdapter(listdt2Pager1);
            mycardviewHolder3.itemView.setTag(position);
        }

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return 13;
    }

    public class MycardviewHolder extends RecyclerView.ViewHolder {
        ViewPager Vp_title_tp;
        LinearLayout ll_dt1_2;
        LinearLayout ll_dt1_1;
        public MycardviewHolder(View itemView) {
            super(itemView);
            Vp_title_tp = (ViewPager) itemView.findViewById(R.id.vp_title_tp);
            ll_dt1_1=(LinearLayout)itemView.findViewById(R.id.ll_dt1_1);
            ll_dt1_2=(LinearLayout)itemView.findViewById(R.id.ll_dt1_2);
        }
        public ViewPager getR(){
            return Vp_title_tp;
        }
    }
    public class MycardviewHolder1 extends RecyclerView.ViewHolder {
        ListView Lv_dt_list;
        TextView Tv_hyh;
        public MycardviewHolder1(View itemView) {
            super(itemView);
            Lv_dt_list = (ListView) itemView.findViewById(R.id.lv_dt_list);
            Tv_hyh=(TextView)itemView.findViewById(R.id.tv_hyh);
            Lv_dt_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FragmentTransaction transaction = mfragmentManager.beginTransaction();
                    transaction.replace(R.id.fm_main, new Diantai_into_Fragment(1,twoBean.get(position).getAlbunId()));
                    transaction.commit();
                }
            });
        }
    }
    public class MycardviewHolder2 extends RecyclerView.ViewHolder {
        TextView Tv_dt3_title;
        ImageView Iv_dt3_1;
        ImageView Iv_dt3_2;
        ImageView Iv_dt3_3;
        TextView Tv_dt3_1;
        TextView Tv_dt3_2;
        TextView Tv_dt3_3;
        public MycardviewHolder2(View itemView) {
            super(itemView);
            Tv_dt3_title = (TextView) itemView.findViewById(R.id.tv_dt3_title);
            Tv_dt3_1 = (TextView) itemView.findViewById(R.id.tv_dt3_1);
            Tv_dt3_2 = (TextView) itemView.findViewById(R.id.tv_dt3_2);
            Tv_dt3_3 = (TextView) itemView.findViewById(R.id.tv_dt3_3);
            Iv_dt3_1=(ImageView)itemView.findViewById(R.id.iv_dt3_1);
            Iv_dt3_2=(ImageView)itemView.findViewById(R.id.iv_dt3_2);
            Iv_dt3_3=(ImageView)itemView.findViewById(R.id.iv_dt3_3);
        }
    }
    public class MycardviewHolder3 extends RecyclerView.ViewHolder {
        ListView Lv_dt4_1;
        ListView Lv_dt4_2;
        public MycardviewHolder3(View itemView) {
            super(itemView);
            Lv_dt4_1 = (ListView) itemView.findViewById(R.id.lv_dt4_1);
            Lv_dt4_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
            Lv_dt4_2 = (ListView) itemView.findViewById(R.id.lv_dt4_2);
            Lv_dt4_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }
    }
    public class TitleBean{
        String Id;
        String ShortTitle;
        String Pic;
        String Type;
        String Uid;
        String TrackId;
        String FousCurrentId;
        String RoomId;
        String SpecialId;

        public String getSpecialId() {
            return SpecialId;
        }

        public void setSpecialId(String specialId) {
            SpecialId = specialId;
        }

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getShortTitle() {
            return ShortTitle;
        }

        public void setShortTitle(String shortTitle) {
            ShortTitle = shortTitle;
        }

        public String getPic() {
            return Pic;
        }

        public void setPic(String pic) {
            Pic = pic;
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getUid() {
            return Uid;
        }

        public void setUid(String uid) {
            Uid = uid;
        }

        public String getTrackId() {
            return TrackId;
        }

        public void setTrackId(String trackId) {
            TrackId = trackId;
        }

        public String getFousCurrentId() {
            return FousCurrentId;
        }

        public void setFousCurrentId(String fousCurrentId) {
            FousCurrentId = fousCurrentId;
        }

        public String getRoomId() {
            return RoomId;
        }

        public void setRoomId(String roomId) {
            RoomId = roomId;
        }
    }
    public class ViewdtPager extends PagerAdapter {
        int[] Im = new int[titleBean.size()];
        String[] st = new String[titleBean.size()];
        View[] image;
        int sizetitle=titleBean.size();
        public ViewdtPager() {
            if(sizetitle==0){
                image = new View[1];
                image[0]= LayoutInflater.from(mContext).inflate(R.layout.news_pager_item, null);
            }else {
                image = new View[titleBean.size()];
                for (int i = 0; i < sizetitle; i++) {
                    image[i] = LayoutInflater.from(mContext).inflate(R.layout.news_pager_item, null);
                    ImageView iv = (ImageView) image[i].findViewById(R.id.iv_news_tit);
                    TextView tv = (TextView) image[i].findViewById(R.id.tv_jiesao_news);
                    Glide.with(mContext).load(titleBean.get(i).getPic()).into(iv);
                    tv.setText(titleBean.get(i).getShortTitle());
                    finalI = i;
                    image[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(titleBean.get(finalI).getType().equals("9")){
                                FragmentTransaction transaction = mfragmentManager.beginTransaction();
                                transaction.replace(R.id.fm_main, new Dt_four_(0,titleBean.get(finalI).getShortTitle(),titleBean.get(finalI).getSpecialId(),threeBean.get(finalI).getName()));
                                transaction.commit();
                            }
                        }
                    });
                }
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
            if (sizetitle == 0) {
                container.removeView(image[0]);
            } else {
                container.removeView(image[position % sizetitle]);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //container.addView(listplay.get(position));
            if(sizetitle==0){
                return image[0];
            }
            ViewGroup vv=(ViewGroup)image[position%sizetitle].getParent();
            if(vv!=null){
                vv.removeAllViews();
            }
            container.addView(image[position%sizetitle]);
            return image[position%sizetitle];
        }
    }
    public class TwoBean{
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
    public class ListdtPager extends BaseAdapter{
        public ListdtPager(){

        }
        @Override
        public int getCount() {
            if(twoBean.size()==0){
                return 0;
            }else {
                return 3;
            }

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
            ImageView Iv_dt_list;
            TextView Tv_dt_list_name;
            TextView Tv_dt_list_tit;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                convertView=LayoutInflater.from(mContext).inflate(R.layout.dt_list_item,null);
                holder=new ViewHolder();
                holder.Iv_dt_list=(ImageView)convertView.findViewById(R.id.iv_dt_list);
                holder.Tv_dt_list_name=(TextView) convertView.findViewById(R.id.iv_dt_list_name);
                holder.Tv_dt_list_tit=(TextView)convertView.findViewById(R.id.iv_dt_list_tit);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder)convertView.getTag();
            }
            Glide.with(mContext).load(twoBean.get(3*biaoji3+position).getCoverMiddle()).into(holder.Iv_dt_list);
            holder.Tv_dt_list_name.setText(twoBean.get(3*biaoji3+position).getIntro());
            holder.Tv_dt_list_tit.setText(twoBean.get(3*biaoji3+position).getNicknaem());;
            return convertView;
        }
    }
    public class ThreeBean{
        String Id;
        String Name;
        String Title;
        List<ThreeBean_item>  three_item;
        public String getId() {
            return Id;
        }
        public void setId(String id) {
            Id = id;
        }
        public String getName() {
            return Name;
        }
        public void setName(String name) {
            Name = name;
        }
        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public List<ThreeBean_item> getThree_item() {
            return three_item;
        }

        public void setThree_item(List<ThreeBean_item> three_item) {
            this.three_item = three_item;
        }
    }
    public class ThreeBean_item{
        String Nicknaem;
        String SmallLogo;
        String Title;
        String Uid;

        public String getNicknaem() {
            return Nicknaem;
        }

        public void setNicknaem(String nicknaem) {
            Nicknaem = nicknaem;
        }

        public String getSmallLogo() {
            return SmallLogo;
        }

        public void setSmallLogo(String smallLogo) {
            SmallLogo = smallLogo;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getUid() {
            return Uid;
        }

        public void setUid(String uid) {
            Uid = uid;
        }
    }
    public class ThreeBean1{
        String Id;
        String TrackId;
        String Title;
        String CoverSmall;
        String PlayPath64;
        String Nickname;
        String AlbumId;
        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getTrackId() {
            return TrackId;
        }

        public void setTrackId(String trackId) {
            TrackId = trackId;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getCoverSmall() {
            return CoverSmall;
        }

        public void setCoverSmall(String coverSmall) {
            CoverSmall = coverSmall;
        }

        public String getPlayPath64() {
            return PlayPath64;
        }

        public void setPlayPath64(String playPath64) {
            PlayPath64 = playPath64;
        }

        public String getNickname() {
            return Nickname;
        }

        public void setNickname(String nickname) {
            Nickname = nickname;
        }

        public String getAlbumId() {
            return AlbumId;
        }

        public void setAlbumId(String albumId) {
            AlbumId = albumId;
        }
    }
    public class FourBean{
        String Id;
        String OrderNum;
        String Name;
        String Title;
        String CoverPath;
        String ContentType;
        String CategoryType;
        public String getId() {
            return Id;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getOrderNum() {
            return OrderNum;
        }

        public void setOrderNum(String orderNum) {
            OrderNum = orderNum;
        }



        public String getCoverPath() {
            return CoverPath;
        }

        public void setCoverPath(String coverPath) {
            CoverPath = coverPath;
        }

        public String getContentType() {
            return ContentType;
        }

        public void setContentType(String contentType) {
            ContentType = contentType;
        }

        public String getCategoryType() {
            return CategoryType;
        }

        public void setCategoryType(String categoryType) {
            CategoryType = categoryType;
        }
    }
    public class Listdt2Pager extends BaseAdapter{
        int i=0;
        public Listdt2Pager(){

        }
        public Listdt2Pager(int k){
            i=k;
        }
        @Override
        public int getCount() {
            if (i == 0) {
                return 3;
            }else {
                return 9;
            }

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
            ImageView Iv_dt4_1;
            ImageView Iv_dt4_2;
            TextView Tv_dt4_1;
            TextView Tv_dt4_2;
            LinearLayout ll_dt4_1;
            LinearLayout ll_dt4_2;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                convertView=LayoutInflater.from(mContext).inflate(R.layout.dt_item_4,null);
                holder=new ViewHolder();
                holder.Iv_dt4_1=(ImageView)convertView.findViewById(R.id.iv_dt4_1);
                holder.Iv_dt4_2=(ImageView)convertView.findViewById(R.id.iv_dt4_2);
                holder.Tv_dt4_1=(TextView) convertView.findViewById(R.id.tv_dt4_1);
                holder.Tv_dt4_2=(TextView) convertView.findViewById(R.id.tv_dt4_2);
                holder.ll_dt4_1=(LinearLayout)convertView.findViewById(R.id.ll_dt4_1);
                holder.ll_dt4_2=(LinearLayout)convertView.findViewById(R.id.ll_dt4_2);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder)convertView.getTag();
            }
            final int ss=position+i;
            if(fourBeen.size()!=0){
                holder.Tv_dt4_1.setText(fourBeen.get(ss*2).getTitle());
                holder.Tv_dt4_2.setText(fourBeen.get(ss*2+1).getTitle());
                holder.ll_dt4_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction transaction = mfragmentManager.beginTransaction();
                        transaction.replace(R.id.fm_main, new Dt_four_(3,fourBeen.get(ss*2).getTitle(),fourBeen.get(ss*2).getId(),fourBeen.get(ss*2).getName()));
                        transaction.commit();
                    }
                });
                holder.ll_dt4_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction transaction = mfragmentManager.beginTransaction();
                        transaction.replace(R.id.fm_main,  new Dt_four_(3,fourBeen.get(ss*2+1).getTitle(),fourBeen.get(ss*2+1).getId(),fourBeen.get(ss*2+1).getName()));
                        transaction.commit();
                    }
                });
            }
            //Glide.with(mContext).load(twoBean.get(position).getCoverMiddle()).into(holder.Iv_dt_list);

            return convertView;
        }
    }
}
