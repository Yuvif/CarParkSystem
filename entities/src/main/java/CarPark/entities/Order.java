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
    private double ordersPrice;

    public Order(int customerId, int carId, String parkingLot, String email,
                 LocalDateTime arrivalTime, LocalDateTime estimatedLeavingTime, double ordersPrice)
    {
        super();
        this.customerId = customerId;
        this.carId = carId;
        this.parkingLot = parkingLot;
        this.email = email;
        this.arrivalTime = arrivalTime;
        this.estimatedLeavingTime = estimatedLeavingTime;
        this.ordersPrice = ordersPrice;
    }

    public Order() {}

    public int getCustomerId() {
        return customerId;
    }
    public int getCarId() {
        return carId;
    }
    public String getParkingLotId() { return parkingLot; }
    public String getEmail() {
        return email;
    }
    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
    public LocalDateTime getEstimatedLeavingTime() {
        return estimatedLeavingTime;
    }
    public double getOrdersPrice() { return ordersPrice; }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public void setCarId(int carId) {
        this.carId = carId;
    }
    public void setParkingLotId(String parkingLot) {
        this.parkingLot = parkingLot;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public void setEstimatedLeavingTime(LocalDateTime estimatedLeavingTime) { this.estimatedLeavingTime = estimatedLeavingTime; }
    public void setOrdersPrice(double ordersPrice) { this.ordersPrice = ordersPrice; }

    public int getId() {
        return id;
    }
}
