package com.wang.demo.reminder_composer;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.wang.demo.R;
import com.wang.ui.message_composer_layout.AccessoryView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by wanglu on 4/7/18.
 */

public class ReminderEditExtraContent extends AccessoryView {
    public static final String TAG = "ReminderEditExtraContent";

    public ReminderEditExtraContent(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ReminderEditExtraContent(@NotNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ReminderEditExtraContent(@NotNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.test_reminder_extra_content, this);
    }

    @NotNull
    @Override
    public String getViewTag() {
        return "ReminderEditExtraContent";
    }
}
