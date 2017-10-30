package com.reader.readingManagement.user;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.reader.readingManagement.ApplicationPreferences;
import com.reader.readingManagement.R;
import com.reader.readingManagement.user.model.UserInfo;
import com.reader.readingManagement.utils.RoundDrawable;

/**
 * Created by Harry Kim on 2017. 2. 23..
 */

public class MyPageActivity extends AppCompatActivity {
    private ImageView profile;
    private TextView nickname, name, email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        profile = ((ImageView) findViewById(R.id.profile_image));
        nickname = ((TextView) findViewById(R.id.profile_nickname));
        name = ((TextView) findViewById(R.id.profile_name));
        email = ((TextView) findViewById(R.id.profile_email));
        UserInfo userInfo = ApplicationPreferences.getInstance().getUserInfo();
        nickname.setText(userInfo.getNickname());
        name.setText(userInfo.getName());
        email.setText(userInfo.getEmail());

        Glide.with(this).load(userInfo.getProfileImageUrl()).asBitmap().listener(
                new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        RoundDrawable.apply(resource, profile);
                        return true;
                    }
                }
        ).into(profile);
    }


}
