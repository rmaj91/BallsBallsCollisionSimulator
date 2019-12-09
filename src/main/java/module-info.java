module com.rmaj91 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.rmaj91 to javafx.fxml;
    exports com.rmaj91;
}