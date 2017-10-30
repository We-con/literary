package com.reader.readingManagement.book.add;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.makeramen.roundedimageview.RoundedImageView;
import com.reader.readingManagement.R;
import com.reader.readingManagement.book.BaseActivity;
import com.reader.readingManagement.book.SearchBookRequest;
import com.reader.readingManagement.model.Book;
import com.reader.readingManagement.model.BookListResult;
import com.reader.readingManagement.network.RequestManager;
import com.reader.readingManagement.utils.CollectionsUtils;
import com.reader.readingManagement.utils.Ln;

import java.util.ArrayList;

import io.realm.Realm;


/**
 * Created by loll_ on 2017-02-14.
 */

public class AddBookActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AddBookAdapter addBookAdapter;
    private ArrayList<Book> bookList;
    private Book selectedBook;
    private RoundedImageView bookImage;
    private EditText searchEditText;
    private String lastQuery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        bookImage = ((RoundedImageView) findViewById(R.id.book_image));
        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
            }
        });
        searchEditText = ((EditText) findViewById(R.id.search_book));
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("ADDTEXT", s.toString());
                requestBookList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.add_book_list);
        mRecyclerView.addItemDecoration(new CustomRecyclerDecoration(AddBookActivity.this, 20));


        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        addBookAdapter = new AddBookAdapter(this, null);
        addBookAdapter.setOnItemClickListener(new AddBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (!CollectionsUtils.isEmpty(bookList)) {
                    Book book = bookList.get(position);
                    selectedBook = book;
                    if (book == null) {
                        return;
                    }
                    Glide.with(AddBookActivity.this).load(book.getThumbnailUrl()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(bookImage);
                }
            }
        });
        mRecyclerView.setAdapter(addBookAdapter);
        initToolbar();
    }


    void hideKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onCreatedToolbar() {
        super.onCreatedToolbar();
        setTitle("책 추가");
        setAction("추가", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedBook == null) {
                    Toast.makeText(AddBookActivity.this, "책을 검색 해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedBook != null) {
                    addBookToDB(selectedBook);
                }

                //TODO : local DB에 책을 추가했으니 서버에도 책을 추가해야겠지?
                /*try {
//                    {"Post":{"id":2,"content":"Content filled텨어럋","likecount":5,"imagepath":"http://jangdock.cafe24.com:3011/image/3ec782b308c148dc23fe58e1c8597786","theme":"WHITE","page":0,"ReadbookId":9,"updatedAt":"2017-02-25T07:36:27.636Z","createdAt":"2017-02-25T07:36:27.636Z"}}
                    AuthIon.withPost(AddBookActivity.this, NeoUrlHelper.addBook(selectedBook.getBookId())).asJsonObject().get(10, TimeUnit.SECONDS);

                } catch (Exception e) {
                    Ln.e("MSG " + NeoUrlHelper.addBook(selectedBook.getBookId()), e);
                    Toast.makeText(AddBookActivity.this, "추가 실패", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                BookInfoActivity.start(AddBookActivity.this, selectedBook);
                */
                finish();
            }
        });
    }

    public class CustomRecyclerDecoration extends RecyclerView.ItemDecoration {
        private final int[] ATTRS = new int[]{android.R.attr.listDivider};
        private final int divHeight;
        private Drawable divider;

        public CustomRecyclerDecoration(int divHeight) {
            this.divHeight = divHeight;
        }

        public CustomRecyclerDecoration(Context context, int divHeight) {
            this.divHeight = divHeight;
            final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
            divider = styledAttributes.getDrawable(0);
            styledAttributes.recycle();
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + divider.getIntrinsicHeight();

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }

    private void requestBookList(String query) {
        if (lastQuery != null) {
            RequestManager.getQueue().cancelAll(lastQuery);
        }
        addBookAdapter.setSearchKey(query);
        SearchBookRequest searchBookRequest = new SearchBookRequest(query,
                new Response.Listener<BookListResult>() {
                    @Override
                    public void onResponse(BookListResult response) {
                        if (response.getBookList() != null) {
                            Toast.makeText(getApplicationContext(), "response 옴", Toast.LENGTH_SHORT).show();
                            bookList = (ArrayList<Book>) response.getBookList().getBooks();
                            addBookAdapter.updateDataset(bookList);
                        } else {
                            Toast.makeText(AddBookActivity.this, "검색결과가 없습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Ln.e("Error", error);
            }
        });
        searchBookRequest.setTag(query);
        lastQuery = query;
        RequestManager.getQueue().add(searchBookRequest);
    }

    private void addBookToDB(Book book) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(book);
        realm.commitTransaction();
    }
}
