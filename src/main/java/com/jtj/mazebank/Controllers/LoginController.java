package com.jtj.mazebank.Controllers;

import com.jtj.mazebank.Models.Model;
import com.jtj.mazebank.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox<AccountType> acc_selector;
    public Label payee_address_lbl;
    public TextField payee_address_fld;
    public Label password_lbl;
    public TextField password_fld;
    public Button login_btn;
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        acc_selector.valueProperty().addListener(e-> Model.getInstance().getViewFactory().setLoginAccountType(acc_selector.getValue()));
        login_btn.setOnAction(event-> onLogin());
    }

    private void onLogin(){
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        if(Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT){
            // Evaluate Client login credentials
            Model.getInstance().evaluateClientCred(payee_address_fld.getText(),password_fld.getText());
            if(Model.getInstance().getClientLoginSuccessFlag()){
                Model.getInstance().getViewFactory().showClientWindow();
                // Close the login stage
                Model.getInstance().getViewFactory().closeStage(stage);
            }
            else {
                password_fld.setText("");
                payee_address_fld.setText("");
                error_lbl.setText("No Such Login Credentials.");
            }
        }
        else {
            Model.getInstance().getViewFactory().showAdminWindow();
        }
    }
}
