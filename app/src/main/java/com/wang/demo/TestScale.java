package com.wang.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wanglu on 4/18/18.
 */

public class TestScale extends View {
    private Paint paint, paint2;
    private Paint linepaint;

    public TestScale(Context context) {
        super(context);
    }

    public TestScale(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestScale(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestScale(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint == null) {
            paint = new Paint();
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // 将画笔设置为空心
            paint.setStyle(Paint.Style.STROKE);
            // 设置画笔颜色
            paint.setColor(Color.BLACK);
            // 设置画笔宽度
            paint.setTextSize(80);
            paint.setTextAlign(Paint.Align.CENTER);
        }

        if (paint2 == null) {
            paint2 = new Paint();
            paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
            // 将画笔设置为空心
            paint2.setStyle(Paint.Style.STROKE);
            // 设置画笔颜色
            paint2.setColor(Color.BLUE);
            // 设置画笔宽度
            paint2.setTextSize(120);
            paint2.setTextAlign(Paint.Align.CENTER);
        }

        if (linepaint == null) {
            linepaint = new Paint();
            linepaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // 将画笔设置为空心
            linepaint.setStyle(Paint.Style.STROKE);
            // 设置画笔颜色
            linepaint.setColor(Color.RED);
            // 设置画笔宽度
        }

//
//        canvas.drawLine(400,0,400,1000, linepaint);
//        canvas.drawText("This is test text", 400, 200, paint);
//        canvas.drawRect(new Rect(100, 100, 200, 200), paint);

        canvas.save();
        canvas.scale(1.5f, 1.5f, 400, 200);
        paint.setTextSize(80);
        canvas.drawText("This is test text", 400, 200, paint);
        canvas.restore();

//        canvas.drawText("This is test text", 400, 200, paint2);
    }
}
