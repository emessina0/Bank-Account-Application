package coe528.project;


import javafx.scene.control.Alert;

public class Customer extends User {
    
    private String password;
    private BankAccount account;
    
    private SetState levelState;

    
    public Customer(String username, String password) {
        super(username);
        this.password = password;
        this.account = new BankAccount(this); // Pass the current customer object to the BankAccount constructor
        this.levelState = new SilverState(this);// Initialize the level state
        
        
    }

    public String getPassword() {
        return password;
    }

    public BankAccount getAccount() {
        return account;
    }
    
    public void purchaseItem(double price) {
       updateLevelState();
        
        double fee = 0; // Initialize fee as 0

        // Check the level state of the customer if so set the fee 
        if (levelState instanceof SilverState) {
            fee = 20;
        } else if (levelState instanceof GoldState) {
            fee = 10;
        } // No fee for PlatinumState

        // Deduct the purchase amount and fee from the account balance
        double totalAmount = price + fee;
        if (account.getBalance() >= totalAmount) {
            account.withdraw(totalAmount);
            showAlert(Alert.AlertType.INFORMATION, "Purchase successful! Fee: $" + fee);
        } else {
            showAlert(Alert.AlertType.ERROR, "Insufficient funds for purchase.");
        }

        // Save the updated balance to the file
        account.saveBalanceToFile("src/coe528/project/customers.txt", getUsername(), getPassword());
        
        // Update the level state after the purchase
        updateLevelState();
    }
    
    // Method to set the level state of the customer
    public void setLevelState(SetState levelState) {
        this.levelState = levelState;
    }

    // Method to get the current level state of the customer
    public SetState getLevelState() {
        return levelState;
    }

    // Method to update the level state based on the account balance
    public void updateLevelState() {
        double balance = account.getBalance();
        if (balance >= 20000) {
            setLevelState(new PlatinumState(this));
        } else if (balance >= 10000) {
            setLevelState(new GoldState(this));
        } else {
            setLevelState(new SilverState(this));
        }
    }
    
     private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Purchase Result");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to get the level name
    public String getLevelName() {
        return levelState.getLevelName();
    }
}