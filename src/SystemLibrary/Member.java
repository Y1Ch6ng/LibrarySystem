package SystemLibrary;

public class Member extends User {
    private int maxBooksAllowed = 5;

    public Member(String name, String userId) {
        super(name, userId);
    }

    @Override
    public void borrowBook(Book book) {
        if (borrowedBooks.size() < maxBooksAllowed && book.isAvailable()) {
            borrowedBooks.add(book);
            book.markAsBorrowed();
            System.out.println(name + " borrowed " + book.getDetails());
        } else {
            System.out.println("Cannot borrow more books or book is unavailable.");
        }
    }

    @Override
    public void returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            borrowedBooks.remove(book);
            book.markAsReturned();
            System.out.println(name + " returned " + book.getDetails());
        } else {
            System.out.println("Book not found in borrowed books.");
        }
    }
}
