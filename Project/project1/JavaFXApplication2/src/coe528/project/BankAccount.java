package coe528.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BankAccount {
    private double balance;
    private SetState levelState;
    private Customer customer; 

    public BankAccount(Customer customer) {
        this.customer = customer;
        balance = 0;
        levelState = new SilverState(customer); // Initial level state is Silver
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        updateLevel();
    }

    public void withdraw(double amount) {
        balance -= amount;
        updateLevel();
    }


    public void saveBalanceToFile(String filename, String username, String password) {
        try {
            // Read existing file contents
            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3 && parts[0].equals(username) && parts[1].equals(password)) {
                    // If the line corresponds to the current user, update the balance
                    line = parts[0] + " " + parts[1] + " " + balance;
                }
                content.append(line).append(System.lineSeparator());
            }
            reader.close();

            // Write updated contents back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(content.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   public void updateLevel() {
        levelState.updateLevel();
    }

    public void setLevelState(SetState levelState) {
        this.levelState = levelState;
    }

    public SetState getLevelState() {
        return levelState;
    }
}