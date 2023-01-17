package CarPark.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@AttributeOverrides({
        @AttributeOverride(name = "userId", column = @Column(name = "customerId"))
})
@Table(name = "customers")
public class Customer extends User {
    private int balance;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customerId", cascade = CascadeType.ALL)
    List<Membership> memberships;

    public Customer(long customerId, String firstName, String lastName, String email, int balance,String password) throws Exception {
        super(customerId, password, email,firstName,lastName);
        this.balance = balance;
        memberships = null;
    }

    public Customer() {
    }

    public void addMemberShip(Membership membership){
        memberships.add(membership);
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

}