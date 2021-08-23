package com.peixoto.api.utils;

import com.peixoto.api.domain.Book;

public class BookFactory {

    public static Book getBookWithoutId() {
        Book book =  new Book();
        book.setAuthor("Rafael Peixoto");
        book.setTitle("Selenium WebDriver");
        book.setBookCategory("Software Test");
        return book;
    }

    public static Book getBookWithId() {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Rafael Peixoto");
        book.setTitle("Selenium WebDriver");
        book.setBookCategory("Software Test");
        return book;
    }
}
