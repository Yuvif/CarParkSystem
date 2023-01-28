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
    private LocalDateTime entryDate;

    private Integer carNumber;
    private Long personId;
    private String email;
    private LocalDateTime exitEstimatedDate;
    private String type;

    @ManyToOne
    @JoinColumn(name = "parkingLotId")
    private Parkinglot parkinglot;

    @OneToOne
    @JoinColumn(name = "parkingSlotId")
    private ParkingSlot parkingSlot;

    public CheckedIn(LocalDateTime entryDate, Long personId, Integer carNumber, String email, LocalDateTime exitEstimatedDate, String type) {
        this.entryDate = entryDate;
        this.personId = personId;
        this.carNumber = carNumber;
        this.email = email;
        this.exitEstimatedDate = exitEstimatedDate;
        this.type = type;
    }

    public CheckedIn() {

    }

    public Parkinglot getParkinglot(){return parkinglot;}

    public void setParkinglot(Parkinglot parkinglot){this.parkinglot = parkinglot;}

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

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
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

    public int getId() { return checkedInId; }
}