import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LoginSignupFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean isLoginMode;
    private JButton switchButton;
    private JButton actionButton;

    public LoginSignupFrame(boolean isLoginMode) {
        this.isLoginMode = isLoginMode;

        setTitle(isLoginMode ? "Login" : "Signup");
        setUndecorated(true); // borderless window
        setSize(400, 300);
        setLocationRelativeTo(null);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel(isLoginMode ? "Login to Mini Bank" : "Create a New Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = createRoundedTextField("Username");
        passwordField = createRoundedPasswordField("Password");

        actionButton = new JButton(isLoginMode ? "Login" : "Signup");
        styleButton(actionButton);
        actionButton.addActionListener(e -> handleAuth());

        switchButton = new JButton(isLoginMode ? "Don't have an account? Sign up" : "Already have an account? Log in");
        switchButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        switchButton.setContentAreaFilled(false);
        switchButton.setForeground(Color.BLUE.darker());
        switchButton.setBorderPainted(false);
        switchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        switchButton.addActionListener(e -> {
            dispose();
            new LoginSignupFrame(!isLoginMode);
        });

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(actionButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(switchButton);

        add(panel);
        setVisible(true);
    }

    public LoginSignupFrame(String mode) {
        if (mode.equalsIgnoreCase("Login")) {
            isLoginMode = true;
            initLoginUI();
        } else if (mode.equalsIgnoreCase("Signup")) {
            isLoginMode = false;
            initSignupUI();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid mode passed to LoginSignupFrame.");
            dispose();
        }
    }

    private void initLoginUI() {
        new LoginSignupFrame(true);
    }

    private void initSignupUI() {
        new LoginSignupFrame(false);
    }

    private JTextField createRoundedTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setToolTipText(placeholder);
        return field;
    }

    private JPasswordField createRoundedPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setToolTipText(placeholder);
        return field;
    }

    private void styleButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(60, 120, 200));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(80, 140, 220));
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(60, 120, 200));
            }
        });
    }

    private void handleAuth() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        if (isLoginMode) {
            if (authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose();
                new DashboardFrame(username); // Launch dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Login failed. Incorrect credentials.");
            }
        } else {
            if (registerUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Signup successful!");
                dispose();
                new DashboardFrame(username); // Launch dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists.");
            }
        }
    }

    private boolean authenticateUser(String username, String password) {
        File file = new File("users.txt");
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(":");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean registerUser(String username, String password) {
        File file = new File("users.txt");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.trim().split(":");
                    if (parts.length == 2 && parts[0].equals(username)) {
                        return false; // User already exists
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(username + ":" + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
