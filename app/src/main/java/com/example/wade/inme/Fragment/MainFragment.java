package com.example.wade.inme.Fragment;



import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.example.wade.inme.Adapter.MainFragmentAdapter;
import com.example.wade.inme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener,ViewPager.OnPageChangeListener {

   int One=0;
    int Two=0;
    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }
    public MainFragment(int one,int two){
        One=one;
        Two=two;
    }
    ImageButton bt_music;
    ImageButton bt_search;
    ImageButton bt_discover;
    ImageButton bt_friends;
    ViewPager vp_mainpager;
    List<Fragment> fragments;
    MusicList_main fragment_musci;
    Musictop_online fragment_top;
    ShareFragment fragment_top1;
    MainFragmentAdapter mainfragmentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("1","onResume");
    }

    @Override
    public void onResume() {
        Log.e("1","onResume");
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("1","onCreateView");
        View view=inflater.inflate(R.layout.fragment_main, container, false);

        bt_music=(ImageButton)view.findViewById(R.id.bt_music_main);
        bt_music.setOnClickListener(this);
        bt_discover=(ImageButton)view.findViewById(R.id.bt_discover_main);
        bt_discover.setOnClickListener(this);
        bt_search=(ImageButton)view.findViewById(R.id.bt_search_main);
        bt_search.setOnClickListener(this);
        bt_friends=(ImageButton)view.findViewById(R.id.bt_friend_main);
        bt_friends.setOnClickListener(this);
        vp_mainpager=(ViewPager)view.findViewById(R.id.vp_main_main);
        vp_mainpager.setOnPageChangeListener(this);

        initActivity();

        return view;
    }




    public void initActivity(){
        fragment_musci=new MusicList_main();
        fragment_top=new Musictop_online(Two);
        fragment_top1=new ShareFragment();
        fragments=new ArrayList<Fragment>() ;
        fragments.add(fragment_musci);
        fragments.add(fragment_top);
        fragments.add(fragment_top1);
        mainfragmentAdapter=new MainFragmentAdapter(this.getChildFragmentManager(),fragments);
        vp_mainpager.setAdapter(mainfragmentAdapter);
        vp_mainpager.setCurrentItem(One);
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.bt_music_main:
                bt_music.setBackgroundResource(R.drawable.actionbar_music_selected);
                bt_discover.setBackgroundResource(R.drawable.actionbar_discover_normal);
                bt_friends.setBackgroundResource(R.drawable.actionbar_friends_normal);
                vp_mainpager.setCurrentItem(0);
                break;
            case  R.id.bt_discover_main:
                bt_discover.setBackgroundResource(R.drawable.actionbar_discover_selected);
                bt_music.setBackgroundResource(R.drawable.actionbar_music_normal);
                bt_friends.setBackgroundResource(R.drawable.actionbar_friends_normal);
                vp_mainpager.setCurrentItem(1);
                break;
            case  R.id.bt_friend_main:
                bt_discover.setBackgroundResource(R.drawable.actionbar_discover_normal);
                bt_music.setBackgroundResource(R.drawable.actionbar_music_normal);
                bt_friends.setBackgroundResource(R.drawable.actionbar_friends_selected);
                vp_mainpager.setCurrentItem(2);
                break;
            case  R.id.bt_search_main:
                //popupWindow.showAtLocation(v,0,10,10);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fm_main, new SearchFragment());
                transaction.commit();
                break;
        }
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("pager",position+"");
        switch (position){
            case 0:
                bt_music.setBackgroundResource(R.drawable.actionbar_music_selected);
                bt_discover.setBackgroundResource(R.drawable.actionbar_discover_normal);
                bt_friends.setBackgroundResource(R.drawable.actionbar_friends_normal);
                break;
            case 1:
                bt_discover.setBackgroundResource(R.drawable.actionbar_discover_selected);
                bt_music.setBackgroundResource(R.drawable.actionbar_music_normal);
                bt_friends.setBackgroundResource(R.drawable.actionbar_friends_normal);
                break;
            case  2:
                bt_discover.setBackgroundResource(R.drawable.actionbar_discover_normal);
                bt_music.setBackgroundResource(R.drawable.actionbar_music_normal);
                bt_friends.setBackgroundResource(R.drawable.actionbar_friends_selected);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
