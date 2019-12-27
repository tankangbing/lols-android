package com.example.onlinelearnActivity.task;

/**
 * Created by ysg on 2017/7/19.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.onlinelearn.R;


public class ChoseFileToolActivity extends Activity {
    private final static String TAG = "UPLOADTOL:ChoseFileToolActivity";
    public final static int resultCode = 0x11;

    ListView listView ;
    TextView textView ;
    ImageView btnParent;
    //记录当前的父类文件夹
    File currentParent;
    //记录当前路径下所有文件的文件数组
    File[] currentFiles;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_file);

        //获取列表全部文件的ListView
        listView = (ListView) findViewById(R.id.file_list);
        textView = (TextView) findViewById(R.id.file_path);
        btnParent = (ImageView) findViewById(R.id.tshow_retrun);
        //获取系统的SD卡目录
        final File root = new File("/storage/");
        //如果SD卡存在
        if(root.exists()){
            currentParent = root ;
            currentFiles = root.listFiles();
            //使用当前目录下的全部文件，文件夹填充ListView
            inflateListView(currentFiles);
            //为listView的列表项的单击事件绑定监听器
            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int postion, long id) {
                    //单击文件,返回文件路径
                    if( currentFiles[postion].isFile() ){
                        //TODO
                        String path = currentFiles[postion].getPath();
                        String fileName=currentFiles[postion].getName();
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("fileName",fileName);
                        bundle.putString("current_path",path);
                        intent.putExtras(bundle);
                        setResult( resultCode, intent );
                        finish();
                    } else {
                        //获取点击文件夹下面的所有的文件
                        File[] tep = currentFiles[postion].listFiles();
                        if( tep == null || tep.length == 0){
                            Toast.makeText(ChoseFileToolActivity.this, "该文件夹下没有文件", Toast.LENGTH_SHORT).show();
                        } else {
                            //获取单击的列表的列表项对应的文件夹，设为当前的父类文件夹
                            currentParent = currentFiles[postion];
                            //保存当前的父文件夹内全部的文件和文件夹
                            currentFiles = tep;
                            //再次更新ListView
                            inflateListView(currentFiles);
                        }
                    }
                }
            });
            //获取上一级目录的按键
            btnParent.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    try{
                        if ( !currentParent.getCanonicalPath().equals("/storage") ) {
                            //获取上一层目录
                            currentParent = currentParent.getParentFile();
                            currentFiles = currentParent.listFiles();
                            //再次更新listView
                            inflateListView(currentFiles);
                        }else {
                            finish();
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void inflateListView(File[] files){
        //创建一个List集合，List集合的元素是 Map
        List< Map<String,Object> > listItems = new ArrayList< Map<String,Object> >();
        List< Map<String,Object> > listItemFile = new ArrayList< Map<String,Object> >();
        File[] currentFiles=new File[files.length-1];
        for (int i=0 ; i<files.length ; i++) {
            Map<String,Object> listItem = new HashMap<String,Object>();
            if (files[i].isFile()) {
                //是文件
                listItem.put("icon", R.drawable.file);
                listItem.put("fileName", files[i].getName());
                listItem.put("index", i);
                listItems.add(listItem);
                listItemFile.add(listItem);
            } else {
                //是文件夹
                listItem.put("icon", R.drawable.files);
                listItem.put("fileName", files[i].getName());
                listItem.put("index", i);
                //添加到List 项
                listItems.add(listItem);
            }
        }
        //listItems.addAll(listItemFile);
        //创建一个 SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.line,
                new String[]{"icon", "fileName"},
                new int[]{R.id.icon, R.id.file_name} );
        //为listView设置 Adapter
        listView.setAdapter(simpleAdapter);
        try {
            textView.setText("当前路径为: "+currentParent.getCanonicalPath());
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    //返回退出app
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try{
                if ( !currentParent.getCanonicalPath().equals("/storage") ) {
                    //获取上一层目录
                    currentParent = currentParent.getParentFile();
                    currentFiles = currentParent.listFiles();
                    //再次更新listView
                    inflateListView(currentFiles);
                }else {
                    finish();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
