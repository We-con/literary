package com.reader.readingManagement.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reader.readingManagement.ApplicationPreferences;
import com.reader.readingManagement.R;
import com.reader.readingManagement.book.BaseActivity;
import com.reader.readingManagement.book.add.AddBookActivity;
import com.reader.readingManagement.model.Book;
import com.reader.readingManagement.my.books.MyBooksActivity;
import com.reader.readingManagement.utils.*;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends BaseActivity {
    RealmResults<Book> mybooks;
    private ViewPager viewpager;
    private ViewGroup booksIndicator;
    private TextView mDefaultIndicator;
    private TextView selectedBook, totalBook;
    private int currentPosition = 0;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    private BookChangedListener bookChangedListener;
    private Book mBook;
    private View alaram;

    @Override
    protected void onCreatedToolbar() {
        super.onCreatedToolbar();
        ImageView actionAlarm = (ImageView) findViewById(R.id.action_alarm);
        actionAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //TODO : 리마인드 기능 나중에 추가하기 ㅠㅠ
//                showAlarm();
            }

        });
        setTitle("문학소년");
    }
/*
    public void alarmPopup(View view) {
        switch (view.getId()) {
            case R.id.alarm_coach:
                view.setVisibility(View.GONE);
                break;
            case R.id.theme_1:
                break;
            case R.id.theme_3:
                break;
            case R.id.theme_4:
                break;
            case R.id.theme_5:
                break;

        }
    }*/
/*
    private void showAlarm() {

        alaram = findViewById(R.id.alarm_coach);
        alaram.setVisibility(View.VISIBLE);
        alaram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alaram.setVisibility(View.GONE);
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "이동합니다.", Toast.LENGTH_SHORT).show();
                switch (v.getId()) {
                    case R.id.theme_1:
                        break;
                    case R.id.theme_3:
                        break;
                    case R.id.theme_4:
                        break;
                    case R.id.theme_5:
                        break;
                }
            }
        };
        alaram.findViewById(R.id.theme_1).setOnClickListener(listener);
        alaram.findViewById(R.id.theme_3).setOnClickListener(listener);
        alaram.findViewById(R.id.theme_4).setOnClickListener(listener);
        alaram.findViewById(R.id.theme_5).setOnClickListener(listener);
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initToolbar();
        appGuide();
/*
        bookChangedListener = new BookChangedListener() {
            @Override
            public void onSelectedBook(Book book, boolean isLast) {
                //여기서 새로운 데이터 갱신해서 mHomeAdapter 에 전달해서 포스트 다시 갱신하시면 됩니다.
            }

        };*/
        mDefaultIndicator = (TextView) findViewById(R.id.default_indicator);
        booksIndicator = (ViewGroup) findViewById(R.id.book_indicator);
        booksIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MyBooksActivity.class);
                startActivity(intent);
            }
        });
        selectedBook = (TextView) findViewById(R.id.book_indicator_cur);
        totalBook = (TextView) findViewById(R.id.book_indicator_total);

        viewpager = (ViewPager) findViewById(R.id.viewpager_main);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                selectedBook.setText(String.valueOf(position+1));
                /*if (bookChangedListener != null) {
                    Book book = (position < mybooks.size() ? mybooks.get(position) : new Book("최근에 남긴 포스트"));
                    bookChangedListener.onSelectedBook(book, position == mFragmentCardAdapter.getCount() - 1);
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager());
        mFragmentCardShadowTransformer = new ShadowTransformer(viewpager, mFragmentCardAdapter);
        viewpager.setAdapter(mFragmentCardAdapter);
        viewpager.setPageTransformer(false, mFragmentCardShadowTransformer);

        findViewById(R.id.addBook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddBookActivity.class));
            }
        });

        /*
        findViewById(R.id.home_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBook!=null) {
                }
            }
        });*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }
/*
    TODO : 홈화면 정리
    private void requestPost(Book book) {
        setPost(post);

    }

    private void setPost(Post post) {
        View view = findViewById(R.id.home_post);
        TextView postTitle = (TextView) view.findViewById(R.id.post_head);
        postTitle.setText(post.getUserNickname());
        TextView postContent = (TextView) view.findViewById(R.id.post_content);
        postContent.setText(post.getContent());
    }
*/

    @Override
    protected void onResume() {
        super.onResume();
        mybooks = Realm.getDefaultInstance().where(Book.class).findAll();
        mFragmentCardAdapter.updateList(mybooks);

        if(CollectionsUtils.isEmpty(mybooks)){
            // 저장된 책이 없을 경우
            mDefaultIndicator.setVisibility(View.VISIBLE);
            booksIndicator.setVisibility(View.GONE);
            return;
        }

        mDefaultIndicator.setVisibility(View.GONE);
        booksIndicator.setVisibility(View.VISIBLE);
        totalBook.setText(String.valueOf(mybooks.size()));
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int booksSize = mybooks.size();
                        if (booksSize > 1) {
                            viewpager.setCurrentItem(mybooks.size() - 1, true);
                        }
                    }
                });
                return false;
            }
        }).sendEmptyMessageDelayed(0, 500);
    }

    public float dpToPixels(int dp) {
        return dp * (getResources().getDisplayMetrics().density);
    }


    interface BookChangedListener {
        void onSelectedBook(Book book, boolean isLast);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void appGuide(){
        ApplicationPreferences pref = ApplicationPreferences.getInstance();
        if(pref.getGuide() != true){
            pref.setGuide(true);
            startActivity(new Intent(HomeActivity.this, GuideActivity.class));
            return ;
        }
    }
}

