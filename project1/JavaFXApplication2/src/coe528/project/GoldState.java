package coe528.project;

/**
 * GoldState represents the GOLD level state of a customer in the bank system.
 * This class is mutable.
 */

/**
 * Abstraction Function:
 * - GoldState represents the GOLD level state of a customer.
 * - It is associated with a customer and determines their level based on their account balance.
 * - If the account balance is between $10,000 (inclusive) and $20,000 (exclusive), the customer is in GOLD state.
 *
 * Rep Invariant:
 * - GoldState instances are associated with a valid customer.
 * - The level of the associated customer must be GOLD.
 */

public class GoldState extends SetState {
    public GoldState(Customer customer) {
         super(customer);
    }

    /**
     * Updates the level of the associated customer based on their account balance.
     * If the account balance is less than $10,000, sets the level to SILVER.
     * If the account balance is $10,000 or more but less than $20,000, keeps the level as GOLD.
     * If the account balance is $20,000 or more, sets the level to PLATINUM.
     *
     * Effects: Modifies the level state of the associated customer.
     * Modifies: The state level
     * Requires: None.
     */
    @Override
    public void updateLevel() {
        double balance = customer.getAccount().getBalance();
        if (balance < 10000) {
            customer.setLevelState(new SilverState(customer));
        } else if (balance >= 20000) {  
            customer.setLevelState(new PlatinumState(customer));
        }
    }

   

    /**
     * Checks if the representation invariant holds true.
     *
     * Effects: None.
     * Modifies: None.
     * Requires: None.
     * @return True if the rep invariant holds, false otherwise.
     */
    
     // GoldState instances should always represent the GOLD level state of a customer
    public boolean repOk() {
       
        return this instanceof GoldState; //If it is Gold state then true if not false
    }

     /**
     * Returns the name of the level, which is GOLD.
     *
     * Effects: None.
     * Modifies: None.
     * Requires: None.
     */
    @Override
    public String getLevelName() {
        return "Gold";
    }
    
}