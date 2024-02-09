package com.jtj.mazebank.Controllers.Admin;

import com.jtj.mazebank.Models.Client;
import com.jtj.mazebank.Models.Model;
import com.jtj.mazebank.Views.ClientCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    public ListView<Client> client_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initData();
        client_listview.setItems(Model.getInstance().getClients());
        client_listview.setCellFactory(e-> new ClientCellFactory());
    }

    private void initData(){
        if(Model.getInstance().getClients().isEmpty()){
            Model.getInstance().setClients();
        }
    }
}
