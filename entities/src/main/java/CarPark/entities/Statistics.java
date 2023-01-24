package CarPark.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

// create a table where the key is a date and for each we keep the number of orders, the nember of orders that were cancelled, the number of orders that were checked in and the number of orders that the customer was late
// create an order relation on the dates and order the data by date


@Entity
@Table(name = "Statistics")
public class Statistics implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;
    private int numberOfOrders;
    private int numberOfOrdersCancelled;
    private int numberOfOrdersLate;
    private int parkingLotId;

    public Statistics(Date date, int numberOfOrders, int numberOfOrdersCancelled, int numberOfOrdersLate, int parkingLotId) {
        this.date = date;
        this.numberOfOrders = numberOfOrders;
        this.numberOfOrdersCancelled = numberOfOrdersCancelled;
        this.numberOfOrdersLate = numberOfOrdersLate;
        this.parkingLotId = parkingLotId;
    }

    public Statistics() {}


    public Date getDate() {
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

    public int getParkingLotId() {
        return parkingLotId;
    }

    public void setDate(Date date) {
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

    public void setParkingLotId(int parkingLotId) {
        this.parkingLotId = parkingLotId;
    }
}
