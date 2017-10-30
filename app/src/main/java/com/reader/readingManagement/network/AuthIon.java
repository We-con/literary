package com.reader.readingManagement.network;

import android.content.Context;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.reader.readingManagement.ApplicationPreferences;

/**
 * Created by naver on 2017. 2. 24..
 */

public class AuthIon {

    public static Builders.Any.B withGet(Context context, String url) {
        return with(context, "GET", url);
    }

    public static Builders.Any.B withPost(Context context, String url) {
        return with(context, "POST", url);
    }

    //TODO : api header에 user_id를 붙인다?
    public static Builders.Any.B with(Context context, String method, String url) {
        return Ion.with(context).load(method, url).addHeader("user_id", ApplicationPreferences.getInstance().getUserInfo().getId());
    }


}
