import java.io.Serializable;

public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private String memberId;
    private String name;
    private String contact;

    public Member(String memberId, String name, String contact) {
        this.memberId = memberId.trim();
        this.name = name.trim();
        this.contact = contact.trim();
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getContact() { return contact; }

    @Override
    public String toString() {
        return String.format("[%s] %s — %s", memberId, name, contact);
    }
}
