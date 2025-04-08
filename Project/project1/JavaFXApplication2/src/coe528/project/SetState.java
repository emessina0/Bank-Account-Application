package coe528.project;


public abstract class SetState {
 
    protected Customer customer;

    public SetState(Customer customer) {
        this.customer = customer;
    }

    public abstract void updateLevel();
    public abstract String getLevelName();   
}
