package com.reader.readingManagement.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.reader.readingManagement.R;

/**
 * Created by loll_ on 2017-02-20.
 */

public class CustomProgressBar extends ProgressBar {
    protected Paint mPaint = new Paint();
    protected int mRealWidth;
    protected int mRealHeight;
    Bitmap indexYellow, indexGreen, indexBlue;

    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void init() {

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.bar_yellow);
        indexYellow = drawable.getBitmap();
        drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.bar_green);
        indexGreen = drawable.getBitmap();
        drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.bar_blue);
        indexBlue = drawable.getBitmap();

        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                measureHeight(heightMeasureSpec));
//        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            mRealHeight = getPaddingTop() + getPaddingBottom() + dp2px(4);
            result = (int) (mRealHeight + 2 * indexYellow.getHeight());
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);
        mRealWidth = getMeasuredWidth() - getPaddingRight() - getPaddingLeft();

        float radio = getProgress() * 1.0f / getMax();
        float progressPosX = (int) (mRealWidth * radio);

        if (progressPosX > mRealWidth) {
            progressPosX = mRealWidth;
        }


        mPaint.setColor(Color.parseColor("#dde4eb"));
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRealHeight);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(dp2px(10));
        canvas.drawLine(0, 0, mRealWidth, 0, mPaint);

        float endX = progressPosX;
        if (endX > 0) {
            mPaint.setColor(Color.parseColor(radio < 0.3 ? "#ffd600" : (radio < 0.7 ? "#3fdabf" : "#4aa2f9")));
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }

        canvas.drawBitmap(radio < 0.3 ? indexYellow : (radio < 0.7 ? indexGreen : indexBlue), progressPosX, -getHeight() / 2 + mRealHeight, mPaint);
        canvas.restore();
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
