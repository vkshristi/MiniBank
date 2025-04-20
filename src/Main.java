import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Ensure the GUI runs on the Event Dispatch Thread (EDT)
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WelcomeFrame();  // This opens your welcome screen with Login & Signup
            }
        });
    }
}
