package il.cshaifasweng.OCSFMediatorExample.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "complaints")
public class Complaint implements Serializable {    //only for customers
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "dateOfSubmit")
    private Date date;

    @Column(name = "description")
    private String compText;
    private Boolean appStatus = true;        // true= complaint filed successfully- yet to be inspected, false = complaint fulfilled
    private Boolean completedOnTime = false;;

    public Complaint(Date date, String compText) {
        this.date = date;
        this.compText = compText;
    }

    public Complaint() {

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
