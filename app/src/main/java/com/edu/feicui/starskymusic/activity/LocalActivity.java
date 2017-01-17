package com.edu.feicui.starskymusic.activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.feicui.starskymusic.R;
import com.edu.feicui.starskymusic.entity.MusicBean;
import com.edu.feicui.starskymusic.fragment.LocalHomeFragment;
import com.edu.feicui.starskymusic.service.MusicService;
import com.edu.feicui.starskymusic.view.CircularSeekBar;
import com.edu.feicui.starskymusic.view.SongNameTextView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocalActivity extends AppCompatActivity {

    @BindView(R.id.iv_sliding_menu)
    ImageView mIvSlidingMenu;
    @BindView(R.id.tv_net)
    TextView mTvNet;
    @BindView(R.id.tv_local)
    TextView mTvLocal;
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R.id.tv_music_name)
    SongNameTextView mTvMusicName;
    @BindView(R.id.iv_play)
    ImageView mIvPlay;
    @BindView(R.id.seek_bar)
    CircularSeekBar mSeekBar;
    @BindView(R.id.iv_more)
    ImageView mIvMore;
    @BindView(R.id.play_status_bar)
    LinearLayout mPlayStatusBar;
    @BindView(R.id.iv_add_list)
    ImageView mIvAddList;
    @BindView(R.id.iv_delete)
    ImageView mIvDelete;
    @BindView(R.id.iv_add_table)
    ImageView mIvAddTable;
    @BindView(R.id.ll_music_manager)
    LinearLayout mLlMusicManager;

    private FragmentManager mFragmentManager;
    private LocalHomeFragment mLocalHomeFragment;
    private MyHandler mHandler = new MyHandler(this);
    private SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    private MusicService.CallBack callBack;
    private boolean mFlag = true;
    private ArrayList<MusicBean> musicBeanList = new ArrayList<>();
    private int mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        mLocalHomeFragment = new LocalHomeFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.fl_content, mLocalHomeFragment, "LocalHomeFragment");
        transaction.commit();

        mTvMusicName.setFocusable(true);
        mTvMusicName.requestFocus();

        getMusInfoAndStService();
        seekTime();
        forSeekBar();
    }

    @OnClick({R.id.iv_sliding_menu, R.id.tv_net, R.id.tv_local, R.id.iv_play, R.id.iv_more,R.id.play_status_bar})
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
                playerMusicByIBinder();
                break;
            case R.id.iv_more:
                break;
            case R.id.play_status_bar:
                Intent intent = new Intent(LocalActivity.this,PlayActivity.class);
                intent.putParcelableArrayListExtra("MUSIC_LIST",musicBeanList);
                intent.putExtra("CURRENT_POSITION",callBack.callCurrentPos());
                intent.putExtra("CURRENT_TIME",callBack.callCurrentTime());
                startActivity(intent);
                break;
        }
    }

    private static class MyHandler extends Handler {
        // 弱引用
        private WeakReference<LocalActivity> reference;

        public MyHandler(LocalActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LocalActivity activity = reference.get();
            if (activity != null) {

                int currentTime = activity.callBack.callCurrentTime();
                int totalTime = activity.callBack.callTotalDate();
                String musicName = activity.callBack.callMusicName();
                boolean isplay = activity.callBack.isPlayering();

                if(isplay){
                    activity.mIvPlay.setImageResource(R.drawable.pause3);
                }else{
                    activity.mIvPlay.setImageResource(R.drawable.play3);
                }

                activity.mSeekBar.setMax(totalTime);
                activity.mSeekBar.setProgress(currentTime);
                activity.mTvMusicName.setText(musicName);

                String current = activity.format.format(new Date(currentTime));
                String total = activity.format.format(new Date(totalTime));


//                activity.currentTimeTxt.setText(current);
//                activity.totalTimeTxt.setText(total);
            }
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            callBack = (MusicService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            callBack = null;
        }
    };

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        initData();
//        getMusInfoAndStService();
//
//        seekTime();
//        forSeekBar();
//    }

    private void getMusInfoAndStService() {
        /** 接收音乐列表资源 */
        musicBeanList = getIntent().getParcelableArrayListExtra("MUSIC_LIST");
        int currentPosition = getIntent().getIntExtra("CURRENT_POSITION", 0);

        /** 构造启动音乐播放服务的Intent，设置音乐资源 */
        Intent intent = new Intent(this, MusicService.class);
        intent.putParcelableArrayListExtra("MUSIC_LIST", musicBeanList);
        intent.putExtra("CURRENT_POSITION", currentPosition);

        startService(intent);
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

//    // 广播可以用来通知更新音乐文件等，此处可无
//    private class MyLocalActivityReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//        }
//    }

    private void seekTime() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mFlag) {
                    if (callBack != null) {

                        mHandler.sendMessage(Message.obtain());

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }

    private void forSeekBar() {
        mSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {

            @Override
            public void onProgressChanged(CircularSeekBar seekBar, int progress, boolean fromUser) {
                if (callBack != null)
                    mProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                if (callBack != null) {
                    callBack.iSeekTo(mProgress);
                }
            }
        });
    }


    /**
     * 播放音乐通过Binder接口实现
     */
    public void playerMusicByIBinder() {
        boolean playerState = callBack.isPlayerMusic();
        if (playerState) {
            mIvPlay.setImageResource(R.drawable.pause3);
        } else {
            mIvPlay.setImageResource(R.drawable.play3);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (conn != null || callBack != null) {
            unbindService(conn);
            callBack = null;
        }
        Intent intent = new Intent(this, MusicService.class);
        stopService(intent);
        mFlag = false;
    }
}
