package coe528.project;


public class PlatinumState extends SetState {


    public PlatinumState(Customer customer) {
        super(customer);
    }

    @Override
    public void updateLevel() {
        double balance = customer.getAccount().getBalance();
        if (balance < 10000) {
            customer.setLevelState(new SilverState(customer));
        } else if (balance >= 10000 && balance < 20000) {
            customer.setLevelState(new GoldState(customer));
        }
    }

    @Override
    public String getLevelName(){
        return "Platinum";
    }

    
}