package com.example.mvclibraryjson.repository;

import com.example.mvclibraryjson.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}