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
    private String parkingLot;
    private String email;
    private LocalDateTime arrivalTime;
    private LocalDateTime estimatedLeavingTime;

    public Order(int customerId, int carId, String parkingLot, String email, LocalDateTime arrivalTime, LocalDateTime estimatedLeavingTime) {
        super();
        this.customerId = customerId;
        this.carId = carId;
        this.parkingLot = parkingLot;
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

    public String getParkingLotId() {
        return parkingLot;
    }

    public void setParkingLotId(String parkingLot) {
        this.parkingLot = parkingLot;
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
