package com.relatosdepapel.books.controller.model;

import com.relatosdepapel.books.data.model.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BooksQueryResponse {

    private List<Book> books;
    private List<AggregationDetails> aggs;
    private PaginationMetadata pagination;

    public BooksQueryResponse(List<Book> books, List<AggregationDetails> aggs) {
        this.books = books;
        this.aggs = aggs;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class PaginationMetadata {
        private int currentPage;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean hasNext;
        private boolean hasPrevious;
    }
}
