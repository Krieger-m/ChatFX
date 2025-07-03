module com.krieger.chat {
    requires javafx.controls;
    requires javafx.fxml;
    requires google.genai; // Often a transitive dependency for Vertex AI, good to include explicitly if issues arise

    opens com.krieger.chat to javafx.fxml;
    exports com.krieger.chat;
    exports com.krieger.chat.geminiTest;
    opens com.krieger.chat.geminiTest to javafx.fxml;
}