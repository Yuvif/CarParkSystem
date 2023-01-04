package CarPark.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "prices")
@Entity
public class Price implements Serializable {
    @Id
    @Column(name = "parking_type", nullable = false, length = 46)
    private String id;

    @Column(name = "payment_method", length = 45)
    private String paymentMethod;

    @Column(name = "price")
    private Integer price;

    @Column(name = "number_Of_Cars")
    private Integer numberOfCars;

    @Column(name = "hours_of_parking")
    private Integer hoursOfParking;


    public Price(String parking_type, String payment_method, int price, int number_of_cars, int hours_of_parking) {
        this.id = parking_type;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}