package com.example.wade.inme.Fragment;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.wade.inme.Adapter.CharstopAdapter;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.R;
import com.example.wade.inme.UIwedget.YLListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Diantai_into_Fragment extends Fragment implements ViewPager.OnPageChangeListener{
    InMeApplicacation inMeApplicacation;
    int Lev;
    String Id;
    String Url;
    TextView Tv_dt_into_title;
    TextView Tv_dt_into_dy;
    TextView Tv_dt_into_1;
    TextView Tv_dt_into_2;
    ImageView Iv_list_huadong13;
    ViewPager Vp_dt_into;
    ImageView Iv_bakc_dt;
    ImageButton Ib_backmain_dt;
    List<MusicInfor> musicInfors;
    private int bmpw = 0; // 游标宽度
    private int offset;// // 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    int screenW;
    public Diantai_into_Fragment() {
        // Required empty public constructor
    }
    public Diantai_into_Fragment(int i,String id) {
        Lev=i;
        Id=id;
        if(i==1){
            Url="http://mobile.ximalaya.com/mobile/others/ca/album/track/"+Id+"/true/1/30?device=iPhone";
        }else if(i==3){
            Url="http://mobile.ximalaya.com/m/category_tag_list?category="+id+"&device=iPhone&type=album";
        }
    }
    public void initData(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest sr = new JsonObjectRequest(Url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
               initView(jsonObject);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(sr);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_diantai_into_, container, false);
        inMeApplicacation=(InMeApplicacation)this.getContext().getApplicationContext();
        Tv_dt_into_title=(TextView)view.findViewById(R.id.tv_dt_into_title);
        Tv_dt_into_dy=(TextView)view.findViewById(R.id.tv_dt_into_dy);
        Tv_dt_into_1=(TextView)view.findViewById(R.id.tv_dt_into_1);
        Tv_dt_into_2=(TextView)view.findViewById(R.id.tv_dt_into_2);
        Iv_list_huadong13=(ImageView) view.findViewById(R.id.iv_list_huadong13);
        Vp_dt_into=(ViewPager) view.findViewById(R.id.vp_dt_into);
        Vp_dt_into.setOnPageChangeListener(this);
        Iv_bakc_dt=(ImageView)view.findViewById(R.id.iv_bakc_dt);
        Ib_backmain_dt=(ImageButton)view.findViewById(R.id.ib_backmain_dt);
        Ib_backmain_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mfragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = mfragmentManager.beginTransaction();
                transaction.replace(R.id.fm_main, new MainFragment(1,2));
                transaction.commit();
            }
        });
        initCursorPos();
        initData();
        return view;
    }
     public void initView(JSONObject jsonObject){
         try {
             DtViewPager dtViewPager=new DtViewPager(jsonObject);
             Vp_dt_into.setAdapter(dtViewPager);
             jsonObject=jsonObject.getJSONObject("album");
             Tv_dt_into_title.setText(jsonObject.getString("title")+"("+jsonObject.getString("categoryName")+")");
             Tv_dt_into_dy.setText("订阅"+jsonObject.getString("followers"));
             Glide.with(getContext()).load(jsonObject.getString("coverLarge")).into(Iv_bakc_dt);
         } catch (JSONException e) {
             e.printStackTrace();
         }
     }
    public void initCursorPos() {
        // 初始化动画
        bmpw = BitmapFactory.decodeResource(getResources(), R.drawable.icon_list_huadong)
                .getWidth();// 获取图片宽度

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenW = dm.widthPixels;// 获取分辨率宽度
        offset = screenW /2;// 计算偏移量
        // Log.e("offset0",offset+" "+bmpw);
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        Iv_list_huadong13.setImageMatrix(matrix);// 设置动画初始位置
    }
    int offset1=0;
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        offset=(int) ((position+positionOffset)*screenW/2);

        //int one =offset;// 页面一到页面二移动量,他的大小就是屏幕的三分之一
        //int two = one * 2;// 页卡1 -> 页卡3 偏移量
        // int three = one * 3;// 页卡1 -> 页卡4 偏移量
        Animation animation = new TranslateAnimation(offset1,offset,0,0);
        if(animation!=null){
            animation.setFillAfter(true);
            animation.setDuration(300);//
            Iv_list_huadong13.startAnimation(animation);
        }
        offset1=offset;
        float nn=(float)position+positionOffset;
        if(nn<0.5){
            Tv_dt_into_1.setTextColor(Color.RED);
            Tv_dt_into_2.setTextColor(Color.GRAY);
        }else if(nn>0.5&&nn<1.5){
            Tv_dt_into_2.setTextColor(Color.RED);
            Tv_dt_into_1.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class DemoAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 1;
        }
        @Override
        public Object getItem(int position) {
            return position;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           convertView=LayoutInflater.from(getContext()).inflate(R.layout.dt_list_into_,null);
            return convertView;
        }

    }
    class DtViewPager extends PagerAdapter{
        View[] v=new View[2];
        TextView Tv_int_dt_fl;
        TextView Tv_int_dt_mz;
        TextView Tv_int_dt_zx;
        TextView Tv_int_dt_dy;
        TextView Tv_int_dt_jj;
        ListView Lv_localmusic_zj;
        public DtViewPager(JSONObject jsonObject){
            v[0]=LayoutInflater.from(getContext()).inflate(R.layout.dt_introduce,null);
            v[1]=LayoutInflater.from(getContext()).inflate(R.layout.fragment_local_zj_fragment,null);
            Tv_int_dt_fl=(TextView)v[0].findViewById(R.id.tv_int_dt_fl);
            Tv_int_dt_mz=(TextView)v[0].findViewById(R.id.tv_int_dt_mz);
            Tv_int_dt_zx=(TextView)v[0].findViewById(R.id.tv_int_dt_zx);
            Tv_int_dt_dy=(TextView)v[0].findViewById(R.id.tv_int_dt_dy);
            Tv_int_dt_jj=(TextView)v[0].findViewById(R.id.tv_int_dt_jj);
            //Tv_int_dt_fl.setText("分类:111111111");
            try {
                initView0(jsonObject.getJSONObject("album"));
                Lv_localmusic_zj=(ListView) v[1].findViewById(R.id.lv_localmusic_zj);
                initView1(jsonObject.getJSONObject("tracks"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        public void initView0(JSONObject jsonObject){
            try {
                Tv_int_dt_fl.setText("分类:"+jsonObject.getString("categoryName"));
                Tv_int_dt_mz.setText("名字:"+jsonObject.getString("title"));
                Tv_int_dt_zx.setText("最新:"+jsonObject.getString("customSubTitle"));
                Tv_int_dt_dy.setText("订阅:"+jsonObject.getString("followers"));
                Tv_int_dt_jj.setText("简介:"+jsonObject.getString("intro"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        public void initView1(JSONObject jsonObject){
            musicInfors=new ArrayList<>();
            try {
                JSONArray jj=jsonObject.getJSONArray("list");
                Log.e("yuyuyu",jj.toString());
                for(int i=0;i<jj.length();i++){
                    JSONObject j1=jj.getJSONObject(i);
                    MusicInfor m=new MusicInfor(j1.getString("title"),j1.getString("trackId"),j1.getInt("playtimes")/60,j1.getString("uid"),
                            j1.getString("coverSmall"),"",j1.getString("playUrl64"),j1.getString("nickname"),j1.getString("albumId"));
                    musicInfors.add(m);
                }
                DtAdapter_item dtAdapter_item=new DtAdapter_item();
                Lv_localmusic_zj.setAdapter(dtAdapter_item);
                Lv_localmusic_zj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        inMeApplicacation.setPlaymusicList(musicInfors);
                        inMeApplicacation.setNowplay(position);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(v[position]);
            return v[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(v[position]);
        }
    }
    public class DtAdapter_item extends BaseAdapter{

        @Override
        public int getCount() {
            return musicInfors.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder holder = null;
            if(convertView==null){
                convertView=LayoutInflater.from(getContext()).inflate(R.layout.charstop_item,null);
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
            holder.Tv_num_top.setText(position+1+"");
            if(position<3){
                holder.Tv_num_top.setTextColor(Color.RED);
            }else {
                holder.Tv_num_top.setTextColor(Color.GRAY);
            }
            holder.Tv_musictitle_chars.setText(musicInfors.get(position).getSongName());
            holder.Tv_musicatr_chars.setText(musicInfors.get(position).getSongArtist());
            holder.Tv_musiczj_chars.setText(musicInfors.get(position).getSongAlubmid());
            return convertView;
        }
    }

}

