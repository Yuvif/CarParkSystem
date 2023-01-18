package CarPark.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@AttributeOverrides({
        @AttributeOverride(name = "userId", column = @Column(name = "customerId"))
})
@Table(name = "customers")
public class Customer extends User {
    private double balance;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customerId", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Membership> memberships;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customerId", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Order> orders;

    public Customer(long customerId, String firstName, String lastName, String email, double balance, String password) throws Exception
    {
        super(customerId, password, email, firstName, lastName);
        this.balance = 0.0;
        memberships = null;
        orders = null;
    }

    public Customer() {}

    public void addMembership(Membership membership) { memberships.add(membership); }
    public void addOrder(Order order) { orders.add(order); }

    public double getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
    public void addToBalance(double sum) { this.balance += sum; }

}