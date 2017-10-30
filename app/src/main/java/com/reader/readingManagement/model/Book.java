package com.reader.readingManagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.reader.readingManagement.post.model.Post;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by naver on 2017. 1. 29..
 */

public class Book extends RealmObject implements Parcelable {
    @PrimaryKey
    String bookId;
    String name;
    String author;
    String thumbnailUrl;
    String page;
    String pub_nm;
    int recentIndexedPage = 0;

    public RealmList<Post> posts;


    public Book() {

    }

    public Book(String bookId) {
        this.bookId = bookId;
    }

    public Book(String bookId, String title, String author, String thumbnailUrl, String page) {
        this.bookId = bookId;
        this.name = title;
        this.author = author;
        this.thumbnailUrl = thumbnailUrl;
        this.page = page;
    }

    protected Book(Parcel in) {
        bookId       = in.readString();
        name         = in.readString();
        author       = in.readString();
        thumbnailUrl = in.readString();
        page         = in.readString();
        pub_nm       = in.readString();
        recentIndexedPage = in.readInt();
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTotalPage() {
        return page;
    }

    public void setTotalPage(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getRecentIndexedPage() {
        return recentIndexedPage;
    }

    public void setRecentIndexedPage(int recentIndexedPage) {
        this.recentIndexedPage = recentIndexedPage;
    }

    public String getPub_nm() {
        return pub_nm;
    }

    public void setPub_nm(String pub_nm) {
        this.pub_nm = pub_nm;
    }

    public static final Parcelable.Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        /*TODO : HomeActivity의 onStop() callback에서 gabbage collector에 의한 잘못된 참조 발생
          TODO : -> realm delete할 때 충돌일어난거같은데.. 모르겟다 슈발..

        */
        if(isValid()) {
            dest.writeString(bookId);
            dest.writeString(name);
            dest.writeString(author);
            dest.writeString(thumbnailUrl);
            dest.writeString(page);
            dest.writeString(pub_nm);
            dest.writeInt(recentIndexedPage);
        }
    }

    public RealmList<Post> getPosts(){
        return posts;
    }
}
