package CarPark.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

// create a table where the key is a date and for each we keep the number of orders, the nember of orders that were cancelled, the number of orders that were checked in and the number of orders that the customer was late
// create an order relation on the dates and order the data by date


@Entity
@Table(name = "Statistics")
public class Statistics implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate date;
    private int numberOfOrders;
    private int numberOfOrdersCancelled;
    private int numberOfOrdersLate;
    private String parkingLotId;

    public Statistics(LocalDate date, int numberOfOrders, int numberOfOrdersCancelled, int numberOfOrdersLate, String parkingLotId) {
        this.date = date;
        this.numberOfOrders = numberOfOrders;
        this.numberOfOrdersCancelled = numberOfOrdersCancelled;
        this.numberOfOrdersLate = numberOfOrdersLate;
        this.parkingLotId = parkingLotId;
    }

    public Statistics() {}


    public LocalDate getDate() {
        return date;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public int getNumberOfOrdersCancelled() {
        return numberOfOrdersCancelled;
    }

    public int getNumberOfOrdersLate() {
        return numberOfOrdersLate;
    }

    public String getParkingLotId() {
        return parkingLotId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public void setNumberOfOrdersCancelled(int numberOfOrdersCancelled) {
        this.numberOfOrdersCancelled = numberOfOrdersCancelled;
    }

    public void setNumberOfOrdersLate(int numberOfOrdersLate) {
        this.numberOfOrdersLate = numberOfOrdersLate;
    }

    public void setParkingLotId(String parkingLotId) {
        this.parkingLotId = parkingLotId;
    }
}
