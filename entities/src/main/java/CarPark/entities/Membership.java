package CarPark.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@Table(name = "Memberships")
public class Membership implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int membershipId;
    private String customerId;
    private int carId;
    private String routineParkingLot;
    private LocalTime routineLeavingHour;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String membershipType;
    private double membershipsPrice;

    @ManyToOne
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Membership(int membershipId, String customerId, int carId, String routineParkingLot, LocalTime routineLeavingHour,
                      LocalDateTime startDate, LocalDateTime endDate, String membershipType, double membershipsPrice) {
        super();
        this.membershipId = membershipId;
        this.customerId = customerId;
        this.carId = carId;
        this.routineParkingLot = routineParkingLot;
        this.routineLeavingHour = routineLeavingHour;
        this.startDate = startDate;
        this.membershipType = membershipType;
        this.endDate = endDate;
        this.membershipsPrice = membershipsPrice;
    }

    public Membership() {}

    public long getMembershipId() {
        return membershipId;
    }
    public String getCustomerId() {
        return customerId;
    }
    public long getCarId() {
        return carId;
    }
    public LocalTime getRoutineLeavingHour() {
        return routineLeavingHour;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public String getRoutineParkingLot() {
        return routineParkingLot;
    }
    public LocalDateTime getEndDate() {
        return endDate;
    }
    public String getMembershipType() { return membershipType; }
    public double getMembershipsPrice() { return membershipsPrice; }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public void setCarId(int carId) {
        this.carId = carId;
    }
    public void setRoutineLeavingHour(LocalTime routineLeavingHour) {
        this.routineLeavingHour = routineLeavingHour;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public void setRoutineParkingLot(String routineParkingLot) {
        this.routineParkingLot = routineParkingLot;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }
    public void setMembershipsPrice(double membershipsPrice) { this.membershipsPrice = membershipsPrice; }

    public int getId() {
        return id;
    }
}