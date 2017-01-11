package com.edu.feicui.starskymusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.edu.feicui.starskymusic.fragment.LocalDocumentFragment;
import com.edu.feicui.starskymusic.fragment.LocalOneFragment;
import com.edu.feicui.starskymusic.fragment.LocalSongerFragment;
import com.edu.feicui.starskymusic.fragment.LocalSpecialFragment;

/**
 * Created by user on 2017/1/11.
 */

public class AllFragmentAdapter extends FragmentPagerAdapter {

    private LocalOneFragment mOneFragment;
    private LocalSpecialFragment mSpecialFragment;
    private LocalSongerFragment mSongerFragment;
    private LocalDocumentFragment mDocumentFragment;

    public AllFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if(mOneFragment == null){
                    mOneFragment = new LocalOneFragment();
                }
                return mOneFragment;
            case 1:
                if(mSpecialFragment == null){
                    mSpecialFragment = new LocalSpecialFragment();
                }
                return mSpecialFragment;
            case 2:
                if(mSongerFragment == null){
                    mSongerFragment = new LocalSongerFragment();
                }
                return mSongerFragment;
            case 3:
                if(mDocumentFragment == null){
                    mDocumentFragment = new LocalDocumentFragment();
                }
                return mDocumentFragment;
            default:
                throw new RuntimeException("未知错误");
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
