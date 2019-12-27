package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.entity.TaskModel;
import com.example.onlinelearn.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by ysg on 2017/7/17.
 */

public class TaskListViewAdapter extends ArrayAdapter {

    List<TaskModel> taskList;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private LayoutInflater mInflater;
    public TaskListViewAdapter(Context context, int textViewResourceId,
                               List objects) {
        super(context, textViewResourceId, objects);
        taskList=objects;
    }
    public int getCount() {
        return taskList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int i, View convertView, ViewGroup parent) {
        View v;
        if (convertView==null){
            v=mInflater.inflate(R.layout.task_item, null);

        }else {
            v=convertView;
        }
        v.setTag(taskList.get(i).getId());
        TextView task_item_title=(TextView)v.findViewById(R.id.task_item_title);
        TextView task_item_date=(TextView)v.findViewById(R.id.task_item_date);
        TextView task_item_tv3=(TextView)v.findViewById(R.id.task_item_tv3);
        TextView task_item_ms=(TextView)v.findViewById(R.id.task_item_ms);
        TextView task_item_fbz=(TextView)v.findViewById(R.id.task_item_fbz);
        task_item_ms.setText(taskList.get(i).getTaskDescribe());
        LinearLayout task_item_click=(LinearLayout)v.findViewById(R.id.task_item_click);
        task_item_click.setTag(taskList.get(i).getId());
        LinearLayout task_item_msclick=(LinearLayout)v.findViewById(R.id.task_item_msclick);
        task_item_msclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView task_item_ms=(TextView)view.findViewById(R.id.task_item_ms);
                if (task_item_ms.getVisibility()==View.GONE){
                    task_item_ms.setVisibility(View.VISIBLE);
                }else {
                    task_item_ms.setVisibility(View.GONE);
                }
            }
        });
        String status="";
        if (taskList.get(i).getTaskStatus().equals("0")){
            status="已保存";
        }else if (taskList.get(i).getTaskStatus().equals("1")){
            status="已发布";
        }
        task_item_title.setText(taskList.get(i).getTaskName());
        task_item_date.setText("作业日期："+sdf.format(taskList.get(i).getStartDate())+" 至 "+sdf.format(taskList.get(i).getSubmitEndDate()));
        task_item_tv3.setText(status);
        return v;
    }
}
