import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The User class represents a generic user of the library system.
 * It is an abstract class that can be extended by specific user types like Member and Librarian.
 */
public abstract class User {
    protected String name;
    protected String userId;
    protected List<Book> borrowedBooks;

    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
        this.borrowedBooks = new ArrayList<>();
    }

    public void borrowBook(Book book) {
        if (book.isAvailable()) {
            borrowedBooks.add(book);
            book.markAsBorrowed();
            System.out.println(name + " borrowed " + book.getTitle());
        } else {
            System.out.println(book.getTitle() + " is not available.");
        }
    }

    public void returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            borrowedBooks.remove(book);
            book.markAsReturned();
            System.out.println(name + " returned " + book.getTitle());
        } else {
            System.out.println("Book not found in borrowed list.");
        }
    }

    public void viewBorrowedBooks() {
        if (borrowedBooks.isEmpty()) {
            System.out.println("No books borrowed.");
        } else {
            System.out.println(name + " has borrowed:");
            for (Book book : borrowedBooks) {
                System.out.println(book.getDetails());
            }
        }
    }

    public String getUserId() {
        return userId;
    }

    // Save user details to file
    public void saveToFile(BufferedWriter writer) throws IOException {
        writer.write(name + "," + userId);
        writer.newLine();
    }

    // Load user details from file
    public static User loadFromFile(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line == null) return null;

        String[] details = line.split(",");
        if (details.length < 2) return null;

        // For simplicity, we'll treat all users loaded as Members.
        return new Member(details[0], details[1]);
    }
}

