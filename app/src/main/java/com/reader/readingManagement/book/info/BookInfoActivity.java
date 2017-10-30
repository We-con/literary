package com.reader.readingManagement.book.info;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reader.readingManagement.R;
import com.reader.readingManagement.book.BaseActivity;
import com.reader.readingManagement.book.SearchBookRequest;
import com.reader.readingManagement.model.Book;
import com.reader.readingManagement.model.BookListResult;
import com.reader.readingManagement.network.RequestManager;
import com.reader.readingManagement.post.add.PostAddActivity;
import com.reader.readingManagement.post.model.Post;
import com.reader.readingManagement.utils.CollectionsUtils;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by naver on 2017. 2. 12..
 */

public class BookInfoActivity extends BaseActivity {
    public static final String EXTRA_BOOK = "book";

    private Book mBook;
    private RecyclerView mRecyclerView;
    private ArrayList<Post> list = new ArrayList<>();
    private BookInfoAdapter bookInfoAdapter;

    public static void start(Context context, Book book) {
        Intent intent = new Intent(context, BookInfoActivity.class);
        intent.putExtra(EXTRA_BOOK, book);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        initToolbar();
        setTitle("책상세");

        ((ImageView) findViewById(R.id.navigation)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_back_nor));
        try {
            Bundle bundle = getIntent().getExtras();
            mBook = bundle.getParcelable(EXTRA_BOOK);
        } catch (NullPointerException e) {
            finish();
        }

        mRecyclerView = ((RecyclerView) findViewById(R.id.recycler_view));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.addItemDecoration(new CustomRecyclerDecoration(dp2px(8)));

        bookInfoAdapter = new BookInfoAdapter(this, mBook);
        bookInfoAdapter.setData(loadMyPosts());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(bookInfoAdapter);

        findViewById(R.id.addPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TTTTTTTTT", String.valueOf(mBook.getRecentIndexedPage()));
                PostAddActivity.startActivity(BookInfoActivity.this, mBook);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mBook = Realm.getDefaultInstance().where(Book.class).equalTo("bookId", mBook.getBookId()).findFirst();
        bookInfoAdapter.setBook(mBook);
        bookInfoAdapter.setData(loadMyPosts());
        bookInfoAdapter.notifyDataSetChanged();
    }


    //TODO : 서버에서 포스트들을 요청하여 넣고있군.
    //TODO : 다른 사람들 포스트들을 이렇게 불러와야 될거같네.../ 내 포스트와 다른 사람 포스트를 다른 루트로 불러오면 그에 따른 이슈도 추가로 발생할 듯...ㅅㅂ..
    private void requestBookList(boolean isMine) {
        SearchBookRequest searchBookRequest = new SearchBookRequest(isMine ? "어린왕자" : "인생학교",
                new Response.Listener<BookListResult>() {
                    @Override
                    public void onResponse(BookListResult response) {
                        if (response.getBookList() != null) {
                            list = new ArrayList<>();
                            for (Book book : response.getBookList().getBooks()) {
//                                list.add(new Post(book));
                            }
                            if (!CollectionsUtils.isEmpty(list)) {
//                                bookInfoAdapter.setData(list);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestManager.getQueue().add(searchBookRequest);
    }

    protected String getBookId() {
        return mBook.getBookId();
    }

    public class CustomRecyclerDecoration extends RecyclerView.ItemDecoration {
        private final int divHeight;


        public CustomRecyclerDecoration(int divHeight) {
            this.divHeight = divHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            outRect.top = divHeight;
        }
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    public RealmResults<Post> loadMyPosts(){
        return Realm.getDefaultInstance().where(Book.class).equalTo("bookId", mBook.getBookId()).findFirst().getPosts().sort("createDate", Sort.DESCENDING);
    }

}
