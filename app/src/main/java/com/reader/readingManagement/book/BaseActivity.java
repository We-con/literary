package com.reader.readingManagement.book;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reader.readingManagement.PushHelper;
import com.reader.readingManagement.PushMessage;
import com.reader.readingManagement.R;
import com.reader.readingManagement.utils.Ln;

/**
 * Created by naver on 2017. 2. 25..
 */

public class BaseActivity extends AppCompatActivity {

    private TextView toolbarTitle;
    private ImageView toolbarAction;
    private ImageView toolbarNavi;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            String tag = getIntent().getStringExtra(PushHelper.TAG);
            Ln.d("at", "ONCREATE" + tag);
            for (PushMessage.Type pushType : PushMessage.Type.values()) {
                if (TextUtils.equals(pushType.name(), tag)) {
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    Ln.d("at", "ONCREATE" + tag + " :remove " + pushType.ordinal());
                    nm.cancel(pushType.ordinal());
                }
            }
        }
    }

    public void initToolbar() {
        toolbarTitle = ((TextView) findViewById(R.id.title));
        toolbarAction = ((ImageView) findViewById(R.id.action));
        toolbarNavi = ((ImageView) findViewById(R.id.navigation));
        if (toolbarNavi != null) {
            toolbarNavi.setVisibility(View.VISIBLE);
            toolbarNavi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        onCreatedToolbar();
    }

    protected void onCreatedToolbar() {
    }

    protected void setTitle(String titleName) {
        if (toolbarTitle != null) {
            toolbarTitle.setText(titleName);
            toolbarTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "ttt", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    protected void setAction(String actionName, View.OnClickListener onClickListener) {
        if (toolbarAction != null) {
            toolbarAction.setVisibility(View.VISIBLE);
            toolbarAction.setOnClickListener(onClickListener);
        }
    }


    protected void setAction(int resId, View.OnClickListener onClickListener) {
        if (toolbarAction != null) {
            toolbarAction.setVisibility(View.VISIBLE);
//            toolbarAction.setCompoundDrawables(ContextCompat.getDrawable(BaseActivity.this, resId), null, null, null);
            toolbarAction.setOnClickListener(onClickListener);
        }
    }


}
