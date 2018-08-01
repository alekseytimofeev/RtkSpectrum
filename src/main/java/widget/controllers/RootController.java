package widget.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import widget.Widget.Parentable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    public String str = "1233";
    private List<Parentable> childs = new ArrayList<>();

    @FXML
    public Button btn;

    @FXML
    public Label lbl;

    @FXML
    private void onButtonClickWorkDB() {
        System.out.println("Spectr!!!!");
        lbl.setText("1111");

        //ExecutorBDcommands.addComand();
    }

    public RootController() {
        System.out.println("Widget RootController\t" + Thread.currentThread().getName());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Widget RootController initialize\t" + Thread.currentThread().getName());
    }

    public void addChild(Parentable child) {
        childs.add(child);
    }

}
