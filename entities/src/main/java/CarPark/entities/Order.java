package CarPark.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Orders")

public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;
    private int carId;
    private String ParkingLotId;
    private String email;
    private LocalDateTime arrivalTime;
    private LocalDateTime estimatedLeavingTime;
    private Date date;
    private Status orderStatus = Status.APPROVED;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public enum Status {APPROVED, CANCELLED};

    public Order(int customerId, int carId, String parkingLotId, String email, LocalDateTime arrivalTime, LocalDateTime estimatedLeavingTime, Date date) {
        super();
        this.customerId = customerId;
        this.carId = carId;
        ParkingLotId = parkingLotId;
        this.email = email;
        this.arrivalTime = arrivalTime;
        this.estimatedLeavingTime = estimatedLeavingTime;
        this.date = date;
    }

    public Order() {}

    public Boolean getStatus() {
        return orderStatus== Status.APPROVED;
    }
    public void setSpotStatus(Status spotStatus) {
        orderStatus = spotStatus;
    }

    public  Date getDate(){
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public int getCarId() {
        return carId;
    }
    public void setCarId(int carId) {
        this.carId = carId;
    }
    public String getParkingLotId() {
        return ParkingLotId;
    }
    public void setParkingLotId(String parkingLotId) {
        ParkingLotId = parkingLotId;
    }
    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public LocalDateTime getEstimatedLeavingTime() {
        return estimatedLeavingTime;
    }
    public void setEstimatedLeavingTime(LocalDateTime estimatedLeavingTime) {
        this.estimatedLeavingTime = estimatedLeavingTime;
    }
}