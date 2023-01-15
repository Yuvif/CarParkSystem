package CarPark.entities;

import com.sun.xml.bind.v2.model.core.ID;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parkinglots")
public class Parkinglot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false, length = 46)
    private String name;

//    @Column(name = "rows")
//    private Integer rows;

    //    @Column(name = "floors")
//    private Integer floors;
//
    @Column(name = "parks_per_row")
    private Integer parksPerRow;

    @Column(name = "total_parking_lots")
    private Integer totalParkingLots;

    public Parkinglot() {

    }

    public Parkinglot(String name, int parksPerRow, int total_parking_lots) {
        this.name = name;
        this.parksPerRow = parksPerRow;
        this.totalParkingLots = total_parking_lots;
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}