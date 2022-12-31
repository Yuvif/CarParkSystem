package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Memberships")

public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int membershipId;
    private int customerId;
    private int carId;
    private boolean type;
    private int routineParkingLot;
    private LocalTime routineLeavingHour;
    private LocalDate startDate;

    public Membership(int membershipId, int customerId, int carId, boolean type, int routineParkingLot,
                      LocalTime routineLeavingHour, LocalDate startDate)

    {
        super();
        this.membershipId = membershipId;
        this.customerId = customerId;
        this.carId = carId;
        this.type = type;
        this.routineParkingLot = routineParkingLot;
        this.routineLeavingHour = routineLeavingHour;
        this.startDate = startDate;
    }

    public Membership() {}

    public long getMembershipId()
    {
        return membershipId;
    }
    public void setMembershipId(int membershipId)
    {
        this.membershipId = membershipId;
    }

    public long getCustomerId()
    {
        return customerId;
    }
    public void setCustomerId(int customerId)
    {
        this.customerId = customerId;
    }

    public long getCarId()
    {
        return carId;
    }
    public void setCarId(int carId)
    {
        this.carId = carId;
    }

    public Boolean getType()
    {
        return type;
    }
    public void setType(Boolean type)
    {
        this.type = type;
    }

    public LocalTime getRoutineLeavingHour()
    {
        return routineLeavingHour;
    }
    public void setRoutineLeavingHour(LocalTime routineLeavingHour)
    {
        this.routineLeavingHour = routineLeavingHour;
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }
    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }

    public int getId()
    {
        return id;
    }
}