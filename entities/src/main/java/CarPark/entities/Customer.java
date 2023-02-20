package CarPark.entities;


import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "customers")
@AttributeOverrides({
        @AttributeOverride(name = "userId", column = @Column(name = "customerId"))
})
public class Customer extends User {
    private double balance;

    @OneToMany(mappedBy = "customerId", cascade = CascadeType.PERSIST)
    @LazyCollection(LazyCollectionOption.FALSE)
     List<Membership> membershipList = new LinkedList<>();

    @OneToMany(mappedBy = "customerId", cascade = CascadeType.PERSIST)
    @LazyCollection(LazyCollectionOption.FALSE)
    List<Order> orderList = new LinkedList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)
    @LazyCollection(LazyCollectionOption.FALSE)
    List<Complaint> complaintList = new LinkedList<>();

    public Customer(String customerId, String firstName, String lastName, String email, double balance,String password, byte[] salt) throws Exception {
        super(customerId, password, salt, email, firstName, lastName);
        this.balance = balance;
    }

    public Customer() {
    }

    public void addMemberShip(Membership membership){
        membershipList.add(membership);
    }

    public List<Membership> getMemberships(){
        return membershipList;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addToBalance(double price){
        this.balance += price;
    }

    public void addOrder(Order new_order)
    {
        orderList.add(new_order);
    }

    public List<Order> getOrderList(){return orderList;}

}