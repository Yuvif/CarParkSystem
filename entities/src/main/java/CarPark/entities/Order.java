package CarPark.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "Orders")

public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int customerId;
    private int carId;
    private int ParkingLotId;
    private LocalDateTime arrivalTime;
    private LocalDateTime estimatedLeavingTime;

    public Order(int customerId, int carId, int parkingLotId, LocalDateTime arrivalTime, LocalDateTime estimatedLeavingTime) {
        super();
        this.customerId = customerId;
        this.carId = carId;
        ParkingLotId = parkingLotId;
        this.arrivalTime = arrivalTime;
        this.estimatedLeavingTime = estimatedLeavingTime;
    }

    public Order() { }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getCarId() { return carId; }

    public void setCarId(int carId) { this.carId = carId; }

    public int getParkingLotId() { return ParkingLotId; }

    public void setParkingLotId(int parkingLotId) { ParkingLotId = parkingLotId; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }

    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public LocalDateTime getEstimatedLeavingTime() { return estimatedLeavingTime; }

    public void setEstimatedLeavingTime(LocalDateTime estimatedLeavingTime) { this.estimatedLeavingTime = estimatedLeavingTime; }

    public int getId() { return id; }
}
