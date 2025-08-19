package com.literalura.repositorio;

import com.literalura.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepositorio extends JpaRepository<Book, Long> {

    Optional<Book> findByGutenbergId(Long gutenbergId);

    @EntityGraph(attributePaths = "author")
    List<Book> findAll();

    @EntityGraph(attributePaths = "author")
    List<Book> findByLanguageIgnoreCase(String language);

    @EntityGraph(attributePaths = "author")
    List<Book> findTop10ByOrderByDownloadCountDesc();

    long countByLanguageIgnoreCase(String language);
}
