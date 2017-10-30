package com.reader.readingManagement;

import android.app.IntentService;
import android.content.Intent;

import com.reader.readingManagement.utils.Ln;

/**
 * Created by naver on 2017. 2. 26..
 */

public class PushIntentService extends IntentService {


    public PushIntentService() {
        super("PushIntentService");
    }

    public PushIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Ln.d("Intetnsad", intent.toString());

        String pushType = intent.getStringExtra("type");
        if (pushType == null) {
            Ln.e("Intetnsad", null);
            return;
        }
        for (PushMessage.Type type : PushMessage.Type.values()) {
            if (type.name().toLowerCase() == pushType.toLowerCase()) {
                Ln.d("Intetnsad", type.getMsg());
                PushHelper.onMessage(this, type);
            }
        }

    }
}
