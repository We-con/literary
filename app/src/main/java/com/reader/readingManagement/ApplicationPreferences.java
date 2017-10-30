package com.reader.readingManagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.reader.readingManagement.user.model.UserInfo;
import com.reader.readingManagement.utils.Ln;

/**
 * Created by Harry Kim on 2017. 2. 23..
 */

public class ApplicationPreferences {
    private static final String KEY_NAME_APP = "boooook";
    private static final String KEY_GUIDE = "guide";
    private static final String KEY_LOGIN_TYPE = "snsType";
    private static final String KEY_LOGIN_USER_ID = "user_id";
    private static final String KEY_LOGIN_USER_NICKNAME = "user_nickname";
    private static final String KEY_LOGIN_PROFILE_URL = "user_profile_url";
    private static final String KEY_LOGIN_USER_NAME = "user_name";
    private static final String KEY_LOGIN_USER_EMAIL = "user_email";


    private static final String KEY_LOGIN_TOKEN = "user_token";
    private static final String KEY_LOGIN_TOKEN_TYPE = "user_token_type";
    private static ApplicationPreferences instance;
    private final SharedPreferences preferences;
    private UserInfo userInfo;
    private String loginType;
    private String tokenType;
    private String token;

    public static void initialize(Context context) {
        instance = new ApplicationPreferences(context);
        Ln.d("Application ", "init");
    }

    private ApplicationPreferences(Context context) {
        preferences = context.getSharedPreferences(KEY_NAME_APP, Context.MODE_PRIVATE);
        tokenType = preferences.getString(KEY_LOGIN_TOKEN_TYPE, "");
        token = preferences.getString(KEY_LOGIN_TOKEN, "");
        String loginType = preferences.getString(KEY_LOGIN_TYPE, "naver_");
        String id = preferences.getString(KEY_LOGIN_USER_ID, "");
        String nickname = preferences.getString(KEY_LOGIN_USER_NICKNAME, "");
        String url = preferences.getString(KEY_LOGIN_PROFILE_URL, "");
        Ln.d("Application ", "load" + id + " nick : " + nickname + " url " + url);
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(nickname) && !TextUtils.isEmpty(url)) {
            Ln.d("Application ", "user");
            setUserInfo(new UserInfo(id, nickname, url), loginType);
        } else {
            Ln.d("Application ", "user not");
        }
    }

    public static ApplicationPreferences getInstance() {
        return instance;
    }

    public boolean getGuide(){
        return preferences.getBoolean(KEY_GUIDE, false);
    }
    public void setGuide(boolean isguided){
        preferences.edit().putBoolean(KEY_GUIDE, isguided).apply();
    }
    void setUserInfo(UserInfo userInfo, String loginType) {
        this.userInfo = userInfo;
        this.loginType = loginType;
        preferences.edit().putString(KEY_LOGIN_USER_ID, userInfo.getId()).apply();
        preferences.edit().putString(KEY_LOGIN_USER_NICKNAME, userInfo.getNickname()).apply();
        preferences.edit().putString(KEY_LOGIN_PROFILE_URL, userInfo.getProfileImageUrl()).apply();
    }



    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setAuth(String tokenType, String accessToken) {
        Log.d("set setAuth", tokenType + " " + accessToken);
        this.tokenType = tokenType;
        this.token = accessToken;
        preferences.edit().putString(KEY_LOGIN_TOKEN_TYPE, tokenType).apply();
        preferences.edit().putString(KEY_LOGIN_TOKEN, token).apply();
    }

    public void setUser(String id, String nickname, String profile_image) {
        if (userInfo == null) {
            userInfo = new UserInfo(id, nickname, profile_image);
            return;
        }
        userInfo.setId(id);
        userInfo.setNickname(nickname);
        userInfo.setProfileImageUrl(profile_image);
        preferences.edit().putString(KEY_LOGIN_USER_ID, id).apply();
        preferences.edit().putString(KEY_LOGIN_USER_NICKNAME, nickname).apply();
        preferences.edit().putString(KEY_LOGIN_PROFILE_URL, profile_image).apply();
    }


    public void setUser(String id, String nickname, String profile_image, String name, String email) {
        if (userInfo == null) {
            userInfo = new UserInfo(id, nickname, profile_image);
            return;
        }
        userInfo.setId(id);
        userInfo.setNickname(nickname);
        userInfo.setProfileImageUrl(profile_image);
        userInfo.setName(name);
        userInfo.setEmail(email);
        preferences.edit().putString(KEY_LOGIN_USER_ID, id).apply();
        preferences.edit().putString(KEY_LOGIN_USER_NICKNAME, nickname).apply();
        preferences.edit().putString(KEY_LOGIN_PROFILE_URL, profile_image).apply();
        preferences.edit().putString(KEY_LOGIN_USER_NAME, name).apply();
        preferences.edit().putString(KEY_LOGIN_USER_EMAIL, email).apply();
    }

    public String getToken() {
        return (String) token;
    }
}
