package com.krieger.chat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private ListView<?> list_view;

    @FXML
    private Label notification_lbl;

    @FXML
    private Button send_btn;

    @FXML
    private TextField txt_input;

    @FXML
    private VBox v_container;

    @FXML
    void sendBtnClicked(ActionEvent event) {

    }


    @FXML
    void initialize() {
        tests();

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
