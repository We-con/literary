package com.reader.readingManagement.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.reader.readingManagement.ApplicationPreferences;
import com.reader.readingManagement.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by Harry Kim on 2017. 2. 23..
 */

public class LoginActivity extends AppCompatActivity {
    private static final int REQ_LOGIN = 0x333;

    public static boolean needLogin(Context context) {
//        if (ApplicationPreferences.getInstance().getUserInfo() != null && !TextUtils.isEmpty(ApplicationPreferences.getInstance().getUserInfo().getId())) {
//            return false;
//        }

        Toast.makeText(context, "로그인상태 : " + ((ApplicationPreferences.getInstance().getUserInfo() != null) ?
                ApplicationPreferences.getInstance().getUserInfo().getId() : "NULL"), Toast.LENGTH_SHORT).show();
        context.startActivity(new Intent(context, LoginActivity.class));
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View logoView = findViewById(R.id.logo);
        ViewCompat.animate(logoView).alpha(1f).setDuration(3000).setInterpolator(new FastOutSlowInInterpolator()).start();
    }

    public void onClickLogin(View v) {
        switch (v.getId()) {
            case R.id.naver:
                Intent intent = new Intent(this, NaverLoginActivity.class);
                startActivityForResult(intent, REQ_LOGIN);
                break;
            case R.id.facebook:
            case R.id.kakao:
                Toast.makeText(this, "아직 ㅠ_ㅠ ", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_LOGIN && resultCode == 100) {
            requestUserInfo();
        }
    }

    private void requestUserInfo() {
        try {
            String token = ApplicationPreferences.getInstance().getToken();// 네이버 로그인 접근 토큰;
            String header = "Bearer " + token;
            JsonObject userInfoString = Ion.with(this).load("GET", "https://openapi.naver.com/v1/nid/me")
                    .addHeader("Authorization", header).asJsonObject().get(10, TimeUnit.SECONDS);
            JsonObject response = userInfoString.get("response").getAsJsonObject();
            Log.d("LOGIN", userInfoString.toString());
            ApplicationPreferences.getInstance().setUser(response.get("id").getAsString(), response.get("nickname").getAsString(), response.get("profile_image").getAsString(), response.get("name").getAsString(), response.get("email").getAsString());
            /**
             * addMember 하는거 필요.
             */

            // TODO : 네이버로부터 사용자정보를 불러온후에 서버에 추가하는구나!
             /*String result = Ion.with(this).load("POST", UrlHelper.getApiUrl(R.id.api_add_member))
                    .setBodyParameter("nickname", ApplicationPreferences.getInstance().getUserInfo().getNickname())
                    .setBodyParameter("user_id", ApplicationPreferences.getInstance().getUserInfo().getId())
                    .setBodyParameter("iamgeUrl", ApplicationPreferences.getInstance().getUserInfo().getProfileImageUrl())
                    .asString().get(30, TimeUnit.SECONDS);*/


//            if (!"FAIL".equals(result)) {
                startActivity(new Intent(this, MyPageActivity.class));
                finish();
//            } else {
//                Toast.makeText(this, "로그인 실패\n다시 로그인하세요.", Toast.LENGTH_SHORT).show();
//                finish();
//            }
        } catch (Exception e) {
            Log.e("LOGIN", "fail", e);
        }

    }
}
