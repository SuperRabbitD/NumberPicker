package com.wang.demo.reminder_composer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.wang.demo.R;
import com.wang.ui.message_composer_layout.ContentView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by wanglu on 4/7/18.
 */

public class ReminderContent extends ContentView {
    public ReminderContent(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ReminderContent(@NotNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ReminderContent(@NotNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.test_content_view, this);
    }
}
