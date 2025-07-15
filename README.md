# 📚 ReadSpace - Java Desktop Book Search App

ReadSpace is a Java Swing desktop application that allows users to **search for books via the Google Books API**, view book details with thumbnails, authors, ratings, and preview links. It also **saves search history and book records to a MySQL database**.

---

## 🚀 Features
- 🔍 **Search Books:** Query books via Google Books API.
- 🌐 **Preview Links:** Open book preview links directly in your browser.
- ⭐ **Display Ratings:** Visual star ratings for books.
- 🗂️ **Search History:** View and manage previous search queries.
- 🌓 **Dark Mode:** Toggle between light and dark themes.
- 💾 **MySQL Integration:** Stores search history and book details.
- 🎨 **Modern UI:** Clean, responsive UI with styled buttons and layouts.

---

## 🛠️ Tech Stack
- **Java 8+**
- **Swing GUI**
- **MySQL Database**
- **Google Books API**
- **org.json Library** for parsing JSON responses

---

## 📦 Project Structure

- /ReadSpaceApp
- ├── Main.java # Main class, entry point
- ├── ReadSpaceUI.java # Builds the UI components
- ├── UIHelper.java # UI utilities (button styling, link handling)
- ├── BookSearcher.java # API integration and result parsing
- ├── DatabaseHelper.java # MySQL database operations
- └── lib/
- └── json-20240303.jar # JSON parsing library
