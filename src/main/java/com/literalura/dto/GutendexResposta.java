package com.literalura.dto;
import java.util.List;
public record GutendexResposta(int count, String next, String previous,
                               List<GutendexBook> results) {}
