package com.edu.feicui.starskymusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.edu.feicui.starskymusic.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by user on 2017/1/9.
 */

public class LocalHomeFragment extends Fragment {


    private FragmentManager mFragmentManager;
    private LocalAllFragment mAllFragment;
    View view;

    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R.id.tv_music_name)
    TextView mTvMusicName;
    @BindView(R.id.tv_all_music_num)
    TextView mTvAllMusicNum;
    @BindView(R.id.tv_download_music_num)
    TextView mTvDownloadMusicNum;
    @BindView(R.id.tv_collect_music_num)
    TextView mTvCollectMusicNum;
    @BindView(R.id.tv_recently_music_num)
    TextView mTvRecentlyMusicNum;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.local_home_fragment, container, false);
        }
        mUnbinder = ButterKnife.bind(this, view);
        mFragmentManager = getChildFragmentManager();
        return view;
    }

    @OnClick({R.id.iv_all, R.id.iv_download, R.id.iv_collect, R.id.iv_recently_play})
    public void onClick(View view) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mFlContent.setVisibility(View.VISIBLE);
        switch (view.getId()) {
            case R.id.iv_all:
                if (mAllFragment == null) {
                    mAllFragment = new LocalAllFragment();
                }
                transaction.add(R.id.fl_content,mAllFragment, "AllFragment");
                break;
            case R.id.iv_download:
                break;
            case R.id.iv_collect:
                break;
            case R.id.iv_recently_play:
                break;
        }
        transaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
