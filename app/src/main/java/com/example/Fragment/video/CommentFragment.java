package com.example.Fragment.video;

/**
 * Created by DELL on 2017/6/28.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinelearn.R;

public class CommentFragment extends Fragment {
    private View view;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =
                (View) inflater.inflate(R.layout.video_comment_fragment, container, false);
        return view;
    }

}