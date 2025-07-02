module com.krieger.chat {
    requires javafx.controls;
    requires javafx.fxml;
    requires google.genai; // Often a transitive dependency for Vertex AI, good to include explicitly if issues arise

    opens com.krieger.chat to javafx.fxml;
    exports com.krieger.chat;
}