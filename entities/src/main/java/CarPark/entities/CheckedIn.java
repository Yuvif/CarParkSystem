package CarPark.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "checkins")
public class CheckedIn implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "entry_date")
    private Date entryDate;
    private Integer carNumber;
    private Integer personId;
    private String email;
    private Date exitEstimatedDate;
    private boolean didHePay = false;

    @OneToOne(fetch = FetchType.LAZY)
    private ParkingSlot parkingSlot;

    public CheckedIn(Date entryDate, Integer personId, Integer carNumber, String email, Date exitEstimatedDate, ParkingSlot parkingSlot) {
        this.entryDate = entryDate;
        this.personId = personId;
        this.carNumber = carNumber;
        this.email = email;
        this.exitEstimatedDate = exitEstimatedDate;
        this.parkingSlot = parkingSlot;
        setParkingSlot(parkingSlot);
        parkingSlot.setSpotStatus(ParkingSlot.Status.USED);
        parkingSlot.setCheckedIn(this);
    }

    public CheckedIn() {

    }

    public ParkingSlot getParkingSlot() {
        return parkingSlot;
    }

    public void setParkingSlot(ParkingSlot parkingSlot) {
        this.parkingSlot = parkingSlot;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
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

    public Date getExitEstimatedDate() {
        return exitEstimatedDate;
    }

    public void setExitEstimatedDate(Date exitEstimatedDate) {
        this.exitEstimatedDate = exitEstimatedDate;
    }
}
