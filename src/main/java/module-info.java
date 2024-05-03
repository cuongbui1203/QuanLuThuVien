module com.library.library {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires bcrypt;

    opens com.java.library to javafx.fxml;
    opens com.java.library.fxml to javafx.fxml;
    opens com.java.library.controller to javafx.fxml;
    opens com.java.library.models to javafx.base;
    exports com.java.library;
}