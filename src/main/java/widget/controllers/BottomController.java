package widget.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import widget.Parentable;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class BottomController implements Parentable, Initializable {

    private RootController parent;


    @FXML
    TableColumn columnTime;

    @FXML
    TableColumn columnDirection;

    @FXML
    TableColumn columnCommand;

    @FXML
    TableColumn columnData;

    @FXML
    TableView table;


    public class Log {
        public String time;
        public String direction;
        public String command;
        public String data;

        public Log(String time, String direction, String command, String data) {
            this.time = time;
            this.direction = direction;
            this.command = command;
            this.data = data;
        }

        public String getTime()
        {
            return time;
        }

        public void setTime(String time)
        {
            this.time = time;
        }

        public String getDirection()
        {
            return direction;
        }

        public void setDirection(String direction)
        {
            this.direction = direction;
        }

        public String getCommand()
        {
            return command;
        }

        public void setCommand(String command)
        {
            this.command = command;
        }

        public String getData()
        {
            return data;
        }

        public void setData(String data)
        {
            this.data = data;
        }
    }




    public void addLod(Log log) {
        hotels.add(0, log);
    }

    ObservableList<Log> hotels = FXCollections.observableList(new LinkedList<>());

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        table.setItems(hotels);

        columnTime.setCellValueFactory(new PropertyValueFactory<Log, String>("time"));
        columnDirection.setCellValueFactory(new PropertyValueFactory<Log, String>("direction"));
        columnCommand.setCellValueFactory(new PropertyValueFactory<Log, String>("command"));
        columnData.setCellValueFactory(new PropertyValueFactory<Log, String>("data"));
    }

    @Override
    public void setParent(RootController parent) {
        this.parent = parent;
    }

}
