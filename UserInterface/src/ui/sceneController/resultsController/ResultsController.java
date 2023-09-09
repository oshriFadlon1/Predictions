package ui.sceneController.resultsController;

import dto.DtoSimulationDetails;
import engine.MainEngine;
import interfaces.InterfaceMenu;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import simulationmanager.SimulationExecutionerManager;
import ui.presenter.EntityPresenter;
import ui.presenter.SimulationPresenter;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultsController implements Initializable {
    private SimulationExecutionerManager executionerManager;
    private SimulationPresenter currSimulationPresenter;
    private ObservableList<EntityPresenter> obsListEntities;
    private InterfaceMenu interfaceMenu;
    // TODO TASK THAT CALL main engine and get a map of integer boolean and by it know which simulation is running and which finished(first, learn how to do task lol XD)
    @FXML
    private Button buttonPause;

    @FXML
    private Button buttonResume;

    @FXML
    private Button buttonStop;

    @FXML
    private Label labelCurrTick;

    @FXML
    private Label labelCurrTimer;

    @FXML
    private Label labelSimulationId;

    @FXML
    private Label labelSimulationStatus;

    @FXML
    private ListView<SimulationPresenter> listViewSimulations;

    @FXML
    private TableColumn<EntityPresenter, String> tableColumnEntity;

    @FXML
    private TableColumn<EntityPresenter, Integer> tableColumnPopulation;

    @FXML
    private TableView<EntityPresenter> tableViewEntities;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.obsListEntities = FXCollections.observableArrayList();
        this.tableColumnEntity.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        this.tableColumnPopulation.setCellValueFactory(new PropertyValueFactory<>("population"));
        this.tableViewEntities.setItems(this.obsListEntities);
    }
    @FXML
    private void selectedItem(){
        this.currSimulationPresenter = this.listViewSimulations.getSelectionModel().getSelectedItem();
        Thread bringDetailsThread = new Thread(()->{
            while(this.currSimulationPresenter == this.listViewSimulations.getSelectionModel().getSelectedItem()){
                DtoSimulationDetails currentDetails = this.executionerManager.getSimulationById(this.currSimulationPresenter.getSimulationId());
                Platform.runLater(()-> {
                    this.labelCurrTick.setText(Integer.toString(currentDetails.getSimulationTick()));
                    this.labelCurrTimer.setText(Integer.toString(currentDetails.getSimulationTimePassed()));
                    //חסר לי משהו population
                });
                try {
                    Thread.sleep(200); // Sleep for 1 second (adjust as needed)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        bringDetailsThread.start();
    }

    public void loadFromWorldDef(InterfaceMenu interfaceMenu) {
        this.interfaceMenu = interfaceMenu;
        this.executionerManager = this.interfaceMenu.getExecutionsManager();
    }
}
