package com.wang.demo.reminder_composer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wang.demo.R;
import com.wang.ui.messageComposerLayout.ComposerBody;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by wanglu on 4/7/18.
 */

public class ReminderComposerEdit extends ComposerBody {
    protected Button button, button2;

    public ReminderComposerEdit(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ReminderComposerEdit(@NotNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ReminderComposerEdit(@NotNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.test_composer_edit, this);
        button = findViewById(R.id.show_extra);
        button2 = findViewById(R.id.show_edit);

        final EditText editText = findViewById(R.id.reminder_edit);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onAccessoryViewButtonClicked(!mMessageComposerViewManager.getAccessoryViewStatus(ReminderEditExtraContent.TAG)
                        , ReminderEditExtraContent.TAG);

                editText.clearFocus();
            }
        });

        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onAccessoryViewButtonClicked(!mMessageComposerViewManager.getAccessoryViewStatus(TestEdit.TAG)
                        , TestEdit.TAG);

                editText.clearFocus();
            }
        });
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onEditStarted();
                }
            }
        });
    }

    @Override
    public void onNotifyAccessoryViewStatusChanged() {
        button.setText("SHOW PICKER");
        button2.setText("SHOW EDIT");
    }

    @Override
    public void onNotifyAccessoryViewStatusChanged(@NotNull String tag, boolean show) {
        if (tag.equals(ReminderEditExtraContent.TAG)) {
            if (show)
                button.setText("HIDE PICKER");
            else
                button.setText("SHOW PICKER");
        } else {
            if (show)
                button2.setText("HIDE EDIT");
            else
                button2.setText("SHOW EDIT");
        }
    }
}
