module com.example.listscanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;


    opens com.example.listscanner to javafx.fxml;
    exports com.example.listscanner;
}