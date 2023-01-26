package CarPark.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "checkins")
public class CheckedIn implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int checkedInId;

    @Column(name = "entry_date")
    private String parkinglot;
    private LocalDateTime entryDate;
    private Integer carNumber;
    private Integer personId;
    private String email;
    private LocalDateTime exitEstimatedDate;
    private String type;
    private String parkinglot_name;



    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "parking_slot_parking_slot_id", nullable = false)
    private ParkingSlot parkingSlot;

    public CheckedIn(String parkinglot, LocalDateTime entryDate, Integer personId, Integer carNumber, String email, LocalDateTime exitEstimatedDate, ParkingSlot parkingSlot) {
        this.parkinglot = parkinglot;
        this.entryDate = entryDate;
        this.personId = personId;
        this.carNumber = carNumber;
        this.email = email;
        this.exitEstimatedDate = exitEstimatedDate;
        this.parkingSlot = parkingSlot;
        this.parkinglot_name = null;
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
        parkingSlot.setSpotStatus(ParkingSlot.Status.USED);
        parkingSlot.setCheckedIn(this);
        this.parkingSlot = parkingSlot;
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateTime entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getExitEstimatedDate() {
        return exitEstimatedDate;
    }

    public void setExitEstimatedDate(LocalDateTime exitEstimatedDate) {
        this.exitEstimatedDate = exitEstimatedDate;
    }

    public String getParkinglotId() {

        return parkinglot;
    }
}