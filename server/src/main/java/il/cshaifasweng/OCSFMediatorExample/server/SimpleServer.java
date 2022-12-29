package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.DbConnect;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Parkinglot;
import il.cshaifasweng.OCSFMediatorExample.entities.Price;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


public class SimpleServer extends AbstractServer {
	ArrayList<Parkinglot> p_l = new ArrayList<>();
	ArrayList<Price> p_t = new ArrayList<>();
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	public SimpleServer(int port) {
		super(port);
	}
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) throws IOException, SQLException {

		String query;
		PreparedStatement preparedStatement;
		ResultSet rs;
		Connection con = DbConnect.getConnect();
		Message message = (Message) msg;
		String request = message.getMessage();
		switch (request) {
			case "add client":
				SubscribedClient connection = new SubscribedClient(client);
				SubscribersList.add(connection);
				break;
			//send parking table for new client
			case "get parking list":
				try {
					if (p_l.isEmpty()) {//Case of first parking table request, we create new instances
						query = "SELECT * FROM parkinglots";
						preparedStatement = con.prepareStatement(query);
						rs = preparedStatement.executeQuery();
						//Create new instance of every parking lot
						while (rs.next()) {
							p_l.add(new Parkinglot(
									rs.getString("name"),
									rs.getInt("parks_per_row"),
									rs.getInt("total_parking_lots")));
						}
					}
					message.setMessage(p_l);
					client.sendToClient(message);
				} catch (SQLException throwable) {
					throwable.printStackTrace();
				}
				break;
			//Create new instance of prices table
			case "get prices table":
				try {
					if (p_t.isEmpty()){//Case of first prices table request, we create new instances
						query = "SELECT * FROM prices";
						preparedStatement = con.prepareStatement(query);
						rs = preparedStatement.executeQuery();
						while (rs.next()) {
							p_t.add(new Price(rs.getString("parking_type"),
									rs.getString("payment_method"),
									rs.getInt("price"),
									rs.getInt("number_of_cars"),
									rs.getInt("hours_of_parking")));
						}
					}
					message.setMessage(p_t);
					client.sendToClient(message);
				} catch (SQLException throwable) {
					throwable.printStackTrace();
				}
				break;
			case "edit price":
			{ //Case we got a message from client to set new price for parking type
				Price new_price = (Price) message.getObj();
				query = "UPDATE prices " + " SET price = ?" + "WHERE (parking_type = ?)";
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, new_price.getPrice().toString());
				preparedStatement.setString(2, new_price.getId());
				preparedStatement.executeUpdate();
				message.setMessage("price updated");
				client.sendToClient(message);
				break;
			}
		}
	}

}
