package com.example.wade.inme.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.wade.inme.Adapter.MyFragmentPagerAdapter;
import com.example.wade.inme.Fragment.JPK.AboutMeFragment;
import com.example.wade.inme.Fragment.JPK.MusicFragment;
import com.example.wade.inme.Fragment.JPK.TrendsFragment;
import com.example.wade.inme.InMeApplicacation;
import com.example.wade.inme.JavaBean.UserBean;
import com.example.wade.inme.R;
import com.example.wade.inme.UIwedget.CircleImageView;

import java.util.ArrayList;



/**
 * Created by Lenovo on 2017/6/25.
 */

public class MeAcitvity extends FragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {
    private ViewPager myviewpager;
   InMeApplicacation inMeApplicacation;
    // 选项卡中的按钮
    private Button btn_music;
    private Button btn_trends;
    private Button btn_me;
    private Button btn_det;
    private ImageView gender;
    private TextView tv_name,tv_focus,tv_fans;
    private CircleImageView cv_avatar;

    //作为指示标签的按钮
    private ImageView cursor;
    //标志指示标签的横坐标
    float cursorX = 0;
    //所有按钮的宽度的数组
    private int[] widthArgs;
    //所有标题按钮的数组
    private Button[] btnArgs;
    //fragment的集合，对应每个子页面
    private UserBean userBean;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        inMeApplicacation=(InMeApplicacation)getApplicationContext();
        userBean =inMeApplicacation.getUserBean();
        initView();

        btn_music.post(new Runnable(){
            @Override
            public void run() {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)cursor.getLayoutParams();
                //减去边距*2，以对齐标题栏文字
                lp.width = btn_music.getWidth()-btn_music.getPaddingLeft()*2;
                cursor.setLayoutParams(lp);
                cursor.setX(btn_music.getPaddingLeft());
            }
        });

        btn_det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MeAcitvity.this,EditUserActivity.class));
            }
        });

        if(userBean.getAvatar().equals("null")){
            Glide.with(MeAcitvity.this).load(R.drawable.a3).into(cv_avatar);
        }else {
            Glide.with(MeAcitvity.this).load(inMeApplicacation.getUrl()+userBean.getAvatar()).into(cv_avatar);

        }

        cv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(MeAcitvity.this,ChangeAvatarActivity.class);
                startActivity(intent);
            }
        });

        if(userBean.getGender().equals("女")) gender.setImageResource(R.drawable.female);
        else gender.setImageResource(R.drawable.male);
        tv_name.setText(userBean.getNickname());
        tv_fans.setText(String.valueOf(userBean.getFanscount()));
        tv_focus.setText(String.valueOf(userBean.getFoucuscount()));
    }



    //初始化布局
    public void initView(){
        gender = (ImageView)findViewById(R.id.gender);
        cv_avatar=(CircleImageView) findViewById(R.id.head);
        tv_name = (TextView) findViewById(R.id.name);
        tv_focus = (TextView) findViewById(R.id.number1);
        tv_fans = (TextView) findViewById(R.id.number2);
        btn_det = (Button) findViewById(R.id.bt_detail);
        myviewpager = (ViewPager)findViewById(R.id.myviewpager);
        btn_music = (Button)findViewById(R.id.btn_music);
        btn_trends = (Button)findViewById(R.id.btn_trends);
        btn_me = (Button)findViewById(R.id.btn_me);
        //初始化按钮数组
        btnArgs = new Button[]{btn_music,btn_trends,btn_me};
        //指示标签设置为红色
        cursor = (ImageView)findViewById(R.id.cursor_btn);
        cursor.setBackgroundColor(Color.RED);

        myviewpager.setOnPageChangeListener(this);
        btn_music.setOnClickListener(this);
        btn_trends.setOnClickListener(this);
        btn_me.setOnClickListener(this);

        fragments.add(new MusicFragment());
        fragments.add(new TrendsFragment());
        fragments.add(new AboutMeFragment());

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),this,fragments);
        myviewpager.setAdapter(adapter);
        //先重置所有按钮颜色
        resetButtonColor();
        //再将第一个按钮字体设置为红色，表示默认选中第一个
        btn_music.setTextColor(Color.RED);
    }

    //重置所有按钮的颜色
    public void resetButtonColor(){
        btn_music.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btn_trends.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btn_me.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btn_music.setTextColor(Color.BLACK);
        btn_trends.setTextColor(Color.BLACK);
        btn_me.setTextColor(Color.BLACK);
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        cursorX = 0;
        cursorX = cursorX + btnArgs[0].getWidth()*(arg1+arg0);
        cursor.setX(cursorX);


    }

    @Override
    public void onPageSelected(int arg0) {
        if(widthArgs==null){
            widthArgs = new int []{btn_music.getWidth(),
                    btn_trends.getWidth(),
                    btn_me.getWidth()};
        }
        resetButtonColor();
        //将滑动到的当前按钮颜色设置为红色
        btnArgs[arg0].setTextColor(Color.RED);
        cursorAnim(arg0);

    }
    @Override
    public void onClick(View whichbtn) {
        // TODO Auto-generated method stub

        switch (whichbtn.getId()) {
            case R.id.btn_music:
                myviewpager.setCurrentItem(0);
                cursorAnim(0);
                break;
            case R.id.btn_trends:
                myviewpager.setCurrentItem(1);
                cursorAnim(1);
                break;
            case R.id.btn_me:
                myviewpager.setCurrentItem(2);
                cursorAnim(2);
                break;

        }
    }
    //指示器的跳转，传入当前所处的页面的下标
    public void cursorAnim(int curItem){
        //每次调用，就将指示器的横坐标设置为0，即开始的位置
        cursorX = 0;
        //再根据当前的curItem来设置指示器的宽度
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)cursor.getLayoutParams();
        //减去边距*2，以对齐标题栏文字
        lp.width = widthArgs[curItem]-btnArgs[0].getPaddingLeft()*2;
        cursor.setLayoutParams(lp);
        //循环获取当前页之前的所有页面的宽度
        for(int i=0; i<curItem; i++){
            cursorX = cursorX + btnArgs[i].getWidth();
        }
        //再加上当前页面的左边距，即为指示器当前应处的位置
        cursor.setX(cursorX+btnArgs[curItem].getPaddingLeft());
    }

}
