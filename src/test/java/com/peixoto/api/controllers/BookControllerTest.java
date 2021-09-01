package com.peixoto.api.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peixoto.api.domain.Book;
import com.peixoto.api.exceptions.BadRequestException;
import com.peixoto.api.requests.BookPostRequestBody;
import com.peixoto.api.services.BookService;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService mockBookService;

    @Mock
    private Book mockBook;

    private JacksonTester<Book> bookJacksonTester;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void listAllBooks_successfullyOneBook() throws Exception {
        Mockito.when(mockBookService.findAll()).thenReturn(List.of(new Book()));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/books"))
            .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        List<Book> books = mapper.readValue(response.getContentAsString(), new TypeReference<List<Book>>() {});

        Assertions.assertThat(response.getStatus()).isEqualTo(200);
        Assertions.assertThat(books.size()).isEqualTo(1);
    }

    @Test
    void listAllBooks_successfullyALotOfBooks() throws Exception {
        Mockito.when(mockBookService.findAll()).thenReturn(List.of(new Book(),
            new Book(), new Book()));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/books"))
            .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        List<Book> books = mapper.readValue(response.getContentAsString(), new TypeReference<List<Book>>() {});

        Assertions.assertThat(response.getStatus()).isEqualTo(200);
        Assertions.assertThat(books.size()).isEqualTo(3);
    }

    @Test
    void listAllBooks_noBooksFound() throws Exception {
        Mockito.when(mockBookService.findAll()).thenReturn(null);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/books"))
            .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(200);
        Assertions.assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    void listBookById_validI_shouldReturnBook() throws Exception {
        Book book = new Book(1L, "title", "author", "category");

        Mockito.when(mockBookService.findById(1L)).thenReturn(book);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/books/1"))
            .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Book foundedBook = mapper.readValue(response.getContentAsString(), new TypeReference<Book>() {});

        Assertions.assertThat(response.getStatus()).isEqualTo(200);
        Assertions.assertThat(foundedBook).isEqualTo(book);
    }

    @Test
    void listBookById_invalidI_throwNotFoundException() throws Exception {
        Mockito.when(mockBookService.findById(1L)).thenThrow(new BadRequestException("Book not found"));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/books/1"))
            .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void insertNewBook_successfully() throws Exception {
        Book book = new Book(1L, "title", "author", "category");
        String bookAsString = new ObjectMapper().writeValueAsString(book);

        MockHttpServletResponse response = mockMvc.perform(
            MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookAsString))
            .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_CREATED);
    }

    @Test
    void insertNewBook_successfully_0() throws Exception {
        BookPostRequestBody book = new BookPostRequestBody("title", "author", "category");
        String bookAsString = new ObjectMapper().writeValueAsString(book);

        Mockito.when(mockBookService.save(book)).thenReturn(null);

        MockHttpServletResponse response = mockMvc.perform(
            MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookAsString))
            .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_CREATED);
    }

    @Test
    void remove_successfully() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/books/2"))
            .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    void update() throws Exception {
        Book book = new Book(1L, "new title", "author", "category");
        String bookAsString = new ObjectMapper().writeValueAsString(book);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bookAsString))
            .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    void external_returnValue() throws Exception {
        Mockito.when(mockBookService.getExternalData()).thenReturn("Hello World");

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/books/external"))
            .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
        Assertions.assertThat(response.getContentAsString()).isEqualTo("Hello World");
    }

    @Test
    void external_returnNull() throws Exception {
        Mockito.when(mockBookService.getExternalData()).thenReturn(null);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/books/external"))
            .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
        Assertions.assertThat(response.getContentAsString()).isEmpty();
    }

}
