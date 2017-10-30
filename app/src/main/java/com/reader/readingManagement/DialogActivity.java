package com.reader.readingManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.reader.readingManagement.model.Book;
import com.reader.readingManagement.post.model.Post;

import io.realm.Realm;

public class DialogActivity extends Activity implements
        OnClickListener {

    final static String DELETE_BOOK = "book";
    final static String DELETE_POST = "post";
    private Button mConfirm, mCancel;
    private Book mBook;
    private Post mPost;
    private boolean isBook;

    public static void startActivity(Activity activity, Book book) {
        Intent intent = new Intent(activity, DialogActivity.class);
        intent.putExtra(DELETE_BOOK, book);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, Post post) {
        Intent intent = new Intent(activity, DialogActivity.class);
        intent.putExtra(DELETE_POST, post);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);
        mBook = getIntent().getParcelableExtra(DELETE_BOOK);
        mPost = getIntent().getParcelableExtra(DELETE_POST);

        isBook = (mBook != null ? true : false);

        setContent();
    }

    private void setContent() {
        ((TextView) findViewById(R.id.dlt_book_title)).setText( "[ " + (isBook ? mBook.getName() : "포스트") + " ]" + "을(를) 정말 삭제하시겠습니까? ");
        mConfirm = (Button) findViewById(R.id.btnConfirm);
        mCancel = (Button) findViewById(R.id.btnCancel);


        mConfirm.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirm:
                deleteFromDB();
                this.finish();
                break;
            case R.id.btnCancel:
                this.finish();
                break;
            default:
                break;
        }
    }

    private void deleteFromDB(){
        if(isBook) {
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Realm.getDefaultInstance().where(Book.class).equalTo("bookId", mBook.getBookId()).findFirst().deleteFromRealm();
                }
            });
        } else {
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Realm.getDefaultInstance().where(Book.class).equalTo("bookId", mPost.getBookId()).findFirst()
                            .getPosts().where().equalTo("id", mPost.getId()).findFirst().deleteFromRealm();
                }
            });
        }
    }
}

