package com.example.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseFragmentAdapter
 * viewPage +fragment使用
 */
public class BaseFragmentAdapter extends FragmentPagerAdapter {

    //Fragment List
    protected List<Fragment> mFragmentList;

    //标题 List
    protected String[] mTitles;

    public BaseFragmentAdapter(FragmentManager fm) {
        this(fm, null, null);
    }

    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] mTitles) {
        super(fm);
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }
        this.mFragmentList = fragmentList;
        this.mTitles = mTitles;
    }

    public void add(Fragment fragment) {
        if (isEmpty()) {
            mFragmentList = new ArrayList<>();
        }
        mFragmentList.add(fragment);
    }

    /**
     * 返回当前fragment
     * @param position
     * @return
     */
    public Fragment getItem(int position) {
        return isEmpty() ? null : mFragmentList.get(position);
    }

    /**
     * 返回Fragment List Size
     * @return
     */
    public int getCount() {
        return isEmpty() ? 0 : mFragmentList.size();
    }

    /**
     * 判断Fragment List是否为空
     * @return
     */
    public boolean isEmpty() {
        return mFragmentList == null;
    }

    /**
     * 返回当前Fragment 的title
     * @param position
     * @return
     */
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }


}
