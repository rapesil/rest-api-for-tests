package com.peixoto.api.services;

import com.peixoto.api.domain.Book;
import com.peixoto.api.exceptions.BadRequestException;
import com.peixoto.api.repository.BookRepository;
import com.peixoto.api.requests.BookPutRequestBody;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository mockBookRepository;

    @Test
    void findALL_shouldReturnAllBooks() {
        Mockito.when(mockBookRepository.findAll())
                .thenReturn(Arrays.asList(new Book(), new Book()));

        Assertions.assertThat(bookService.findAll().size()).isEqualTo(2);
    }

    @Test
    void findById_shouldReturnABook() {
        Mockito.when(mockBookRepository.findById(1L))
                .thenReturn(Optional.of(
                        new Book(1L, "title", "author", "cat")));

        Book book = bookService.findById(1L);
        Assertions.assertThat(book.getId()).isEqualTo(1L);
        Assertions.assertThat(book.getTitle()).isEqualTo("title");
        Assertions.assertThat(book.getAuthor()).isEqualTo("author");
        Assertions.assertThat(book.getBookCategory()).isEqualTo("cat");
    }

    @Test
    void findById_shouldThrowBadRequestException() {
        Assertions.assertThatThrownBy(() -> bookService.findById(1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Book not found");
    }

    @Test
    void save_shouldSaveNewBook() {
        Book book = new Book();
        book.setAuthor("Rafael Peixoto");
        book.setBookCategory("Software Test");
        book.setTitle("Selenium WebDriver");

        Assertions.assertThatCode(() -> bookService.save(book))
                .doesNotThrowAnyException();
    }

    @Test
    void replace_shouldReplaceBook() {
        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setAuthor("Rafael Peixoto");
        savedBook.setBookCategory("Software Test");
        savedBook.setTitle("Selenium WebDriver");

        BookPutRequestBody newBook = new BookPutRequestBody(
                1L,
                "Agile Testing",
                "Lisa Crispin",
                "Software Test"
        );

        Mockito.when(mockBookRepository.findById(1L))
                .thenReturn(Optional.of(savedBook));

        Assertions.assertThatCode(() -> bookService.replace(newBook))
                .doesNotThrowAnyException();
    }

    @Test
    void remove_shouldRemoveBook() {
        Mockito.when(mockBookRepository.findById(1L))
                .thenReturn(Optional.of(
                        new Book(1L, "title", "author", "cat")));

        Assertions.assertThatCode(() -> bookService.remove(1L))
                .doesNotThrowAnyException();
    }

    @Test
    void remove_shouldThrowException_whenBookNotExists() {
        Assertions.assertThatThrownBy(() -> bookService.remove(1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Book not found");
    }

}
