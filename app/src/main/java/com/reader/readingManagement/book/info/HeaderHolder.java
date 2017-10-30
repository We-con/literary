package com.reader.readingManagement.book.info;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.reader.readingManagement.R;
import com.reader.readingManagement.model.Book;

/**
 * Created by naver on 2017. 2. 12..
 */
class HeaderHolder extends BaseViewHolder<Book> {

    private final ImageView thumnail;
    private final TextView title, author;
    private final TextView currentPage, totalPage;
    private final ProgressBar progressBar;

    public HeaderHolder(View itemView) {
        super(itemView);
        thumnail = (ImageView) itemView.findViewById(R.id.my_books_thumnail);
        title = (TextView) itemView.findViewById(R.id.my_books_name);
        author = (TextView) itemView.findViewById(R.id.my_books_author);
        currentPage= (TextView) itemView.findViewById(R.id.my_books_currentP);
        totalPage = (TextView) itemView.findViewById(R.id.my_books_totalP);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
    }


    @Override
    public void onBind(Book data, View.OnClickListener onClickListener, Context context) {
        if (data == null) {
            return;
        }
        Glide.with(context).load(data.getThumbnailUrl()).into(thumnail);

        title.setText(data.getName());
        author.setText(data.getAuthor());
        currentPage.setText("P " + String.valueOf(data.getRecentIndexedPage()));
        totalPage.setText("P " + data.getTotalPage());
        progressBar.setMax(Integer.parseInt(data.getPage()));
        progressBar.setProgress(data.getRecentIndexedPage());
    }

}
