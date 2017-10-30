package com.reader.readingManagement.post.add;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.reader.readingManagement.R;

import java.util.ArrayList;

/**
 * ImageView 에 형광팬으로 추가하는 뷰
 * Created by naver on 2017. 2. 19..
 */

public class HighlightPenImageView extends ImageView {
    public static final int PEN_THIN = 10;
    public static final int PEN_MID = 20;
    public static final int PEN_THICK = 30;
    public static final int PEN_DEFAULT = 1;

    public static final int COLOR_YELLOW = R.color.pen_yellow;
    public static final int COLOR_RED = R.color.pen_red;
    public static final int COLOR_BLUD = R.color.pen_blue;
    public static final int COLOR_DEFAULT = COLOR_YELLOW;
    public static final int COLOR_REMOVE = R.color.removeColor;
    public static final int SIZE = 5;

    private Paint currentPaint;
    private Paint removePaint;
    private Paint highlightPaint;

    private float prevX;
    private float prevY;
    private float firstX;
    private float firstY;

    private float currentX;
    private float currentY;
    private ArrayList<Line> lines = new ArrayList<>();
    private Line currentLine;
    private boolean finishLine;
    private float thickness;
    private float lastX;
    private float lastY;
    private boolean toSave;
    private Drawable ltRound;
    private Drawable lbRound;
    private Drawable rtRound;
    private Drawable rbRound;

    public HighlightPenImageView(Context context) {
        super(context);
    }

    public HighlightPenImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HighlightPenImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    private void init() {
        thickness = dpToPx(PEN_THIN);
        highlightPaint = new Paint();
        highlightPaint.setDither(true);
        setPenColor(COLOR_DEFAULT);
        highlightPaint.setStyle(Paint.Style.STROKE);
        currentPaint = highlightPaint;

        removePaint = new Paint();
        removePaint.setDither(true);
        removePaint.setColor(ContextCompat.getColor(getContext(), COLOR_REMOVE));  // alpha.r.g.b
        removePaint.setStyle(Paint.Style.STROKE);
        setThickness(PEN_DEFAULT);

        ltRound = ContextCompat.getDrawable(getContext(), R.drawable.ic_round_lt);
        lbRound = ContextCompat.getDrawable(getContext(), R.drawable.ic_round_lb);
        rtRound = ContextCompat.getDrawable(getContext(), R.drawable.ic_round_rt);
        rbRound = ContextCompat.getDrawable(getContext(), R.drawable.ic_round_rb);
    }

    public void setThickness(float rate) {
        float adjustedThickness = thickness * rate;
        highlightPaint.setStrokeWidth(adjustedThickness);
        //이거 필요없는거같은데
//        removePaint.setStrokeWidth(adjustedThickness);
    }

    float dpToPx(int dp) {
        return (dp * getResources().getDisplayMetrics().density);
    }

    public void setPenColor(int color) {
        highlightPaint.setColor(ContextCompat.getColor(getContext(), color));  // alpha.r.g.b

    }

    @Override
    public void buildDrawingCache() {
        toSave = true;
        invalidate();
        super.buildDrawingCache();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        toSave = false;
        float originalX = event.getX();
        float originalY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstX = originalX;
                firstY = originalY;
                addLine(firstX, firstY);
                break;

            case MotionEvent.ACTION_MOVE:
                currentX = originalX;
                currentY = originalY;
                drawLine(currentX, currentY);

                prevX = currentX;
                prevY = currentY;
                break;
            case MotionEvent.ACTION_CANCEL:
                currentX = originalX;
                currentY = originalY;
                drawLine(currentX, currentY);
                finishLine();
                break;

            case MotionEvent.ACTION_UP:
                lastX = originalX;
                lastY = originalY;
                finishLine();
                break;
        }
        invalidate();
        return true;
    }

    /**
     * Line 을 이용해, 여러줄처리하는 부분은 나중에 추가
     */
    void addLine(float x, float y) {
        for (Line line : lines) {
            if (sameLine(line, x, y)) {
                Log.d("DIFFFFF", "same!!!!!!!!");
                currentLine = new Line(line);
                lines.remove(line);
                return;
            }
        }
        Log.d("DIFFFFF", "new Line!!!!!!!!");
        currentLine = new Line(firstX, firstY, firstX, firstY);
    }

    private static final float SIMILARITY = 50;

    private boolean sameLine(Line line, float x, float y) {
        float diffX = Math.abs(line.getFirstX() - x);
        float diffY = Math.abs(line.getFirstY() - y);
        Log.d("DIFFFFF", diffX + " : " + diffY + " ");
        return diffX < SIMILARITY && diffY < SIMILARITY;
    }

    void drawLine(float x, float y) {
        if (currentLine == null) {
            addLine(x, y);
        } else {
            if (currentLine.getFirstX() - x < 0) {
                currentPaint = removePaint;
            }
            currentPaint = highlightPaint;
            currentLine.setLastX(x);
            currentLine.setLastY(y);
        }
    }

    void finishLine() {
        if (Math.abs(currentLine.firstX - currentLine.lastX) > 100) {
            currentLine.setLinePaint(new Paint(currentPaint));
            lines.add(currentLine);
        }
        finishLine = true;
        currentLine = null;
    }


    /**
     * lines 를 만들어서 관리 하도록 만들자
     *
     * @param canvas
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Line line : lines) {
            if (!toSave) {
                Rect cancelRec = new Rect(((int) line.firstX - (int)dpToPx(SIZE))
                        , ((int) line.firstY - (int)dpToPx(SIZE))
                        , ((int) line.firstX + (int)dpToPx(SIZE))
                        , ((int) line.firstY + (int)dpToPx(SIZE)));
                removePaint.setStrokeWidth(line.getLinePaint().getStrokeWidth());
                canvas.drawRect(cancelRec, removePaint);
            }
            canvas.drawLine(line.firstX, line.firstY, line.lastX, line.firstY, line.getLinePaint());
        }
        if (currentLine != null) {
            removePaint.setStrokeWidth(currentPaint.getStrokeWidth());
            if (Math.abs(currentLine.firstX - currentLine.lastX) < 100) {
                canvas.drawLine(currentLine.firstX, currentLine.firstY, currentLine.lastX, currentLine.firstY, removePaint);
            } else {
                canvas.drawLine(currentLine.firstX, currentLine.firstY, currentLine.lastX, currentLine.firstY, currentPaint);
            }
        }
        if (finishLine) {
            currentLine = null;
            finishLine = false;
        }
    }
}
