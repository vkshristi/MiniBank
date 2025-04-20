import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {

    public WelcomeFrame() {
        setTitle("Mini Bank - Welcome");
        setUndecorated(false); // Set true if you want no title bar
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(40, 40, 60));

        // Welcome Title
        JLabel title = new JLabel("Welcome to Mini Bank", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(40, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // Buttons Panel
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));
        btnPanel.setOpaque(false);

        JButton loginBtn = createStyledButton("Login");
        JButton signupBtn = createStyledButton("Signup");

        loginBtn.addActionListener(e -> {
            dispose();
            new LoginSignupFrame("Login");
        });

        signupBtn.addActionListener(e -> {
            dispose();
            new LoginSignupFrame("Signup");
        });

        btnPanel.add(loginBtn);
        btnPanel.add(signupBtn);

        add(btnPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(100, 160, 220));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(70, 130, 180));
            }
        });
        return btn;
    }
}
