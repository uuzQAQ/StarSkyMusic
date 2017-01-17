package com.edu.feicui.starskymusic.activity;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.feicui.starskymusic.R;
import com.edu.feicui.starskymusic.entity.MusicBean;
import com.edu.feicui.starskymusic.service.MusicService;
import com.edu.feicui.starskymusic.view.CircularSeekBar;
import com.pkmmte.view.CircularImageView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 2017/1/16.
 */

public class PlayActivity extends Activity{

    @BindView(R.id.tv_play_music_name)
    TextView mTvPlayMusicName;
    @BindView(R.id.tv_play_songer)
    TextView mTvPlaySonger;
    @BindView(R.id.sc_music)
    CircularSeekBar mScMusic;
    @BindView(R.id.iv_music)
    CircularImageView mIvMusic;
    @BindView(R.id.tv_total_time)
    TextView mTvTotalTime;
    @BindView(R.id.cs_sound)
    CircularSeekBar mCsSound;
    @BindView(R.id.iv_start)
    ImageView mIvStart;

    private int mProgress;
    private MyHandler mHandler = new MyHandler(this);
    private SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    private MusicService.CallBack callBack;
    private boolean mFlag = true;
    private ArrayList<MusicBean> musicBeanList = new ArrayList<>();
    private int maxVolume;//最大音量
    private int currentVolume;//当前音量
    private AudioManager audioManager;//音频管理
    private boolean isRandoms;
    //// TODO: 2017/1/16  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);
        ButterKnife.bind(this);

        //音频管理
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);


        getMusInfoAndStService();
        seekTime();
        forSeekBar();

        //// TODO: 2017/1/16  列表，随机播放
    }

    @OnClick({R.id.iv_back, R.id.iv_random, R.id.iv_last, R.id.iv_start, R.id.iv_next, R.id.iv_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_random:
                isRandoms = isRandoms ? false : true;
                if(isRandoms){
                    Toast.makeText(this, "随机播放", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "全部播放", Toast.LENGTH_SHORT).show();
                }
                //// TODO: 2017/1/17 随机播放不随机单前的 
                callBack.randomMusic(isRandoms);
                break;
            case R.id.iv_last:
                callBack.isPlayPre();
                break;
            case R.id.iv_start:
                playerMusicByIBinder();
                break;
            case R.id.iv_next:
                callBack.isPlayNext();
                break;
            case R.id.iv_list:
                break;
        }
    }

    public void playerMusicByIBinder() {
        boolean playerState = callBack.isPlayerMusic();
        if (playerState) {
            mIvStart.setImageResource(R.drawable.pause);
        } else {
            mIvStart.setImageResource(R.drawable.play2);
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

    private void forSeekBar() {
        mScMusic.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {

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

        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mCsSound.setMax(maxVolume);
        mCsSound.setProgress(currentVolume);

        mCsSound.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                adjustVolume(progress);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
    }


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

    private void getMusInfoAndStService() {
        /** 接收音乐列表资源 */
        musicBeanList = getIntent().getParcelableArrayListExtra("MUSIC_LIST");
        int currentPosition = getIntent().getIntExtra("CURRENT_POSITION", 0);
        int cuttentTime = getIntent().getIntExtra("CURRENT_TIME",0);

        /** 构造启动音乐播放服务的Intent，设置音乐资源 */
        Intent intent = new Intent(this, MusicService.class);
        intent.putParcelableArrayListExtra("MUSIC_LIST", musicBeanList);
        intent.putExtra("CURRENT_POSITION", currentPosition);
        intent.putExtra("CURRENT_TIME",cuttentTime);
        startService(intent);
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
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

    private static class MyHandler extends Handler {
        // 弱引用
        private WeakReference<PlayActivity> reference;

        public MyHandler(PlayActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PlayActivity activity = reference.get();
            if (activity != null) {

                int currentTime = activity.callBack.callCurrentTime();
                int totalTime = activity.callBack.callTotalDate();
                String musicName = activity.callBack.callMusicName();
                boolean isplay = activity.callBack.isPlayering();
                String artist = activity.callBack.callArtist();
                String imagePath = activity.callBack.callImage();


                if(isplay){
                    activity.mIvStart.setImageResource(R.drawable.pause);
                }else{
                    activity.mIvStart.setImageResource(R.drawable.play2);
                }

                if(imagePath.length() > 1){
                    activity.mIvMusic.setImageDrawable(Drawable.createFromPath(imagePath));
                }

                activity.mScMusic.setMax(totalTime);
                activity.mScMusic.setProgress(currentTime);
                activity.mTvPlayMusicName.setText(musicName);
                //无播放歌曲
                activity.mTvPlaySonger.setText(artist);

                String current = activity.format.format(new Date(currentTime));
                String total = activity.format.format(new Date(totalTime));


//                activity.currentTimeTxt.setText(current);
                activity.mTvTotalTime.setText(total);
            }
        }
    }

    private void adjustVolume(int progress){
        //最终音量 = 最大音量 * 改变的百分比 + 当前音量
        int volume = progress;
        //如果最终音量大于最大音量，结果为最大音量
        volume = volume > maxVolume ? maxVolume : volume;
        //如果最终音量小于0，结果为0
        volume = volume < 0 ? 0 : volume;
        //设置音量
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volume,AudioManager.FLAG_SHOW_UI);
    }

}
