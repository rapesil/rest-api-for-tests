package com.peixoto.api.services;

import com.peixoto.api.domain.Book;
import com.peixoto.api.exceptions.BadRequestException;
import com.peixoto.api.mapper.BookMapper;
import com.peixoto.api.repository.BookRepository;
import com.peixoto.api.requests.BookPostRequestBody;
import com.peixoto.api.requests.BookPutRequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookService {

    @Autowired
    private final BookRepository bookRepository;

    private static final String title1 = "Selenium WebDriver: Descomplicando testes automatizados com Java";
    private static final String title2 = "Agile Testing : A Practical Guide for Testers and Agile Teams";

    private static List<Book> books;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(long id) {
        return bookRepository.findById(id)
                .orElseThrow(()->new BadRequestException("Book not found"));
    }

    @Transactional(rollbackOn = Exception.class)
    public Book save(Book book) {
        return bookRepository.save(book);

    }

    @Transactional(rollbackOn = Exception.class)
    public void update(BookPutRequestBody book) {
        Book savedBook = findById(book.getId());
        savedBook.setTitle(book.getTitle());
        savedBook.setCategory(book.getCategory());
        savedBook.setAuthor(book.getAuthor());
        savedBook.setId(book.getId());
        bookRepository.save(savedBook);
    }

    public void remove(long id) {
        bookRepository.deleteById(id);
    }
}
