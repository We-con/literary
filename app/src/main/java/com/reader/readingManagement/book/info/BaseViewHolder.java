package com.reader.readingManagement.book.info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.reader.readingManagement.model.Book;


/**
 * Created by naver on 2017. 2. 12..
 */

public abstract class BaseViewHolder<T extends Object> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void onBind(Book data, View.OnClickListener onClickListener, Context context);
}
