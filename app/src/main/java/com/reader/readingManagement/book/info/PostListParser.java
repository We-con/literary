package com.reader.readingManagement.book.info;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.reader.readingManagement.post.model.Post;


import java.util.ArrayList;

/**
 * Created by naver on 2017. 2. 25..
 */

public class PostListParser extends BaseParser {

    /**
     * {"message":{"result":{"post":{"posts":[{"id":2,"content":"Content filled텨어럋","likecount":5,"imagepath":"http://jangdock.cafe24.com:3011/image/3ec782b308c148dc23fe58e1c8597786","theme":"WHITE","page":0,"createdAt":"2017-02-25T07:36:27.636Z","updatedAt":"2017-02-25T07:36:27.636Z","ReadbookId":9}]}}}}
     */
    static ArrayList<Post> parseJson(JsonObject jsonObject) {
        JsonObject result = getResult(jsonObject);
        if (result == null) {
            return null;
        }
        JsonArray posts = result.get("post").getAsJsonObject().get("posts").getAsJsonArray();
        ArrayList<Post> postList = new ArrayList<>();
        for (JsonElement j : posts) {
            JsonObject post = j.getAsJsonObject();
        }
        return postList;
    }

}
