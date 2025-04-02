package com.example.mvclibraryjson.service;

import com.example.mvclibraryjson.model.Author;
import com.example.mvclibraryjson.repository.AuthorRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    private final ObjectMapper objectMapper;

    public AuthorService(AuthorRepository authorRepository,
                         ObjectMapper objectMapper) {
        this.authorRepository = authorRepository;
        this.objectMapper = objectMapper;
    }

    public Page<Author> getAll(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    public Author getOne(Long id) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        return authorOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public List<Author> getMany(List<Long> ids) {
        return authorRepository.findAllById(ids);
    }

    public Author create(Author author) {
        return authorRepository.save(author);
    }

    public Author patch(Long id, JsonNode patchNode) throws IOException {
        Author author = authorRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(author).readValue(patchNode);

        return authorRepository.save(author);
    }

    public List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException {
        Collection<Author> authors = authorRepository.findAllById(ids);

        for (Author author : authors) {
            objectMapper.readerForUpdating(author).readValue(patchNode);
        }

        List<Author> resultAuthors = authorRepository.saveAll(authors);
        return resultAuthors.stream()
                .map(Author::getId)
                .toList();
    }

    public Author delete(Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if (author != null) {
            authorRepository.delete(author);
        }
        return author;
    }

    public void deleteMany(List<Long> ids) {
        authorRepository.deleteAllById(ids);
    }
}
