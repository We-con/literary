package com.reader.readingManagement.book.info;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.reader.readingManagement.R;
import com.reader.readingManagement.model.Book;
import com.reader.readingManagement.post.HeaderPostListAdapter;

/**
 * Created by naver on 2017. 2. 12..
 */
public class BookInfoAdapter extends HeaderPostListAdapter {
    Book mBook;

    public BookInfoAdapter(AppCompatActivity activity, Book book) {
        super(activity);
        mBook = book;
        setHasHeader(true);
    }

    public void setBook(Book book){
        mBook = book;
    }

    @Override
    protected RecyclerView.ViewHolder createHeaderView(LayoutInflater layoutInflater, ViewGroup parent) {
        return new HeaderHolder(layoutInflater.inflate(R.layout.book_info_header, parent, false));
    }

    @Override
    protected void onBindHeaderView(RecyclerView.ViewHolder holder, int position) {
        ((HeaderHolder) holder).onBind(mBook, onClickListener, getActivity());
    }

}
