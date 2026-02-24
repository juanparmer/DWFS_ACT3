package com.relatosdepapel.books.controller;


import com.relatosdepapel.books.controller.model.BookRequest;
import com.relatosdepapel.books.controller.model.BooksQueryResponse;
import com.relatosdepapel.books.data.model.Book;
import com.relatosdepapel.books.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;


@Tag(name = "Books", description = "API para gestionar el catálogo de libros")
@RestController
@RequestMapping("/books")
// Mirar la direccion de origins, debe ser la de vercel, CrossOrigin ignora CORS
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
public class BooksController {


    private final BookService service;


    @Operation(summary = "Buscar libros", description = "Buscar libros con filtros opcionales")
    @ApiResponse(responseCode = "200", description = "Lista de libros encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @Parameter(name = "params", description = "Parámetros de consulta: title, author, category, isbn, publicationDate, rating, page, size", required = false)
    @GetMapping
    public ResponseEntity<BooksQueryResponse> getBooks(
            @RequestHeader Map<String, String> headers,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String publicationDate,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "false") Boolean aggregate) {

        log.info("headers: {}", headers);
        BooksQueryResponse books = service.getBooks(title, author, category, isbn, publicationDate, rating, page, size, aggregate);
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Crear libro", description = "Crea un nuevo libro")
    @ApiResponse(responseCode = "201", description = "Libro creado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Payload del libro", required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))
    )
    @PostMapping
    public ResponseEntity<Book> getBook(@RequestBody BookRequest request) {

        Book createdBook = service.createBook(request);

        if (createdBook != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

    @Operation(summary = "Obtener libro por id", description = "Devuelve un libro por su identificador")
    @ApiResponse(responseCode = "200", description = "Libro encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @Parameter(name = "id", description = "ID del libro", required = true)
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable String id) {
        log.info("Request received for product {}", id);
        Book book = service.getBook(id);

        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Actualizar libro completo", description = "Actualiza todos los campos de un libro (PUT)")
    @ApiResponse(responseCode = "200", description = "Libro actualizado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @Parameter(name = "id", description = "ID del libro", required = true)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Payload completo del libro", required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookRequest.class))
    )
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable String id, @RequestBody BookRequest request) {
        log.info("Update request received for book {}", id);
        Book updatedBook = service.updateBook(id, request);

        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Actualizar libro parcialmente", description = "Actualiza solo los campos proporcionados (PATCH)")
    @ApiResponse(responseCode = "200", description = "Libro actualizado parcialmente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @Parameter(name = "id", description = "ID del libro", required = true)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Campos a actualizar (solo los proporcionados se modificarán)", required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookRequest.class))
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Book> patchBook(@PathVariable String id, @RequestBody BookRequest request) {
        log.info("Patch request received for book {}", id);
        Book patchedBook = service.patchBook(id, request);

        if (patchedBook != null) {
            return ResponseEntity.ok(patchedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar libro", description = "Elimina un libro por id")
    @ApiResponse(responseCode = "204", description = "Libro eliminado")
    @Parameter(name = "id", description = "ID del libro", required = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        Boolean removed = service.deleteBook(id);

        if (Boolean.TRUE.equals(removed)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
