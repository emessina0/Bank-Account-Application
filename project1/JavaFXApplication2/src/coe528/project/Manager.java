package coe528.project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Manager extends User {
    private String password;
    

    public Manager(String username, String password) {
        super(username);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public boolean customerExists(String username) {
        try {
            File inputFile = new File("src/coe528/project/customers.txt");
            Scanner scanner = new Scanner(inputFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                if (parts.length > 0 && parts[0].equals(username)) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.err.println("Error checking customer existence: " + e.getMessage());
        }
        return false;
    }
    
    
    public void addCustomer(String username, String password, double balance) {
       
        
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/coe528/project/customers.txt", true));
            writer.write(username + " " + password + " " + balance);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.err.println("Error adding customer: " + e.getMessage());
        }
    }

    public void deleteCustomer(String username) {
    try {
        File inputFile = new File("src/coe528/project/customers.txt");
        File tempFile = new File("src/coe528/project/deleted.txt");

        Scanner scanner = new Scanner(inputFile);
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            if (parts.length >= 3 && !parts[0].equals(username)) { // Check if username matches
                writer.write(line);
                writer.newLine();
            }
        }
        scanner.close();
        writer.close();

        // Delete the original file
        if (!inputFile.delete()) {
            System.err.println("Error deleting customer: Unable to delete original file");
            return;
        }

        // Rename the temporary file to the original file name
        if (!tempFile.renameTo(inputFile)) {
            System.err.println("Error deleting customer: Unable to rename temporary file");
        }
    } catch (IOException e) {
        System.err.println("Error deleting customer: " + e.getMessage());
    }
    }
}