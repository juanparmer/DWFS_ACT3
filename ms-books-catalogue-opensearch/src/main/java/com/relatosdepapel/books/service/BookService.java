package com.relatosdepapel.books.service;

import com.relatosdepapel.books.controller.model.BookDto;
import com.relatosdepapel.books.controller.model.BookRequest;
import com.relatosdepapel.books.controller.model.BooksQueryResponse;
import com.relatosdepapel.books.data.model.Book;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

public interface BookService {

    BooksQueryResponse getBooks(String title, String author, String category, String isbn, String publicationDate, Integer rating, Integer page, Integer size, Boolean aggregate);

    Book createBook(BookRequest book);

    Book updateBook(String id, BookRequest request);

    Book patchBook(String id, BookRequest request);

    Boolean deleteBook(String id);

    Book getBook(String id);
}
