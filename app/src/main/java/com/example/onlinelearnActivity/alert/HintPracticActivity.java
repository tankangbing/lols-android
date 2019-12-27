package com.example.onlinelearnActivity.alert;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

import com.example.onlinelearn.R;
import com.example.util.ApplicationUtil;
/**
 * 提示页面：首次使用app提示左右滑动切换题目
 * */

public class HintPracticActivity extends Activity implements OnTouchListener{
	private ApplicationUtil  app;
	LinearLayout hint_ll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hint_practic);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);//需要添加的语句
		initialize();
		hint_ll=(LinearLayout)findViewById(R.id.hint_ll);
		hint_ll.setOnTouchListener(this);
	}

	public void initialize() {
		app = (ApplicationUtil) getApplication();
		app.addActivity(this);
	}


	//触摸事件，点击关闭该页面
	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		System.out.println("11");
		this.finish();
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
}
