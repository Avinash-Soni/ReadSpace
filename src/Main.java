public class Main {
    public static void main(String[] args) {
        DatabaseHelper.createDatabase();
        javax.swing.SwingUtilities.invokeLater(ReadSpaceUI::createUI);
    }
}
