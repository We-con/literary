package com.reader.readingManagement.post.add;

import com.reader.readingManagement.R;

/**
 * Created by naver on 2017. 2. 19..
 */

public enum LineColor {
    RED(R.color.pen_red, R.id.color_red),
    YELLOW(R.color.pen_yellow, R.id.color_yellow),
    BLUE(R.color.pen_blue, R.id.color_blue);

    private final int resId;
    private final int id;

    LineColor(int resId, int id) {
        this.resId = resId;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getResId() {
        return resId;
    }
}
