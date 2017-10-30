package com.reader.readingManagement.post.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.reader.readingManagement.R;

/**
 * Created by naver on 2017. 2. 22..
 */
public class FullImageActivity extends AppCompatActivity {
    public static final String EXTRA_IMAGE_URL = "image_url";
    private String imageUrl;

    public static void startActivity(Activity activity, String imageUrl) {
        Intent fullImageActivity = new Intent(activity, FullImageActivity.class);
        fullImageActivity.putExtra(EXTRA_IMAGE_URL, imageUrl);
        activity.startActivity(fullImageActivity);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        }
        setContentView(R.layout.activity_fullimage);
        Glide.with(this).load(imageUrl).into((ImageView) findViewById(R.id.full_image));
    }
}
