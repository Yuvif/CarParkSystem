package CarPark.entities;

import javax.persistence.*;

@Entity
@Table(name = "Employees")
@AttributeOverrides({
        @AttributeOverride(name = "userId", column = @Column(name = "employeeId"))
})
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class Employee extends User {
    private String workersRole;
    @OneToOne
    @JoinColumn(name = "complaint_to_inspect_complaint_id")
    private Complaint complaintToInspect= null;

    public Complaint getComplaintToInspect() {
        return complaintToInspect;
    }

    public void setComplaintToInspect(Complaint complaintToInspect) {
        this.complaintToInspect = complaintToInspect;
    }


    public Employee(long employeeId, String firstName, String lastName, String email, String workersRole,String password) {
        super(employeeId,password,email,firstName,lastName);
        this.workersRole = workersRole;
    }

    public Employee() {
    }



    public String getWorkersRole() {
        return workersRole;
    }

    public void setWorkersRole(String workersRole) {
        this.workersRole = workersRole;
    }

}