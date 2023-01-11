package CarPark.server.handlers;

import CarPark.entities.Price;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.PricesMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaQuery;
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
            case EDIT_PRICE:
                editPrice();
                class_message.response_type = PricesMessage.ResponseType.PRICE_EDITED;
                break;
        }
    }

    private void editPrice() throws Exception {
        Price old_price = session.get(Price.class, class_message.new_price.getId());
        old_price.setPrice(class_message.new_price.getPrice());
    }

    private List<Price> getPriceList() throws Exception {
        //generatePricesTable();
        CriteriaQuery<Price> query = cb.createQuery(Price.class);
        query.from(Price.class);
        List<Price> data = session.createQuery(query).getResultList();
        return data;
    }

    private void generatePricesTable() throws Exception {

        Price pr1 = new Price("Casual ordered parking", "Per hour", 7, 1, 1);
        session.save(pr1);
        session.flush();
        Price pr2 = new Price("Casual parking", "Per hour", 8, 1, 1);
        session.save(pr2);
        session.flush();
        Price pr3 = new Price("Monthly subscriber few cars", "Permanent price", 100, 1, 60);
        session.save(pr3);
        session.flush();
        Price pr4 = new Price("Monthly subscriber one car", "Permanent price", 120, 1, 54);
        session.save(pr4);
        session.flush();
        Price pr5 = new Price("Premium monthly subscriber", "Permanent price", 108, 1, 72);
        session.save(pr5);
        session.flush();
    }
}
