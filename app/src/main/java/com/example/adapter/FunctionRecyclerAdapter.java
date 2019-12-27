package com.example.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.onlinelearn.R;
import com.example.util.SpUtils;

import java.util.List;

/**
 * 须知页面的数据加载
 */
public class FunctionRecyclerAdapter extends BaseRecyclerAdapter<String> {

    private Context context;
    private TextView function_name;//章节名称
    private TextView more;
    private TextView dot;

    public FunctionRecyclerAdapter(Context context, List<String> datas) {
        super(context, R.layout.list_function_item, datas);
        this.context =context;
    }

    @Override
    public void convert(BaseRecyclerHolder holder,String item, int position) {

        function_name= holder.getView(R.id.function_name);
        more =holder.getView(R.id.more);
        dot =holder.getView(R.id.dot);
        function_name.setText(item);
        more.setTypeface(iconfont);
        dot.setTypeface(iconfont);
        String oldv =SpUtils.getString(context,SpUtils.OLD_VERSION);
        String newv =SpUtils.getString(context,SpUtils.NEW_VERSION);

        if (!(oldv.equals(newv)) && item.equals("版本更新")){
            dot.setVisibility(View.VISIBLE);

        }else{
            dot.setVisibility(View.INVISIBLE);
        }


    }

}
