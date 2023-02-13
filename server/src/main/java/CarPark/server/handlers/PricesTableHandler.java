package CarPark.server.handlers;

import CarPark.entities.Customer;
import CarPark.entities.Parkinglot;
import CarPark.entities.Price;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.PricesMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

public class PricesTableHandler extends MessageHandler {

    private PricesMessage class_message;
    public PricesTableHandler(Message msg, Session session, ConnectionToClient client) {
        super(msg, session, client);
        this.class_message = (PricesMessage) this.message;
    }

    public void setClass_message(Message msg) {
        this.class_message = (PricesMessage) msg;
    }

    @Override
    public void handleMessage() throws Exception {
        switch (class_message.request_type) {
            case GET_PRICES_TABLE:
                class_message.priceList = getPriceList();
                class_message.response_type = PricesMessage.ResponseType.SET_PRICES_TABLE;
                break;
            case EDIT_PRICE://Change requested entity modified price and wait for approve
                changeRequest();
                break;
            case GET_CURRENT_BALANCE:
                getCustomerBalance();
                break;
            case APPROVE_PRICE: //Approve price edit request
                editPrice();
                break;
            case GET_REQUESTS_TABLE:
                class_message.priceList = newPrices();
                class_message.response_type = PricesMessage.ResponseType.SET_REQUESTS_TABLE;
        }
    }


    private void editPrice() throws Exception
    {
        Price old_price = session.get(Price.class, class_message.new_price.getId());
        old_price.setPrice(class_message.new_price.getNewPrice());
        class_message.response_type = PricesMessage.ResponseType.PRICE_EDITED;
    }

    private void changeRequest() throws Exception
    {
        Price old_price = session.get(Price.class, class_message.new_price.getId());
        old_price.setNewPrice(class_message.new_price.getPrice());
        class_message.response_type = PricesMessage.ResponseType.WAITING_FOR_APPROVEMENT;
    }

    private List<Price> newPrices() throws Exception{
        CriteriaQuery<Price> query = cb.createQuery(Price.class);
        query.from(Price.class);
        List<Price> data = session.createQuery(query).getResultList();
        List<Price> prices=new ArrayList<>();;
        for (Price price : data)
        {
            if (!price.getPrice().equals(price.getNewPrice()))
                prices.add(price);
        }
        return prices;
    }

    private List<Price> getPriceList() throws Exception {
        CriteriaQuery<Price> query = cb.createQuery(Price.class);
        query.from(Price.class);
        List<Price> data = session.createQuery(query).getResultList();
        List<Price> prices=new ArrayList<>();;
        for (Price price : data)
        {
            if (price.getParkinglot().getName().equals(class_message.parkingLot))
                prices.add(price);
        }
        return prices;
    }
    private void getCustomerBalance(){
        Customer customer = session.get(Customer.class,class_message.customer.getId());
        class_message.price = customer.getBalance();
        class_message.response_type = PricesMessage.ResponseType.SET_CURRENT_BALANCE;
    }

    private void generatePricesTable() throws Exception {
        CriteriaQuery<Parkinglot> query = cb.createQuery(Parkinglot.class);
        query.from(Parkinglot.class);
        List<Parkinglot> p_l = session.createQuery(query).getResultList();
        int i;
        for (i = 0;i < 5;i++) {
            Price pr1 = new Price(p_l.get(i),"Casual ordered parking", "Per hour", 7, 1, 1);
            session.save(pr1);
            session.flush();
            Price pr2 = new Price(p_l.get(i),"Casual parking", "Per hour", 8, 1, 1);
            session.save(pr2);
            session.flush();
            Price pr3 = new Price(p_l.get(i),"Monthly subscriber few cars", "Permanent price", 100, 1, 60);
            session.save(pr3);
            session.flush();
            Price pr4 = new Price(p_l.get(i),"Monthly subscriber one car", "Permanent price", 120, 1, 54);
            session.save(pr4);
            session.flush();
            Price pr5 = new Price(p_l.get(i),"Premium monthly subscriber", "Permanent price", 108, 1, 72);
            session.save(pr5);
            session.flush();
        }
    }
}
