package SystemLibrary;

public class Librarian extends User {
    public Librarian(String name, String userId) {
        super(name, userId);
    }

    public void addBook(Library library, Book book) {
        library.addBook(book);
        System.out.println("Book added: " + book.getDetails());
    }

    public void removeBook(Library library, Book book) {
        library.removeBook(book);
        System.out.println("Book removed: " + book.getDetails());
    }

    @Override
    public void borrowBook(Book book) {
        borrowedBooks.add(book);
        book.markAsBorrowed();
        System.out.println(name + " borrowed " + book.getDetails());
    }

    @Override
    public void returnBook(Book book) {
        borrowedBooks.remove(book);
        book.markAsReturned();
        System.out.println(name + " returned " + book.getDetails());
    }
}

