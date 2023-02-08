package CarPark.entities.messages;

import CarPark.entities.Customer;
import CarPark.entities.Price;

import java.util.List;

public class PricesMessage extends Message {
    public RequestType request_type;
    public ResponseType response_type;
    public List<Price> priceList;
    public Price new_price;
    public Customer customer;
    public double price;


    public PricesMessage(MessageType message_type,RequestType requestType,Customer current_customer){
        super(message_type);
        this.request_type = requestType;
        this.customer = current_customer;
    }

    public PricesMessage(MessageType message_type, RequestType requestType) {
        super(message_type);
        this.request_type = requestType;
    }

    //case of edit price request
    public PricesMessage(MessageType message_type, RequestType requestType, Price new_price) {
        super(message_type);
        this.request_type = requestType;
        this.new_price = new_price;//edited price obj
    }

    public PricesMessage(MessageType message_type, ResponseType response_type) {
        super(message_type);
        this.response_type = response_type;
    }




    public enum RequestType {
        GET_CURRENT_BALANCE,
        GET_PRICES_TABLE,
        EDIT_PRICE
    }

    public enum ResponseType {
        SET_CURRENT_BALANCE,
        SET_PRICES_TABLE,
        PRICE_EDITED
    }
}
