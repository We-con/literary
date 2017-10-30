package com.reader.readingManagement.book.add;

import android.os.AsyncTask;

import com.android.volley.Response;
import com.reader.readingManagement.model.Book;
import com.reader.readingManagement.network.JacksonApiRequest;
import com.reader.readingManagement.network.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by loll_ on 2017-02-21.
 */

public class AddBookListLoadAsyncTask extends AsyncTask<String, Void, ArrayList<Book>> {
    ArrayList<Book> list = new ArrayList<>();

    @Override
    protected ArrayList<Book> doInBackground(String... params) {
        JacksonApiRequest<Book> request = new JacksonApiRequest<>(
                "http://jangdock.cafe24.com:3011/api/v1/book/search",
                Book.class,
                new Response.Listener<Book>() {
                    @Override
                    public void onResponse(Book response) {
                        list.add(response);
                        System.out.println(response);
                    }
                }
        );

        Map<String, String> map = new HashMap<>();
        map.put("keyword", "어린왕자");
        request.appendParams(map);
        RequestManager.getQueue().add(request);
        return list;
    }

}
