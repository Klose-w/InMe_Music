package com.example.wade.inme.Fragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wade.inme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link Musictop_online#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Musictop_online extends Fragment implements View.OnClickListener,ViewPager.OnPageChangeListener{
    TextView textView_gx;
    TextView textView_gd;
    TextView textView_dt;
    TextView textView_ph;
    ImageView Iv_list_huadong;
    private List<Fragment> listmusic;
    NewsFragment newsFragment;
    RecommendFragment recommendFragment;
    DiantaiFragment diantaiFragment;
    MusicChartsFragment onlineCharts;
    LocalMusicFragment.LocalpagerAdapter localpagerAdapter;
    private int bmpw = 0; // 游标宽度
    private int offset;// // 动画图片偏移量
    private int currIndex = 0;
    ViewPager viewPager;
    int screenW;
    int Two=0;
    public Musictop_online() {
        // Required empty public constructor
    }
    public Musictop_online(int two) {
        // Required empty public constructor
        Two=two;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Musictop_online.
     */
    // TODO: Rename and change types and number of parameters
    public static Musictop_online newInstance(String param1, String param2) {
        Musictop_online fragment = new Musictop_online();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_musictop_online, container, false);
        viewPager=(ViewPager)view.findViewById(R.id.vp_line_all);
        textView_gx=(TextView)view.findViewById(R.id.tv_line_gx);
        textView_gx.setOnClickListener(this);
        textView_gd=(TextView)view.findViewById(R.id.tv_line_gd);
        textView_gd.setOnClickListener(this);
        textView_dt=(TextView)view.findViewById(R.id.tv_line_dt);
        textView_dt.setOnClickListener(this);
        textView_ph=(TextView)view.findViewById(R.id.tv_line_ph);
        textView_ph.setOnClickListener(this);
        viewPager.setOnPageChangeListener(this);
        Iv_list_huadong=(ImageView)view.findViewById(R.id.iv_list_huadong1);
        initview();
        return view;
    }
    public void initview(){
        listmusic=new ArrayList<Fragment>();
        newsFragment=new NewsFragment();
        recommendFragment=new RecommendFragment();
        diantaiFragment=new DiantaiFragment();
        onlineCharts=new MusicChartsFragment();
        listmusic.add(recommendFragment);
        listmusic.add(newsFragment);
        listmusic.add(diantaiFragment);
        listmusic.add(onlineCharts);
        initCursorPos();
        localpagerAdapter=new LocalMusicFragment.LocalpagerAdapter(getParentFragment().getChildFragmentManager(),listmusic);
        viewPager.setAdapter(localpagerAdapter);
        viewPager.setCurrentItem(Two);

    }
    public void initCursorPos() {
        // 初始化动画
        bmpw = BitmapFactory.decodeResource(getResources(), R.drawable.icon_list_huadong)
                .getWidth();// 获取图片宽度

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenW = dm.widthPixels;// 获取分辨率宽度
        offset = screenW /4;// 计算偏移量
        // Log.e("offset0",offset+" "+bmpw);
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        Iv_list_huadong.setImageMatrix(matrix);// 设置动画初始位置
    }
    int offset1=0;
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Log.e("position",position+" "+positionOffset+" "+positionOffsetPixels);
        offset=(int) ((position+positionOffset)*screenW/4);

        //int one =offset;// 页面一到页面二移动量,他的大小就是屏幕的三分之一
        //int two = one * 2;// 页卡1 -> 页卡3 偏移量
        // int three = one * 3;// 页卡1 -> 页卡4 偏移量
        Animation animation = new TranslateAnimation(offset1,offset,0,0);

        if(animation!=null){
            animation.setFillAfter(true);
            animation.setDuration(300);//
            Iv_list_huadong.startAnimation(animation);
        }
        offset1=offset;
        float nn=(float)position+positionOffset;
        if(nn<0.5){
            textView_gx.setTextColor(Color.RED);
            textView_gd.setTextColor(Color.GRAY);
            textView_dt.setTextColor(Color.GRAY);
            textView_ph.setTextColor(Color.GRAY);
        }else if(nn>0.5&&nn<1.5){
            textView_gd.setTextColor(Color.RED);
            textView_gx.setTextColor(Color.GRAY);
            textView_dt.setTextColor(Color.GRAY);
            textView_ph.setTextColor(Color.GRAY);
        }
        else if(nn>1.5&&nn<2.5){
            textView_dt.setTextColor(Color.RED);
            textView_gd.setTextColor(Color.GRAY);
            textView_gx.setTextColor(Color.GRAY);
            textView_ph.setTextColor(Color.GRAY);
        }else if(nn>2.5){
            textView_ph.setTextColor(Color.RED);
            textView_gx.setTextColor(Color.GRAY);
            textView_gd.setTextColor(Color.GRAY);
            textView_dt.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.tv_line_gx:
                viewPager.setCurrentItem(0);
                textView_gx.setTextColor(Color.RED);
                textView_gd.setTextColor(Color.GRAY);
                textView_dt.setTextColor(Color.GRAY);
                textView_ph.setTextColor(Color.GRAY);
                break;
            case R.id.tv_line_gd:
                viewPager.setCurrentItem(1);
                textView_gd.setTextColor(Color.RED);
                textView_gx.setTextColor(Color.GRAY);
                textView_dt.setTextColor(Color.GRAY);
                textView_ph.setTextColor(Color.GRAY);
                break;
            case R.id.tv_line_dt:
                viewPager.setCurrentItem(2);
                textView_dt.setTextColor(Color.RED);
                textView_gd.setTextColor(Color.GRAY);
                textView_gx.setTextColor(Color.GRAY);
                textView_ph.setTextColor(Color.GRAY);
                break;
            case R.id.tv_line_ph:
                viewPager.setCurrentItem(3);
                textView_ph.setTextColor(Color.RED);
                textView_gx.setTextColor(Color.GRAY);
                textView_gd.setTextColor(Color.GRAY);
                textView_dt.setTextColor(Color.GRAY);
                break;
        }
    }


    public static class LocalpagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> listfragment;
        public LocalpagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            listfragment=fragmentList;
        }
        @Override
        public Fragment getItem(int position) {
            return listfragment.get(position);
        }

        @Override
        public int getCount() {
            return listfragment.size();
        }
    }
}
