import com.hotel.ui.*;
import javax.swing.*;

public class HotelApp {
    public static void main(String[] args) {
        // Use system look-and-feel for native OS integration
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
