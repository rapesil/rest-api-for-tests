package com.peixoto.api.services;

import com.peixoto.api.domain.Book;
import com.peixoto.api.exceptions.BadRequestException;
import com.peixoto.api.repository.BookRepository;
import com.peixoto.api.requests.BookPostRequestBody;
import com.peixoto.api.requests.BookPutRequestBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        assertThat(bookService.findAll().size()).isEqualTo(2);
    }

    @Test
    void findALL_shouldThrowException_whenThereIsNoBooks() {
        Mockito.when(mockBookRepository.findAll())
            .thenReturn(null);

        assertThatThrownBy(() -> bookService.findAll())
            .isInstanceOf(NullPointerException.class)
            .hasMessage("There is no books");
    }

    @Test
    void findById_shouldReturnABook() {
        Mockito.when(mockBookRepository.findById(1L))
                .thenReturn(Optional.of(
                        new Book(1L, "title", "author", "cat")));

        Book book = bookService.findById(1L);
        assertThat(book.getId()).isEqualTo(1L);
        assertThat(book.getTitle()).isEqualTo("title");
        assertThat(book.getAuthor()).isEqualTo("author");
        assertThat(book.getBookCategory()).isEqualTo("cat");
    }

    @Test
    void findById_shouldThrowBadRequestException() {
        assertThatThrownBy(() -> bookService.findById(1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Book not found");
    }

    @Test
    void save_shouldSaveNewBook() {
        BookPostRequestBody book = new BookPostRequestBody();
        book.setAuthor("Rafael Peixoto");
        book.setBookCategory("Software Test");
        book.setTitle("Selenium WebDriver");

        assertThatCode(() -> bookService.save(book))
                .doesNotThrowAnyException();

        Mockito.verify(mockBookRepository, times(1)).save(any());
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

        assertThatCode(() -> bookService.replace(newBook))
                .doesNotThrowAnyException();
    }

    @Test
    void replace_shouldReplaceBook_whenCategoryIsNull() {
        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setAuthor("Rafael Peixoto");
        savedBook.setBookCategory("Software Test");
        savedBook.setTitle("Selenium WebDriver");

        BookPutRequestBody newBook = new BookPutRequestBody(
            1L,
            "Agile Testing",
            "Lisa Crispin",
            null
        );

        Mockito.when(mockBookRepository.findById(1L))
            .thenReturn(Optional.of(savedBook));

        assertThatCode(() -> bookService.replace(newBook))
            .doesNotThrowAnyException();
    }

    @Test
    void remove_shouldThrowException_whenBookNotExists() {
        assertThatThrownBy(() -> bookService.remove(1L))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("Book not found");
    }

    @Test
    void remove_shouldRemoveBook() {
        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setAuthor("Rafael Peixoto");
        savedBook.setBookCategory("Software Test");
        savedBook.setTitle("Selenium WebDriver");

        when(mockBookRepository.findById(1L)).thenReturn(Optional.of(savedBook));

        assertThatCode(() -> bookService.remove(1L)).doesNotThrowAnyException();

        verify(mockBookRepository, times(1)).deleteById(1L);
    }

}
