package com.example.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.onlinelearn.R;

/**
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度
 * @author xiaanming
 *
 */
public class RoundProgressBar extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    /**
     * 进度状态，暂停或开始或下载完毕
     */
    private int status;

    /**
     * 选择状态，1未选中或者2选中或者0下载状态
     */
    private int statusXz=0;

    //圆的半径
    private static final int PROGRESS_RADIUS = 30;//圆的半径
    private int mRadius = dp2px(PROGRESS_RADIUS);

    public static final int STROKE = 0;
    public static final int FILL = 1;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.GREEN);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);

        mTypedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆环
         */
        int centre = getWidth()/2; //获取圆心的x坐标
        int radius = (int) (centre - roundWidth/2); //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环

        Log.e("log", centre + "");

        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
        int percent = (int)(((float)progress / (float)max) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        float textWidth = paint.measureText(percent + "%");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间

        if(textIsDisplayable && percent != 0 && style == STROKE){
            canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize/2, paint); //画出进度百分比
        }


        /**
         * 画圆弧 ，画圆环的进度
         */

        //设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限

        //三角形

        //Path mPath = new Path();
        //mPath.lineTo(centre-radius/3, (float) (centre-radius*Math.sqrt(3.0)/2));

        switch (style) {
            case STROKE:{
                if (statusXz==0){
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(oval, 0, 360 * progress / max, false, paint);  //根据进度画圆弧
                    paint.setStrokeWidth(dp2px(2));
                    if (status==0){//暂停
                        paint.setColor(Color.parseColor("#D1D1D1"));
                        canvas.drawLine(centre-radius/3, (float) (centre-radius*Math.sqrt(3.0)/3), centre-radius/3, (float) (centre+radius*Math.sqrt(3.0)/3), paint);
                        canvas.drawLine(centre-radius/3, (float) (centre+radius*Math.sqrt(3.0)/3), centre+radius*2/3, centre, paint);
                        canvas.drawLine(centre+radius*2/3, centre, centre-radius/3, (float) (centre-radius*Math.sqrt(3.0)/3), paint);
                    }else if (status==1){//下载中
                        paint.setColor(Color.parseColor("#01A1EB"));
                        canvas.drawLine(centre-radius/3, centre-radius/2, centre-radius/3, centre+radius/2, paint);
                        canvas.drawLine(centre+radius/3, centre-radius/2, centre+radius/3, centre+radius/2, paint);
                    }else if (status==2){//下载完毕
                        paint.setColor(Color.parseColor("#01A1EB"));
                        canvas.drawLine(centre-radius/2, centre, centre-radius/4, centre+radius/2, paint);
                        canvas.drawLine(centre-radius/4, centre+radius/2, centre+radius/2, centre-radius/3, paint);
                    }
                }else if(statusXz==1){
                    paint.setColor(Color.parseColor("#D1D1D1"));
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(oval, 0, 360 * progress / max, false, paint);  //根据进度画圆弧
                    canvas.drawLine(centre-radius/2, centre, centre-radius/4, centre+radius/2, paint);
                    canvas.drawLine(centre-radius/4, centre+radius/2, centre+radius/2, centre-radius/3, paint);
                }else if (statusXz==2){
                    paint.setColor(Color.parseColor("#01A1EB"));
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(oval, 0, 360 * progress / max, false, paint);  //根据进度画圆弧
                    canvas.drawLine(centre-radius/2, centre, centre-radius/4, centre+radius/2, paint);
                    canvas.drawLine(centre-radius/4, centre+radius/2, centre+radius/2, centre-radius/3, paint);
                }

                /*paint.setColor(Color.parseColor("#01A1EB"));
                canvas.drawLine(centre-radius/3, (float) (centre-radius*Math.sqrt(3.0)/3), centre-radius/3, (float) (centre+radius*Math.sqrt(3.0)/3), paint);
                canvas.drawLine(centre-radius/3, (float) (centre+radius*Math.sqrt(3.0)/3), centre+radius*2/3, centre, paint);
                canvas.drawLine(centre+radius*2/3, centre, centre-radius/3, (float) (centre-radius*Math.sqrt(3.0)/3), paint);*/
               /* canvas.drawLine(centre-radius/3, centre-radius/2, centre-radius/3, centre+radius/2, paint);
                canvas.drawLine(centre+radius/3, centre-radius/2, centre+radius/3, centre+radius/2, paint);*/
                break;
            }
            case FILL:{
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if(progress !=0)
                    canvas.drawArc(oval, 0, 360 * progress / max, true, paint);  //根据进度画圆弧
                break;
            }
        }

    }


    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     * @param max
     */
    public synchronized void setMax(int max) {
        if(max < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
 * 刷新界面调用postInvalidate()能在非UI线程刷新
 * @param progress
 */
    public synchronized void setProgress(int progress) {
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress > max){
            progress = max;
        }
        if(progress <= max){
            this.progress = progress;
            postInvalidate();
        }

    }

    public synchronized int getStatus() {
        return status;
    }

    public synchronized void setStatus(int status) {
        if (status==0){//暂停

        }else if (status==1){//下载中

        }else {//下载完毕

        }
        this.status = status;
        postInvalidate();
    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public synchronized int getStatusXz() {
        return statusXz;
    }

    public synchronized void setStatusXz(int statusXz) {
        this.statusXz = statusXz;
        postInvalidate();
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }
}
