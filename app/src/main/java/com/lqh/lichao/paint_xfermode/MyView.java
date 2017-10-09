package com.lqh.lichao.paint_xfermode;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017-10-09.
 */

public class MyView extends View {

    Paint mPaint;
    float mItemSize = 0;
    float mItemHorizontalOffset = 0;
    float mItemVerticalOffset = 0;
    float mCircleRadius = 0;
    float mRectSize = 0;
    int mCircleColor = 0xffffcc44;//黄色
    int mRectColor = 0xff66aaff;//蓝色
    float mTextSize = 25;

    private static final Xfermode[] sModes = {
            new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
            new PorterDuffXfermode(PorterDuff.Mode.SRC),
            new PorterDuffXfermode(PorterDuff.Mode.DST),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
            new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.XOR),
            new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
            new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
            new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
            new PorterDuffXfermode(PorterDuff.Mode.SCREEN)
    };

    private static final String[] sLabels = {
            "Clear", "Src", "Dst", "SrcOver",
            "DstOver", "SrcIn", "DstIn", "SrcOut",
            "DstOut", "SrcATop", "DstATop", "Xor",
            "Darken", "Lighten", "Multiply", "Screen"
    };

    public MyView(Context context) {
        super(context);
        init(null, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrc, int defStyleAttr) {
        if(Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);//取消硬件加速
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        for(int row = 0; row < 4; row++){
            for(int column = 0; column < 4; column++){
                canvas.save();
                int layer = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
                mPaint.setXfermode(null);
                int index = row * 4 + column;
                float translateX = (mItemSize + mItemHorizontalOffset) * column;
                float translateY = (mItemSize + mItemVerticalOffset) * row;
                canvas.translate(translateX, translateY);
                //画文字
                String text = sLabels[index];
                mPaint.setColor(Color.BLACK);
                float textXOffset = mItemSize / 2;
                float textYOffset = mTextSize + (mItemVerticalOffset - mTextSize) / 2;
                canvas.drawText(text, textXOffset, textYOffset, mPaint);
                canvas.translate(0, mItemVerticalOffset);
                //画边框
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(0xff000000);
                canvas.drawRect(2, 2, mItemSize - 2, mItemSize - 2, mPaint);
                mPaint.setStyle(Paint.Style.FILL);
                //画圆
                mPaint.setColor(mCircleColor);
                float left = mCircleRadius + 3;
                float top = mCircleRadius + 3;
                canvas.drawCircle(left, top, mCircleRadius, mPaint);
                mPaint.setXfermode(sModes[index]);
                //画矩形
                mPaint.setColor(mRectColor);
                float rectRight = mCircleRadius + mRectSize;
                float rectBottom = mCircleRadius + mRectSize;
                canvas.drawRect(left, top, rectRight, rectBottom, mPaint);
                mPaint.setXfermode(null);
                //canvas.restore();
                canvas.restoreToCount(layer);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mItemSize = w / 4.5f;
        mItemHorizontalOffset = mItemSize / 6;
        mItemVerticalOffset = mItemSize * 0.426f;
        mCircleRadius = mItemSize / 3;
        mRectSize = mItemSize * 0.6f;
    }
}
