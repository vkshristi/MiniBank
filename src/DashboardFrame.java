import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DashboardFrame extends JFrame {
    private BankAccount account;
    private JLabel balanceLabel;
    private DefaultTableModel tableModel;
    private String username;
    private JTabbedPane tabbedPane;
    private boolean darkModeEnabled = false;

    public DashboardFrame(String username) {
        this.username = username;
        this.account = new BankAccount(username);

        setTitle("Mini Bank Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(240, 240, 240));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        balanceLabel = new JLabel("Balance: ₹" + account.getBalance());
        balanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        topBar.add(welcomeLabel, BorderLayout.WEST);
        topBar.add(balanceLabel, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("🏦 Banking", createBankingPanel());
        tabbedPane.addTab("📄 History", createHistoryPanel());
        tabbedPane.addTab("⚙️ Settings", createSettingsTab());

        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createBankingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 40));

        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");

        styleActionButton(depositBtn);
        styleActionButton(withdrawBtn);

        depositBtn.addActionListener(e -> showTransactionDialog("Deposit"));
        withdrawBtn.addActionListener(e -> showTransactionDialog("Withdraw"));

        panel.add(depositBtn);
        panel.add(withdrawBtn);

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Type", "Amount", "Date"}, 0);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSettingsTab() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel settingsLabel = new JLabel("Settings");
        settingsLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        settingsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox darkModeCheckbox = new JCheckBox("Enable Dark Mode");
        darkModeCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        darkModeCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton exitButton = new JButton("Exit");
        exitButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exitButton.setBackground(new Color(220, 70, 70));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.setMaximumSize(new Dimension(120, 40));

        exitButton.addActionListener(e -> System.exit(0));
        exitButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                exitButton.setBackground(new Color(200, 50, 50));
            }

            public void mouseExited(MouseEvent e) {
                exitButton.setBackground(new Color(220, 70, 70));
            }
        });

        darkModeCheckbox.addActionListener(e -> {
            darkModeEnabled = darkModeCheckbox.isSelected();
            applyTheme(this.getContentPane(), darkModeEnabled);
        });

        settingsPanel.add(settingsLabel);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        settingsPanel.add(darkModeCheckbox);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        settingsPanel.add(exitButton);

        return settingsPanel;
    }

    private void styleActionButton(JButton button) {
        button.setPreferredSize(new Dimension(120, 50));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(100, 150, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(80, 130, 240));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(100, 150, 255));
            }
        });
    }

    private void showTransactionDialog(String type) {
        JPanel panel = new JPanel();
        JTextField amountField = new JTextField(10);
        panel.add(new JLabel("Enter amount:"));
        panel.add(amountField);

        int result = JOptionPane.showConfirmDialog(this, panel, type,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive.");
                    return;
                }

                boolean success = false;
                if (type.equals("Deposit")) {
                    account.deposit(amount);
                    success = true;
                    JOptionPane.showMessageDialog(this,
                            "Rs." + amount + " deposited successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    if (account.withdraw(amount)) {
                        success = true;
                        JOptionPane.showMessageDialog(this,
                                "Rs." + amount + " withdrawn successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Insufficient Balance.");
                        return;
                    }
                }

                if (success) {
                    account.saveTransaction(type, amount);
                    balanceLabel.setText("Balance: ₹" + account.getBalance());
                    loadTransactionHistory();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
            }
        }
    }

    private void loadTransactionHistory() {
        tableModel.setRowCount(0);
        List<Transaction> txns = account.getTransactionHistory();
        for (Transaction txn : txns) {
            tableModel.addRow(new Object[]{
                    txn.getType(),
                    "₹" + txn.getAmount(),
                    txn.getDate()
            });
        }
    }

    private void applyTheme(Component comp, boolean dark) {
        Color bg = dark ? new Color(34, 34, 34) : Color.WHITE;
        Color fg = dark ? new Color(220, 220, 220) : Color.BLACK;

        comp.setBackground(bg);
        comp.setForeground(fg);

        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                applyTheme(child, dark);
            }
        }

        if (comp instanceof JTable) {
            JTable table = (JTable) comp;
            table.setBackground(bg);
            table.setForeground(fg);
            table.getTableHeader().setBackground(dark ? new Color(60, 60, 60) : new Color(230, 230, 230));
            table.getTableHeader().setForeground(fg);
        }

        repaint();
    }
}
