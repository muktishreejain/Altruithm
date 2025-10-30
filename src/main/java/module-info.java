module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;


    opens com.example.demo1 to javafx.fxml, com.google.gson;
    exports com.example.demo1;
}