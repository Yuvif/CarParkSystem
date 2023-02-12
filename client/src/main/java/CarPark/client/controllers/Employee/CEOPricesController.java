package CarPark.client.controllers.Employee;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.CEO;
import CarPark.entities.Price;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.PricesMessage;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;

public class CEOPricesController {
    @FXML
    private TableColumn<Price, String> typeCol;
    @FXML
    private TableColumn<Price, Integer> pricesCol;
    @FXML
    private TableColumn<Price,String> plCol;

    @FXML
    private TableView<Price> pricesTable;
    @FXML
    private TableColumn<Price, Void> approveCol;

    @FXML
    void initialize () throws IOException{
        typeCol.setCellValueFactory(new PropertyValueFactory<>("parkingType"));
        pricesCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        plCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getParkinglot().getName()));
        CEO ceo = (CEO)SimpleClient.getCurrent_user();
        pricesTable.setItems(FXCollections.observableArrayList(ceo.getChangeRequests()));
        addButtonToTable();
    }

    //add cancel button to every order
    private void addButtonToTable() {
        approveCol = new TableColumn("");
        Callback<TableColumn<Price, Void>, TableCell<Price, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Price, Void> call(final TableColumn<Price, Void> param) {
                final TableCell<Price, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Approve");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Price price = getTableRow().getItem();
                            PricesMessage msg = new PricesMessage(Message.MessageType.REQUEST,
                                    PricesMessage.RequestType.APPROVE_PRICE, price);
                            try {
                                SimpleClient.getClient().sendToServer(msg);
                                pricesTable.getItems().remove(getTableRow().getItem()); //remove the order from table
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                            btn.getStyleClass().add("assets/style.css/myButton");
                        }
                    }
                };
                return cell;
            }
        };

        approveCol.setCellFactory(cellFactory);
        pricesTable.getColumns().add(approveCol);
    }

    @FXML
    private void back(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("CEOPage");
    }
}
