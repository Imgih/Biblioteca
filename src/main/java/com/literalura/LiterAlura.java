package com.literalura;

import com.literalura.model.Author;
import com.literalura.model.Book;
import com.literalura.service.CatalogService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LiterAlura implements CommandLineRunner {

    private final CatalogService catalog;
    public LiterAlura(CatalogService catalog) { this.catalog = catalog; }

    public static void main(String[] args) {
        SpringApplication.run(LiterAlura.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner sc = new Scanner(System.in);
        int op = -1;

        while (op != 0) {
            mostrarMenu();
            op = lerOpcao(sc);

            switch (op) {
                case 1 -> buscar(sc);
                case 2 -> listarLivros();
                case 3 -> listarAutores();
                case 4 -> listarAutoresVivos(sc);
                case 5 -> listarLivrosPorIdioma(sc);
                case 0 -> System.out.println("saindo...");
                default -> System.out.println("Entrada inválida. Tente novamente.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("""
                ----------
                Escolha o número de sua opção:
                1- buscar livro pelo título
                2- Listar livros registrados
                3- Listar autores registrados
                4- Listar autores vivos em um determinado ano
                5- Listar livros em um determinado idioma
                0- sair
                """);
    }

    private int lerOpcao(Scanner sc) {
        System.out.print("Opção: ");
        String s = sc.nextLine();
        if (s == null || s.trim().isEmpty()) return -1;
        try { return Integer.parseInt(s.trim()); } catch (NumberFormatException e) { return -1; }
    }

    private void buscar(Scanner sc) {
        System.out.print("Digite o título: ");
        var saved = catalog.fetchAndSaveByTitle(sc.nextLine().trim());

        saved.ifPresentOrElse(this::imprimirCardLivro,
                () -> System.out.println("Nenhum resultado encontrado.\n"));
    }

    private void imprimirCardLivro(Book b) {
        System.out.println("\n----- LIVRO -----");
        System.out.println("Título: " + b.getTitle());
        System.out.println("Autor: " + (b.getAuthor() != null ? b.getAuthor().getName() : "-"));
        System.out.println("Idioma: " + b.getLanguage());
        System.out.println("Número de downloads: " + (b.getDownloadCount() == null ? 0 : b.getDownloadCount()));
        System.out.println("-----------------\n");
    }

    private void listarLivros() {
        List<Book> list = catalog.listAllBooks();
        if (list.isEmpty()) { System.out.println("Nenhum livro registrado.\n"); return; }
        for (Book b : list) imprimirCardLivro(b);
    }

    private void listarAutores() {
        List<Author> list = catalog.listAllAuthors();
        if (list.isEmpty()) { System.out.println("Nenhum autor registrado.\n"); return; }
        for (Author a : list) {
            System.out.println("- " + a.getName()
                    + " (nasc.: " + (a.getBirthYear() == null ? "-" : a.getBirthYear())
                    + ", falec.: " + (a.getDeathYear() == null ? "-" : a.getDeathYear()) + ")");
        }
        System.out.println();
    }

    private void listarAutoresVivos(Scanner sc) {
        System.out.print("Informe o ano: ");
        try {
            int y = Integer.parseInt(sc.nextLine().trim());
            var list = catalog.listAuthorsAliveInYear(y);
            if (list.isEmpty()) { System.out.println("Nenhum autor vivo nesse ano.\n"); return; }
            list.forEach(a -> System.out.println("- " + a.getName()));
            System.out.println();
        } catch (NumberFormatException e) {
            System.out.println("Ano inválido.\n");
        }
    }

    private void listarLivrosPorIdioma(Scanner sc) {
        System.out.print("Informe o idioma (ex.: en, pt, es): ");
        var list = catalog.listBooksByLanguage(sc.nextLine().trim());
        if (list.isEmpty()) { System.out.println("Nenhum livro nesse idioma.\n"); return; }
        for (Book b : list) imprimirCardLivro(b);
    }
}
