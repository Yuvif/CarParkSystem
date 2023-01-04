package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;
import org.hibernate.SessionFactory;

import org.hibernate.Session;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class SimpleServer extends AbstractServer {

	public SimpleServer(int port) {
		super(port);
	}

	private static <T> void addNewInstance(T obj) {

		SimpleChatServer.session.beginTransaction();
		SimpleChatServer.session.save(obj);
		SimpleChatServer.session.flush();
		SimpleChatServer.session.getTransaction().commit();
	}
//
//	ArrayList<Parkinglot> p_l = new ArrayList<>();
//	ArrayList<Price> p_t = new ArrayList<>();
//	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

	public static void main(String[] args) throws IOException {


		if (args.length != 1) {
			System.out.println("Required argument: <port>");
		} else {

			SimpleServer server = new SimpleServer(Integer.parseInt(args[0]));
			server.listen();
		}
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) throws IOException, SQLException {


		switch (((LinkedList<Object>) msg).get(0).toString()) {
//			case "add client":
//				SubscribedClient connection = new SubscribedClient(client);
////				SubscribersList.add(connection);
////				break;
//			//send parking table for new client
//////			case "get parking list":
//////				try {
//////					if (p_l.isEmpty()) {//Case of first parking table request, we create new instances
//////						query = "SELECT * FROM parkinglots";
////						preparedStatement = con.prepareStatement(query);
////						rs = preparedStatement.executeQuery();
////						//Create new instance of every parking lot
////						while (rs.next()) {
////							p_l.add(new Parkinglot(
////									rs.getString("name"),
////									rs.getInt("parks_per_row"),
////									rs.getInt("total_parking_lots")));
////						}
////					}
////					message.setMessage(p_l);
////					client.sendToClient(message);
//				} catch (SQLException throwable) {
//					throwable.printStackTrace();
//				}
//				break;
//			//Create new instance of prices table
//			case "get prices table":
//				try {
//					if (p_t.isEmpty()){//Case of first prices table request, we create new instances
//						query = "SELECT * FROM prices";
//						preparedStatement = con.prepareStatement(query);
//						rs = preparedStatement.executeQuery();
//						while (rs.next()) {
//							p_t.add(new Price(rs.getString("parking_type"),
//									rs.getString("payment_method"),
//									rs.getInt("price"),
//									rs.getInt("number_of_cars"),
//									rs.getInt("hours_of_parking")));
//						}
//					}
//					message.setMessage(p_t);
//					client.sendToClient(message);
//				} catch (SQLException throwable) {
//					throwable.printStackTrace();
//				}
//				break;
//			case "edit price":
//			{ //Case we got a message from client to set new price for parking type
//				Price new_price = (Price) message.getObj();
//				query = "UPDATE prices " + " SET price = ?" + "WHERE (parking_type = ?)";
//				preparedStatement = con.prepareStatement(query);
//				preparedStatement.setString(1, new_price.getPrice().toString());
//				preparedStatement.setString(2, new_price.getId());
//				preparedStatement.executeUpdate();
//				message.setMessage("price updated");
//				client.sendToClient(message);
//				break;
//			}
			case "#COMPLAINT" -> addComplaint((LinkedList<Object>) msg);
			case "#PULL_COMPLAINTS" -> pullOpenComplaints(client);
			case "#CREATE_ORDER" -> addOrder((LinkedList<Object>) msg);
			case "#PULL_ORDERS_OF_CLIENT" -> pullOrdersOfCustomer(client, (LinkedList<Object>) msg);
			case "#PULL_ORDERS_OF_PARKINGLOT" -> pullOrdersOfParkingLot(client, (LinkedList<Object>) msg);
		}
	}



	private void addComplaint(LinkedList<Object> msg) {
		addNewInstance((Complaint) msg.get(1));
	}

//	A function to add an order.
//	----- WE STILL NEED TO DECIDE HOW TO MANAGE ORDERS -----
//	AT THIS POINT ANY ORDER IS ADDED TO THE DATABASE
	private void addOrder(LinkedList<Object> msg) {
		addNewInstance((Order) msg.get(1));
	}
	private void pullOpenComplaints(ConnectionToClient client) throws IOException {
		List<Complaint> complaints = SimpleChatServer.getAllOpenComplaints();
		List<Object> msg = new LinkedList<>();
		msg.add("#PULL_COMPLAINTS");
		msg.add(complaints);
		client.sendToClient(msg);
	}

	private void pullOrdersOfCustomer(ConnectionToClient client, LinkedList<Object> msg) throws IOException {
		int customerId = (int) msg.get(1);
		List<Order> orders = SimpleChatServer.getAllOrdersOfCustomer(customerId);
		List<Object> msgToClient = new LinkedList<>();
		msgToClient.add("#PULL_ORDERS_OF_CUSTOMER");
		msgToClient.add(orders);
		client.sendToClient(msgToClient);
	}

	private void pullOrdersOfParkingLot(ConnectionToClient client, LinkedList<Object> msg) throws IOException {
		int parkingLotId = (int) msg.get(1);
		List<Order> orders = SimpleChatServer.getAllOrdersOfParkingLot(parkingLotId);
		List<Object> msgToClient = new LinkedList<>();
		msgToClient.add("#PULL_ORDERS_OF_PARKING_LOT");
		msgToClient.add(orders);
		client.sendToClient(msgToClient);
	}
}
