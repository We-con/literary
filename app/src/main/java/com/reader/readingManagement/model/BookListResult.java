package com.reader.readingManagement.model;

import java.util.List;

/**
 * Created by naver on 2017. 2. 22..
 */

public class BookListResult {
    private BookList bookList;

    public BookList getBookList() {
        return bookList;
    }

    public void setBookList(BookList bookList) {
        this.bookList = bookList;
    }

    public static class BookList {
        private List<Book> books;

        public List<Book> getBooks() {
            return books;
        }

        public void setBooks(List<Book> books) {
            this.books = books;
        }
    }
}
