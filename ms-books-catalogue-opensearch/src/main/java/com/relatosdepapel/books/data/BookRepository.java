package com.relatosdepapel.books.data;

import com.relatosdepapel.books.data.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface BookRepository extends ElasticsearchRepository<Book, String> {


}



