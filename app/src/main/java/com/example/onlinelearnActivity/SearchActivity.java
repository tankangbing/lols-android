package com.example.onlinelearnActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.SearchAdapter;
import com.example.entity.SearchItem;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.courseWare.document.DocumentActivity;
import com.example.onlinelearnActivity.courseWare.practic.CourseWareChapterTestActivity;
import com.example.onlinelearnActivity.courseWare.test.CourseWareTestActivity;
import com.example.onlinelearnActivity.courseWare.video.VideoActivity;
import com.example.onlinelearnActivity.search.SearchTypeActivity;
import com.example.spt.CourseUtil;
import com.example.spt.jaxb.course.Item;
import com.example.spt.jaxb.course.Manifest;
import com.example.util.FinalStr;
import com.example.util.HttpUtil;
import com.example.util.SysUtil;
import com.example.util.XStreamUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by ysg on 2018/1/23.
 */

public class SearchActivity extends BaseActivity {

    List<Item> tItems = null;
    List<SearchItem> itemSearch = new ArrayList<SearchItem>();

    @BindView(R.id.search_list)
    ListView search_lv;//搜索结果显示列表
    @BindView(R.id.search_btn)
    Button search_btn;//搜索按钮
    @BindView(R.id.search_content)
    EditText search_content;//搜索内容
    @BindView(R.id.search_retrun)
    TextView search_retrun;//返回按钮
    @BindView(R.id.search_icon_lock)
    TextView search_icon_lock;//搜索图标
    @BindView(R.id.search_icon_close)
    TextView search_icon_close;//删除内容按钮
    @BindView(R.id.search_type)
    LinearLayout search_type;//选择搜索类型

    SearchAdapter searchAdapter;
    boolean isShow = false;
    private Typeface iconfont;
    String coursewareId= "";
    String searchType= "0"; // 搜索类型 0目录 1 问答
    String nodeId = "";
  /*  @BindView(R.id.search_selecttype)
    Spinner search_selecttype;//下拉框 选择搜索类型*/

    //定义一个String类型的List数组作为数据源
    private List<String> dataList;

    //定义一个ArrayAdapter适配器作为spinner的数据适配器
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
    @Override
    protected void initViews(View view) {
        dataList = new ArrayList<String>();
        dataList.add("目录");
        dataList.add("问答");
        iconfont = Typeface.createFromAsset(mContext.getAssets(), "iconfont.ttf");

        search_retrun.setTypeface(iconfont);
        search_icon_lock.setTypeface(iconfont);
        search_icon_close.setTypeface(iconfont);
       /* adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dataList);
        //为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //为spinner绑定我们定义好的数据适配器
        search_selecttype.setAdapter(adapter);

        //为spinner绑定监听器，这里我们使用匿名内部类的方式实现监听器
        search_selecttype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                textView.setText("您当前选择的是："+adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                textView.setText("请选择您的城市");

            }
        });*/



    }

    public void click(View v){
        if(v.getId() == R.id.search_btn||v.getId() == R.id.search_icon_lock){
            String content = search_content.getText().toString();
            itemSearch = searchContent(content);
            if(itemSearch.size() > 5){
                isShow = false;
            }else{
                isShow = true;
            }
            searchAdapter =new SearchAdapter(this,itemSearch,content ,isShow);
            search_lv.setAdapter(searchAdapter);
            search_type.setVisibility(View.GONE);
            search_lv.setVisibility(View.VISIBLE);
        }else if(v.getId() == R.id.search_type_ml){
            Intent intent = new Intent();
            intent.setClass(this, SearchTypeActivity.class);
            intent.putExtra("searchType", "0");
            startActivity(intent);
        }else if(v.getId() == R.id.search_type_wd){
            Intent intent = new Intent();
            intent.setClass(this, SearchTypeActivity.class);
            intent.putExtra("searchType", "1");
            startActivity(intent);
        }else if(v.getId() == R.id.search_retrun){
            finish();
        }else if(v.getId() == R.id.search_icon_close){
            search_content.setText("");
        }
    }

    public List<SearchItem>  searchContent(String content){
        List<SearchItem> itemS = new ArrayList<SearchItem>();
        if (tItems == null ){
            String xmlpath =getFilesDir().getPath()+"/course_ware";
            File filePath=new File(xmlpath,hdaplication.getCoursewareId()+".xml");
            Manifest manifest=new XStreamUtil().str2JavaFormXML(filePath,Manifest.class);
            tItems = CourseUtil.getCourseOrganization(manifest);
        }
        if(tItems!=null){//修复<搜索>，没有课程信息的课程，点击搜索会闪退
            for(Item itemY: tItems){//遍历一级目录
                List<Item> itemss = itemY.getItem();
                for(Item itemE: itemss){//遍历二级目录
                    String titles = itemE.getTitle();
                    if (!titles.contains(content)){
                        List<Item> items = itemE.getItem();
                        if(items!=null){//修复<搜索>，点击进入“阴影与透视”课程，输入关键字点击搜索，闪退
                            for(Item item: items){//遍历三级目录
                                String title = item.getTitle();
                                if(title.contains(content)){
                                    SearchItem searchItem = new SearchItem();
                                    searchItem.setTitle(item.getTitle());
                                    searchItem.setIdentifier(item.getIdentifier());
                                    searchItem.setNodeId(itemE.getIdentifier());
                                    searchItem.setNodeTitle(itemE.getTitle());
                                    String behavior=(String) item.getIdentifierref().subSequence(0, 1);
                                    searchItem.setType(behavior);
                                    if (behavior.equals("0")){
                                        searchItem.setShowAsk(item.getShowAsk());
                                        searchItem.setShowEvaluate(item.getShowEvaluate());
                                        searchItem.setShowNotice(item.getShowNotice());
                                        searchItem.setShowSchedule(item.getShowSchedule());
                                    }
                                    itemS.add(searchItem);
                                }
                            }
                        }

                    }else{
                        List<Item> items = itemE.getItem();
                        if(items!=null){//修复<搜索>，点击进入“阴影与透视”课程，输入关键字点击搜索，闪退
                            for(Item item: items){//遍历三级目录
                                SearchItem searchItem = new SearchItem();
                                searchItem.setTitle(item.getTitle());
                                searchItem.setIdentifier(item.getIdentifier());
                                searchItem.setNodeId(itemE.getIdentifier());
                                searchItem.setNodeTitle(itemE.getTitle());
                                String behavior=(String) item.getIdentifierref().subSequence(0, 1);
                                searchItem.setType(behavior);
                                if (behavior.equals("0")){
                                    searchItem.setShowAsk(item.getShowAsk());
                                    searchItem.setShowEvaluate(item.getShowEvaluate());
                                    searchItem.setShowNotice(item.getShowNotice());
                                    searchItem.setShowSchedule(item.getShowSchedule());
                                }
                                itemS.add(searchItem);
                            }
                        }

                    }
                }
            }
        }
        return itemS;
    }

    /**
     * 设置监听
     */
    public void setListener(){
        search_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ((isShow && i>0 && i<= itemSearch.size()) || (!isShow && i <= 5 )){
                    SearchItem item =  itemSearch.get(i-1);
                    hdaplication.setBehaviorId(item.getIdentifier());
                    nodeId=item.getNodeId();
                    //提交当前学习行为与父节点id
                    setClickRecord();
                    if(item.getType().equals("0")){ //视频
                        Intent intent = new Intent();
                        intent.setClass(SearchActivity.this, VideoActivity.class);
                        intent.putExtra("fileName",item.getTitle());
                        intent.putExtra("chapterId", item.getIdentifier());
                        intent.putExtra("showSchedule", item.getShowSchedule());
                        intent.putExtra("showNotice", item.getShowNotice());
                        intent.putExtra("showEvaluate", item.getShowEvaluate());
                        intent.putExtra("showAsk", item.getShowAsk());
                        startActivity(intent);
                    } else if (item.getType().equals("1")) {//文档
                        Intent intent = new Intent();
                        intent.setClass(SearchActivity.this, DocumentActivity.class);
                        String chapterName = item.getNodeTitle();
                        String fileName =item.getTitle();
                        intent.putExtra("fileName",fileName);
                        intent.putExtra("chapterName",chapterName);
                        startActivity(intent);
                    } else if (item.getType().equals("2")) {//网页
//                        Intent intent = new Intent();//修复<搜索>，查询到网页链接，点击，闪退
                        //intent.setClass(getActivity(), TaskActivity.class);
//                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"app不支持查看网页，请使用电脑打开",Toast.LENGTH_SHORT).show();
                    } else if (item.getType().equals("3")) {//练习
                        Intent intent = new Intent();
                        intent.setClass(SearchActivity.this, CourseWareChapterTestActivity.class);
                        intent.putExtra("title", item.getTitle());
                        startActivity(intent);
                    } else if (item.getType().equals("4")) {//测试
                        Intent intent = new Intent();
                        intent.setClass(SearchActivity.this, CourseWareTestActivity.class);
                        intent.putExtra("title", item.getTitle());
                        startActivity(intent);
                    } else if (item.getType().equals("5")) {//考试

                    }
                }else if ((isShow && i == itemSearch.size() + 2) || (!isShow && i == 8)){
                    Intent intent = new Intent();
                    intent.setClass(SearchActivity.this, SearchTypeActivity.class);
                    intent.putExtra("searchType", "1");
                    startActivity(intent);
                }else if (!isShow && i == 6){// 显示更多数据
                    isShow = true;
                    searchAdapter.setShow(true);
                    searchAdapter.notifyDataSetChanged();
                }
            }
        });

        //取消loginType
        search_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(search_content.getText().length() != 0){
                    search_icon_close.setVisibility(View.VISIBLE);
                }else{
                    search_icon_close.setVisibility(View.GONE);
                }
            }
        });
    }
    @Override
    protected void doBusiness(Context mContext) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    /**
     * 设置toolbar内容
     */
    public void setToolbar(){
        super.setToolbar();
        setToobbarShow(false);
    }

    public void setClickRecord(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Object> map = new HashMap<String,Object>(0);
                map.put("accountId", hdaplication.getStuid());
                map.put("classId", hdaplication.getClassId());
                map.put("behaviorId", hdaplication.getBehaviorId());
                map.put("nodeId", nodeId);
                String result = HttpUtil.getResultByPost(FinalStr.ACCESSPRYPATH+"/appControler.do?setClickRecord",
                        map,SearchActivity.this,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                }else{
                }
            }
        }).start();
    }
}
