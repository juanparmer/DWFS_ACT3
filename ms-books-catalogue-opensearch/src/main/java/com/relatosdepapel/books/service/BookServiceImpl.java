package com.relatosdepapel.books.service;

import com.relatosdepapel.books.controller.model.BookRequest;
import com.relatosdepapel.books.controller.model.BooksQueryResponse;
import com.relatosdepapel.books.data.DataAccessRepository;
import com.relatosdepapel.books.data.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final DataAccessRepository repository;

    @Override
    public BooksQueryResponse getBooks(String title, String author, String category, String isbn, String publicationDate, Integer rating, Integer page, Integer size, Boolean aggregate) {
        return repository.findBooks(title, author, category, isbn, publicationDate, rating, page, size, aggregate != null && aggregate);
    }

    @Override
    public Book getBook(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    @Override
    public Book createBook(BookRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be null");
        }

        String title = request.getTitle() != null ? request.getTitle().trim() : "";
        String author = request.getAuthor() != null ? request.getAuthor().trim() : "";
        String category = request.getCategory() != null ? request.getCategory().trim() : "";

        if (!StringUtils.hasLength(title)
                || !StringUtils.hasLength(author)
                || !StringUtils.hasLength(category)
                || request.getVisible() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required book fields");
        }

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setVisible(request.getVisible());

        if (request.getPublicationDate() != null) {
            book.setPublicationDate(request.getPublicationDate());
        }
        if (request.getIsbn() != null) {
            book.setIsbn(request.getIsbn().trim());
        }
        if (request.getRating() != null) {
            book.setRating(request.getRating());
        }
        if (request.getStock() != null) {
            book.setStock(request.getStock());
        }

        return repository.save(book);
    }

    @Override
    public Book updateBook(String id, BookRequest request) {
        // PUT - Update total (reemplaza todos los campos)
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be null");
        }

        // Verificar que el libro existe
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        String title = request.getTitle() != null ? request.getTitle().trim() : "";
        String author = request.getAuthor() != null ? request.getAuthor().trim() : "";
        String category = request.getCategory() != null ? request.getCategory().trim() : "";

        if (!StringUtils.hasLength(title)
                || !StringUtils.hasLength(author)
                || !StringUtils.hasLength(category)
                || request.getVisible() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required book fields for update");
        }

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setVisible(request.getVisible());
        book.setPublicationDate(request.getPublicationDate());
        book.setIsbn(request.getIsbn() != null ? request.getIsbn().trim() : null);
        book.setRating(request.getRating());
        book.setStock(request.getStock());

        return repository.update(id, book);
    }

    @Override
    public Book patchBook(String id, BookRequest request) {
        // PATCH - Update parcial (solo actualiza campos enviados)
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be null");
        }

        Book bookUpdates = new Book();

        if (request.getTitle() != null && StringUtils.hasLength(request.getTitle().trim())) {
            bookUpdates.setTitle(request.getTitle().trim());
        }
        if (request.getAuthor() != null && StringUtils.hasLength(request.getAuthor().trim())) {
            bookUpdates.setAuthor(request.getAuthor().trim());
        }
        if (request.getCategory() != null && StringUtils.hasLength(request.getCategory().trim())) {
            bookUpdates.setCategory(request.getCategory().trim());
        }
        if (request.getIsbn() != null) {
            bookUpdates.setIsbn(request.getIsbn().trim());
        }
        if (request.getPublicationDate() != null) {
            bookUpdates.setPublicationDate(request.getPublicationDate());
        }
        if (request.getRating() != null) {
            bookUpdates.setRating(request.getRating());
        }
        if (request.getStock() != null) {
            bookUpdates.setStock(request.getStock());
        }
        if (request.getVisible() != null) {
            bookUpdates.setVisible(request.getVisible());
        }

        Book updatedBook = repository.updatePartial(id, bookUpdates);
        if (updatedBook == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }

        return updatedBook;
    }

    @Override
    public Boolean deleteBook(String id) {
        Book book = repository.findById(id).orElse(null);
        if (book != null) {
            repository.delete(book);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}

