package com.example;

import com.example.data.Author;
import com.example.data.Book;
import com.example.repo.BookRepository;
import io.quarkus.runtime.StartupEvent;
import org.hibernate.search.mapper.orm.Search;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/hello")
public class ExampleResource {

    @Inject
    BookRepository bookRepository;

    @Inject
    EntityManager entityManager;

    @Transactional
    void onStart(@Observes StartupEvent startupEvent) throws InterruptedException {
        if(Book.count() > 0) {
            Search.session(entityManager)
                    .massIndexer()
                    .startAndWait();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> hello() {
        return bookRepository.findAllBooks();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Book saveBook(Book book){
        bookRepository.persist(book);
        return book;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Book updateBook(@PathParam("id") Integer id, Book book){

        return book;
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Book deleteBook(@PathParam("id") Integer id, Book book){
        Book.deleteById(id);
        return book;
    }

    @Path("/author/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Author> searchForAuthor(@QueryParam("pattern") String pattern,
                                      @QueryParam("size")Optional<Integer> size){
        return Search.session(entityManager)
                .search(Author.class)
                .where(predicate -> pattern == null || pattern.trim().isEmpty()
                ? predicate.matchAll()
                        : predicate.simpleQueryString().fields("fistName", "lastName", "books.title").matching(pattern))
                .sort(sort -> sort.field("firstName_sort").then().field("lastName_sort"))
                .fetchAll()
                .hits();
    }
}