import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class UIHelper {
    public static JButton styleButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setBackground(ReadSpaceUI.darkMode ? new Color(100, 100, 100) : new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return button;
    }

    public static void openLink(java.net.URL url) {
        try {
            Desktop.getDesktop().browse(new URI(url.toString()));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Could not open link.");
        }
    }
}
