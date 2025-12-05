import java.io.Serializable;
import java.time.LocalDate;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private String isbn;
    private String title;
    private String author;
    private boolean issued;

    public Book(String isbn, String title, String author) {
        this.isbn = isbn.trim();
        this.title = title.trim();
        this.author = author.trim();
        this.issued = false;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isIssued() { return issued; }
    public void setIssued(boolean issued) { this.issued = issued; }

    @Override
    public String toString() {
        return String.format("[%s] %s — %s%s", isbn, title, author, (issued ? " (issued)" : ""));
    }
}
