package com.literalura.dto;
import java.util.List;
public record GutendexBook(Long id, String title,
                              List<GutendexAuthors> authors,
                              List<String> languages,
                              Integer download_count) {}
