module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;  
    requires java.sql;   

    
    requires com.google.gson; 
    opens com.example.demo1 to javafx.fxml, com.google.gson;
    exports com.example.demo1;
}