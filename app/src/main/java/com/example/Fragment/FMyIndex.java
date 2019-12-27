package com.example.Fragment;

import com.example.adapter.DefaultAdapter;
import com.example.adapter.FunctionRecyclerAdapter;
import com.example.entity.FileInfo;
import com.example.entity.LearnClassEntity;
import com.example.jdbc.Dao.FileDAO;
import com.example.jdbc.Dao.impl.FileDAOImpl;
import com.example.onlinelearn.R;
import com.example.onlinelearnActivity.CourseActivity;
import com.example.onlinelearnActivity.FuWuActivity;
import com.example.onlinelearnActivity.MyClassActivity;
import com.example.onlinelearnActivity.MyDownloadActivity;
import com.example.onlinelearnActivity.MyInfoActivity;
import com.example.onlinelearnActivity.MyProgressActivity;
import com.example.onlinelearnActivity.VersionActivity;
import com.example.util.ApplicationUtil;
import com.example.util.FileUtil;
import com.example.util.FinalStr;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FMyIndex extends BaseFragment{

    List<String> functionNames ;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;//数据列表
    @BindView(R.id.username_tv)
    TextView username_tv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;//加载

    private FunctionRecyclerAdapter functionRecyclerAdapter;
    private ApplicationUtil hdaplication;//全局参数对象
    private FileDAO fileDAO = null; //文件dao

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
        hdaplication = (ApplicationUtil)getActivity().getApplication();
        username_tv.setText(hdaplication.getUserXm());
        fileDAO = new FileDAOImpl(getActivity());

        functionNames =new ArrayList<>();

        //功能模块增删
        if (FinalStr.IS_MY_INFO){
            functionNames.add("我的信息");
        }

        functionNames.add("我的课程");

        if (FinalStr.IS_MY_PROGRESS){
            functionNames.add("我的进度");
        }
        /*if (FinalStr.IS_MY_DOWNLOAD){
            functionNames.add("我的下载");//修复去掉我的下载功能
        }*/
        functionNames.add("清除缓存");
        functionNames.add("设置");
        functionNames.add("隐私政策");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        functionRecyclerAdapter = new FunctionRecyclerAdapter(getActivity(), functionNames);
        recyclerView.setAdapter(functionRecyclerAdapter);

        //设置无任务风格
        refreshLayout.setRefreshHeader(new FalsifyHeader(getActivity()));

        refreshLayout.setRefreshFooter(new FalsifyFooter(getActivity()));

        refreshLayout.setReboundDuration(500);//回弹动画时长（毫秒）

        setListener();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_function;
    }

    @Override
    public void fetchData() {

    }



    /**
     * 设置监听器
     */
    private void setListener() {

        /**
         * 问题咧监听
         */
        functionRecyclerAdapter.setOnItemClickListener(new DefaultAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, RecyclerView.ViewHolder holder, Object o, int position) {
                Intent intent=new Intent();

                switch (functionNames.get(position)){
                    case "我的课程":
                        intent.setClass(getActivity(),MyClassActivity.class);
                        startActivity(intent);
                        break;
                    case "我的下载":
                        intent.setClass(getActivity(),MyDownloadActivity.class);
                        startActivity(intent);
                        break;
                    case "设置":
                        intent.setClass(getActivity(),VersionActivity.class);
                        startActivity(intent);
                        break;
                    case "我的信息":
                        intent.setClass(getActivity(),MyInfoActivity.class);
                        startActivity(intent);
                        break;
                    case "我的进度":
                        intent.setClass(getActivity(),MyProgressActivity.class);
                        startActivity(intent);
                        break;
                    case "隐私政策":
                        intent.setClass(getActivity(),FuWuActivity.class);
                        startActivity(intent);
                        break;
                    case "清除缓存":

                        List<FileInfo> fileInfos =fileDAO.queryAllFiles(hdaplication.getStuid());
                        Log.d("XX",fileInfos.size()+"");
                        if (fileInfos.size()==0){
                            Log.d("XX",fileInfos.size()+"1111111111111");
                            ((CourseActivity)getActivity()).showMessageDialog("暂没有缓存数据");
                            return;
                        }
                        //删除文件表里面的
                        if (fileDAO.deleteFiles(fileInfos)){
                            //删除磁盘里面的
                            for (FileInfo fileInfo :fileInfos){
                                File file = FileUtil.isExist(FinalStr.DOWNLOAD_PATH,fileInfo.getAttach_name());
                                String fileName =fileInfo.getBehavior_id()+fileInfo.getFile_type();
                                File file2 =FileUtil.isExist(FinalStr.DOWNLOAD_PATH,fileName);
                                if (file!=null){
                                    file.delete();
                                }
                                if (file2!=null){
                                    file2.delete();
                                }
                            }
                            ((CourseActivity)getActivity()).showMessageDialog("清除缓存成功");
                        }else{
                            ((CourseActivity)getActivity()).showMessageDialog("清除缓存失败");
                        }

                        break;

                }

            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
            }
        });
    }


}
