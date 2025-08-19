package com.literalura.service;

import com.literalura.HTTPCLIENT;
import com.literalura.dto.GutendexAuthors;
import com.literalura.dto.GutendexBook;
import com.literalura.model.Author;
import com.literalura.model.Book;
import com.literalura.repositorio.AuthorRepositorio;
import com.literalura.repositorio.LivroRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogService {

    private final HTTPCLIENT client;          // melhor injetar o bean
    private final AuthorRepositorio authorRepo;
    private final LivroRepositorio bookRepo;

    public CatalogService(HTTPCLIENT client, AuthorRepositorio authorRepo, LivroRepositorio bookRepo) {
        this.client = client;
        this.authorRepo = authorRepo;
        this.bookRepo = bookRepo;
    }

    @Transactional
    public Optional<Book> fetchAndSaveByTitle(String title) {
        Optional<GutendexBook> opt = client.searchFirstByTitle(title);
        if (opt.isEmpty()) return Optional.empty();
        GutendexBook dto = opt.get();

        if (dto.id() != null) {
            var existing = bookRepo.findByGutenbergId(dto.id());
            if (existing.isPresent()) return existing;
        }

        String lang = (dto.languages() != null && !dto.languages().isEmpty())
                ? dto.languages().get(0) : "unknown";

        GutendexAuthors a = (dto.authors() != null && !dto.authors().isEmpty())
                ? dto.authors().get(0) : null;

        String name = (a != null && a.name() != null) ? a.name() : "Autor Desconhecido";
        Author author = authorRepo.findByNameIgnoreCase(name)
                .orElseGet(() -> authorRepo.save(new Author(
                        name, a != null ? a.birth_year() : null, a != null ? a.death_year() : null)));

        Book book = new Book(dto.id(), dto.title(), lang,
                dto.download_count() != null ? dto.download_count() : 0, author);

        return Optional.of(bookRepo.save(book));
    }

    // consultas usadas no menu
    public List<Book> listAllBooks() { return bookRepo.findAll(); }
    public List<Book> listBooksByLanguage(String lang) { return bookRepo.findByLanguageIgnoreCase(lang); }
    public long countBooksByLanguage(String lang) { return bookRepo.countByLanguageIgnoreCase(lang); }
    public List<Author> listAllAuthors() { return authorRepo.findAll(); }
    public List<Author> listAuthorsAliveInYear(int year) { return authorRepo.findAliveInYear(year); }
    public List<Book> top10Downloads() { return bookRepo.findTop10ByOrderByDownloadCountDesc(); }
}
