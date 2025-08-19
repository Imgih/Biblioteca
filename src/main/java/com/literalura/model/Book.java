package com.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books",
        indexes = {
                @Index(name = "idx_books_language", columnList = "language"),
                @Index(name = "idx_books_download_count", columnList = "downloadCount")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_books_gutenberg_id", columnNames = {"gutenbergId"})
        }
)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // id do catálogo do Project Gutenberg (virá da API)
    @Column
    private Long gutenbergId; // pode ser null nos testes manuais

    @Column(nullable = false, length = 500)
    private String title;

    // vamos manter apenas o PRIMEIRO idioma retornado pela API
    @Column(nullable = false, length = 10)
    private String language;

    @Column
    private Integer downloadCount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_book_author"))
    private Author author;

    public Book() {}

    public Book(Long gutenbergId, String title, String language, Integer downloadCount, Author author) {
        this.gutenbergId = gutenbergId;
        this.title = title;
        this.language = language;
        this.downloadCount = downloadCount;
        this.author = author;
    }

    // getters & setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getGutenbergId() { return gutenbergId; }
    public void setGutenbergId(Long gutenbergId) { this.gutenbergId = gutenbergId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Integer getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", gutenbergId=" + gutenbergId +
                ", title='" + title + '\'' +
                ", language='" + language + '\'' +
                ", downloadCount=" + downloadCount +
                ", author=" + (author != null ? author.getName() : "null") +
                '}';
    }
}
