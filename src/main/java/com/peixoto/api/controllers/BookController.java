package com.peixoto.api.controllers;

import com.peixoto.api.domain.Book;
import com.peixoto.api.requests.BookPostRequestBody;
import com.peixoto.api.requests.BookPutRequestBody;
import com.peixoto.api.services.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("books")
@Log4j2
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    @PreAuthorize("hasRole('USER')" +
        "|| hasRole('ADMIN')"
    )
    public ResponseEntity<List<Book>> listAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')" +
            "|| hasRole('ADMIN')"
    )
    public ResponseEntity<Book> listBookById(@PathVariable long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> insertNewBook(@NotNull @Valid @RequestBody BookPostRequestBody book)  {
        return new ResponseEntity<>(bookService.save(book), HttpStatus.CREATED);
    }

    @GetMapping("/external")
    public ResponseEntity<String> externalApi() {
        return new ResponseEntity<String>(bookService.getExternalData(), HttpStatus.valueOf(org.apache.http.HttpStatus.SC_OK));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')" +
            "|| hasRole('ADMIN')"
    )
    public ResponseEntity<Void> remove(@PathVariable long id) {
        bookService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')" +
            "|| hasRole('ADMIN')"
    )
    public ResponseEntity<Void> update(@NotNull @RequestBody BookPutRequestBody book) {
        bookService.replace(book);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
