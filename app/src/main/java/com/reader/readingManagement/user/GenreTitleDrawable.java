package com.reader.readingManagement.user;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;

public class GenreTitleDrawable extends Drawable {
    private final Paint paint;
    private final Bitmap bitmap;
    private int width;
    private int height;
    private int targetDensity = DisplayMetrics.DENSITY_DEFAULT;
    private Rect rect = new Rect();

    public GenreTitleDrawable(Bitmap bitmap) {
        paint = new Paint();
        this.bitmap = bitmap;
        try {
            if (bitmap != null) {
                computeBitmapSize();
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                paint.setAntiAlias(true);
                paint.setShader(shader);
            }
        } catch (Exception e) {
            Log.e("asd", e.toString(),e);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Gravity.apply(Gravity.FILL, width, height,
                getBounds(), rect);

        RectF rectF = new RectF();
        rectF.set(rect);
        canvas.drawRoundRect(rectF, rectF.width() / 2, rectF.height() / 2, paint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    private void computeBitmapSize() {
        width = bitmap.getScaledWidth(targetDensity);
        height = width;
        Gravity.apply(Gravity.FILL, width, height,
                getBounds(), rect);
    }

    public void setTargetDensity(DisplayMetrics displayMetrics) {
        int density = displayMetrics.densityDpi;
        if (targetDensity != density) {
            targetDensity = density == 0 ? DisplayMetrics.DENSITY_DEFAULT : density;
            if (bitmap != null) {
                computeBitmapSize();
            }
            invalidateSelf();
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
