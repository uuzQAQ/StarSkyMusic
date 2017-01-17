package com.edu.feicui.starskymusic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by user on 2017/1/17.
 */

public class SongNameTextView extends TextView {

    public SongNameTextView(Context context) {
        super(context);
    }

    public SongNameTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SongNameTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
