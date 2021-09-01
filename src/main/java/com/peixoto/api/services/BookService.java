package com.peixoto.api.services;

import com.peixoto.api.domain.Book;
import com.peixoto.api.exceptions.BadRequestException;
import com.peixoto.api.repository.BookRepository;
import com.peixoto.api.requests.BookPostRequestBody;
import com.peixoto.api.requests.BookPutRequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookService {

    @Autowired
    private final BookRepository bookRepository;

    public List<Book> findAll() {
        if(bookRepository.findAll() == null) {
            throw new NullPointerException("There is no books");
        }
        return bookRepository.findAll();
    }

    public Book findById(long id) {
        return bookRepository.findById(id)
            .orElseThrow(()->new BadRequestException("Book not found"));
    }

    @Transactional(rollbackOn = Exception.class)
    public Book save(BookPostRequestBody book) {
        Book newBook = new Book();
        newBook.setTitle(book.getTitle());
        newBook.setAuthor(book.getAuthor());
        newBook.setBookCategory(book.getBookCategory());
        return bookRepository.save(newBook);
    }

    @Transactional(rollbackOn = Exception.class)
    public void replace(BookPutRequestBody book) {
        Book savedBook = findById(book.getId());

        savedBook.setTitle(book.getTitle());
        savedBook.setBookCategory(book.getBookCategory());
        savedBook.setAuthor(book.getAuthor());
        savedBook.setId(book.getId());

        bookRepository.save(savedBook);
    }

    public void remove(long id) {
        findById(id);
        bookRepository.deleteById(id);
    }

    public String getExternalData() {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "https://reqres.in/api/users?page=2";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(fooResourceUrl, String.class);
        return forEntity.getBody();
    }
}
