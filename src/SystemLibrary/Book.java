package SystemLibrary;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Book {
    private int id;
    private String title;
    private String author;
    private String ISBN;
    private boolean isAvailable;

    public Book(int id, String title, String author, String ISBN, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.isAvailable = isAvailable;
    }

    public Book(String title, String author, String ISBN) {
        this(-1, title, author, ISBN, true); // Default constructor for adding a new book
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    // Setter for ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter for Title
    public String getTitle() {
        return title;
    }

    // Getter for Author
    public String getAuthor() {
        return author;
    }

    // Getter for ISBN
    public String getISBN() {
        return ISBN;
    }

    // Getter for Availability
    public boolean isAvailable() {
        return isAvailable;
    }

    // Mark the book as borrowed
    public void markAsBorrowed() {
        isAvailable = false;
    }

    // Mark the book as returned
    public void markAsReturned() {
        isAvailable = true;
    }

    public void saveToDatabase() {
        String sql;
        if (id == -1) {
            // Insert new book
            sql = "INSERT INTO books (title, author, isbn, is_available) VALUES (?, ?, ?, ?)";
        } else {
            // Update existing book
            sql = "UPDATE books SET title = ?, author = ?, isbn = ?, is_available = ? WHERE id = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, ISBN);
            stmt.setBoolean(4, isAvailable);

            if (id != -1) {
                stmt.setInt(5, id); // Set ID for update
            }

            int affectedRows = stmt.executeUpdate();

            if (id == -1 && affectedRows > 0) {
                // Retrieve generated ID for new book
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.id = generatedKeys.getInt(1); // Set the generated ID
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromDatabase() {
        if (id == -1) {
            System.out.println("Cannot delete a book that is not yet saved in the database.");
            return;
        }

        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getBoolean("is_available")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDetails() {
        return "Title: " + title + ", Author: " + author + ", ISBN: " + ISBN + ", Available: " + isAvailable;
    }
}


