package com.reader.readingManagement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by loll_ on 2017-02-26.
 */

public class PushActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        Button push1 = (Button)findViewById(R.id.push1);
        push1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushHelper.onMessage(getApplicationContext(), PushMessage.Type.REMIND_1);
            }
        });
        Button push2 = (Button)findViewById(R.id.push2);
        push1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushHelper.onMessage(getApplicationContext(), PushMessage.Type.REMIND_2);
            }
        });
        Button push3 = (Button)findViewById(R.id.push3);
        push1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushHelper.onMessage(getApplicationContext(), PushMessage.Type.COMMENT);
            }
        });
        Button push4 = (Button)findViewById(R.id.push4);
        push1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushHelper.onMessage(getApplicationContext(), PushMessage.Type.SAME_BOOK);
            }
        });
    }
}
