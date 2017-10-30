package com.reader.readingManagement;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;

import com.nhn.android.naverlogin.OAuthLogin;
import com.reader.readingManagement.model.Book;
import com.reader.readingManagement.network.RequestManager;
import com.reader.readingManagement.network.UrlHelper;
import com.reader.readingManagement.post.model.Post;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by naver on 2017. 1. 29..
 */

public class BookApplication extends Application {

    private String OAUTH_CLIENT_ID = "WgsW2X3P2VOx7QHoG383", OAUTH_CLIENT_SECRET = "9nhSAZ6oVf", OAUTH_CLIENT_NAME = "Book";

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationPreferences.initialize(this);

        //////////////////////////////////////
        // 카메라 이슈 해결
        // FileUriExposedException에 대하여 정책적으로 무시 설정.
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ////////////////////////////////////////

        initRealm();
        initGlide();
        UrlHelper.initialize(this);
        initRequestQueue();
        initNaverLogin();

        //TODO : 서버로부터 Booklist와 PostList를 읽어와서 local DB를 초기화시켜야겠지?
    }

    private void initNaverLogin() {
        OAuthLogin.getInstance().init(this, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
    }

    private void initRequestQueue() {
        RequestManager.init(this);
    }


    private void initRealm() {
        Log.d("STARTAPP", "앱 시작됨 ");

        Realm.init(getApplicationContext());

        //TEST//
//        RealmResults<Book> books = Realm.getDefaultInstance().where(Book.class).findAll();
//        for (Book book : books) {
//            RealmList<Post> posts = book.getPosts();
//
//            if (posts != null) {
//                for (Post post : posts) {
//                    Log.d("STARTAPP", book.getName() + " - " + post.getContent());
//                }
//            } else {
//                Log.d("STARTAPP", book.getName() + "  포스트 없음");
//            }
//        }
    }

    private void initGlide() {

    }


}
