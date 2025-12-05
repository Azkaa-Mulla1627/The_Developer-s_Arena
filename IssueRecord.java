import java.io.Serializable;
import java.time.LocalDate;

public class IssueRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private String isbn;
    private String memberId;
    private LocalDate issueDate;

    public IssueRecord(String isbn, String memberId, LocalDate issueDate) {
        this.isbn = isbn;
        this.memberId = memberId;
        this.issueDate = issueDate;
    }

    public String getIsbn() { return isbn; }
    public String getMemberId() { return memberId; }
    public LocalDate getIssueDate() { return issueDate; }

    @Override
    public String toString() {
        return String.format("Book %s issued to %s on %s", isbn, memberId, issueDate);
    }
}

