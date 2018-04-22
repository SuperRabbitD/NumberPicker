package com.wang.demo.reminder_composer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.wang.demo.R;
import com.wang.ui.messageComposerLayout.AccessoryView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by wanglu on 4/7/18.
 */

public class TestEdit extends AccessoryView {
    public static final String TAG = "TestEdit";

    public TestEdit(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TestEdit(@NotNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TestEdit(@NotNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.test_edit, this);
    }

    @NotNull
    @Override
    public String getViewTag() {
        return "TestEdit";
    }
}
