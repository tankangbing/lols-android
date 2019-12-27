package com.example.onlinelearnActivity.courseWare.practic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import com.example.entity.PracticLearnBehavior;
import com.example.jsonReturn.ErrorCountReturn;
import com.example.jsonReturn.RemoveErrorQuestionsReturn;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.BaseActivity;
import com.example.util.FinalStr;
import com.example.view.ListViewCompat;
import com.example.entity.MessageBean;
import com.example.view.SlideView;
import com.example.util.ApplicationUtil;

import com.example.util.HttpUtil;
import com.example.util.JsonUtil;
import com.example.util.SysUtil;
import com.example.view.dialog.MaterialDialog;

/**
 * 错题库首页面
 * */

public class CourseWareLxCtActivity extends BaseActivity implements OnItemClickListener,OnClickListener {
	private ApplicationUtil hdaplication;//全局参数对象
	TextView ct_tvcount,ct_tv1,ct_tv2;//错题总数、可移除错题、不可移除错题
	private Handler handler;//异步刷新组件
	String count="0",correctCount="0",errorCount="0";
	MaterialDialog alertDialog;
    TextView ct_retrun;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_ware_lx_ct);
		setCache();
		//setUi();
		updateUi();
	}

    private ListViewCompat mListView;
    private void initView() {
        mListView = (ListViewCompat) findViewById(R.id.list);
        PrivateListingAdapter mAdapter = new PrivateListingAdapter(this);
        ArrayList<MessageBean> mMessageList = new ArrayList<MessageBean>();
        MessageBean item = new MessageBean();
        item.ct_iv = R.drawable.icon_back;
        item.ct_tvtitle = "可移除错题";
        item.ct_tv = correctCount +"题";
        mMessageList.add(item);
        MessageBean item2 = new MessageBean();
        item2.ct_iv = R.drawable.icon_back;
        item2.ct_tvtitle = "不可移除错题";
        item2.ct_tv = errorCount +"题";

        mMessageList.add(item2);
        mAdapter.setmMessageItems(mMessageList);
        mListView.setAdapter(mAdapter);
        mListView.setDateType(0);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (position == 0) {//点击可移除错题
            if (correctCount.equals("0")) {
                Toast.makeText(CourseWareLxCtActivity.this, "没有相关的信息", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent =new Intent();
                intent.setClass(CourseWareLxCtActivity.this,PracticePagerActivity.class);
                intent.putExtra("batchId", "do#5#"+correctCount+"#");
                startActivity(intent);
            }
        }else  {//点击不可移除错题    //修复点击不可移除错题为0得时候奔溃
            if(errorCount.equals("0")){
                Toast.makeText(CourseWareLxCtActivity.this, "没有相关的信息", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent =new Intent();
                intent.setClass(CourseWareLxCtActivity.this,PracticePagerActivity.class);
                intent.putExtra("batchId", "do#6#"+errorCount+"#");
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initViews(View view) {
        ct_tvcount=(TextView)findViewById(R.id.ct_tvcount);//全部错题总数
       // ct_tv1=(TextView)findViewById(R.id.ct_tv1);//可移除错题
        //ct_tv2=(TextView)findViewById(R.id.ct_tv2);//不可移除错题
        //通过意图获取上个页面传过来的错题总数
        Intent intent=getIntent();
        String count=intent.getStringExtra("count");
        ct_tvcount.setText(count);
    }

    @Override
    protected void doBusiness(Context mContext) {

    }

    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        setToobbarShow(false);
        ct_retrun=(TextView)findViewById(R.id.ct_retrun);
        iconfont = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
        ct_retrun.setTypeface(iconfont);
    }

    public void setCache() {
		hdaplication = (ApplicationUtil) getApplication();
		hdaplication.addActivity(this);
        hdaplication.setStatus("0");
	}
	public void setUi(){
		ct_tvcount=(TextView)findViewById(R.id.ct_tvcount);//全部错题总数
	//	ct_tv1=(TextView)findViewById(R.id.ct_tv1);//可移除错题
	//	ct_tv2=(TextView)findViewById(R.id.ct_tv2);//不可移除错题
		//通过意图获取上个页面传过来的错题总数
		Intent intent=getIntent();
		String count=intent.getStringExtra("count");
		ct_tvcount.setText(count);
	}

	public void updateUi(){
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case 0: //无法加载数据
						Toast.makeText(CourseWareLxCtActivity.this,"无法加载数据,请检查你的网络连接或者联系我们！",
								Toast.LENGTH_LONG).show();
						break;
					case 1: //加载数据
						setList();
						break;
					case 2://没有获取时，清除页面
						Toast.makeText(CourseWareLxCtActivity.this, "没有相关的信息", Toast.LENGTH_LONG).show();
						break;
				}
				super.handleMessage(msg);
			}
		};
	}

	public void setList() {
		ct_tvcount.setText(count);
        initView();
	//	ct_tv1.setText(correctCount+"题");
	//	ct_tv2.setText(errorCount+"题");
	}

	//获取错题数量
	public void getData(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String,Object> map = new HashMap<String,Object>(0);
				map.put("classId", hdaplication.getClassId());
				map.put("behaviorId", hdaplication.getBehaviorId());
				map.put("accountId", hdaplication.getStuid());
				map.put("questionForm", "2");
				String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?getErrorCount",
						map,CourseWareLxCtActivity.this,null);
				if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
					sendMessage(0);
				}else{
					ErrorCountReturn errorCountReturn=JsonUtil.parserString(result, ErrorCountReturn.class);
					if (errorCountReturn!=null&&errorCountReturn.isSuccess()) {
						count=errorCountReturn.getCount();
						errorCount=errorCountReturn.getErrorCount();
						correctCount=errorCountReturn.getCorrectCount();

                        PracticLearnBehavior practicLearnBehavior=hdaplication.getPracticLearnBehavior();
                        practicLearnBehavior.setErrorQuestionIdsStr(errorCountReturn.getErrorQuestionIdsStr());
                        practicLearnBehavior.setQuestionErrorCount(Integer.parseInt(count));
                        hdaplication.setPracticLearnBehavior(practicLearnBehavior);
                        sendMessage(1);
					}else {
                        sendMessage(2);
                    }
				}
			}
		}).start();
	}

	//移除可移除的错题
	public void removeError(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String,Object> map = new HashMap<String,Object>(0);
				map.put("classId", hdaplication.getClassId());
				map.put("behaviorId", hdaplication.getBehaviorId());
				map.put("accountId", hdaplication.getStuid());
				map.put("questionId", "all");
				String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?removeErrorQuestions",
						map,CourseWareLxCtActivity.this,null);
				if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
					sendMessage(0);
				}else{
                    RemoveErrorQuestionsReturn removeErrorQuestionsReturn=JsonUtil.parserString(result, RemoveErrorQuestionsReturn.class);
					if (removeErrorQuestionsReturn!=null&&removeErrorQuestionsReturn.isSuccess()) {
                        if (removeErrorQuestionsReturn.getMsg().equals("移除成功")){
                            getData();
                        }
					}
				}
			}
		}).start();
	}

	private void sendMessage(int str){
		Message msg = new Message();
		msg.what = str;
		CourseWareLxCtActivity.this.handler.sendMessage(msg);
	}

	//点击事件
	public void click(View v){
		if (v.getId()==R.id.ct_retrun) {//点击返回
            hdaplication.setStatus("1");
			this.finish();
		}else if (v.getId()==R.id.ct_iv_del) {//点击移除
			if (correctCount.equals("0")) {//修复错题集当没有可移除错题时，点击移除错题按钮，页面没有提示信息
				Toast.makeText(CourseWareLxCtActivity.this, "没有相关的信息", Toast.LENGTH_SHORT).show();
			}else {
				alertDialog=new MaterialDialog(CourseWareLxCtActivity.this);
				alertDialog.setTitle("提示");
				alertDialog.setMessage("可移除错题 共"+correctCount+"道");
				alertDialog.setNegativeButton("取消",new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						alertDialog.dismiss();
					}
				});
				alertDialog.setPositiveButton("全部移除",new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						removeError();
						alertDialog.dismiss();
					}
				});
				alertDialog.show();
			}
		}else if (v.getId()==R.id.ct_do) {//点击全部错题
            if("0".equals(count)){//修复 错题集，当不可移除错题为0题时，点击，闪退
                Toast.makeText(CourseWareLxCtActivity.this, "没有相关的信息", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent =new Intent();
                intent.setClass(CourseWareLxCtActivity.this,PracticePagerActivity.class);
                intent.putExtra("batchId", "do#2#"+count+"#");
                startActivity(intent);
            }
		}/*else if (v.getId()==R.id.ct_do1) {//点击可移除错题
			if (correctCount.equals("0")) {
				Toast.makeText(CourseWareLxCtActivity.this, "没有相关的信息", Toast.LENGTH_SHORT);
			}else {
				Intent intent =new Intent();
				intent.setClass(CourseWareLxCtActivity.this,PracticePagerActivity.class);
				intent.putExtra("batchId", "do#5#"+correctCount+"#");
				startActivity(intent);
			}
		}else if (v.getId()==R.id.ct_do2) {//点击不可移除错题
			Intent intent =new Intent();
			intent.setClass(CourseWareLxCtActivity.this,PracticePagerActivity.class);
			intent.putExtra("batchId", "do#6#"+errorCount+"#");
			startActivity(intent);
		}*/
	}

	//返回当前页面重新加载数据
	public void onResume() {
		// TODO Auto-generated method stub
        if(hdaplication.getStatus().equals("0")){
            getData();
        }else {
            PracticLearnBehavior practicLearnBehavior=hdaplication.getPracticLearnBehavior();
            count=practicLearnBehavior.getQuestionErrorCount()+"";
            errorCount=practicLearnBehavior.getLastErrorCount()+"";
            correctCount=(practicLearnBehavior.getQuestionErrorCount()-practicLearnBehavior.getLastErrorCount())+"";
            sendMessage(1);
        }
		super.onResume();
	}

    //返回退出错题页
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hdaplication.setStatus("1");
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    public class PrivateListingAdapter extends BaseAdapter implements
            SlideView.OnSlideListener {
        private static final String TAG = "SlideAdapter";

        private Context mContext;
        private LayoutInflater mInflater;

        private List<MessageBean> mMessageItems = new ArrayList<MessageBean>();
        private SlideView mLastSlideViewWithStatusOn;

        public PrivateListingAdapter(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }

        public void setmMessageItems(List<MessageBean> mMessageItems) {
            this.mMessageItems = mMessageItems;
        }

        @Override
        public int getCount() {
            if (mMessageItems == null) {
                mMessageItems = new ArrayList<MessageBean>();
            }
            return mMessageItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessageItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(position == 0){
                ViewHolder holder;
                SlideView slideView = (SlideView) convertView;
                if (slideView == null) {
                    View itemView = mInflater.inflate(R.layout.course_ware_lx_ct_item,
                            null);

                    slideView = new SlideView(mContext);
                    slideView.setContentView(itemView);

                    holder = new ViewHolder(slideView);
                    slideView.setOnSlideListener(this);
                    slideView.setTag(holder);
                } else {
                    holder = (ViewHolder) slideView.getTag();
                }
                MessageBean item = mMessageItems.get(position);
                item.slideView = slideView;
                item.slideView.shrink();

                // holder.ct_iv.setImageResource(item.ct_iv);
                holder.ct_tvtitle.setText(item.ct_tvtitle);
                holder.ct_tv.setText(item.ct_tv);
                holder.deleteHolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (correctCount.equals("0")) {
                            Toast.makeText(CourseWareLxCtActivity.this, "没有相关的信息", Toast.LENGTH_SHORT);
                        }else {
                            alertDialog=new MaterialDialog(CourseWareLxCtActivity.this);
                            alertDialog.setTitle("提示");
                            alertDialog.setMessage("可移除错题 共"+correctCount+"道");
                            alertDialog.setNegativeButton("取消",new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.setPositiveButton("全部移除",new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    removeError();
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }
                    }
                });

                return slideView;
            }else{
                View itemView = mInflater.inflate(R.layout.course_ware_lx_ct_item,
                        null);

                ViewHolder holder = new ViewHolder(itemView);

                MessageBean item = mMessageItems.get(position);

                // holder.ct_iv.setImageResource(item.ct_iv);
                holder.ct_tvtitle.setText(item.ct_tvtitle);
                holder.ct_tv.setText(item.ct_tv);

                return itemView;
            }
        }

        private class ViewHolder {
            public ImageView ct_iv;
            public TextView ct_tvtitle;
            public TextView ct_tv;
            public ViewGroup deleteHolder;

            ViewHolder(View view) {
                ct_iv = (ImageView) view.findViewById(R.id.ct_iv);
                ct_tvtitle = (TextView) view.findViewById(R.id.ct_tvtitle);
                ct_tv = (TextView) view.findViewById(R.id.ct_tv);
                deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
            }
        }

        @Override
        public void onSlide(View view, int status) {
            if (mLastSlideViewWithStatusOn != null
                    && mLastSlideViewWithStatusOn != view) {
                mLastSlideViewWithStatusOn.shrink();
            }

            if (status == SLIDE_STATUS_ON) {
                mLastSlideViewWithStatusOn = (SlideView) view;
            }
        }
    }

}
