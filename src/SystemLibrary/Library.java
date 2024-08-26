package SystemLibrary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Library {
    public void addBook(Book book) {
        book.saveToDatabase();
    }

    public void removeBook(Book book) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM books WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, book.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Book searchBookByTitle(String title) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM books WHERE title LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getBoolean("is_available")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM books";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getBoolean("is_available")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void registerUser(User user) {
        // Similar database logic for saving users
    }

    public void issueBook(User user, Book book) {
        if (book.isAvailable()) {
            user.borrowBook(book);
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO borrowed_books (user_id, book_id) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, user.getUserId());
                stmt.setInt(2, book.getId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Book is not available for borrowing.");
        }
    }

    public void returnBook(User user, Book book) {
        user.returnBook(book);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM borrowed_books WHERE user_id = ? AND book_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUserId());
            stmt.setInt(2, book.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
