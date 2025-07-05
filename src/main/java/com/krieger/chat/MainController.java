package com.krieger.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    private HBox h_container;

    @FXML
    private HBox input_container;

    @FXML
    private ListView<Message> list_view;

    @FXML
    private Label notification_lbl;

    @FXML
    private Button send_btn;

    @FXML
    private TextField txt_input;

    @FXML
    private VBox v_container;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    @FXML
    void sendBtnClicked(ActionEvent event) {
        String userText = txt_input.getText();
        if (userText == null || userText.isEmpty()) return;
        out.println(userText);
        list_view.getItems().add(new Message(userText, Message.Type.USER));
        txt_input.clear();

        // Read server response in a background thread
        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    final String responseLine = line;
                    // Only add non-empty lines
                     if (!responseLine.trim().isEmpty()) { 
                        javafx.application.Platform.runLater(() -> {
                            list_view.getItems().add(new Message(responseLine, Message.Type.RESPONSE));
                        });
                    }
                    // Stop reading after "END" or connection close message
                    if ("END".equals(responseLine) || "Verbindung wird beendet.".equals(responseLine)) {
                        break;
                    }
                }
                if ("exit".equalsIgnoreCase(userText)) {
                    socket.close();
                }
            } catch (IOException e) {
                javafx.application.Platform.runLater(() ->
                    notification_lbl.setText("Fehler: " + e.getMessage())
                );
            }
        }).start();
    }

    public void initializeConnection() {
        new Thread(Server::startServer).start();
        try {
            Thread.sleep(500);
            socket = Client.establishConnection("localhost", 4999);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException | InterruptedException e) {
            notification_lbl.setText("Verbindung fehlgeschlagen: " + e.getMessage());
        }
    }

    @FXML
    void initialize() {
        list_view.setPlaceholder(new Label("nothing to show...\nstart a new chat"));
        list_view.setCellFactory(listView -> new ListCell<Message>() {
            private final Label label = new Label();
            private final HBox hbox = new HBox(label);
            {
                label.setWrapText(true);
                label.setStyle("-fx-padding: 8; -fx-font-size: 14px;");
                setPrefWidth(0);
                hbox.setFillHeight(true);
                hbox.setPrefWidth(0);
            }
            @Override
            protected void updateItem(Message item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item.getText());
                    label.setMaxWidth(list_view.getWidth() - 32);
                    if (item.getType() == Message.Type.RESPONSE) {
                        hbox.setStyle("-fx-alignment: CENTER-Left;");
                        label.setStyle("-fx-padding: 8; -fx-font-size: 14px; -fx-background-color: #dff9fb; -fx-background-radius: 8;");
                    } else {
                        hbox.setStyle("-fx-alignment: CENTER-Right;");
                        label.setStyle("-fx-padding: 8; -fx-font-size: 14px; -fx-background-color: #f6e58d; -fx-background-radius: 8;");
                    }
                    setGraphic(hbox);
                }
            }
        });
        tests();
        initializeConnection();
        txt_input.setOnAction(event -> sendBtnClicked(null));
    }

    public void tests() {
        assert h_container != null : "fx:id=\"h_container\" was not injected: check your FXML file 'main-view.fxml'.";
        assert input_container != null : "fx:id=\"input_container\" was not injected: check your FXML file 'main-view.fxml'.";
        assert list_view != null : "fx:id=\"list_view\" was not injected: check your FXML file 'main-view.fxml'.";
        assert notification_lbl != null : "fx:id=\"notification_lbl\" was not injected: check your FXML file 'main-view.fxml'.";
        assert send_btn != null : "fx:id=\"send_btn\" was not injected: check your FXML file 'main-view.fxml'.";
        assert txt_input != null : "fx:id=\"txt_input\" was not injected: check your FXML file 'main-view.fxml'.";
        assert v_container != null : "fx:id=\"v_container\" was not injected: check your FXML file 'main-view.fxml'.";
    }
}
