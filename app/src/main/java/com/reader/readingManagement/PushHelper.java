package com.reader.readingManagement;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.reader.readingManagement.book.info.BookInfoActivity;
import com.reader.readingManagement.home.HomeActivity;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;

/**
 * Created by naver on 2017. 2. 26..
 */

public class PushHelper {

    public static final String TAG = "push_tag";

    static void onMessage(Context context, PushMessage.Type pushType) {

        final long requestCode = (int) System.currentTimeMillis();
        Intent intent;
        switch (pushType) {
            case COMMENT:
            case SAME_BOOK:
                intent = new Intent(context, BookInfoActivity.class);
                break;
            case REMIND_1:
                intent = new Intent(context, BookInfoActivity.class);
                break;
            case REMIND_2:
                intent = new Intent(context, BookInfoActivity.class);
                break;
            default:
                intent = new Intent(context, HomeActivity.class);
                break;
        }
        intent.putExtra("PUSH_TYPE", pushType.name());

        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent((int) requestCode, FLAG_CANCEL_CURRENT);


        notificationBuilder.setContentTitle("알림입니다.")
                .setContentText(pushType.getMsg())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_alarm)
                .setWhen(System.currentTimeMillis()+10000)
                .setAutoCancel(true)
                .setPriority(2)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(pushType.name(), pushType.ordinal(), notificationBuilder.build());
    }
}
