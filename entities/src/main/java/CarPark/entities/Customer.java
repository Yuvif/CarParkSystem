package CarPark.entities;

import javax.persistence.*;

@Entity
@Table(name = "customers")
public class Customer extends User {
    private int balance;

    public Customer(long customerId, String firstName, String lastName, String email, int balance,String password,boolean isLogged) throws Exception {
        super(customerId, password, email,firstName,lastName,isLogged);
        this.balance = balance;
    }

    public Customer() {
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

}