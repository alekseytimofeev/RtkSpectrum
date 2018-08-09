package widget.controllers;

import javafx.fxml.FXML;
import widget.Parentable;

public class MeasurementController implements Parentable {

    private RootController parent;

    @Override
    public void setParent(RootController parent) {
        this.parent = parent;
    }

    @FXML
    public void onClickCommandBD() {
        System.out.println("Command");
    }
}

