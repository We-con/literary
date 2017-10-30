package com.reader.readingManagement.book;

import com.android.volley.Response;
import com.reader.readingManagement.R;
import com.reader.readingManagement.model.BookListResult;
import com.reader.readingManagement.network.JacksonApiRequest;
import com.reader.readingManagement.network.UrlHelper;

/**
 * Created by naver on 2017. 2. 22..
 */

public class SearchBookRequest extends JacksonApiRequest {

    public SearchBookRequest(String query, Response.Listener<BookListResult> responseListener, Response.ErrorListener errorListener) {
        super(UrlHelper.getApiUrl(R.id.api_book_search, query), BookListResult.class, responseListener, errorListener);
    }
}
