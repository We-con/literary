package com.reader.readingManagement.book.add;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.reader.readingManagement.R;
import com.reader.readingManagement.utils.CollectionsUtils;
import com.reader.readingManagement.model.Book;

import java.util.ArrayList;


/**
 * Created by loll_ on 2017-02-14.
 */

public class AddBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_TYPE = 0;
    private static final int BOOK_TYPE = 1;

    private final RequestManager glide;
    private ArrayList<Book> list;
    private String searchKey;
    static OnItemClickListener mOnItemClickListener;

    protected RecyclerView.ViewHolder createHeaderView(ViewGroup parent) {
        return new HeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.add_book_header, parent, false));

    }

    protected void onBindHeaderView(RecyclerView.ViewHolder holder) {
        ((HeaderHolder) holder).onBind(searchKey);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView bookInfo;
        private TextView page;
        private ImageView imageView;
        public TextView bookName;

        public ViewHolder(View v) {
            super(v);
            bookName = (TextView) v.findViewById(R.id.book_name);
            bookInfo = (TextView) v.findViewById(R.id.book_author);
            imageView = ((ImageView) v.findViewById(R.id.book_image));
            page = ((TextView) v.findViewById(R.id.book_page));
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(v, getLayoutPosition());
        }

        public void onBind(RequestManager glide, Book book) {
            if (book == null) {
                return;
            }
            bookInfo.setText(book.getAuthor() + " | " + book.getPub_nm() + " 펴냄");
            bookName.setText(book.getName());
            page.setText(!TextUtils.isEmpty(book.getTotalPage()) ? String.valueOf(book.getTotalPage())+".p" : "페이지 정보 없음");
            glide.load(book.getThumbnailUrl()).into(imageView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public AddBookAdapter(Activity activity, ArrayList<Book> list) {
        this.list = list;
        glide = Glide.with(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER_TYPE:
                return createHeaderView(parent);
            case BOOK_TYPE:
            default:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);

                ViewHolder vh = new ViewHolder(v);
                return vh;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER_TYPE : BOOK_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case HEADER_TYPE:
                onBindHeaderView(holder);
                break;
            case BOOK_TYPE:
                ((ViewHolder) holder).onBind(glide, list.get(position));
                break;
        }
    }


    public void setSearchKey(String stringKey) {
        this.searchKey = stringKey;

    }

    public void updateDataset(ArrayList<Book> list) {
        if (!CollectionsUtils.isEmpty(list)) {
            this.list = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return (CollectionsUtils.isEmpty(list) ? 0 : list.size());
    }


    class HeaderHolder extends RecyclerView.ViewHolder {

        TextView textView;
        TextView textCount;

        public HeaderHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.add_book_header_title);
            textCount = (TextView) itemView.findViewById(R.id.add_book_header_count);
        }

        public void onBind(String data) {
            if (data == null) {
                return;
            }
            if (CollectionsUtils.isEmpty(list)) {
                textView.setText("\"" + data + "\"");
                textCount.setText("의 0개의 검색결과");
                return;
            }
            textView.setText("\"" + data + "\"");
            textCount.setText("의 " + Integer.toString(list.size()) + "개의 검색결과");

        }
    }

}
