package coe528.project;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


public class BankAccountApplication extends Application {
    private Stage stage;
    private BorderPane loginPanel;
    private VBox customerPanel;
    private VBox managerPanel;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label balanceLabel;
    private Label levelLabel;
    private User currentUser;
    private Manager manager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Bank Account Application");

        manager = new Manager("manager_username", "manager_password");

        loginPanel = createLoginPanel();
        customerPanel = createCustomerPanel();
        managerPanel = createManagerPanel();

        Scene scene = new Scene(loginPanel, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    private BorderPane createLoginPanel() {
        BorderPane loginPanel = new BorderPane();
        VBox fields = new VBox(10);
        fields.setPadding(new Insets(20));

        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        fields.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton);
        loginPanel.setCenter(fields);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                authenticateUser(username, password);
                updateBalanceLabel();
                updateLevelLabel();
            }
        });

        return loginPanel;
    }

    private VBox createCustomerPanel() {
        VBox customerPanel = new VBox(10);
        customerPanel.setPadding(new Insets(20));

        balanceLabel = new Label();
        levelLabel = new Label();

        Button depositButton = new Button("Deposit");
        Button withdrawButton = new Button("Withdraw");
        Button balanceButton = new Button("Show Balance");
        Button purchaseButton = new Button("Purchase");
        Button logoutButton = new Button("Logout");

        depositButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String amountString = showInputDialog("Enter amount to deposit:");
                try {
                    double amount = Double.parseDouble(amountString);
                    if (amount > 0) {
                        if (currentUser instanceof Customer) {
                            Customer customer = (Customer) currentUser;
                            customer.getAccount().deposit(amount);
                            customer.getAccount().saveBalanceToFile("src/coe528/project/customers.txt", customer.getUsername(), customer.getPassword());
                            updateBalanceLabel();
                            updateLevelLabel();
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Invalid user type", "Error");
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Invalid amount. Please enter a positive number.", "Error");
                    }
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Invalid amount format. Please enter a valid number.", "Error");
                }
            }
        });

        withdrawButton.setOnAction(event -> {
        String amountString = showInputDialog("Enter amount to withdraw:");
        try {
            double amount = Double.parseDouble(amountString);
            if (amount > 0) {
                if (currentUser instanceof Customer) {
                    Customer customer = (Customer) currentUser;
                    if (customer.getAccount().getBalance() >= amount) {
                        customer.getAccount().withdraw(amount);
                        customer.getAccount().saveBalanceToFile("src/coe528/project/customers.txt", customer.getUsername(), customer.getPassword());
                        updateBalanceLabel();
                        updateLevelLabel();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Insufficient balance", "Error");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Invalid user type", "Error");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid amount. Please enter a positive number.", "Error");
            }
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Invalid amount format. Please enter a valid number.", "Error");
        }
    });
        
    
        balanceButton.setOnAction(event -> {
    if (currentUser instanceof Customer) {
        Customer customer = (Customer) currentUser;
        showAlert(Alert.AlertType.INFORMATION, "Balance: $" + customer.getAccount().getBalance(), "Balance");
        updateBalanceLabel(); // Update balance label when balance button is clicked
    } else {
        showAlert(Alert.AlertType.ERROR, "Invalid user type", "Error");
    }
});
     purchaseButton.setOnAction(event -> {
    if (currentUser instanceof Customer) {
        Customer customer = (Customer) currentUser;

        // Display a list of items for purchase
        String[] items = {"SteelSeries Mouse - $80", "Red Dragon Keyboard - $100", "Item 3 - $80",
                "Airpods - $300", "IPhone 11 - $1000"};
        
        ChoiceDialog<String> dialog = new ChoiceDialog<>(items[0], items);
        dialog.setTitle("Purchase Item");
        dialog.setHeaderText("Select an item to purchase:");
        dialog.setContentText("Item:");

        // Dialog wait for user input
        dialog.showAndWait().ifPresent(selectedItem -> {
            double price = Double.parseDouble(selectedItem.substring(selectedItem.indexOf("$") + 1));
            customer.purchaseItem(price);
            showAlert(Alert.AlertType.INFORMATION, "Item purchased successfully!", "Purchase Successful");
            updateBalanceLabel();
            updateLevelLabel();
        });
    } else {
        showAlert(Alert.AlertType.ERROR, "Invalid user type", "Error");
    }
});   

         logoutButton.setOnAction(event -> {
    stage.getScene().setRoot(createLoginPanel());
    currentUser = null;
});
        customerPanel.getChildren().addAll(balanceLabel, levelLabel, depositButton, withdrawButton, balanceButton, purchaseButton, logoutButton);

        return customerPanel;
    }

   private VBox createManagerPanel() {
       VBox managerPanel = new VBox();
    managerPanel.setPadding(new Insets(20));
    managerPanel.setSpacing(10);

    Button addCustomerButton = new Button("Add Customer");
    Button deleteCustomerButton = new Button("Delete Customer");
    Button logoutButton = new Button("Logout");

    addCustomerButton.setOnAction(event -> {
        
        // Prompt for username and password
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setTitle("Add Customer");
        usernameDialog.setHeaderText(null);
        usernameDialog.setContentText("Enter username:");
        usernameDialog.showAndWait().ifPresent(username -> {
            
             // Check if the username already exists
        if (manager.customerExists(username)) {
            showAlert("Username already exists! Please choose a different username.");
            return; 
        }
            
            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Add Customer");
            passwordDialog.setHeaderText(null);
            passwordDialog.setContentText("Enter password:");
            passwordDialog.showAndWait().ifPresent(password -> {
                // Check if username and password are not empty
                if (!username.isEmpty() && !password.isEmpty()) {
                    // Use the provided manager object to add the customer
                    manager.addCustomer(username, password, 100);
                    
                    showAlert("Customer added successfully!");
                } else {
                    
                    showAlert("Username or password cannot be empty!");
                }
            });
        });
    });

    deleteCustomerButton.setOnAction(event -> {
        // Prompt for the username of the customer to delete
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Customer");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter username of the customer to delete:");

        // Get the username
        dialog.showAndWait().ifPresent(username -> {
            // Check if username is not empty
            if (!username.isEmpty()) {
                
                manager.deleteCustomer(username);
                
                showAlert("Customer deleted successfully!");
            } else {
                
                showAlert("Username cannot be empty!");
            }
        });
    });

    logoutButton.setOnAction(event -> {
    stage.getScene().setRoot(createLoginPanel());
    currentUser = null;
});
    managerPanel.getChildren().addAll(addCustomerButton, deleteCustomerButton, logoutButton);
    return managerPanel;
}

    private void authenticateUser(String username, String password) {
        File userFile = new File("src/coe528/project/managers.txt");
        File customerFile = new File("src/coe528/project/customers.txt");

        try {
            Scanner scanner = new Scanner(userFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    String storedUsername = parts[0];
                    String storedPassword = parts[1];
                    if (username.equals(storedUsername) && password.equals(storedPassword)) {
                        currentUser = new Manager(username, password);
                        showManagerPanel();
                        return;
                    }
                }
            }
            scanner.close();

         Scanner customerScanner = new Scanner(customerFile);
            while (customerScanner.hasNextLine()) {
                String line = customerScanner.nextLine();
                String[] parts = line.split(" ");
                if (parts.length >= 3) {
                    String storedUsername = parts[0];
                    String storedPassword = parts[1];
                    if (username.equals(storedUsername) && password.equals(storedPassword)) {
                        double balance = Double.parseDouble(parts[2].substring(parts[2].indexOf("$") + 1)); // Extract balance from the line
                        currentUser = new Customer(username, password);
                        ((Customer) currentUser).getAccount().deposit(balance); // Deposit the balance
                        showCustomerPanel();
                        customerScanner.close();
                        return;
                    }
                }
            }
            customerScanner.close();

            // If no matching username and password were found
            showAlert(Alert.AlertType.ERROR, "Invalid username or password", "Error");
        } catch (FileNotFoundException ex) {
            showAlert(Alert.AlertType.ERROR, "User file not found", "Error");
        }
    }

    private void showAlert(Alert.AlertType alertType, String message, String title) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String showInputDialog(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input");
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        dialog.showAndWait();
        return dialog.getResult();
    }

    private void showCustomerPanel() {
        Scene scene = new Scene(customerPanel, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void showManagerPanel() {
        Scene scene = new Scene(managerPanel, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void updateBalanceLabel() {
        if (currentUser instanceof Customer) {
            Customer customer = (Customer) currentUser;
            balanceLabel.setText("Balance: $" + customer.getAccount().getBalance());
        }
    }
    
    private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

    private void showLoginScene() {
    stage.setScene(new Scene(loginPanel, 300, 200));
}
    
    private void updateLevelLabel() {
        if (currentUser instanceof Customer) {
            Customer customer = (Customer) currentUser;
            customer.updateLevelState();
            String level = customer.getLevelName();
            System.out.println("Customer level: " + level);
            levelLabel.setText("Level: " + level);
        } else {
            levelLabel.setText("Level: N/A");
        }
    }
}