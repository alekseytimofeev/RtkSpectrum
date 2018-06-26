package widget.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWidgetController implements Initializable
{
    @FXML
    public Button btn;

    @FXML
    private void onButtonClickWorkDB() {
        System.out.println("Spectr!!!!");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println( "initialize: " + btn);
        System.out.println( "location: " + location);
        System.out.println( "ResourceBundle: " + resources);
    }

    public MainWidgetController() {
        System.out.println("MainWidgetController: " + btn);
    }
}
