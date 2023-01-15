package CarPark.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Orders")

public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int customerId;
    private int carId;
    private int ParkingLotId;
    private String email;
    private LocalDateTime arrivalTime;
    private LocalDateTime estimatedLeavingTime;

    public Order(int customerId, int carId, int parkingLotId, String email, LocalDateTime arrivalTime, LocalDateTime estimatedLeavingTime) {
        super();
        this.customerId = customerId;
        this.carId = carId;
        this.ParkingLotId = parkingLotId;
        this.email = email;
        this.arrivalTime = arrivalTime;
        this.estimatedLeavingTime = estimatedLeavingTime;
    }

    public Order() {
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

    public int getParkingLotId() {
        return ParkingLotId;
    }

    public void setParkingLotId(int parkingLotId) {
        ParkingLotId = parkingLotId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setEstimatedLeavingTime(LocalDateTime estimatedLeavingTime) { this.estimatedLeavingTime = estimatedLeavingTime; }

    public int getId() {
        return id;
    }
}
