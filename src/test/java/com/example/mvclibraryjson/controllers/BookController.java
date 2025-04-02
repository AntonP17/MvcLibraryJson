package com.example.mvclibraryjson.controllers;

import com.example.mvclibraryjson.model.Book;
import com.example.mvclibraryjson.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookController.class)
public class BookController {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllBooks() throws Exception {
        // Arrange
        List<Book> books = Arrays.asList(new Book(), new Book());
        Page<Book> bookPage = new PageImpl<>(books);
        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(bookPage);

        // Act & Assert
        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    public void testGetBookById() throws Exception {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book");
        when(bookService.getBookById(anyLong())).thenReturn(book);

        // Act & Assert
        mockMvc.perform(get("/api/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Sample Book"));
    }

    @Test
    public void testCreateBook() throws Exception {
        // Arrange
        Book book = new Book();
        book.setTitle("New Book");
        when(bookService.saveBook(any(Book.class))).thenReturn(book);

        // Act & Assert
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"New Book\", \"year\": 2023}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Book"));
    }

    @Test
    public void testUpdateBook() throws Exception {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Updated Book");
        when(bookService.updateBook(anyLong(), any(Book.class))).thenReturn(book);

        // Act & Assert
        mockMvc.perform(put("/api/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated Book\", \"year\": 2023}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"));
    }

    @Test
    public void testDeleteBook() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/books/{id}", 1L))
                .andExpect(status().isOk());
    }

}
