module com.thales.ajc.projet {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.gluonhq.connect;

    opens com.thales.ajc.projet to javafx.fxml;
    exports com.thales.ajc.projet;
}