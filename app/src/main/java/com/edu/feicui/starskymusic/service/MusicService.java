package com.edu.feicui.starskymusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.edu.feicui.starskymusic.entity.MusicBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by user on 2017/1/11.
 */

public class MusicService extends Service {

    
    //// TODO: 2017/1/11 还需添加重复播放 单曲播放等功能 
    private MediaPlayer mPlayer;
    private ArrayList<MusicBean> musicPathLists;
    private int currentPos;
    private int prevPos;
    private int currentTime;
    private boolean isRandom;


    public interface CallBack {
        boolean isPlayerMusic();//开始或者暂停
        int callTotalDate();//返回总持续时间
        int callCurrentTime();//返回当前时间
        void iSeekTo(int m_second);//跳进度
        void isPlayPre();//上一首
        void isPlayNext();//下一首
        boolean isPlayering();//是否播放状态
        String callMusicName();//获取歌曲名
        String callArtist();//获取艺术家
        int callCurrentPos();//获取当前是第几首
        String callImage();
        void randomMusic(boolean isRandom);
    }

    public class MyBinder extends Binder implements CallBack {

        @Override
        public boolean isPlayerMusic() {
            return playerMusic();
        }

        @Override
        public int callTotalDate() {
            if (mPlayer != null) {
                return mPlayer.getDuration();
            } else {
                return 0;
            }
        }

        @Override
        public int callCurrentTime() {
            if (mPlayer != null) {
                if(currentTime != 0 && currentTime > mPlayer.getCurrentPosition()){
                    mPlayer.seekTo(currentTime);
                    currentTime = 0;
                }
                return mPlayer.getCurrentPosition();
            } else {
                return 0;
            }
        }

        @Override
        public void iSeekTo(int m_second) {
            if (mPlayer != null) {
                mPlayer.seekTo(m_second);
            }
        }

        @Override
        public void isPlayPre() {
            if (--currentPos < 0) {
                currentPos = musicPathLists.size() - 1;
            }
            initMusic();
            playerMusic();
        }

        @Override
        public void isPlayNext() {
            if (++currentPos > musicPathLists.size() - 1) {
                currentPos = 0;
            }
            initMusic();
            playerMusic();
        }

        @Override
        public boolean isPlayering() {
            if(mPlayer.isPlaying()){
                return true;
            }else{
                return false;
            }
        }

        @Override
        public String callMusicName() {
            if(mPlayer != null && musicPathLists != null){
                return musicPathLists.get(currentPos).getTitle();
            }else{
                return "无播放歌曲";
            }
        }

        @Override
        public String callArtist() {
            if(mPlayer != null && musicPathLists != null){
                return musicPathLists.get(currentPos).getArtist();
            }else{
                return "无";
            }
        }

        @Override
        public int callCurrentPos() {
            if(mPlayer != null && musicPathLists != null){
                return currentPos;
            }else{
                return 0;
            }
        }

        @Override
        public String callImage() {
            if(mPlayer != null && musicPathLists != null){
                return musicPathLists.get(currentPos).getImage();
            }else{
                return "";
            }
        }

        @Override
        public void randomMusic(boolean isRandoms) {
            isRandom = isRandoms;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new MediaPlayer();
    }

    private void initMusic() {
        // 根路径
        //      String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmd.mp3";

        if(musicPathLists == null){
            return;
        }
        mPlayer.reset();
        try {
            mPlayer.setDataSource(musicPathLists.get(currentPos).getMusicPath());
            mPlayer.prepare();

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//播放完一首重写初始化
                @Override
                public void onCompletion(MediaPlayer mp) {
                    prevPos = currentPos;
                    if(isRandom){//随机播放
                        int random = new Random().nextInt(musicPathLists.size());
                        while(prevPos < -1){
                            if(prevPos == random){
                                random = new Random().nextInt(musicPathLists.size());
                                continue;
                            }
                            break;
                        }
                        currentPos = random;
                    }else{
                        currentPos++;
                    }

                    if (currentPos >= musicPathLists.size()) {
                        currentPos = 0;
                    }
                    //       mp.start();
                    initMusic();
                    playerMusic();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean playerMusic() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            return false;
        } else {
            mPlayer.start();
            return true;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {//对传？ 当前时间和第几首

        musicPathLists = intent.getParcelableArrayListExtra("MUSIC_LIST");
        currentPos = intent.getIntExtra("CURRENT_POSITION", -1);
        currentTime = intent.getIntExtra("CURRENT_TIME",0);

        initMusic();

        playerMusic();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
        }
    }


}
