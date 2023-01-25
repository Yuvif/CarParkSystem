package CarPark.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "checkins")
public class CheckedIn implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int checkedInId;

    @Column(name = "entry_date")
    private String parkinglot;
    private LocalDateTime entryDate;
    private int carNumber;
    private Integer userId;
    private String email;
    private LocalDateTime estimatedLeavingTime;
    private boolean isPayed = false;
    private String parkinglot_name;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "parking_slot_parking_slot_id", nullable = false)
    private ParkingSlot parkingSlot;


    public CheckedIn(String parkinglot, LocalDateTime entryDate, Integer userId, Integer carNumber, String email, LocalDateTime estimatedLeavingTime, ParkingSlot parkingSlot) {
        this.parkinglot = parkinglot;
        this.entryDate = entryDate;
        this.userId = userId;
        this.carNumber = carNumber;
        this.email = email;
        this.estimatedLeavingTime = estimatedLeavingTime;
        this.parkingSlot = parkingSlot;
        this.parkinglot_name = null;
        setParkingSlot(parkingSlot);
        parkingSlot.setSpotStatus(ParkingSlot.Status.USED);
        parkingSlot.setCheckedIn(this);
    }

    public CheckedIn() {

    }

    public String getParkinglot_name(){
        return parkinglot_name;
    }

    public void setParkinglot_name(String parkinglot_id){
        this.parkinglot_name = parkinglot_id;
    }

    public ParkingSlot getParkingSlot() {
        return parkingSlot;
    }

    public void setParkingSlot(ParkingSlot parkingSlot) {
        this.parkingSlot = parkingSlot;
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateTime entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setPersonId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getEstimatedLeavingTime() {
        return estimatedLeavingTime;
    }

    public void setEstimatedLeavingTime(LocalDateTime estimatedLeavingTime) {
        this.estimatedLeavingTime = estimatedLeavingTime;
    }

    public String getParkinglotId() {

        return parkinglot;
    }
}