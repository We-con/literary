package com.reader.readingManagement.network;

import static com.reader.readingManagement.network.UrlHelper.BASE_API_URL;

/**
 * Created by naver on 2017. 2. 25..
 */

public class NeoUrlHelper {
    public static String getBaseAPI() {
        return BASE_API_URL + "/v1";
    }

    public static String addBook(String bookId) {
        return getBaseAPI() + "/book/" + bookId;
    }

    public static String addPost(String bookId) {
        return getBaseAPI() + "/post/" + bookId;
    }

    public static String getPostMy(String bookId) {
        return getBaseAPI() + "/post/" + bookId + "/my";
    }

    public static String getPostOther(String bookId) {
        return getBaseAPI() + "/post/" + bookId + "/all";
    }


}
