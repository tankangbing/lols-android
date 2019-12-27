package com.example.onlinelearnActivity.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adapter.SearchAdapter;
import com.example.entity.SearchItem;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.BaseActivity;
import com.example.onlinelearnActivity.SearchActivity;
import com.example.onlinelearnActivity.courseWare.document.DocumentActivity;
import com.example.onlinelearnActivity.courseWare.practic.CourseWareChapterTestActivity;
import com.example.onlinelearnActivity.courseWare.test.CourseWareTestActivity;
import com.example.onlinelearnActivity.courseWare.video.VideoActivity;
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
 * Created by ysg on 2018/1/26.
 */

public class SearchTypeActivity extends BaseActivity {
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
    @BindView(R.id.search_type)
    LinearLayout search_type;//选择搜索类型
    @BindView(R.id.search_icon_lock)
    TextView search_icon_lock;//搜索图标


    boolean isShow = false;
    private Typeface iconfont;
    String coursewareId= "";
    String searchType= "0"; // 搜索类型 0目录 1 问答
    String nodeId = "";
  /*  @BindView(R.id.search_selecttype)
    Spinner search_selecttype;//下拉框 选择搜索类型*/

    //定义一个String类型的List数组作为数据源
    private List<String> dataList;
    SearchAdapter searchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search_type.setVisibility(View.GONE);
        searchType = getIntent().getExtras().getString("searchType");
        if(searchType.equals("1")){//问答搜索 暂不支持
            search_content.setText("暂不支持问题搜索！");
            search_content.setEnabled(false);
        }
    }
    @Override
    protected void initViews(View view) {
        iconfont = Typeface.createFromAsset(mContext.getAssets(), "iconfont.ttf");

        search_retrun.setTypeface(iconfont);
        search_icon_lock.setTypeface(iconfont);
    }

    public void click(View v){
        if(v.getId() == R.id.search_btn){
            String content = search_content.getText().toString();
            if(searchType.equals("0")){
                itemSearch = searchContent(content);
            }else if(searchType.equals("1")){

            }
            if(itemSearch.size() > 5){
                isShow = false;
            }else{
                isShow = true;
            }
            searchAdapter =new SearchAdapter(this,itemSearch,content,isShow);
            search_lv.setAdapter(searchAdapter);
            search_lv.setVisibility(View.VISIBLE);
        }else if(v.getId() == R.id.search_retrun){
            finish();
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
        for(Item itemY: tItems){//遍历一级目录
            List<Item> itemss = itemY.getItem();
            for(Item itemE: itemss){//遍历二级目录
                String titles = itemE.getTitle();
                if (!titles.contains(content)){
                    List<Item> items = itemE.getItem();
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
                }else{
                    List<Item> items = itemE.getItem();
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
                        intent.setClass(SearchTypeActivity.this, VideoActivity.class);
                        intent.putExtra("fileName",item.getTitle());
                        intent.putExtra("chapterId", item.getIdentifier());
                        intent.putExtra("showSchedule", item.getShowSchedule());
                        intent.putExtra("showNotice", item.getShowNotice());
                        intent.putExtra("showEvaluate", item.getShowEvaluate());
                        intent.putExtra("showAsk", item.getShowAsk());
                        startActivity(intent);
                    } else if (item.getType().equals("1")) {//文档
                        Intent intent = new Intent();
                        intent.setClass(SearchTypeActivity.this, DocumentActivity.class);
                        String chapterName = item.getNodeTitle();
                        String fileName =item.getTitle();
                        intent.putExtra("fileName",fileName);
                        intent.putExtra("chapterName",chapterName);
                        startActivity(intent);
                    } else if (item.getType().equals("2")) {//网页
                        Intent intent = new Intent();
                        //intent.setClass(getActivity(), TaskActivity.class);
                        startActivity(intent);
                    } else if (item.getType().equals("3")) {//练习
                        Intent intent = new Intent();
                        intent.setClass(SearchTypeActivity.this, CourseWareChapterTestActivity.class);
                        intent.putExtra("title", item.getTitle());
                        startActivity(intent);
                    } else if (item.getType().equals("4")) {//测试
                        Intent intent = new Intent();
                        intent.setClass(SearchTypeActivity.this, CourseWareTestActivity.class);
                        intent.putExtra("title", item.getTitle());
                        startActivity(intent);
                    } else if (item.getType().equals("5")) {//考试

                    }
                }else if ((isShow && i == itemSearch.size() + 2) || (!isShow && i == 8)){
                    Intent intent = new Intent();
                    intent.setClass(SearchTypeActivity.this, SearchTypeActivity.class);
                    intent.putExtra("searchType", "1");
                    startActivity(intent);
                }else if (!isShow && i == 6){// 显示更多数据
                    isShow = true;
                    searchAdapter.setShow(true);
                    searchAdapter.notifyDataSetChanged();
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
                        map,SearchTypeActivity.this,null);
                if(SysUtil.isBlank(result) || "NO_NET".equals(result)){
                }else{
                }
            }
        }).start();
    }
}
