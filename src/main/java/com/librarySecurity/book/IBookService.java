package com.librarySecurity.book;

import java.util.List;
import java.util.Optional;

public interface IBookService {
    List<Book> getAllBooks();
    Book add(Book book);
    Optional<Book> findBookById(Long bookId);
    void delete(Long bookId);
    Book update(Book book);
}
