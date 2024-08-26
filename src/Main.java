import java.util.Scanner;
import SystemLibrary.Book;
import SystemLibrary.Library;
import SystemLibrary.User;
import SystemLibrary.DBConnection;
import SystemLibrary.Librarian;
import SystemLibrary.Member;

public class Main {
    public static void main(String[] args) {
        // Create Library
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);

        /*// Add some initial books to the library
        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "123456789");
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee", "987654321");
        Book book3 = new Book("1984", "George Orwell", "112233445");

        book1.saveToDatabase();3
        book2.saveToDatabase();
        book3.saveToDatabase();*/

        // Create Users
        Member member = new Member("Cheperd", "M001");
        Librarian librarian = new Librarian("Wilson", "L001");

        // Register users in the library
        library.registerUser(member);
        library.registerUser(librarian);

        int choice;
        do {
            System.out.println("\n--- Library Management System ---");
            System.out.println("1. View all books");
            System.out.println("2. Search for a book by title");
            System.out.println("3. Borrow a book (Member)");
            System.out.println("4. Return a book (Member)");
            System.out.println("5. Add a new book (Librarian)");
            System.out.println("6. Remove a book (Librarian)");
            System.out.println("7. View borrowed books (Member)");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("\nAvailable Books:");
                    for (Book book : library.getBooks()) {
                        System.out.println(book.getDetails());
                    }
                    break;

                case 2:
                    System.out.print("\nEnter the book title to search: ");
                    String title = scanner.nextLine();
                    Book foundBook = library.searchBookByTitle(title);
                    if (foundBook != null) {
                        System.out.println("Book found: " + foundBook.getDetails());
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;

                case 3:
                    System.out.print("\nEnter the title of the book to borrow: ");
                    String borrowTitle = scanner.nextLine();
                    Book borrowBook = library.searchBookByTitle(borrowTitle);
                    if (borrowBook != null) {
                        library.issueBook(member, borrowBook);
                    } else {
                        System.out.println("Book not available for borrowing.");
                    }
                    break;

                case 4:
                    System.out.print("\nEnter the title of the book to return: ");
                    String returnTitle = scanner.nextLine();
                    Book returnBook = library.searchBookByTitle(returnTitle);
                    if (returnBook != null) {
                        library.returnBook(member, returnBook);
                    } else {
                        System.out.println("Book not found in your borrowed books.");
                    }
                    break;

                case 5:
                    System.out.print("\nEnter the title of the new book: ");
                    String newTitle = scanner.nextLine();
                    System.out.print("Enter the author of the new book: ");
                    String newAuthor = scanner.nextLine();
                    System.out.print("Enter the ISBN of the new book: ");
                    String newISBN = scanner.nextLine();
                    Book newBook = new Book(newTitle, newAuthor, newISBN);
                    newBook.saveToDatabase();
                    break;

                case 6:
                    System.out.print("\nEnter the title of the book to remove: ");
                    String removeTitle = scanner.nextLine();
                    Book removeBook = library.searchBookByTitle(removeTitle);
                    if (removeBook != null) {
                        library.removeBook(removeBook);
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;

                case 7:
                    member.viewBorrowedBooks();
                    break;

                case 0:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }
}
