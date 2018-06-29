package widget.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable
{
    @FXML
    public Button btn;

    @FXML
    public Label lbl;

    @FXML
    private void onButtonClickWorkDB() {
        System.out.println("Spectr!!!!");
        lbl.setText("1111");
        System.out.println("Thread name:" + Thread.currentThread().getName() + " id : " + Thread.currentThread().getId());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println( "initialize: " + btn);
        System.out.println( "location: " + location);
        System.out.println( "ResourceBundle: " + resources);
    }

    public RootController() {
        System.out.println("RootController: " + btn);
    }
}
