package com.reader.readingManagement.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.reader.readingManagement.user.GenreTitleDrawable;

/**
 * Created by naver on 2017. 2. 25..
 */

public class RoundDrawable {

    static public void apply(Bitmap resource, ImageView view){
        GenreTitleDrawable circleImage = new GenreTitleDrawable(resource);
        circleImage.setTargetDensity(view.getResources().getDisplayMetrics());
        view.setImageDrawable(circleImage);
    }
}
