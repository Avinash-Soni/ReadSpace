import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.HyperlinkEvent;

public class ReadSpaceUI {
    static boolean darkMode = false;
    static JEditorPane resultPane;
    static JFrame frame;

    public static void createUI() {
        frame = new JFrame("Read Space");
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Color bgColor = darkMode ? new Color(30, 30, 30) : new Color(240, 248, 255);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(bgColor);

        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(bgColor), BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(darkMode ? new Color(50, 50, 50) : new Color(135, 206, 235));
        JLabel titleLabel = new JLabel("Read Space", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(darkMode ? Color.WHITE : Color.BLACK);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        return headerPanel;
    }

    private static JPanel createCenterPanel(Color bgColor) {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        topPanel.setBackground(darkMode ? new Color(50, 50, 50) : new Color(173, 216, 230));

        JTextField searchField = new JTextField(25);
        JComboBox<String> searchSuggestions = new JComboBox<>();
        DatabaseHelper.updateSearchSuggestions(searchSuggestions);

        JButton searchButton = UIHelper.styleButton("Search");
        JButton clearHistoryButton = UIHelper.styleButton("Clear History");
        JButton showHistoryButton = UIHelper.styleButton("Show Search History");
        JButton darkModeButton = UIHelper.styleButton("Toggle Dark Mode");

        resultPane = new JEditorPane();
        resultPane.setContentType("text/html");
        resultPane.setEditable(false);
        resultPane.setBackground(darkMode ? new Color(20, 20, 20) : Color.WHITE);
        resultPane.setForeground(darkMode ? Color.WHITE : Color.BLACK);
        resultPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                UIHelper.openLink(e.getURL());
            }
        });

        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                resultPane.setText(BookSearcher.searchBooks(query, darkMode));
                DatabaseHelper.saveSearchHistory(query);
                DatabaseHelper.updateSearchSuggestions(searchSuggestions);
            }
        });

        clearHistoryButton.addActionListener(e -> {
            DatabaseHelper.clearHistory();
            resultPane.setText("<html><body><p>Search history cleared.</p></body></html>");
            DatabaseHelper.updateSearchSuggestions(searchSuggestions);
        });

        showHistoryButton.addActionListener(e -> {
            resultPane.setText(DatabaseHelper.getSearchHistory(darkMode));
        });

        darkModeButton.addActionListener(e -> {
            darkMode = !darkMode;
            frame.dispose();
            createUI();
        });

        topPanel.add(searchField);
        topPanel.add(searchSuggestions);
        topPanel.add(searchButton);
        topPanel.add(clearHistoryButton);
        topPanel.add(showHistoryButton);
        topPanel.add(darkModeButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(bgColor);
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(resultPane), BorderLayout.CENTER);

        return centerPanel;
    }
}
