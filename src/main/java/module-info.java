module ba.edu.ibu.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens ba.edu.ibu.javafx to javafx.fxml;
    exports ba.edu.ibu.javafx;
}