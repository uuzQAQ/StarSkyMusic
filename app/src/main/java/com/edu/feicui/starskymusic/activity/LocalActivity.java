package com.edu.feicui.starskymusic.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.feicui.starskymusic.R;
import com.edu.feicui.starskymusic.fragment.LocalAllFragment;
import com.edu.feicui.starskymusic.fragment.LocalHomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocalActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private LocalHomeFragment mLocalHomeFragment;

    @BindView(R.id.iv_sliding_menu)
    ImageView mIvSlidingMenu;
    @BindView(R.id.tv_net)
    TextView mTvNet;
    @BindView(R.id.tv_local)
    TextView mTvLocal;
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R.id.tv_music_name)
    TextView mTvMusicName;
    @BindView(R.id.iv_play)
    ImageView mIvPlay;
    @BindView(R.id.iv_more)
    ImageView mIvMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        mLocalHomeFragment = new LocalHomeFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.fl_content,mLocalHomeFragment,"LocalHomeFragment");
        transaction.commit();
    }

    @OnClick({R.id.iv_sliding_menu, R.id.tv_net, R.id.tv_local, R.id.iv_play, R.id.iv_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_sliding_menu:
                break;
            case R.id.tv_net:
                mTvLocal.setTextColor(Color.parseColor("#d5d5d5"));
                mTvLocal.setTextSize(18f);
                mTvNet.setTextColor(Color.WHITE);
                mTvNet.setTextSize(22f);
                break;
            case R.id.tv_local:
                mTvNet.setTextColor(Color.parseColor("#d5d5d5"));
                mTvNet.setTextSize(18f);
                mTvLocal.setTextColor(Color.WHITE);
                mTvLocal.setTextSize(22f);
                break;
            case R.id.iv_play:
                break;
            case R.id.iv_more:
                break;
        }
    }
}
