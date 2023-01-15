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

    public Membership(int membershipId, long customerId, int carId, String routineParkingLot,
                      LocalTime routineLeavingHour, LocalDate startDate, LocalDate endDate, String membershipType) {
        super();
        this.membershipId = membershipId;
        this.customerId = customerId;
        this.carId = carId;
        this.routineParkingLot = routineParkingLot;
        this.routineLeavingHour = routineLeavingHour;
        this.startDate = startDate;
        this.membershipType = membershipType;
        this.endDate = endDate;
    }

    public Membership() {}

    public long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public LocalTime getRoutineLeavingHour() {
        return routineLeavingHour;
    }

    public void setRoutineLeavingHour(LocalTime routineLeavingHour) {
        this.routineLeavingHour = routineLeavingHour;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getRoutineParkingLot() {
        return routineParkingLot;
    }

    public void setRoutineParkingLot(String routineParkingLot) {
        this.routineParkingLot = routineParkingLot;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String MembershipType() { return membershipType; }

    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }

    public int getId() {
        return id;
    }
}