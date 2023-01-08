package CarPark.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "prices")
public class Price implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "parking_type", nullable = false, length = 46)
    private String parkingType;

    @Column(name = "payment_method", length = 45)
    private String paymentMethod;

    @Column(name = "price")
    private Integer price;

    @Column(name = "number_Of_Cars")
    private Integer numberOfCars;

    @Column(name = "hours_of_parking")
    private Integer hoursOfParking;


    public Price(String parking_type, String payment_method, int price, int number_of_cars, int hours_of_parking) {
        this.parkingType= parking_type;
        this.paymentMethod = payment_method;
        this.price = price;
        this.numberOfCars = number_of_cars;
        this.hoursOfParking = hours_of_parking;
    }

    public Price() {

    }


    public Integer getHoursOfParking() {
        return hoursOfParking;
    }

    public void setHoursOfParking(Integer hoursOfParking) {
        this.hoursOfParking = hoursOfParking;
    }

    public Integer getNumberOfCars() {
        return numberOfCars;
    }

    public void setNumberOfCars(Integer numberOfCars) {
        this.numberOfCars = numberOfCars;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParking_type(String parking_type) {
        this.parkingType = parking_type;
    }

    public int getId() {
        return id;
    }
}