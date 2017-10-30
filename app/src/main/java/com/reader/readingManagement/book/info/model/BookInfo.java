package com.reader.readingManagement.book.info.model;

/**
 * Created by naver on 2017. 2. 12..
 */
public class BookInfo {
    int bookId;//서버에서 할당해주는 책 식별자
    String titleName;//책 이름
    String thumbnail;//img URL

    public BookInfo(String name) {
        titleName = name;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
