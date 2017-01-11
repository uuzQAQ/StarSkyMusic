package com.edu.feicui.starskymusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edu.feicui.starskymusic.R;
import com.edu.feicui.starskymusic.adapter.AllFragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by user on 2017/1/10.
 */

public class LocalAllFragment extends Fragment {


    private Unbinder mUnbinder;
    private AllFragmentAdapter adapter;
    View view;

    @BindView(R.id.tv_one)
    TextView mTvOne;
    @BindView(R.id.tv_special)
    TextView mTvSpecial;
    @BindView(R.id.tv_songer)
    TextView mTvSonger;
    @BindView(R.id.tv_document)
    TextView mTvDocument;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.local_play_type_fragment, container, false);
        }
        initView();
        return view;
    }

    @OnClick({R.id.tv_one, R.id.tv_special, R.id.tv_songer, R.id.tv_document})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_one:
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.tv_special:
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.tv_songer:
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.tv_document:
                mViewPager.setCurrentItem(3, false);
                break;
            default:
                throw new RuntimeException("未知错误");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }


    //初始化视图
    private void initView() {
        mUnbinder = ButterKnife.bind(this, view);
        adapter = new AllFragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(listener);
        mTvOne.setSelected(true);
    }


    //viewPager监听->Button的切换
    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //Button，UI改变
            mTvOne.setSelected(position == 0);
            mTvSpecial.setSelected(position == 1);
            mTvSonger.setSelected(position == 2);
            mTvDocument.setSelected(position == 3);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
