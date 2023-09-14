package ui.sceneController.resultsController;

import dto.DtoAllSimulationDetails;
import dto.DtoSimulationDetails;
import enums.SimulationState;
import interfaces.InterfaceMenu;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ui.presenter.CustomItemCell;
import ui.presenter.EntityPresenter;
import ui.presenter.SimulationPresenter;
import ui.sceneController.SceneMenu;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ResultsController implements Initializable {
    private SceneMenu sceneMenu;

    private SimulationPresenter currSimulationPresenter;
    private ObservableList<EntityPresenter> obsListEntities;
    private ObservableList<SimulationPresenter> obsListSimulations;
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
    private Label labelIdSimulation;

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
        this.obsListSimulations = FXCollections.observableArrayList();
        this.tableColumnEntity.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        this.tableColumnPopulation.setCellValueFactory(new PropertyValueFactory<>("population"));
        this.tableViewEntities.setItems(this.obsListEntities);
        this.listViewSimulations.setItems(this.obsListSimulations);
        this.listViewSimulations.setCellFactory(param -> new CustomItemCell());
    }

    public void setSceneMenu(SceneMenu sceneMenu) {
        this.sceneMenu = sceneMenu;
    }

    @FXML
    private void selectedItem(){
        this.currSimulationPresenter = this.listViewSimulations.getSelectionModel().getSelectedItem();
        Thread bringDetailsThread = new Thread(()->{
            while(this.currSimulationPresenter == this.listViewSimulations.getSelectionModel().getSelectedItem()){
                DtoSimulationDetails currentDetails = this.interfaceMenu.getSimulationById(this.currSimulationPresenter.getSimulationId());
                int population1 = currentDetails.getEntity1Population();
                int population2 = currentDetails.getEntity2Population();

                Platform.runLater(()-> {
                    this.labelCurrTick.setText(Integer.toString(currentDetails.getSimulationTick()));
                    this.labelCurrTimer.setText(Long.toString(currentDetails.getSimulationTimePassed()));
                    this.labelIdSimulation.setText(Integer.toString(currentDetails.getSimulationId()));
                    this.labelSimulationStatus.setText(getModeOfCurrentSimulation(currentDetails));
                    this.obsListEntities.clear();
                    this.obsListEntities.add(new EntityPresenter(currentDetails.getEntity1Name(), population1));
                    if(population2 != -1 && !currentDetails.getEntity2Name().equalsIgnoreCase("")){
                        this.obsListEntities.add(new EntityPresenter(currentDetails.getEntity2Name(), population2));
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        bringDetailsThread.start();
    }

    private static String getModeOfCurrentSimulation(DtoSimulationDetails currentDetails) {
        if (currentDetails.getIsSimulationFinished() == false)
            return "Running";
    else if (true)
        return "Finished";
    else
        return "waiting";
    }

    public void loadFromWorldDef(InterfaceMenu interfaceMenu) {
        this.interfaceMenu = interfaceMenu;
        //this.executionerManager = this.interfaceMenu.getExecutionsManager();
    }

    @FXML
    void ReRunSimulation(ActionEvent event) {
        this.sceneMenu.navigateToNewExecutionTab();
    }

    public void fetchAllSimulations() {
        //this.obsListSimulations.clear();
        DtoAllSimulationDetails allCurrentSimulations = this.interfaceMenu.getAllSimulations();
        Map<Integer, SimulationState> simulationToIsRunningMap = allCurrentSimulations.getMapOfAllSimulations();
        for(int currId: simulationToIsRunningMap.keySet()) {
            SimulationPresenter simulationToCheck = checkIfSimulationExists(currId);
            if (simulationToCheck == null) {
                SimulationPresenter currSimulationToAdd = new SimulationPresenter(currId, simulationToIsRunningMap.get(currId).toString());
                this.obsListSimulations.add(currSimulationToAdd);
            } else {
                if (simulationToIsRunningMap.get(currId).toString().equalsIgnoreCase(SimulationState.FINISHED.toString())) {
                    simulationToCheck.setSimulationState(SimulationState.FINISHED.toString());
                }
                else if (simulationToIsRunningMap.get(currId).toString().equalsIgnoreCase(SimulationState.WAITING.toString())) {
                    simulationToCheck.setSimulationState(SimulationState.WAITING.toString());
                }
                else if (simulationToIsRunningMap.get(currId).toString().equalsIgnoreCase(SimulationState.RUNNING.toString())) {
                    simulationToCheck.setSimulationState(SimulationState.RUNNING.toString());
                }
                this.obsListSimulations.set(currId - 1, simulationToCheck);
            }
        }
    }

    private SimulationPresenter checkIfSimulationExists(int currId) {
        for(SimulationPresenter currSimulation: this.obsListSimulations){
            if(currId == currSimulation.getSimulationId()){
                return currSimulation;
            }
        }
        return null;
    }

    public void onPausePressed(){
        if(this.currSimulationPresenter != null){
            this.interfaceMenu.pauseCurrentSimulation(currSimulationPresenter.getSimulationId());
        }
    }

    public void onResumePressed(){
        this.interfaceMenu.resumeCurretnSimulation(currSimulationPresenter.getSimulationId());
    }

    public void onStopPressed(){
        this.interfaceMenu.stopCurrentSimulation(currSimulationPresenter.getSimulationId());
    }


    public void clearScreen() {
        this.obsListEntities.clear();
        this.obsListSimulations.clear();
    }
}
