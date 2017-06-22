package com.example.wade.inme.Adapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wade.inme.Activity.PlayNowMusic_Main;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.MainActivity;
import com.example.wade.inme.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wade on 2017/6/2.
 */

public class PlayViewPagerAdapter extends PagerAdapter implements Serializable {
    List<View> listplay;
    View nowView;
    View lastView;
    View nextView;
    Context mContext;
    ImageView Iv_fengmian_main2;
    ImageView Iv_fengmian_main3;
    ImageView Iv_fengmian_main4;
    TextView Tv_musicname_main2;
    TextView Tv_musicname_main3;
    TextView Tv_musicname_main4;
    TextView Tv_singername_main2;
    TextView Tv_singername_main3;
    TextView Tv_singername_main4;
    MusicInfor lastMusic;
    MusicInfor nextMusic;
    MusicInfor nowMusic;
    InMeApplicacation inMeApplicacation;
    int shuxi=0;
    public PlayViewPagerAdapter(Context context){
        mContext=context;
        inMeApplicacation=(InMeApplicacation)mContext.getApplicationContext();
        listplay=new ArrayList<View>();
        initMusic();
        initViews();
    }
    public PlayViewPagerAdapter(List<View> listplay, Context context){
        this.listplay=listplay;
        mContext=context;
    }
    public void initMusic(){
        lastMusic=inMeApplicacation.getLastmusic();
        nowMusic=inMeApplicacation.getNowmusic();
        nextMusic=inMeApplicacation.getNextmusic();
        if(lastMusic==null){
            Log.e("fff","ddd");
        }

    }
    public void changeViews(){
        if(lastMusic.getBiaoji()==0){
            Iv_fengmian_main2.setImageBitmap(inMeApplicacation.getArtworkFromFile(mContext,0));
        }else {
            Glide.with(mContext).load(lastMusic.getSongbitmap()).into(Iv_fengmian_main2);
        }
        if(nowMusic.getBiaoji()==0){
            Iv_fengmian_main3.setImageBitmap(inMeApplicacation.getArtworkFromFile(mContext,1));
        }else {
            Glide.with(mContext).load(nowMusic.getSongbitmap()).into(Iv_fengmian_main3);
        }
        if(nextMusic.getBiaoji()==0){
            Iv_fengmian_main4.setImageBitmap(inMeApplicacation.getArtworkFromFile(mContext,2));
        }else {
            Glide.with(mContext).load(nextMusic.getSongbitmap()).into(Iv_fengmian_main4);
        }
        Tv_singername_main3.setText(nowMusic.getSongArtist());
        Tv_musicname_main3.setText(nowMusic.getSongName());
        Tv_singername_main2.setText(lastMusic.getSongArtist());
        Tv_musicname_main2.setText(lastMusic.getSongName());
        Tv_singername_main4.setText(nextMusic.getSongArtist());
        Tv_musicname_main4.setText(nextMusic.getSongName());
        listplay.clear();
        listplay.add(lastView);
        listplay.add(nowView);
        listplay.add(nextView);
        notifyDataSetChanged();

    }
    public void initViews(){
        nowView = LayoutInflater.from(mContext).inflate(
                R.layout.rec_listplay, null);
        nowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, PlayNowMusic_Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle=new Bundle();
                bundle.putSerializable("nowmusic",inMeApplicacation.getNowmusic());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        Iv_fengmian_main3=(ImageView)nowView.findViewById(R.id.iv_fengmian_main);
        Tv_musicname_main3=(TextView)nowView.findViewById(R.id.tv_musicname_main);
        Tv_singername_main3=(TextView)nowView.findViewById(R.id.tv_singername_main);

        /*******/
        lastView = LayoutInflater.from(mContext).inflate(
                R.layout.rec_listplay, null);
        Iv_fengmian_main2=(ImageView)lastView.findViewById(R.id.iv_fengmian_main);
        Tv_musicname_main2=(TextView)lastView.findViewById(R.id.tv_musicname_main);
        Tv_singername_main2=(TextView)lastView.findViewById(R.id.tv_singername_main);

        /*******/
        nextView = LayoutInflater.from(mContext).inflate(
                R.layout.rec_listplay, null);
        Iv_fengmian_main4=(ImageView)nextView.findViewById(R.id.iv_fengmian_main);
        Tv_musicname_main4=(TextView)nextView.findViewById(R.id.tv_musicname_main);
        Tv_singername_main4=(TextView)nextView.findViewById(R.id.tv_singername_main);
       changeViews();


    }
    @Override
    public int getCount() {
        return listplay.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       //container.addView(listplay.get(position));
        container.addView(listplay.get(position));

        return listplay.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(listplay.get(position));


    }

    /*@Override
    public void onClick(View v) {
        Log.e("dd","ddd");
        Intent intent=new Intent(mContext, PlayNowMusic_Main.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("nowmusic",inMeApplicacation.getNowmusic());
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }*/
}
