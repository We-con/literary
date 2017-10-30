package com.reader.readingManagement.post;

import com.reader.readingManagement.R;

/**
 * Created by naver on 2017. 2. 19..
 */

public enum PostTheme {
    NONE(R.drawable.ic_remind_happy, R.id.theme_0),
    RED(R.drawable.ic_remind_love, R.id.theme_1),
    YELLOW(R.drawable.ic_remind_sad, R.id.theme_2),
    WHITE(R.drawable.ic_remind_knowledge, R.id.theme_3),
    BLACK(R.drawable.ic_blue, R.id.theme_4);

    private final int resId;
    private final int id;

    PostTheme(int resId, int id) {
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
