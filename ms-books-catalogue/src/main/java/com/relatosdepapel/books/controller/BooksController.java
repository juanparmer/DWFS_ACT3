package com.relatosdepapel.books.controller;

import com.relatosdepapel.books.controller.model.BookDto;
import com.relatosdepapel.books.controller.model.BookRequest;
import com.relatosdepapel.books.data.model.Book;
import com.relatosdepapel.books.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
@Slf4j
public class BooksController {

    private final BookService service;

    @GetMapping
    public List<Book> search(@RequestParam(required = false) Map<String, String> params) {
        Map<String, String> filters = (params == null) ? new HashMap<>() : new HashMap<>(params);
        filters.putIfAbsent("visible", "true");
        return service.search(filters);
    }

    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody BookRequest request) {
        Book saved = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping("/{id}")
    public Book getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @Valid @RequestBody BookDto request) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Book> partialUpdate(@PathVariable Long id, @RequestBody BookDto request) {
        Book updated = service.patch(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
