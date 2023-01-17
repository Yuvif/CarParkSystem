package CarPark.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "Memberships")
public class Membership implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int membershipId;
    private long customerId;
    private int carId;
    private String routineParkingLot;
    private LocalTime routineLeavingHour;
    private LocalDate startDate;
    private LocalDate endDate;
    private String membershipType;
    private double membershipsPrice;

    public Membership(int membershipId, long customerId, int carId, String routineParkingLot, LocalTime routineLeavingHour,
                      LocalDate startDate, LocalDate endDate, String membershipType, double membershipsPrice) {
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
    public long getCustomerId() {
        return customerId;
    }
    public long getCarId() {
        return carId;
    }
    public LocalTime getRoutineLeavingHour() {
        return routineLeavingHour;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public String getRoutineParkingLot() {
        return routineParkingLot;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public String getMembershipType() { return membershipType; }
    public double getMembershipsPrice() { return membershipsPrice; }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }
    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
    public void setCarId(int carId) {
        this.carId = carId;
    }
    public void setRoutineLeavingHour(LocalTime routineLeavingHour) {
        this.routineLeavingHour = routineLeavingHour;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setRoutineParkingLot(String routineParkingLot) {
        this.routineParkingLot = routineParkingLot;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }
    public void setMembershipsPrice(double membershipsPrice) { this.membershipsPrice = membershipsPrice; }

    public int getId() {
        return id;
    }
}