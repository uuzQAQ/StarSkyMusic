package com.edu.feicui.starskymusic.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.feicui.starskymusic.R;
import com.edu.feicui.starskymusic.adapter.MusicListAdapter;
import com.edu.feicui.starskymusic.entity.MusicBean;
import com.edu.feicui.starskymusic.view.Mylayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by user on 2017/1/10.
 */

public class LocalOneFragment extends Fragment {

    private Unbinder mUnbinder;
    private Handler mHandler = new Handler();
    private List<MusicBean> mMediaLists = new ArrayList<>();
    private MusicListAdapter adapter;
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

        //适配器
        adapter = new MusicListAdapter(getContext());
        mRvOne.setLayoutManager(new Mylayout(getContext()));
        mRvOne.setAdapter(adapter);

        asyncQueryMedia();
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

    public void queryMusic(String dirName) {
        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Audio.Media.DATA + " like ?",
                new String[]{dirName + "%"},
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (cursor == null) return;

        // id title singer data time image
        MusicBean music;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // 如果不是音乐
            String isMusic = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));
            if (isMusic != null && isMusic.equals("")) continue;

            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

            if (isRepeat(title, artist)) continue;

            music = new MusicBean();
            music.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
            music.setTitle(title);
            music.setArtist(artist);
            music.setMusicPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
            music.setLength(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
            music.setImage(getAlbumImage(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))));

            mMediaLists.add(music);
        }

        cursor.close();
    }

    //判断是否重复
    private boolean isRepeat(String title, String artist) {
        for (MusicBean music : mMediaLists) {
            if (title.equals(music.getTitle()) && artist.equals(music.getArtist())) {
                return true;
            }
        }
        return false;
    }


    //获得音乐路径
    private String getAlbumImage(int albumId) {
        String result = "";
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(
                    Uri.parse("content://media/external/audio/albums/"
                            + albumId), new String[]{"album_art"}, null,
                    null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); ) {
                result = cursor.getString(0);
                break;
            }
        } catch (Exception e) {
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return null == result ? null : result;
    }

    public void asyncQueryMedia() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mMediaLists.clear();
                queryMusic(Environment.getExternalStorageDirectory() + File.separator);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setList(mMediaLists);
                    }
                });
            }
        }).start();
    }
}
