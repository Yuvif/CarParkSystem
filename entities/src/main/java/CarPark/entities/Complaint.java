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
    private Boolean appStatus = false;   //true - means that the complaint has been handled, false - otherwise
    private Boolean completedOnTime = false;
    private String pl_name;

    @ManyToOne
    private Parkinglot parkinglot;

    @ManyToOne
    private Customer customer;

    public Complaint(Date date, String text, Customer customer, String pl_name) {
        this.date = date;
        this.compText = text;
        this.customer =customer;
        this.pl_name = pl_name;
    }

    public Complaint() {}

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public String getPl_name() { return pl_name; }

    public void setPl_name(String pl_name) { this.pl_name = pl_name; }
}