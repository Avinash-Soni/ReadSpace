import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.*;

public class BookSearcher {

    public static String searchBooks(String query, boolean darkMode) {
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        StringBuilder html = new StringBuilder("<html><body style='font-family:Arial; background-color:" + (darkMode ? "#141414" : "#FFFFFF") + "; color:" + (darkMode ? "#FFFFFF" : "#000000") + ";'>");
        html.append("<div style='display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; padding: 10px;'>");

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) response.append(scanner.nextLine());
            scanner.close();

            JSONArray items = new JSONObject(response.toString()).getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo");
                String title = volumeInfo.optString("title", "Unknown Title");
                String authors = volumeInfo.has("authors") ? volumeInfo.getJSONArray("authors").join(", ").replace("\"", "") : "Unknown Author";
                String thumbnail = volumeInfo.has("imageLinks") ? volumeInfo.getJSONObject("imageLinks").optString("thumbnail", "") : "";
                double rating = volumeInfo.optDouble("averageRating", 0.0);
                String preview = volumeInfo.optString("previewLink", "");

                html.append("<div style='border: 1px solid ")
                        .append(darkMode ? "#555" : "#ccc")
                        .append("; border-radius: 5px; padding: 15px; background-color:")
                        .append(darkMode ? "#2a2a2a" : "#f9f9f9")
                        .append("; text-align: center;'>");

                html.append("<h3>").append(title).append("</h3>")
                        .append("<p><strong>Authors:</strong> ").append(authors).append("</p>");
                if (rating > 0) html.append("<p><strong>Rating:</strong> ").append(getStarRating(rating)).append(" (").append(rating).append("/5)</p>");
                if (!thumbnail.isEmpty()) html.append("<img src='").append(thumbnail).append("' width='120' style='margin-bottom: 10px;'/>");
                if (!preview.isEmpty()) html.append("<div><a href='").append(preview).append("'><button style='margin-top: 10px;'>Read Online</button></a></div>");

                html.append("</div>");

                DatabaseHelper.saveBookToDatabase(title, authors);
            }

        } catch (Exception e) {
            return "<html><body><p>Error: " + e.getMessage() + "</p></body></html>";
        }

        html.append("</div></body></html>");
        return html.toString();
    }

    private static String getStarRating(double rating) {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < 5; i++) stars.append(i < Math.round(rating) ? "★" : "☆");
        return stars.toString();
    }
}
