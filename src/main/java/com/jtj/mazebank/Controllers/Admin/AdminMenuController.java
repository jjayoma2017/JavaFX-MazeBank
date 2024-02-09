package com.jtj.mazebank.Controllers.Admin;

import com.jtj.mazebank.Models.Model;
import com.jtj.mazebank.Views.AdminMenuOptions;
import com.jtj.mazebank.Views.ClientMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button create_client_btn;
    public Button clients_btn;
    public Button deposit_btn;
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners(){
        create_client_btn.setOnAction(e-> onCreateClient());
        clients_btn.setOnAction(e->onClients());
        deposit_btn.setOnAction(e-> onDeposit());
        logout_btn.setOnAction(e->onLogout());
    }

    private void onDeposit() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.DEPOSITS);
    }

    private  void onCreateClient(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CREATE_CLIENTS);
    }

    private void onClients(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CLIENTS);
    }

    private void onLogout(){
        // get Stage
        Stage stage = (Stage) clients_btn.getScene().getWindow();
        // Close the client window
        Model.getInstance().getViewFactory().closeStage(stage);
        // Show Login Window
        Model.getInstance().getViewFactory().showLoginWindow();
        // Set login success back to false
        Model.getInstance().setAdminLoginSuccessFlag(false);
    }
}
