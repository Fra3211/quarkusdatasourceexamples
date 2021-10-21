package com.example.repo;

import com.example.data.Book;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class BookRepository implements PanacheRepositoryBase<Book, Integer> {

    public List<Book> findAllBooks() {
        return findAll().list();
    }
}
