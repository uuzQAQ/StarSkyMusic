package com.edu.feicui.starskymusic.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.edu.feicui.starskymusic.R;
import com.edu.feicui.starskymusic.view.SongNameTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 2017/1/11.
 */

public class TestActivity extends Activity {

    @BindView(R.id.tv_music_name)
    SongNameTextView mTvMusicName;
    @BindView(R.id.et)
    EditText mEt;
    @BindView(R.id.btn)
    Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn)
    public void onClick() {
        String text = mEt.getText().toString();
        mTvMusicName.setText(text);
    }
}
