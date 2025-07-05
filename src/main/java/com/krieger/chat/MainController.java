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
    private ListView<String> list_view;

    @FXML
    private Label notification_lbl;

    @FXML
    private Button send_btn;

    @FXML
    private TextField txt_input;

    @FXML
    private VBox v_container;

    private Socket server;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String response = null;

    @FXML
    void sendBtnClicked(ActionEvent event) {

        String command = txt_input.getText();
        if (command == null || command.isEmpty()) return;
        out.println(command);
        list_view.getItems().add("\n\tYou: \n"+command+"\n");
        response = Server.generateAiResponse(command);
        
        list_view.getItems().add("\n\tResponse: \n"+response+"\n");
        try {
            String line;
            while ((line = in.readLine()) != null && !line.equals("END")) {

                list_view.getItems().add(line);
                list_view.getItems().add("\n");
                if ("Verbindung wird beendet.".equals(line)) break;
            }
            if ("exit".equalsIgnoreCase(command)) {
                socket.close();
            }
        } catch (IOException e) {
            notification_lbl.setText("Fehler: " + e.getMessage());
        }
        txt_input.clear();
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


        list_view.setCellFactory((listView) -> new ListCell<String>() {

            private final Label label = new Label();
            {
                label.setWrapText(true);
                label.setStyle("-fx-padding: 8; -fx-font-size: 14px;");

                setPrefWidth(0);
            }

            @Override 
            protected void updateItem(String item, boolean empty){
                super.updateItem(item, empty);
                if(empty || item == null){
                    setGraphic(null);
                } else {
                    label.setText(item);
                    label.setMaxWidth(list_view.getWidth()-32);
                    setGraphic(label);
                }
            }
            
        });
        
        
        tests();
        initializeConnection();

        txt_input.setOnAction(event -> sendBtnClicked(null));


    }

    public void tests(){
        assert h_container != null : "fx:id=\"h_container\" was not injected: check your FXML file 'main-view.fxml'.";
        assert input_container != null : "fx:id=\"input_container\" was not injected: check your FXML file 'main-view.fxml'.";
        assert list_view != null : "fx:id=\"list_view\" was not injected: check your FXML file 'main-view.fxml'.";
        assert notification_lbl != null : "fx:id=\"notification_lbl\" was not injected: check your FXML file 'main-view.fxml'.";
        assert send_btn != null : "fx:id=\"send_btn\" was not injected: check your FXML file 'main-view.fxml'.";
        assert txt_input != null : "fx:id=\"txt_input\" was not injected: check your FXML file 'main-view.fxml'.";
        assert v_container != null : "fx:id=\"v_container\" was not injected: check your FXML file 'main-view.fxml'.";
    }



}
