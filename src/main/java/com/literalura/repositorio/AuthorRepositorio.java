package com.literalura.repositorio;

import com.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepositorio extends JpaRepository<Author, Long> {

    Optional<Author> findByNameIgnoreCase(String name);

    // autores "vivos" em um ano: nasceu <= ano e (sem morte ou morte >= ano)
    @Query("""
           SELECT a FROM Author a
           WHERE (a.birthYear IS NULL OR a.birthYear <= :year)
             AND (a.deathYear IS NULL OR a.deathYear >= :year)
           """)
    List<Author> findAliveInYear(int year);
}
