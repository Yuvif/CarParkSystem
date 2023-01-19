package CarPark.entities;
import CarPark.entities.Parkinglot;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "complaints")
public class Complaint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String customerId;    //only for customers
    @Column(name = "dateOfSubmit")
    private Date date;

    @Column(name = "description")
    private String compText;
    private Boolean appStatus = true;        // true= complaint filed successfully- yet to be inspected, false = complaint fulfilled
    private Boolean completedOnTime = false;

    @ManyToOne
    @JoinColumn(name = "parkinglot_id")
    private Parkinglot parkinglot;


    public Complaint(Date date, String compText, Parkinglot parkinglot, String customerId) {
        this.date = date;
        this.compText = compText;
        this.customerId = customerId;
        setParkinglot(parkinglot);
    }

    public Complaint(Date date, String text, String customerIdT) {
        date = date;
        this.compText = text;
        this.customerId = customerIdT;
    }

    public Complaint(String customerId) {

        this.customerId = customerId;
    }

    public Complaint() {

    }

    public void setParkinglot(Parkinglot parkinglot) {
        this.parkinglot = parkinglot;
        parkinglot.getComplaints().add(this);
    }

    public Parkinglot getParkinglot() {
        return parkinglot;
    }


    public String getCompText() {
        return compText;
    }

    public void setCompText(String compText) {
        compText = compText;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Boolean getStatus() {
        return appStatus;
    }

    public void setStatus(Boolean status) {
        this.appStatus = status;
    }

    public Boolean getCompletedOnTime() {
        return completedOnTime;
    }

    public void setCompletedOnTime(Boolean completedOnTime) {
        this.completedOnTime = completedOnTime;
    }


}
