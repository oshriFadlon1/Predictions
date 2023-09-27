package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class AllocationsController implements Initializable {
    @FXML
    private TableColumn<?, ?> columnCurrentRuns;

    @FXML
    private TableColumn<?, ?> columnFinishedRuns;

    @FXML
    private TableColumn<?, ?> columnRequestId;

    @FXML
    private TableColumn<?, ?> columnRequestedRuns;

    @FXML
    private TableColumn<?, ?> columnSimulationName;

    @FXML
    private TableColumn<?, ?> columnSimulationStatus;

    @FXML
    private TableColumn<?, ?> columnTermination;

    @FXML
    private TableColumn<?, ?> columnUserName;

    @FXML
    private TableView<?> tableViewRequests;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
