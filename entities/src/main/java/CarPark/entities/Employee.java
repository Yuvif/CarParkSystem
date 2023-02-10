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

    @ManyToOne
    @JoinColumn(name = "complaint_2_inspect_id")
    private Complaint complaint2Inspect = null;

    public Complaint getComplaint2Inspect() {
        return complaint2Inspect;
    }
    public void setComplaint2Inspect(Complaint complaint){
        complaint2Inspect = complaint;
    }


    public Employee(long employeeId, String firstName, String lastName, String email, String workersRole,String password) {
        super(employeeId,password,email,firstName,lastName);
        this.workersRole = workersRole;
    }

    public Employee() {
    }

//    @Override
//    public void setComplaintToInspect(Complaint complaint) {
//
//    }


    public String getWorkersRole() {
        return workersRole;
    }

    public void setWorkersRole(String workersRole) {
        this.workersRole = workersRole;
    }

}