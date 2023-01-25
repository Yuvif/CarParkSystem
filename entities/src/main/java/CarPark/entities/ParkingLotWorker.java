//package CarPark.entities;
//import javax.persistence.*;
//
//@Entity
//@Table(name = "ParkingLotWorkers")
//public class ParkingLotWorker extends Employee {
//
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "parkinglot_parking_lot_id")
//    private Parkinglot parkinglot;
//
//
//    public ParkingLotWorker(int employeeId, String firstName, String lastName, String email, String workersRole, String
//                            password) {
//        super(employeeId, firstName, lastName, email, workersRole,password);
//    }
//
//    public ParkingLotWorker() {}
//
//    public Parkinglot getParkinglot() {
//        return parkinglot;
//    }
//
//    public void setParkinglot(Parkinglot parkinglot) {
//        this.parkinglot = parkinglot;
//    }
//    public Parkinglot getParkingLot() {
//        return parkinglot;
//    }
//    public void setParkingLot(Parkinglot parkingLot) {
//        this.parkinglot = parkingLot;
//    }
//
//
//}