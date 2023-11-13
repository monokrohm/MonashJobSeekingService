module com.jssgui.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.jssgui.gui to javafx.fxml;
    exports com.jssgui.gui;
}