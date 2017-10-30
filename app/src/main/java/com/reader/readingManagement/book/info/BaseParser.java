package com.reader.readingManagement.book.info;

import com.google.gson.JsonObject;
import com.reader.readingManagement.utils.Ln;

/**
 * Created by naver on 2017. 2. 25..
 */
public class BaseParser {
    /**
     * {"message":{"result":{"post":{"posts":[{"id":2,"content":"Content filled텨어럋","likecount":5,"imagepath":"http://jangdock.cafe24.com:3011/image/3ec782b308c148dc23fe58e1c8597786","theme":"WHITE","page":0,"createdAt":"2017-02-25T07:36:27.636Z","updatedAt":"2017-02-25T07:36:27.636Z","ReadbookId":9}]}}}}
     */
    public static final String MESSAGE = "message",
            RESULT = "message";

    public static JsonObject getResult(JsonObject jsonObject) {
        if (jsonObject != null) {
            try {
                return jsonObject.get(MESSAGE).getAsJsonObject().get(RESULT).getAsJsonObject();
            } catch (Exception e) {
                Ln.e("PARSE exception", e);
            }
            return null;
        }
        return null;
    }
}
