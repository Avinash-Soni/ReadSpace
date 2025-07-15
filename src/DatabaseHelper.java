import java.sql.*;
import javax.swing.*;

public class DatabaseHelper {
    static final String DB_URL = "jdbc:mysql://localhost:3306/readspace";
    static final String USER = "root";
    static final String PASS = "hrishabh";

    public static void createDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS search_history (id INT AUTO_INCREMENT PRIMARY KEY, query VARCHAR(255))");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS books (id INT AUTO_INCREMENT PRIMARY KEY, title NVARCHAR(255), authors TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveSearchHistory(String query) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO search_history (query) VALUES (?)");
            pstmt.setString(1, query);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveBookToDatabase(String title, String authors) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            PreparedStatement check = conn.prepareStatement("SELECT * FROM books WHERE title=?");
            check.setString(1, title);
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO books (title, authors) VALUES (?, ?)");
                pstmt.setString(1, title);
                pstmt.setString(2, authors);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getSearchHistory(boolean darkMode) {
        StringBuilder html = new StringBuilder("<html><body style='font-family:Arial; background-color:" + (darkMode ? "#141414" : "#FFFFFF") + "; color:" + (darkMode ? "#FFFFFF" : "#000000") + ";'><h2>Search History</h2>");
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT query FROM search_history ORDER BY id DESC")) {

            if (!rs.isBeforeFirst()) {
                html.append("<p>No search history yet.</p>");
            } else {
                while (rs.next()) {
                    html.append("<p>").append(rs.getString("query")).append("</p><hr>");
                }
            }
        } catch (SQLException e) {
            return "<html><body><p>Error retrieving history: " + e.getMessage() + "</p></body></html>";
        }
        html.append("</body></html>");
        return html.toString();
    }

    public static void clearHistory() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM search_history");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateSearchSuggestions(JComboBox<String> comboBox) {
        comboBox.removeAllItems();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT query FROM search_history ORDER BY id DESC LIMIT 5")) {
            while (rs.next()) {
                comboBox.addItem(rs.getString("query"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
