package com.reader.readingManagement.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.reader.readingManagement.CheerManager;
import com.reader.readingManagement.DialogActivity;
import com.reader.readingManagement.post.add.PostAddActivity;
import com.reader.readingManagement.utils.Ln;
import com.reader.readingManagement.widget.CustomProgressBar;
import com.reader.readingManagement.R;
import com.reader.readingManagement.book.add.AddBookActivity;
import com.reader.readingManagement.book.info.BookInfoActivity;
import com.reader.readingManagement.model.Book;


public class BookFragment extends Fragment {

    public static final String EXTRA_BOOK = "book";
    private Book mBook;
    private RoundedImageView mImageView;
    private TextView mCardTitle, mCardAuthor, mCardWord;
    private TextView mTotalPage, mCurrentPage;
    private TextView mPostNum, mCheer;
    private CustomProgressBar mProgress;
    private ImageView mPostWriteBtn;

    public static BookFragment newInstance(Book book) {
        BookFragment bookFragment = new BookFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_BOOK, book);
        bookFragment.setArguments(bundle);
        return bookFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ln.d("INITTEST", "onCreate : ");
        mBook = getArguments().getParcelable(EXTRA_BOOK);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Ln.d("INITTEST", "onCreateView : ");

        View view = inflater.inflate(R.layout.main_cardview, container, false);
        mImageView = (RoundedImageView) view.findViewById(R.id.card_thumbnail);
        mCardTitle = (TextView) view.findViewById(R.id.main_card_book_title);
        mTotalPage = (TextView) view.findViewById(R.id.total_page);
        mCurrentPage = (TextView) view.findViewById(R.id.current_page);
        mProgress = ((CustomProgressBar) view.findViewById(R.id.progressbar));
        mCardAuthor = (TextView) view.findViewById(R.id.main_card_book_author);
        mPostNum = (TextView) view.findViewById(R.id.post_num);
        mCheer = (TextView) view.findViewById(R.id.main_card_one_word);
        mPostWriteBtn = (ImageView) view.findViewById(R.id.btn_write);

        mPostWriteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PostAddActivity.startActivity(getActivity(), mBook);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), isAddBookFragment() ? AddBookActivity.class : BookInfoActivity.class);
                intent.putExtra("book", mBook);
                getActivity().startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isAddBookFragment()) {
            Ln.d("INITTEST", "onViewCreated : " + mBook.getName());
            mCardTitle.setText(mBook.getName());
            mCardAuthor.setText(mBook.getAuthor());
            mTotalPage.setText("P " + mBook.getTotalPage());
            mProgress.setMax(Integer.parseInt(mBook.getTotalPage()));
            mProgress.setProgress(mBook.getRecentIndexedPage());
            mCurrentPage.setText("P " + String.valueOf(mBook.getRecentIndexedPage()));
            mCurrentPage.setTextColor(getColor());
            mPostNum.setText(String.valueOf(mBook.getPosts().size()));
            mCheer.setText(CheerManager.getInstance().getCheer(mBook));
            view.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    // TODO : 책 삭제 구현
                    DialogActivity.startActivity(getActivity(), mBook);
                    return true;
                }
            });
        } else {
            Log.d("INITTEST", "onViewCreated: else ");

            mImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_circle_black_18dp));
            mCardTitle.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            mTotalPage.setVisibility(View.GONE);
            mCurrentPage.setVisibility(View.GONE);
            mPostNum.setVisibility(View.GONE);
            mPostWriteBtn.setVisibility(View.GONE);
            mCardAuthor.setVisibility(View.INVISIBLE);
            mCheer.setVisibility(View.GONE);
            view.findViewById(R.id.post_unit).setVisibility(View.GONE);
            (view.findViewById(R.id.card_bg)).setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.nf_bg));
        }
        if (mBook != null && mBook.getThumbnailUrl() != null) {
            Glide.with(this).load(mBook.getThumbnailUrl()).into(mImageView);}
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBook != null && mBook.isValid()) {
            Glide.with(this).load(mBook.getThumbnailUrl()).into(mImageView);
            mProgress.setProgress(mBook.getRecentIndexedPage());

        }
    }


    boolean isAddBookFragment() {
        Log.d("INITTEST", "isAddBookFragment - mBook :  " + mBook.getBookId() == null ? "null" : "not null");
        return mBook.getBookId() == null;
    }

    public void putParcelable(Book book) {
        mBook = book;
    }

    public int getColor(){
        float radio = mBook.getRecentIndexedPage() * 1.0f / Integer.parseInt(mBook.getTotalPage());
        return Color.parseColor(radio < 0.3 ? "#ffd600" : (radio < 0.7 ? "#3fdabf" : "#4aa2f9"));
    }


}
