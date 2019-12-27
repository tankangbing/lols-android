package com.example.view.captchalib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ValidateSeekBar extends SeekBar implements OnSeekBarChangeListener {

	private Context context;
	//记录旧的位置
	private int oldsign;
	//写进度条上text的画笔
	private Paint mPaint;

	private String textStr ="按住左边滑块，拖动完成上方拼图";
	//text内容
	private String text=textStr;
	//text字体大小
	private int textSize =20;
	//text颜色
	private String textColor ="#607B8B";
	//是否能验证
	private boolean IS_CAN_VALIDATE =false;

	public interface ValidateSeekBarCallBack {
		void onProgressChangedCallBack(SeekBar seekbar, int progress, boolean arg2);
		void onStartTrackingTouchCallBack(SeekBar seekbar);
		void onStopTrackingTouchCallBack(SeekBar seekbar);
	}
	private ValidateSeekBarCallBack callback;

	public ValidateSeekBar(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public ValidateSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public ValidateSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {
		this.mPaint = new Paint();
		this.mPaint.setColor(Color.parseColor(textColor));
		this.mPaint.setTextSize(textSize);
		setOnSeekBarChangeListener(this);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Rect rect = new Rect();
		this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
		int x = (getWidth() / 2) - rect.centerX();
		int y = (getHeight() / 2) - rect.centerY();
		canvas.drawText(this.text, x, y, this.mPaint);
	}

	@Override
	public void onProgressChanged(SeekBar seekbar, int progress, boolean arg2) {
		//控制textview会闪的bug

		if(progress<(seekbar.getMax()/9+10)&&progress!=0){
			text="";
		}

		if(progress>oldsign+(seekbar.getMax()/9+10)){

			seekbar.setProgress(oldsign);
			IS_CAN_VALIDATE =false;
			return;
		}
		IS_CAN_VALIDATE =true;
		seekbar.setProgress(progress);
		oldsign = progress;
		if(this.callback!=null){
			this.callback.onProgressChangedCallBack(seekbar,progress,arg2);
		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekbar) {

		seekbar.setProgress(oldsign);
		if(this.callback!=null){
			this.callback.onStartTrackingTouchCallBack(seekbar);
		}
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekbar) {

		oldsign=0;

		if(this.callback!=null&&IS_CAN_VALIDATE){
			this.callback.onStopTrackingTouchCallBack(seekbar);
			IS_CAN_VALIDATE =false;
		}

	}

	public void setValidateSeekBarCallBack(ValidateSeekBarCallBack callback){
		this.callback =callback;
	}

	public void refreshText(){
		text=textStr;
	}

}
