package CarPark.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "parkinglots")
@Entity
public class Parkinglot implements Serializable {
    @Id
    @Column(name = "name", nullable = false, length = 46)
    private String id;

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
        this.id = name;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}