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
    private Integer parksPerRow;

    @Column(name = "total_parking_lots")
    private Integer totalParkingLots;


    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "parkingSlotId")
    private List<ParkingSlot> parkingSlots;

    @OneToMany(mappedBy = "parkinglot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Complaint> complaints = new ArrayList<>();

//    @Column(name = "rows")
//    private Integer rows = 3;
//
//    @Column(name = "floors")
//    private Integer floors = 3;

    public Parkinglot() {

    }

    public Parkinglot(String name, int parksPerRow, int totalParkingLots) {
        this.name = name;
        this.parksPerRow = parksPerRow;
        this.totalParkingLots = totalParkingLots;
        this.parkingSlots = new ArrayList<ParkingSlot>();
        this.complaints = new ArrayList<Complaint>();

    }
    public Parkinglot(Parkinglot parkinglot) {
        this.name = parkinglot.name;
        this.parksPerRow = parkinglot.parksPerRow;
        this.totalParkingLots = parkinglot.totalParkingLots;
        this.parkingSlots = parkinglot.parkingSlots;
    }

    public List<ParkingSlot> getParkingSlots() {
        return parkingSlots;
    }
    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setParkingSlots(List<ParkingSlot> ParkedIns) {
        this.parkingSlots = ParkedIns;
    }


    public void addParkingSlots(ParkingSlot ParkedIn) {
        parkingSlots.add(ParkedIn);
    }
    public Integer getTotalParkingLots() {
        return totalParkingLots;
    }

    public void setTotalParkingLots(Integer totalParkingLots) {
        this.totalParkingLots = totalParkingLots;
    }

    public Integer getParksPerRow() {
        return parksPerRow;
    }

    public void setParksPerRow(Integer parksPerRow) {
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