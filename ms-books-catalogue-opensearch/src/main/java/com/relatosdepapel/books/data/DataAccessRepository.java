package com.relatosdepapel.books.data;

import com.relatosdepapel.books.controller.model.AggregationDetails;
import com.relatosdepapel.books.controller.model.BooksQueryResponse;
import com.relatosdepapel.books.data.model.Book;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.opensearch.data.client.orhlc.NativeSearchQueryBuilder;
import org.opensearch.data.client.orhlc.OpenSearchAggregations;
import org.opensearch.index.query.BoolQueryBuilder;
import org.opensearch.index.query.MultiMatchQueryBuilder.Type;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.aggregations.Aggregation;
import org.opensearch.search.aggregations.AggregationBuilders;
import org.opensearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DataAccessRepository {

    @Value("${server.fullAddress}")
    private String serverFullAddress;

    private final BookRepository bookRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    private static final String[] TITLE_SEARCH_FIELDS = {"title", "title._2gram", "title._3gram"};
    private static final String[] AUTHOR_SEARCH_FIELDS = {"author", "author._2gram", "author._3gram"};

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book update(String id, Book book) {
        // Update total - reemplaza todos los campos
        book.setId(id);
        return bookRepository.save(book);
    }

    public Book updatePartial(String id, Book bookUpdates) {
        // Update parcial - solo actualiza campos no nulos
        Optional<Book> existingBookOpt = bookRepository.findById(id);
        if (existingBookOpt.isPresent()) {
            Book existingBook = existingBookOpt.get();

            if (bookUpdates.getTitle() != null) {
                existingBook.setTitle(bookUpdates.getTitle());
            }
            if (bookUpdates.getAuthor() != null) {
                existingBook.setAuthor(bookUpdates.getAuthor());
            }
            if (bookUpdates.getCategory() != null) {
                existingBook.setCategory(bookUpdates.getCategory());
            }
            if (bookUpdates.getIsbn() != null) {
                existingBook.setIsbn(bookUpdates.getIsbn());
            }
            if (bookUpdates.getPublicationDate() != null) {
                existingBook.setPublicationDate(bookUpdates.getPublicationDate());
            }
            if (bookUpdates.getRating() != null) {
                existingBook.setRating(bookUpdates.getRating());
            }
            if (bookUpdates.getStock() != null) {
                existingBook.setStock(bookUpdates.getStock());
            }
            if (bookUpdates.getVisible() != null) {
                existingBook.setVisible(bookUpdates.getVisible());
            }
            if (bookUpdates.getPrice() != null) {
                existingBook.setPrice(bookUpdates.getPrice());
            }

            return bookRepository.save(existingBook);
        }
        return null;
    }

    public Boolean delete(Book book) {
        bookRepository.delete(book);
        return Boolean.TRUE;
    }

    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @SneakyThrows
    public BooksQueryResponse findBooks(String title, String author, String category, String isbn, String publicationDate, Integer rating, Double minPrice, Double maxPrice, Integer page, Integer size, Boolean aggregate) {

        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();

        if (!StringUtils.isEmpty(category)) {
            querySpec.must(QueryBuilders.termQuery("category.keyword", category));
        }

        if (!StringUtils.isEmpty(title)) {
            querySpec.must(QueryBuilders.multiMatchQuery(title, TITLE_SEARCH_FIELDS).type(Type.BOOL_PREFIX));
        }

        if (!StringUtils.isEmpty(author)) {
            querySpec.must(QueryBuilders.multiMatchQuery(author, AUTHOR_SEARCH_FIELDS).type(Type.BOOL_PREFIX));
        }

        if (!StringUtils.isEmpty(isbn)) {
            querySpec.must(QueryBuilders.termQuery("isbn", isbn));
        }

        if (!StringUtils.isEmpty(publicationDate)) {
            querySpec.must(QueryBuilders.termQuery("publicationDate", publicationDate));
        }

        if (rating != null) {
            querySpec.must(QueryBuilders.termQuery("rating", rating));
        }

        if (minPrice != null || maxPrice != null) {
            var rangeQuery = QueryBuilders.rangeQuery("price");
            if (minPrice != null) {
                rangeQuery.gte(minPrice);
            }
            if (maxPrice != null) {
                rangeQuery.lte(maxPrice);
            }
            querySpec.must(rangeQuery);
        }

        // Si no he recibido ningun parametro, busco todos los elementos.
        if (!querySpec.hasClauses()) {
            querySpec.must(QueryBuilders.matchAllQuery());
        }

        // Filtros implícitos - solo libros visibles y con stock disponible
        querySpec.must(QueryBuilders.termQuery("visible", true));
        querySpec.must(QueryBuilders.rangeQuery("stock").gt(0));

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec);

        // Validar y ajustar parámetros de paginación
        int currentPage = (page != null && page >= 0) ? page : 0;
        int pageSize = (size != null && size > 0 && size <= 100) ? size : 10; // Máximo 100 elementos por página

        if (aggregate) {
            nativeSearchQueryBuilder.withAggregations(AggregationBuilders.terms("Category Aggregation").field("category.keyword").size(1000));
            // Mas info sobre size 1000 - https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-bucket-terms-aggregation.html#search-aggregations-bucket-terms-aggregation-size
            nativeSearchQueryBuilder.withMaxResults(0);
        } else {
            // Paginación
            nativeSearchQueryBuilder.withPageable(PageRequest.of(currentPage, pageSize));
        }

        Query query = nativeSearchQueryBuilder.build();
        SearchHits<Book> result = elasticsearchOperations.search(query, Book.class);

        List<AggregationDetails> responseAggs = new LinkedList<>();

        if (result.hasAggregations()) {
            OpenSearchAggregations aggregations = (OpenSearchAggregations) result.getAggregations();
            Map<String, Aggregation> aggs = Objects.requireNonNull(aggregations).aggregations().asMap();
            ParsedStringTerms categoryAgg = (ParsedStringTerms) aggs.get("Category Aggregation");

            String queryParams = getQueryParams(title, author, category);
            categoryAgg.getBuckets()
                    .forEach(
                            bucket -> responseAggs.add(
                                    new AggregationDetails(
                                            bucket.getKey().toString(),
                                            (int) bucket.getDocCount(),
                                            serverFullAddress + "/books?category=" + bucket.getKey() + queryParams)));
        }

        List<Book> books = result.getSearchHits().stream().map(SearchHit::getContent).toList();

        // Crear metadatos de paginación
        BooksQueryResponse response = new BooksQueryResponse(books, responseAggs);

        if (!aggregate) {
            long totalElements = result.getTotalHits();
            int totalPages = (int) Math.ceil((double) totalElements / pageSize);

            BooksQueryResponse.PaginationMetadata pagination = new BooksQueryResponse.PaginationMetadata(
                    currentPage,
                    pageSize,
                    totalElements,
                    totalPages,
                    currentPage < totalPages - 1,
                    currentPage > 0
            );

            response.setPagination(pagination);
        }

        return response;
    }

    @SneakyThrows
    public List<Book> findByCategory(String category) {
        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();
        querySpec.must(QueryBuilders.termQuery("category.keyword", category));
        querySpec.must(QueryBuilders.termQuery("visible", true));
        querySpec.must(QueryBuilders.rangeQuery("stock").gt(0));

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec)
                .withPageable(PageRequest.of(0, 100));

        Query query = nativeSearchQueryBuilder.build();
        SearchHits<Book> result = elasticsearchOperations.search(query, Book.class);
        return result.getSearchHits().stream().map(SearchHit::getContent).toList();
    }

    @SneakyThrows
    public List<Book> findByAuthor(String author) {
        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();
        querySpec.must(QueryBuilders.multiMatchQuery(author, AUTHOR_SEARCH_FIELDS).type(Type.BOOL_PREFIX));
        querySpec.must(QueryBuilders.termQuery("visible", true));
        querySpec.must(QueryBuilders.rangeQuery("stock").gt(0));

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec)
                .withPageable(PageRequest.of(0, 100));

        Query query = nativeSearchQueryBuilder.build();
        SearchHits<Book> result = elasticsearchOperations.search(query, Book.class);
        return result.getSearchHits().stream().map(SearchHit::getContent).toList();
    }

    @SneakyThrows
    public List<Book> findByTitle(String title) {
        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();
        querySpec.must(QueryBuilders.multiMatchQuery(title, TITLE_SEARCH_FIELDS).type(Type.BOOL_PREFIX));
        querySpec.must(QueryBuilders.termQuery("visible", true));
        querySpec.must(QueryBuilders.rangeQuery("stock").gt(0));

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec)
                .withPageable(PageRequest.of(0, 100));

        Query query = nativeSearchQueryBuilder.build();
        SearchHits<Book> result = elasticsearchOperations.search(query, Book.class);
        return result.getSearchHits().stream().map(SearchHit::getContent).toList();
    }

    @SneakyThrows
    public List<Book> findByIsbn(String isbn) {
        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();
        querySpec.must(QueryBuilders.termQuery("isbn", isbn));
        querySpec.must(QueryBuilders.termQuery("visible", true));
        querySpec.must(QueryBuilders.rangeQuery("stock").gt(0));

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec)
                .withPageable(PageRequest.of(0, 100));

        Query query = nativeSearchQueryBuilder.build();
        SearchHits<Book> result = elasticsearchOperations.search(query, Book.class);
        return result.getSearchHits().stream().map(SearchHit::getContent).toList();
    }

    /**
     * Componemos una URI basada en serverFullAddress y query params para cada argumento, siempre que no viniesen vacios
     *
     * @param title    - título del libro
     * @param author   - autor del libro
     * @param category - categoría del libro
     * @return query params string
     */
    private String getQueryParams(String title, String author, String category) {
        String queryParams = (StringUtils.isEmpty(title) ? "" : "&title=" + title)
                + (StringUtils.isEmpty(author) ? "" : "&author=" + author);
        // Eliminamos el ultimo & si existe
        return queryParams.endsWith("&") ? queryParams.substring(0, queryParams.length() - 1) : queryParams;
    }
}
