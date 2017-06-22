package com.example.wade.inme.Fragment;


import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wade.inme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocalMusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocalMusicFragment extends Fragment implements View.OnClickListener,ViewPager.OnPageChangeListener{

    Local_dq_fragment local_dq_fragment;
    Local_gs_fragment local_gs_fragment;
    Local_zj_fragment local_zj_fragment1;
    Local_wjj_fragment local_wjj_fragment;
    TextView textView_da;
    TextView textView_gs;
    TextView textView_zj;
    TextView textView_wjj;
    ImageView Iv_list_huadong;
    private int bmpw = 0; // 游标宽度
    private int offset;// // 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    int screenW;
    LocalpagerAdapter localpagerAdapter;
    private List<Fragment> listmusic;
    ViewPager viewPager;
    ImageButton Ib_backmain;

    public LocalMusicFragment() {
        // Required empty public constructor
    }


    public static LocalMusicFragment newInstance(String param1, String param2) {
        LocalMusicFragment fragment = new LocalMusicFragment();
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
        View view=inflater.inflate(R.layout.fragment_local_music, container, false);
        Ib_backmain=(ImageButton)view.findViewById(R.id.ib_backmain);
        Ib_backmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                MainFragment mainFragment=new MainFragment();
                transaction.replace(R.id.fm_main, mainFragment);
                transaction.commit();
            }
        });
        viewPager=(ViewPager)view.findViewById(R.id.vp_local_music);
        textView_da=(TextView)view.findViewById(R.id.tv_local_dq);
        textView_da.setOnClickListener(this);
        textView_gs=(TextView)view.findViewById(R.id.tv_local_gs);
        textView_gs.setOnClickListener(this);
        textView_zj=(TextView)view.findViewById(R.id.tv_local_zj);
        textView_zj.setOnClickListener(this);
        textView_wjj=(TextView)view.findViewById(R.id.tv_local_wjj);
        textView_wjj.setOnClickListener(this);
        viewPager.setOnPageChangeListener(this);
        Iv_list_huadong=(ImageView)view.findViewById(R.id.iv_list_huadong);
        initview();
        return view;
    }





    public void initview(){
        listmusic=new ArrayList<Fragment>();
        local_dq_fragment=new Local_dq_fragment();
        local_gs_fragment=new Local_gs_fragment();
        local_zj_fragment1=new Local_zj_fragment();
        local_wjj_fragment=new Local_wjj_fragment();
        listmusic.add(local_dq_fragment);
        listmusic.add(local_gs_fragment);
        listmusic.add(local_zj_fragment1);
        listmusic.add(local_wjj_fragment);
        initCursorPos();
        localpagerAdapter=new LocalpagerAdapter(this.getChildFragmentManager(),listmusic);
        viewPager.setAdapter(localpagerAdapter);

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
            textView_da.setTextColor(Color.RED);
            textView_gs.setTextColor(Color.GRAY);
            textView_zj.setTextColor(Color.GRAY);
            textView_wjj.setTextColor(Color.GRAY);
        }else if(nn>0.5&&nn<1.5){
            textView_gs.setTextColor(Color.RED);
            textView_da.setTextColor(Color.GRAY);
            textView_zj.setTextColor(Color.GRAY);
            textView_wjj.setTextColor(Color.GRAY);
        }
        else if(nn>1.5&&nn<2.5){
            textView_zj.setTextColor(Color.RED);
            textView_gs.setTextColor(Color.GRAY);
            textView_da.setTextColor(Color.GRAY);
            textView_wjj.setTextColor(Color.GRAY);
        }else if(nn>2.5){
            textView_wjj.setTextColor(Color.RED);
            textView_gs.setTextColor(Color.GRAY);
            textView_zj.setTextColor(Color.GRAY);
            textView_da.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void onPageSelected(int position) {
        /*int one =offset;// 页面一到页面二移动量,他的大小就是屏幕的三分之一
        int two = one * 2;// 页卡1 -> 页卡3 偏移量
        int three = one * 3;// 页卡1 -> 页卡4 偏移量
        Animation animation = null;
        switch (position){
            case 0:
                if (currIndex == 1) {
                    //从1滑动到0位置
                    animation = new TranslateAnimation(one, 0, 0, 0);
                } else if (currIndex == 2) {
                    //从2滑动到0位置
                    animation = new TranslateAnimation(two, 0, 0, 0);
                }else if (currIndex == 3) {
                    //从2滑动到0位置
                    animation = new TranslateAnimation(three, 0, 0, 0);
                }
                textView_da.setTextColor(Color.RED);
                textView_gs.setTextColor(Color.GRAY);
                textView_zj.setTextColor(Color.GRAY);
                textView_wjj.setTextColor(Color.GRAY);
                break;
            case 1:
                if (currIndex == 0) {
                    //从0滑动到1位置
                    animation = new TranslateAnimation(0, one, 0, 0);
                } else if (currIndex == 2) {
                    //从2滑动到1位置
                    animation = new TranslateAnimation(two, one, 0, 0);
                }else if (currIndex == 3) {
                    //从2滑动到1位置
                    animation = new TranslateAnimation(three, one, 0, 0);
                }
                textView_gs.setTextColor(Color.RED);
                textView_da.setTextColor(Color.GRAY);
                textView_zj.setTextColor(Color.GRAY);
                textView_wjj.setTextColor(Color.GRAY);
                break;
            case 2:
                if (currIndex == 0) {
                    //从0滑动到2位置
                    animation = new TranslateAnimation(0, two, 0, 0);
                } else if (currIndex == 1) {
                    //从1滑动到2位置
                    animation = new TranslateAnimation(one, two, 0, 0);
                }else if (currIndex == 3) {
                    //从3滑动到2位置
                    animation = new TranslateAnimation(three, two, 0, 0);
                }
                textView_zj.setTextColor(Color.RED);
                textView_gs.setTextColor(Color.GRAY);
                textView_da.setTextColor(Color.GRAY);
                textView_wjj.setTextColor(Color.GRAY);
                break;
            case 3:
                if (currIndex == 0) {
                    //从0滑动到3位置
                    animation = new TranslateAnimation(0, three, 0, 0);
                } else if (currIndex == 1) {
                    //从1滑动到3位置
                    animation = new TranslateAnimation(one, three, 0, 0);
                }else if (currIndex == 2) {
                    //从2滑动到3位置
                    animation = new TranslateAnimation(two, three, 0, 0);
                }
                textView_wjj.setTextColor(Color.RED);
                textView_gs.setTextColor(Color.GRAY);
                textView_zj.setTextColor(Color.GRAY);
                textView_da.setTextColor(Color.GRAY);
                break;
        }
        currIndex = position;
        animation.setFillAfter(true);
        animation.setDuration(300);//
        Iv_list_huadong.startAnimation(animation);*/
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.tv_local_dq:
                viewPager.setCurrentItem(0);
                textView_da.setTextColor(Color.RED);
                textView_gs.setTextColor(Color.GRAY);
                textView_zj.setTextColor(Color.GRAY);
                textView_wjj.setTextColor(Color.GRAY);
                break;
            case R.id.tv_local_gs:
                viewPager.setCurrentItem(1);
                textView_gs.setTextColor(Color.RED);
                textView_da.setTextColor(Color.GRAY);
                textView_zj.setTextColor(Color.GRAY);
                textView_wjj.setTextColor(Color.GRAY);
                break;
            case R.id.tv_local_zj:
                viewPager.setCurrentItem(2);
                textView_zj.setTextColor(Color.RED);
                textView_gs.setTextColor(Color.GRAY);
                textView_da.setTextColor(Color.GRAY);
                textView_wjj.setTextColor(Color.GRAY);
                break;
            case R.id.tv_local_wjj:
                viewPager.setCurrentItem(3);
                textView_wjj.setTextColor(Color.RED);
                textView_gs.setTextColor(Color.BLACK);
                textView_zj.setTextColor(Color.BLACK);
                textView_da.setTextColor(Color.BLACK);
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
