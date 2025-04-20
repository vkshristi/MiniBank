import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class BankAccount {
    private String username;
    private double balance;
    private List<Transaction> transactionHistory;
    private final String FILE_PATH;

    public BankAccount(String username) {
        this.username = username;
        this.FILE_PATH = "transaction_" + username + ".txt";
        this.transactionHistory = new ArrayList<>();
        this.balance = 0.0;
        loadTransactionsFromFile();
    }

    public double getBalance() {
        return balance;
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void saveTransaction(String type, double amount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write(type + "," + amount + "," + date + "," + balance);
            writer.newLine();
            transactionHistory.add(new Transaction(type, amount, date));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    private void loadTransactionsFromFile() {
        transactionHistory.clear();
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String lastLine = null;

            while ((line = reader.readLine()) != null) {
                lastLine = line;
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String type = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    String date = parts[2];
                    transactionHistory.add(new Transaction(type, amount, date));
                }
            }

            // Load last saved balance from file
            if (lastLine != null) {
                String[] parts = lastLine.split(",");
                if (parts.length >= 4) {
                    this.balance = Double.parseDouble(parts[3]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
