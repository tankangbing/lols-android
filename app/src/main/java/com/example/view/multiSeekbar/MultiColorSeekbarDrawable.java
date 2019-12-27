package com.example.view.multiSeekbar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.entity.VideoSpeedSingle;

import java.util.List;

/**
 * Created by TJR on 2017/6/30.
 * seekbar自定义背景
 */
public class MultiColorSeekbarDrawable extends Drawable {

    private RectF rectF;//画图矩形
    private Paint mPaint = new Paint();//画笔
    private int mReachColor;//外边框&填充颜色
    private int mUnReachColor ;//非填充颜色
    private final float mLineWidth =3.0f;//空心圆线宽
    private final int offset =20;//偏移量,遮盖圆角
    private final int LEFT_CONNER =0;//左边圆角
    private final int RIGHT_CONNER =1;//右边圆角
    private final int NO_CONNER =2;//中间矩形
    private int max =100;//seekbar最大值
    private float drawableWidth ;//控件宽度
    //片段数据
    List<VideoSpeedSingle> list =null;

    public MultiColorSeekbarDrawable() {
        rectF = new RectF();
        mReachColor = Color.parseColor("#FF378DC7");
        mUnReachColor = Color.parseColor("#FFDBDFEC");

    }
    @Override
    public void draw(@NonNull Canvas canvas) {
        final Rect bounds = getBounds();
        float lineHeight = bounds.height()/4.0f;
        float lineCenter = bounds.centerY();
        drawableWidth = bounds.width();

        mPaint.setAntiAlias(true); // 消除锯齿

        //如果list为空时
        if (list==null){
            drawRectStroke(canvas,lineHeight,lineCenter,drawableWidth);
        }else {

            for (int i=0;i<list.size();i++){
                if (i>0){
                    //画中间的未观看片段
                    drawFill(canvas,mUnReachColor,NO_CONNER,lineHeight,lineCenter,drawableWidth,
                            computeRealWidth(Integer.parseInt(list.get(i-1).getEndPoint())),
                            computeRealWidth(Integer.parseInt(list.get(i).getStartPoint())));

                }
                //画中间的已观看片段
                drawFill(canvas,mReachColor,NO_CONNER,lineHeight,lineCenter,drawableWidth,
                        computeRealWidth(Integer.parseInt(list.get(i).getStartPoint())),
                        computeRealWidth(Integer.parseInt(list.get(i).getEndPoint())));

            }
            drawFill(canvas,mUnReachColor,NO_CONNER,lineHeight,lineCenter,drawableWidth,
                    0, computeRealWidth(Integer.parseInt(list.get(0).getStartPoint())));
            drawFill(canvas,mUnReachColor,NO_CONNER,lineHeight,lineCenter,drawableWidth,
                    computeRealWidth(Integer.parseInt(list.get(list.size()-1).getEndPoint())), drawableWidth);

        }
    }


    /**
     * 画外边框
     */
    private void drawRectStroke(Canvas canvas, float lineHeight, float lineCenter, float drawableWidth){
        mPaint.setStrokeWidth((float) mLineWidth);
        mPaint.setColor(mReachColor);
        mPaint.setStyle(Paint.Style.STROKE);//空心
        rectF.set(0, lineCenter - lineHeight, drawableWidth, lineCenter + lineHeight);
        canvas.drawRect(rectF, mPaint);

    }

    /**
     * 画实心颜色块
     */
    private void drawFill(Canvas canvas, int color, int style, float lineHeight, float lineCenter, float drawableWidth, float point1, float point2){
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        switch (style){
            case 0:
                mPaint.setStrokeCap(Paint.Cap.ROUND);// 圆角

                rectF.set(0, lineCenter - lineHeight, point1+offset, lineCenter + lineHeight);
                canvas.drawRoundRect(rectF, lineHeight, lineHeight, mPaint);
                break;
            case 1:
                mPaint.setStrokeCap(Paint.Cap.ROUND);// 圆角
                rectF.set(point1-offset, lineCenter - lineHeight, drawableWidth, lineCenter + lineHeight);
                canvas.drawRoundRect(rectF, lineHeight, lineHeight, mPaint);
                break;
            case 2:

                rectF.set(point1, lineCenter - lineHeight, point2, lineCenter + lineHeight);
                canvas.drawRect(rectF, mPaint);
                break;
        }

    }
    private float computeRealWidth(int point){

        return (point * 1.0f / max)*drawableWidth;
    }


    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    /**
     * 更新背景
     * @param list
     * @param max
     */
    public void refreshBackground(List<VideoSpeedSingle> list, int max){
        this.list =list;
        this.max =max;
    }
}
