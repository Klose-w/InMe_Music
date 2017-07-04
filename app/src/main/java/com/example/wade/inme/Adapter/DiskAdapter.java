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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wade.inme.Activity.PlayNowMusic_Main;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.MusicInfor;
import com.example.wade.inme.MainActivity;
import com.example.wade.inme.R;
import com.example.wade.inme.UIwedget.CircleImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wade on 2017/6/2.
 */

public class DiskAdapter extends PagerAdapter implements Serializable {
    List<View> listplay;
    View nowView;
    View lastView;
    View nextView;
    Context mContext;
    CircleImageView Iv_fengmian_main2;
    CircleImageView Iv_fengmian_main3;
    CircleImageView Iv_fengmian_main4;
    MusicInfor lastMusic;
    MusicInfor nextMusic;
    MusicInfor nowMusic;
    InMeApplicacation inMeApplicacation;
    int shuxi=0;

    public DiskAdapter(Context context){
        mContext=context;
        inMeApplicacation=(InMeApplicacation)mContext.getApplicationContext();
        listplay=new ArrayList<View>();

        initMusic();
        initViews();
    }

    public DiskAdapter(List<View> listplay, Context context){
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
            Iv_fengmian_main2.setImageBitmap(inMeApplicacation.getArtwork(mContext,0));
        }else {
            Glide.with(mContext).load(lastMusic.getSongbitmap()).into(Iv_fengmian_main2);
        }
        if(nowMusic.getBiaoji()==0){
            Iv_fengmian_main3.setImageBitmap(inMeApplicacation.getArtwork(mContext,1));
        }else {
            Glide.with(mContext).load(nowMusic.getSongbitmap()).into(Iv_fengmian_main3);
        }
        if(nextMusic.getBiaoji()==0){
            Iv_fengmian_main4.setImageBitmap(inMeApplicacation.getArtwork(mContext,2));
        }else {
            Glide.with(mContext).load(nextMusic.getSongbitmap()).into(Iv_fengmian_main4);
        }
        listplay.clear();
        listplay.add(lastView);
        listplay.add(nowView);
        listplay.add(nextView);
       // notifyDataSetChanged();

    }
    public void initViews(){
        nowView = LayoutInflater.from(mContext).inflate(
                R.layout.diskview, null);
        nowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(mContext, PlayNowMusic_Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle=new Bundle();
                bundle.putSerializable("nowmusic",inMeApplicacation.getNowmusic());
                intent.putExtras(bundle);
                mContext.startActivity(intent);*/
            }
        });
        Iv_fengmian_main3=(CircleImageView)nowView.findViewById(R.id.ci_xuanzhuan_m);
        /*******/
        lastView = LayoutInflater.from(mContext).inflate(
                R.layout.diskview, null);
        Iv_fengmian_main2=(CircleImageView)lastView.findViewById(R.id.ci_xuanzhuan_m);

       // rotateAnimation.start();


        /*******/
        nextView = LayoutInflater.from(mContext).inflate(
                R.layout.diskview, null);
        Iv_fengmian_main4=(CircleImageView)nextView.findViewById(R.id.ci_xuanzhuan_m);

        changeViews();


    }
    public  void setRotateAnimationstart(boolean isstart){


        if(isstart){
            Log.e("dsfkjhdkfhdk","dhfgdhgjfd");
            Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.tip);
            LinearInterpolator lin=new LinearInterpolator();
            animation.setInterpolator(lin);
            animation.setFillAfter(true);
            Iv_fengmian_main3.setAnimation(animation);

        }else {
            Iv_fengmian_main3.clearAnimation();
        }
       // notifyDataSetChanged();

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
