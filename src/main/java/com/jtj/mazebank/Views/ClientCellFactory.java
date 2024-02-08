package com.jtj.mazebank.Views;

import com.jtj.mazebank.Controllers.Admin.ClientCellController;
import com.jtj.mazebank.Models.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class ClientCellFactory extends ListCell<Client> {
    @Override
    protected void updateItem(Client client, boolean empty) {
        super.updateItem(client, empty);
        if(empty){
            setText(null);
            setGraphic(null);
        }
        else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ClientCell.fxml"));
            ClientCellController clientCellController = new ClientCellController(client);
            loader.setController(clientCellController);
            setText(null);
            try {
                setGraphic(loader.load());
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
}
