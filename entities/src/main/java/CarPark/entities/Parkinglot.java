package CarPark.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "parkinglots")
@Entity
public class Parkinglot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int parkingLotId;

    @Column(name = "name", nullable = false, length = 46)
    private String name;

    @Column(name = "parks_per_row")
    private int parksPerRow;

    @Column(name = "total_parking_slots")
    private int totalParkingSlots;


    @OneToMany(mappedBy = "parkinglot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Complaint> complaints = new ArrayList<>();

    @OneToMany
    private List<ParkingSlot> parkingSlot;

    @OneToMany
    private List<CheckedIn> checkedIns;

    public Parkinglot(String parkingLotName) {

    }


    public List<ParkingSlot> getParkingSlot() {
        return parkingSlot;
    }

    public void setParkingSlot(List<ParkingSlot> parkingSlot) {
        this.parkingSlot = parkingSlot;
    }
//    @Column(name = "rows")
//    private Integer rows = 3;
//
//    @Column(name = "floors")
//    private Integer floors = 3;

    public Parkinglot() {
    }

    public Parkinglot(String name, int parksPerRow, int total_parking_lots) {
        this.name = name;
        this.parksPerRow = parksPerRow;
        this.totalParkingSlots = total_parking_lots;
        this.complaints = new ArrayList<Complaint>();

    }


    public List<Complaint> getComplaints() {
        return complaints;
    }


    public String getName(){return name;}

    public int getTotalParkingSlots() {
        return totalParkingSlots;
    }

    public void setTotalParkingLots(int totalParkingSlots) {
        this.totalParkingSlots = totalParkingSlots;
    }

    public int getParksPerRow() {
        return parksPerRow;
    }

    public void setParksPerRow(int parksPerRow) {
        this.parksPerRow = parksPerRow;
    }

//    public Integer getFloors() {
//        return floors;
//    }
//
//    public void setFloors(Integer floors) {
//        this.floors = floors;
//    }
//
//    public Integer getRows() {
//        return rows;
//    }
//
//    public void setRows(Integer rows) {
//        this.rows = rows;
//    }

    public String getId() {
        return name;
    }

    public void setId(String id) {
        this.name = id;
    }

    public int getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(int parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

}