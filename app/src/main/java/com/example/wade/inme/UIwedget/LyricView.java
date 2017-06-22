package com.example.wade.inme.UIwedget;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.OvershootInterpolator;
import android.widget.ScrollView;

import com.example.wade.inme.JavaBean.LyricInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wade on 2017/6/13.
 */

public class LyricView extends View {
    private int textColor= Color.parseColor("#000000");
    private int nowtextColor= Color.parseColor("#ff0000");
    private int tishiColor=Color.parseColor("#0000ff");
    Context mContext;
    List<LyricInfo> lyricInfos;
    private String hineString="当前音乐无歌词";
    private int mLineCount;//行数
    private int mLineHeight;//行高
    private float mScrolly=0;//y方向偏移
    private int nowLinetuo=0;//当前拖动行
    private int nowLineplay=0;//当前播放行
    private int hangjianjy=80;
    private Paint nowPaint,textPaint;//当前行画笔当，其他画笔
    private int maxsuduScr;//最大滑动距离
    public float guodujuli;//渐变过渡的距离
    LyricBroadcast lyricBroadcast;
    boolean mUserTouch=false;
    private VelocityTracker velocityTracker;
    ValueAnimator animator1;
    int Huitan=0;
    boolean mTouch=false;
    class LyricBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            long nowtime=bundle.getLong("playprogress");
            if(lyricInfos.size()>0){
                if(getIndex(nowtime)!=nowLineplay&&getIndex(nowtime)!=-1){
                    if(!mUserTouch){
                        //
                        mScrolly=(nowLineplay-1)*mLineHeight;
                        smoothScrollTo(mScrolly+mLineHeight);
                    }
                    nowLineplay=getIndex(nowtime);

                }
            }
        }
    }
    public int getIndex(long lo){
        lo=lo/1000;
        //Log.e("l",lo+"");
        int i;
        for(i=0;i<lyricInfos.size();i++){
            if(lyricInfos.get(i).getTime()==lo){
               // Log.e("iii",i+"");
                return i+1;

            }
        }
        return -1;
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1==10){
               // smoothScrollTo(nowLineplay*mLineHeight);
               // Thread.sleep();
                if(!mTouch){
                    mUserTouch=false;
                   //
                }

            }
        }
    };
    public LyricView(Context context) {
        super(context);
        initView(context);
    }

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }
    public void initView(Context context){
        mContext=context;
        lyricInfos=new ArrayList<LyricInfo>();
        maxsuduScr=ViewConfiguration.get(mContext).getScaledMaximumFlingVelocity();
        initPaint();
        setmLineHeight();

    }
    @Override
    protected void onMeasure(int width,int height){
        super.onMeasure(width,height);
        guodujuli=getMeasuredHeight()*0.3f;
    }
    //初始化歌词链表
    public void setLyricInfos(List<LyricInfo> lyricInfos1){
        this.lyricInfos=lyricInfos1;
        mLineCount=lyricInfos.size();
        lyricBroadcast=new LyricBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.changeplay");
        mContext.registerReceiver(lyricBroadcast, filter);
       /* new Thread(new Runnable() {
            int i=0;
            @Override
            public void run() {
                while (i<lyricInfos.size()){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mScrolly=mScrolly+mLineHeight;
                    if(i<lyricInfos.size()){
                        nowLineplay=i;
                    }
                    Log.e("ffff",i+" ");
                    i++;
                    Message msg=new Message();
                    msg.arg1=10;
                    handler.sendMessage(msg);
                }

            }
        }).start();*/

    }
    public void setmLineHeight(){
        Rect lineBound=new Rect();
        nowPaint.getTextBounds(hineString,0,hineString.length(),lineBound);
        mLineHeight=lineBound.height()+hangjianjy;
    }
    public void initPaint(){
        nowPaint=new Paint();
        nowPaint.setTextSize(50);
        nowPaint.setDither(true);
        nowPaint.setAntiAlias(true);
        nowPaint.setTextAlign(Paint.Align.CENTER);

        textPaint=new Paint();
        textPaint.setDither(true);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(60);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        if(mScrolly<0){
            mScrolly=0;
        }
        if(lyricInfos!=null){
            if(mScrolly>(mLineHeight*lyricInfos.size()+100)){
                    mScrolly=mLineHeight*lyricInfos.size()+100;
            }
        }

        if(lyricInfos!=null&&lyricInfos.size()>0){
            for(int i=0;i<mLineCount;i++){
                float x=getMeasuredWidth()*0.5f;
                float y=getMeasuredHeight()*0.5f+(i+0.5f)*mLineHeight-6-hangjianjy*0.5f-mScrolly;
                //出去之后不绘制
                if(y+mLineHeight*0.5f<0){
                    continue;
                }
                //还未到达也不绘制
                if(y-mLineHeight*0.5f>getMeasuredHeight()){
                    break;
                }
                //字体颜色
                if(i==nowLineplay-1){
                    nowPaint.setColor(nowtextColor);
                }else {
                    nowPaint.setColor(textColor);
                }
                //过渡距离颜色
                if(y>getMeasuredHeight()-guodujuli||y<guodujuli){
                    if(y<guodujuli){
                        nowPaint.setAlpha(26+(int)(23000.0f*y/guodujuli*0.01f));
                    }else {
                        nowPaint.setAlpha(26 + (int) (23000.0f * (getMeasuredHeight() - y) / guodujuli * 0.01f));
                    }
                }else {
                    nowPaint.setAlpha(255);
                }
                canvas.drawText(lyricInfos.get(i).getText(),x,y,nowPaint);
            }
        }else {
            nowPaint.setColor(tishiColor);
            canvas.drawText("没有歌词",getMeasuredWidth()*0.5f,(getMeasuredHeight()+mLineHeight-6)*0.5f,nowPaint);
        }
    }

    //滑动到指定位置，设置动画
    private void smoothScrollTo(float toY){
        final ValueAnimator animator=ValueAnimator.ofFloat(mScrolly,toY);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(mUserTouch){
                    animation.cancel();
                    return;
                }
                mScrolly=(float)animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(640);
        animator.setInterpolator(new OvershootInterpolator(0.5f));
        animator.start();
    }
    private void touchScrollTo(float toY){
        if(animator1!=null){
            animator1.cancel();
        }
        animator1=ValueAnimator.ofFloat(mScrolly,toY);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(!mUserTouch){
                    animation.cancel();
                    return;
                }
                //Log.e("toy",iii+" "+mScrolly);
                mScrolly=(float)animation.getAnimatedValue();
                invalidate();
            }
        });
        animator1.setDuration(640);
        animator1.setInterpolator(new OvershootInterpolator(0.2f));
        animator1.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                animator1=null;
            }
        }).start();

    }

    private float mDownY,mlastScrolly;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(velocityTracker==null){
            velocityTracker=VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_CANCEL:
                if(null!=velocityTracker){
                    velocityTracker.clear();
                    velocityTracker.recycle();
                    velocityTracker=null;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mTouch=true;
                mUserTouch=true;
                mlastScrolly=mScrolly;
                mDownY=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.computeCurrentVelocity(1000);
                //float changeY=event.getY()-mDownY;
               // touchScrollTo(-velocityTracker.getYVelocity());
                if(animator1==null) {
                    if(Math.abs(velocityTracker.getYVelocity())<2000)
                    {
                        mScrolly=mScrolly-(velocityTracker.getYVelocity()/40);
                        invalidate();
                    }else {
                        touchScrollTo(mScrolly-velocityTracker.getYVelocity()/3);

                    }
                }


                //Log.e("move","move"+changeY);
                break;
            case MotionEvent.ACTION_UP:
                if(null!=velocityTracker){
                    velocityTracker.clear();
                    velocityTracker.recycle();
                    velocityTracker=null;
                }
                mTouch=false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Message msg=new Message();
                        msg.arg1=10;
                        handler.sendMessage(msg);
                    }
                }).start();
                break;

        }
        return true;
    }


}
