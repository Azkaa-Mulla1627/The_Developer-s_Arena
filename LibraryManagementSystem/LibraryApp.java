import java.util.List;
import java.util.Scanner;

public class LibraryApp {
    private Library lib = new Library();
    private Scanner sc = new Scanner(System.in);

    public void run() {
        System.out.println("=== Console Library Management System ===");
        boolean running = true;
        while (running) {
            showMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": addBook(); break;
                case "2": removeBook(); break;
                case "3": listBooks(); break;
                case "4": findBook(); break;
                case "5": addMember(); break;
                case "6": removeMember(); break;
                case "7": listMembers(); break;
                case "8": issueBook(); break;
                case "9": returnBook(); break;
                case "10": listIssued(); break;
                case "0": running = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
        System.out.println("Exiting. Goodbye!");
        sc.close();
    }

    private void showMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1. Add Book");
        System.out.println("2. Remove Book");
        System.out.println("3. List Books");
        System.out.println("4. Find Book by ISBN");
        System.out.println("5. Add Member");
        System.out.println("6. Remove Member");
        System.out.println("7. List Members");
        System.out.println("8. Issue Book");
        System.out.println("9. Return Book");
        System.out.println("10. List Issued Records");
        System.out.println("0. Exit");
        System.out.print("> ");
    }

    private void addBook() {
        System.out.print("ISBN: "); String isbn = sc.nextLine();
        System.out.print("Title: "); String title = sc.nextLine();
        System.out.print("Author: "); String author = sc.nextLine();
        if (isbn.isBlank() || title.isBlank()) {
            System.out.println("ISBN and Title required.");
            return;
        }
        boolean ok = lib.addBook(new Book(isbn, title, author));
        System.out.println(ok ? "Book added." : "Book with that ISBN already exists.");
    }

    private void removeBook() {
        System.out.print("ISBN to remove: "); String isbn = sc.nextLine();
        boolean ok = lib.removeBook(isbn);
        if (ok) System.out.println("Removed.");
        else System.out.println("Failed: book may not exist or is currently issued.");
    }

    private void listBooks() {
        List<Book> list = lib.listBooks();
        if (list.isEmpty()) System.out.println("No books.");
        else list.forEach(System.out::println);
    }

    private void findBook() {
        System.out.print("ISBN to find: "); String isbn = sc.nextLine();
        Book b = lib.findBook(isbn);
        System.out.println(b == null ? "Not found." : b);
    }

    private void addMember() {
        System.out.print("Member ID: "); String id = sc.nextLine();
        System.out.print("Name: "); String name = sc.nextLine();
        System.out.print("Contact: "); String contact = sc.nextLine();
        if (id.isBlank() || name.isBlank()) { System.out.println("ID and Name required."); return; }
        boolean ok = lib.addMember(new Member(id, name, contact));
        System.out.println(ok ? "Member added." : "Member ID already exists.");
    }

    private void removeMember() {
        System.out.print("Member ID to remove: "); String id = sc.nextLine();
        boolean ok = lib.removeMember(id);
        if (ok) System.out.println("Removed.");
        else System.out.println("Failed: member may not exist or has issued books.");
    }

    private void listMembers() {
        List<Member> list = lib.listMembers();
        if (list.isEmpty()) System.out.println("No members.");
        else list.forEach(System.out::println);
    }

    private void issueBook() {
        System.out.print("ISBN to issue: "); String isbn = sc.nextLine();
        System.out.print("Member ID: "); String mid = sc.nextLine();
        String res = lib.issueBook(isbn, mid);
        System.out.println(res);
    }

    private void returnBook() {
        System.out.print("ISBN to return: "); String isbn = sc.nextLine();
        String res = lib.returnBook(isbn);
        System.out.println(res);
    }

    private void listIssued() {
        var list = lib.listIssuedRecords();
        if (list.isEmpty()) System.out.println("No issued books.");
        else list.forEach(System.out::println);
    }

    public static void main(String[] args) {
        new LibraryApp().run();
    }
}