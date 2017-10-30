package com.reader.readingManagement.my.books;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.reader.readingManagement.book.BaseActivity;
import com.reader.readingManagement.book.add.AddBookActivity;
import com.reader.readingManagement.widget.CustomProgressBar;
import com.reader.readingManagement.R;
import com.reader.readingManagement.utils.CollectionsUtils;
import com.reader.readingManagement.book.info.BookInfoActivity;
import com.reader.readingManagement.model.Book;


import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by loll_ on 2017-02-14.
 */

public class MyBooksActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private BookListAdapter mMyBookListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);
        initToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_books_list);
        mRecyclerView.addItemDecoration(new CustomRecyclerDecoration(dp2px(10)));

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RealmResults<Book> list = Realm.getDefaultInstance().where(Book.class).findAll();

        mMyBookListAdapter = new BookListAdapter(list);
        mMyBookListAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mMyBookListAdapter);
    }

    @Override
    protected void onCreatedToolbar() {
        setTitle("내 서재");
        setAction("추가", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyBooksActivity.this, AddBookActivity.class));
            }
        });

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


    public class BookListAdapter extends RecyclerView.Adapter {
        private RealmResults<Book> list = null;

        public BookListAdapter(RealmResults<Book> list) {
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyBooksHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_books_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((MyBooksHolder) holder).onBind(getItem(position));
        }


        private Book getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setData(RealmResults<Book> list) {
            if (CollectionsUtils.isEmpty(list)) {
                return;
            }
            this.list = list;
            notifyItemRangeChanged(1, list.size());
        }

        class MyBooksHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            Book mBook;
            ImageView myBookThumnail;
            TextView myBookName, myBookAuthor;
            TextView myBookCurPage, myBookTotalPage;
            CustomProgressBar customProgressBar;

            public MyBooksHolder(View itemView) {
                super(itemView);

                myBookThumnail = (ImageView) itemView.findViewById(R.id.my_books_thumnail);
                myBookName = (TextView) itemView.findViewById(R.id.my_books_name);
                myBookAuthor = (TextView) itemView.findViewById(R.id.my_books_author);
                customProgressBar = ((CustomProgressBar) itemView.findViewById(R.id.progressbar));
                myBookCurPage = ((TextView) itemView.findViewById(R.id.my_books_currentP));
                myBookTotalPage = ((TextView) itemView.findViewById(R.id.my_books_totalP));
                itemView.setOnClickListener(this);

            }

            public void onBind(Book book) {
                mBook = book;
                //getBaseContext()?
                Glide.with(getBaseContext()).load(book.getThumbnailUrl()).into(myBookThumnail);
                myBookName.setText(book.getName());
                myBookAuthor.setText(book.getAuthor());
                myBookTotalPage.setText("P " + mBook.getTotalPage());
                customProgressBar.setMax(Integer.parseInt(mBook.getTotalPage()));
                customProgressBar.setProgress(mBook.getRecentIndexedPage());
                myBookCurPage.setText("P " + String.valueOf(mBook.getRecentIndexedPage()));

            }

            @Override
            public void onClick(View v) {
                //책상세 진입
                Intent intent = new Intent(MyBooksActivity.this, BookInfoActivity.class);
                intent.putExtra("book", mBook);
                MyBooksActivity.this.startActivity(intent);
            }
        }

    }
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
