module com.jtj.mazebank {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.jtj.mazebank to javafx.fxml;
    opens com.jtj.mazebank.Controllers to javafx.fxml;
    exports com.jtj.mazebank;
    exports com.jtj.mazebank.Controllers;
    exports com.jtj.mazebank.Controllers.Admin;
    exports com.jtj.mazebank.Controllers.Client;
    exports com.jtj.mazebank.Models;
    exports com.jtj.mazebank.Views;
}