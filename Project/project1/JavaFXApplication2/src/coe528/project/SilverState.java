package coe528.project;


public class SilverState extends SetState {
 
    public SilverState(Customer customer) {
        super(customer);
    }

    @Override
    public void updateLevel() {
        // Check the account balance and update the level if necessary
        double balance = customer.getAccount().getBalance();
        if (balance >= 10000 && balance < 20000) {
            customer.setLevelState(new GoldState(customer));
        } else if (balance >= 20000) {
            customer.setLevelState(new PlatinumState(customer));
        }
    }

    @Override
    public String getLevelName() {
        return "Silver";
    }   
}