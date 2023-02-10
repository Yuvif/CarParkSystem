package CarPark.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "complaints")
public class Complaint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int complaintId;
    @Column(name = "dateOfSubmit")
    private Date date;

    @Column(name = "description")
    private String compText;
    private Boolean appStatus = false;        // true= complaint filed successfully- yet to be inspected, false = complaint fulfilled
    private Boolean completedOnTime = false;

    @ManyToOne
    private Parkinglot parkinglot;

    @ManyToOne
    private Customer customer;


    public Customer getCustomer() {
        return customer;
    }


    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Complaint(Date date, String text, Customer customer) {
        this.date = date;
        this.compText = text;
        this.customer =customer;
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


    public Date getDate() {
        return date;
    }

    public Boolean getAppStatus() {
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

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public int getComplaintId() {
        return complaintId;
    }
}