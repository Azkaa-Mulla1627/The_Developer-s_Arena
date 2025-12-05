import java.time.LocalDate;
import java.util.*;

public class Library {
    private Map<String, Book> books = new HashMap<>();
    private Map<String, Member> members = new HashMap<>();
    // Map ISBN -> IssueRecord
    private Map<String, IssueRecord> issued = new HashMap<>();

    private static final String BOOKS_FILE = "books.dat";
    private static final String MEMBERS_FILE = "members.dat";
    private static final String ISSUED_FILE = "issued.dat";

    public Library() {
        loadData();
    }

    /* ---------- Persistence ---------- */
    @SuppressWarnings("unchecked")
    private void loadData() {
        books = LibraryDataStore.load(BOOKS_FILE, books);
        members = LibraryDataStore.load(MEMBERS_FILE, members);
        issued = LibraryDataStore.load(ISSUED_FILE, issued);
    }

    private void saveData() {
        LibraryDataStore.save(books, BOOKS_FILE);
        LibraryDataStore.save(members, MEMBERS_FILE);
        LibraryDataStore.save(issued, ISSUED_FILE);
    }

    /* ---------- Book ops ---------- */
    public boolean addBook(Book b) {
        if (books.containsKey(b.getIsbn())) return false;
        books.put(b.getIsbn(), b);
        saveData();
        return true;
    }

    public boolean removeBook(String isbn) {
        Book b = books.get(isbn);
        if (b == null) return false;
        if (b.isIssued()) return false; // don't remove issued book
        books.remove(isbn);
        saveData();
        return true;
    }

    public Book findBook(String isbn) {
        return books.get(isbn);
    }

    public List<Book> listBooks() {
        List<Book> list = new ArrayList<>(books.values());
        list.sort(Comparator.comparing(Book::getTitle));
        return list;
    }

    /* ---------- Member ops ---------- */
    public boolean addMember(Member m) {
        if (members.containsKey(m.getMemberId())) return false;
        members.put(m.getMemberId(), m);
        saveData();
        return true;
    }

    public boolean removeMember(String memberId) {
        if (!members.containsKey(memberId)) return false;
        // ensure member has no issued books
        boolean hasIssued = issued.values().stream()
                .anyMatch(r -> r.getMemberId().equals(memberId));
        if (hasIssued) return false;
        members.remove(memberId);
        saveData();
        return true;
    }

    public Member findMember(String memberId) { return members.get(memberId); }

    public List<Member> listMembers() {
        List<Member> list = new ArrayList<>(members.values());
        list.sort(Comparator.comparing(Member::getName));
        return list;
    }

    /* ---------- Issue / Return ---------- */
    public String issueBook(String isbn, String memberId) {
        Book b = books.get(isbn);
        if (b == null) return "Book not found.";
        if (b.isIssued()) return "Book already issued.";
        Member m = members.get(memberId);
        if (m == null) return "Member not found.";
        b.setIssued(true);
        IssueRecord r = new IssueRecord(isbn, memberId, LocalDate.now());
        issued.put(isbn, r);
        saveData();
        return "Issued successfully.";
    }

    public String returnBook(String isbn) {
        Book b = books.get(isbn);
        if (b == null) return "Book not found.";
        if (!b.isIssued()) return "Book is not issued.";
        IssueRecord r = issued.remove(isbn);
        b.setIssued(false);
        saveData();
        if (r == null) return "Returned but no record found.";
        return "Returned successfully (was issued to: " + r.getMemberId() + " on " + r.getIssueDate() + ")";
    }

    public List<IssueRecord> listIssuedRecords() {
        List<IssueRecord> list = new ArrayList<>(issued.values());
        list.sort(Comparator.comparing(IssueRecord::getIssueDate));
        return list;
    }
}
