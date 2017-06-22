package com.example.wade.inme.UIwedget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.wade.inme.R;

/**
 *
 * CircleSeekBar
 *
 * @author lee
 *
 */

public class CircleSeekBar extends View {

    private final boolean DEBUG = true;
    private final String TAG = "CircleSeekBar";
    private Rect bpSrcRect,bpDesRect;
    private Context mContext = null;
    private AttributeSet mAttrs = null;
    private float prosser=0;//进度
    private Bitmap backbitmap = null;

    private int mThumbWidth = 0;

    private Paint mSeekBarBackgroundPaint = null;


    private int mViewHeight = 0;
    private int mViewWidth = 0;
    private int mSeekBarSize = 0;
    private int mSeekBarRadius = 10;
    private int mSeekBarCenterX = 0;
    private int mSeekBarCenterY = 0;
    private Paint mSeekbarProgressPaint;
    private RectF rectpross;

    public CircleSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mAttrs = attrs;
        init();
    }

    public CircleSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        init();
    }

    public CircleSeekBar(Context context) {
        super(context);
        mContext = context;
        init();

    }
    public void init(){
        mSeekBarBackgroundPaint = new Paint();
        mSeekBarBackgroundPaint.setColor(Color.argb(255,77,77,77));
        mSeekBarBackgroundPaint.setAntiAlias(true);
        mSeekBarBackgroundPaint.setStyle(Paint.Style.STROKE);
        mSeekBarBackgroundPaint.setStrokeWidth(5);
        mSeekbarProgressPaint=new Paint();
        mSeekbarProgressPaint.setStrokeWidth(5);
        mSeekbarProgressPaint.setStyle(Paint.Style.STROKE);
        mSeekbarProgressPaint.setColor(Color.RED);
    }

    public void setpre(float num){
        prosser=num;
        invalidate();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(DEBUG) Log.d(TAG, "onMeasure");
        mViewWidth = getWidth();
        mViewHeight = getHeight();

        mSeekBarSize = mViewWidth > mViewHeight ? mViewHeight : mViewWidth;

        mSeekBarCenterX = mViewWidth / 2;
        mSeekBarCenterY = mViewHeight / 2;
        bpSrcRect=new Rect(0,0,mViewWidth,mViewHeight);
        bpDesRect=new Rect(0,0,mViewWidth,mViewHeight);
        mSeekBarRadius = mSeekBarSize / 2 - mThumbWidth / 2;
        rectpross=new RectF(5,5,mViewWidth-5,mViewHeight-5);
        int left = mSeekBarCenterX - mSeekBarRadius;
        int right = mSeekBarCenterX + mSeekBarRadius;
        int top = mSeekBarCenterY - mSeekBarRadius;
        int bottom = mSeekBarCenterY + mSeekBarRadius;

        // 起始位置，三点钟方向

    }
    public Bitmap suofang(Bitmap bm){

        int width = bm.getWidth();
        int height = bm.getHeight();
        Log.e("dd",height+"");
        Log.e("dd1",mViewHeight+"");

        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f);
        // 得到新的图片
        Bitmap bit1 = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix, true);
        return bit1;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        mSeekBarBackgroundPaint.setColor(Color.argb(255,77,77,77));
        canvas.drawCircle(mSeekBarCenterX, mSeekBarCenterY, mSeekBarRadius-5, mSeekBarBackgroundPaint);
        mSeekBarBackgroundPaint.setColor(Color.argb(255,255,255,255));

        canvas.drawArc(rectpross, 270.0F, prosser, false, mSeekbarProgressPaint);
        mSeekBarBackgroundPaint.setColor(Color.argb(255,255,255,255));
        //canvas.drawCircle(mSeekBarCenterX, mSeekBarCenterY, mSeekBarRadius-25,mSeekBarBackgroundPaint);
        Bitmap backbitmap1=((BitmapDrawable)getResources().getDrawable(R.drawable.pause_btn)).getBitmap();
        backbitmap=suofang(backbitmap1);
        //canvas.drawBitmap(backbitmap1,bpSrcRect,bpDesRect,null);


        super.onDraw(canvas);
    }


}
