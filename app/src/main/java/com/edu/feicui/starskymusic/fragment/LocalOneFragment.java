package com.edu.feicui.starskymusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.feicui.starskymusic.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by user on 2017/1/10.
 */

public class LocalOneFragment extends Fragment {

    private Unbinder mUnbinder;
    View view;

    @BindView(R.id.iv_cirplay)
    ImageView mIvCirplay;
    @BindView(R.id.iv_set)
    ImageView mIvSet;
    @BindView(R.id.ll_normal)
    LinearLayout mLlNormal;
    @BindView(R.id.iv_check)
    ImageView mIvCheck;
    @BindView(R.id.tv_set_cancel)
    TextView mTvSetCancel;
    @BindView(R.id.ll_set)
    LinearLayout mLlSet;
    @BindView(R.id.rv_one)
    RecyclerView mRvOne;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.loacl_one_fragment, container, false);
        }
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.iv_cirplay, R.id.iv_set, R.id.iv_check, R.id.tv_set_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cirplay:
                break;
            case R.id.iv_set:
                break;
            case R.id.iv_check:
                break;
            case R.id.tv_set_cancel:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    //...........................

    
}
