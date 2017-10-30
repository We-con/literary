package com.reader.readingManagement.model;


/**
 * Created by 밈석 on 2017-01-23.
 */
public class RecyItem {
    private String mText, mBookTitle, mBookAuthor, mMyWord;
    //이미지?

    public RecyItem(String text, String title, String author, String myword) {
        mText = text;
        mBookTitle = title;
        mBookAuthor = author;
        mMyWord = myword;
    }

    public String getText() {
        return mText;
    }

    public String getTitle() {
        return mBookTitle;
    }

    public String getAuthor() {
        return mBookAuthor;
    }

    public String getMyWord() {
        return mMyWord;
    }

    public void setText(String text) {
        mText = text;
    }

}
