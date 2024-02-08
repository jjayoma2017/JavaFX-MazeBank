package com.jtj.mazebank.Controllers.Admin;

import com.jtj.mazebank.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane admin_parent;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().addListener((observableValue, oldVal, newVal) ->{
            // add switch statement
            switch (newVal){
                case CREATE_CLIENTS -> admin_parent.setCenter(Model.getInstance().getViewFactory().getCreateClientView());
            }

        } );
    }
}
