package com.example.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.onlinelearn.R;



public class AlertDialog {
	Context context;
	android.app.AlertDialog ad;
	TextView titleView;
	TextView messageView;
	TextView alert_tv;
	Button alertmsgSure;
	Button alertmsgCancel;
	LinearLayout alertmsgMain;
	
	public AlertDialog(Context context) {
		this.context=context;
        ad=new android.app.AlertDialog.Builder(context,R.style.CommunalDialog).create();
		ad.show();
		Window window = ad.getWindow();
		window.setContentView(R.layout.custom_alert_msg);
		alert_tv=(TextView)window.findViewById(R.id.alert_tv);
		titleView=(TextView)window.findViewById(R.id.alertmsgTitle);
		messageView=(TextView)window.findViewById(R.id.alertmsgContent);
		alertmsgSure=(Button)window.findViewById(R.id.alertmsgSure);
		alertmsgCancel = (Button)window.findViewById(R.id.alertmsgCancel);
		alertmsgMain = (LinearLayout)window.findViewById(R.id.alertmsgMain);
		alertmsgMain.setOnClickListener(viewlistener);
	}

    public AlertDialog(Context context,boolean b) {
        this.context=context;
        ad=new android.app.AlertDialog.Builder(context,R.style.CommunalDialog).create();
        ad.show();
        Window window = ad.getWindow();
        window.setContentView(R.layout.custom_alert_msg);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alert_tv=(TextView)window.findViewById(R.id.alert_tv);
        titleView=(TextView)window.findViewById(R.id.alertmsgTitle);
        messageView=(TextView)window.findViewById(R.id.alertmsgContent);
        alertmsgSure=(Button)window.findViewById(R.id.alertmsgSure);
        alertmsgCancel = (Button)window.findViewById(R.id.alertmsgCancel);
        alertmsgMain = (LinearLayout)window.findViewById(R.id.alertmsgMain);
        alertmsgMain.setOnClickListener(viewlistener);
    }

	public AlertDialog(Context context,String str,String color,int layout,int txtid) {
		this.context=context;
		ad=new android.app.AlertDialog.Builder(context).create();
		ad.show();
		Window window = ad.getWindow();
		window.setContentView(layout);
		TextView load_txt = (TextView)window.findViewById(txtid);
		load_txt.setText(str);
		load_txt.setTextColor(Color.parseColor(color));
	}
	
	private OnClickListener viewlistener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Toast.makeText(context, "点击提示框外部可关闭", 200).show();
		}
	};
	
	public void setAlertmsgSureOnclick(OnClickListener listener,String title){
		alertmsgSure.setText(title);
		alertmsgSure.setOnClickListener(listener);
	}
	
	public void delbtn(){//去掉按钮
		alertmsgSure.setVisibility(View.GONE);
		alertmsgCancel.setVisibility(View.GONE);
		alert_tv.setVisibility(View.GONE);
	}
	public void setAlertmsgCancelOnclick(OnClickListener listener,String title){
		alertmsgCancel.setText(title);
		alertmsgCancel.setOnClickListener(listener);
	}
	
	public void setTitle(int resId)
	{
		titleView.setText(resId);
	}
	
	public void setTitle(String title) {
		titleView.setText(title);
	}
	
	public void setMessage(int resId) {
		messageView.setText(resId);
	}
 
	public void setMessage(String message)
	{
		messageView.setText(Html.fromHtml(message));
	}
	
	public void show(){
		ad.show();
	}

	public void dismiss() {
		ad.dismiss();
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	
}