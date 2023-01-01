package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Parkinglot;
import javafx.collections.FXCollections;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;
	//public ParkingLotSkeletonController parkingLotSkeleton;
	private Controller controller;

	public SimpleClient(String host, int port) {
		super(host, port);
	}


	public void setController(Controller controller) {
		this.controller = controller;
	}

	public static void main(String[] args) {
	}

	@Override
	protected void handleMessageFromServer(Object msg) {     //function handles message from server
		try {
			switch (((LinkedList<Object>) msg).get(0).toString()) {       //switch with all command options sent between client and server
				case "#PULL_COMPLAINTS" -> pushComplaints((LinkedList<Object>) msg);
				case "#PULL_PARKINGLOTS" -> pushParkinglots((LinkedList<Object>) msg);

			}
		} catch (Exception e) {
			System.out.println(Arrays.toString(e.getStackTrace()));
			System.out.println(e.getMessage());
			System.out.println("Client Error");
			e.getStackTrace();
		}
	}
	private void pushComplaints(LinkedList<Object> msg) {
		ComplaintHandlerTableController tableController = (ComplaintHandlerTableController) controller;
		tableController.pullComplaints(FXCollections.observableArrayList(((List<Complaint>) msg.get(1))));
	}

	private void pushParkinglots(LinkedList<Object> msg) {
		ParkingListController tableController = (ParkingListController) controller;
		tableController.pullParkinglots(FXCollections.observableArrayList(((List<Parkinglot>) msg.get(1))));
	}
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}