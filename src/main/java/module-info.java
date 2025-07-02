module com.krieger.chat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.krieger.chat to javafx.fxml;
    exports com.krieger.chat;
}