package com.reader.readingManagement.user;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.reader.readingManagement.ApplicationPreferences;
import com.reader.readingManagement.R;

/**
 * Created by naver on 2017. 2. 3..
 */

public class NaverLoginActivity extends Activity {

    private static final String TAG = NaverLoginActivity.class.getCanonicalName();
    private OAuthLogin mOAuthLoginModule;
    private TextView mLoginStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver_login);
        mLoginStatus = (TextView) findViewById(R.id.login_status);

        OAuthLoginButton mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
        mOAuthLoginButton.setBgResourceId(R.mipmap.naver_login);

        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler);
    }

    private String status;
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                status = "status : " + mOAuthLoginModule.getState(NaverLoginActivity.this).toString() + "\n" +
                        "accessToken : " + mOAuthLoginModule.getAccessToken(NaverLoginActivity.this) + "\n" +
                        "refreshToken : " + mOAuthLoginModule.getRefreshToken(NaverLoginActivity.this) + "\n" +
                        "expiresAt : " + mOAuthLoginModule.getExpiresAt(NaverLoginActivity.this) + "\n" +
                        "tokenType : " + mOAuthLoginModule.getTokenType(NaverLoginActivity.this);
                ApplicationPreferences.getInstance().setAuth(mOAuthLoginModule.getTokenType(NaverLoginActivity.this), mOAuthLoginModule.getAccessToken(NaverLoginActivity.this));
                mLoginStatus.setText(status);
                setResult(100);
                finish();
            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(NaverLoginActivity.this).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(NaverLoginActivity.this);
                Toast.makeText(NaverLoginActivity.this, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
