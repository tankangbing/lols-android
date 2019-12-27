package com.example.Fragment.video;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.Fragment.BaseFragment;
import com.example.adapter.VideoNoticeAdapter;
import com.example.onlinelearn.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NoticeFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;//加载


    List<String> mDatas = new ArrayList<>();
    private VideoNoticeAdapter mAdapter;


    @Override
    protected void initView(View view) {
        ButterKnife.bind(this,view);


        mDatas.add("");//设置只有一个数据项

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
       /* DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext,
                LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);*/
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new VideoNoticeAdapter(mContext, mDatas);
        mRecyclerView.setAdapter(mAdapter);

        //设置无任务风格
        refreshLayout.setRefreshHeader(new FalsifyHeader(getActivity()));

        refreshLayout.setRefreshFooter(new FalsifyFooter(getActivity()));

        refreshLayout.setReboundDuration(500);//回弹动画时长（毫秒）
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



    /**
     * 加载layout
     * @return
     */
    protected int getLayoutId() {
        return R.layout.fragment_list_layout;
    }

    @Override
    public void fetchData() {

    }
}
