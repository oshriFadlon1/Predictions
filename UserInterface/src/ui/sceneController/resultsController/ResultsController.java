package ui.sceneController.resultsController;

import dto.DtoAllSimulationDetails;
import dto.DtoCountTickPopulation;
import dto.DtoHistogramInfo;
import dto.DtoSimulationDetails;
import enums.SimulationState;
import interfaces.InterfaceMenu;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import ui.presenter.CustomItemCell;
import ui.presenter.EntityPresenter;
import ui.presenter.HistogramPresenter;
import ui.presenter.SimulationPresenter;
import ui.sceneController.SceneMenu;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ResultsController implements Initializable {
    private SceneMenu sceneMenu;

    private SimulationPresenter currSimulationPresenter;
    private ObservableList<EntityPresenter> obsListEntities;
    private ObservableList<SimulationPresenter> obsListSimulations;
    private ObservableList<String> obsListEntityNames;
    private ObservableList<String> obsListPropertyNames;
    private ObservableList<HistogramPresenter> obsListHistogram;

    private InterfaceMenu interfaceMenu;
    //private ExecutorService bringDetailsThread;
    // TODO TASK THAT CALL main engine and get a map of integer boolean and by it know which simulation is running and which finished(first, learn how to do task lol XD)
    @FXML
    private Button buttonPause;

    @FXML
    private Button buttonResume;

    @FXML
    private Button buttonStop;
    @FXML
    private Button buttonRerun;

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

    @FXML
    private Label avgPropertyValue;

    @FXML
    private Label avgTickValue;

    @FXML
    private TableColumn<HistogramPresenter, Integer> columnCount;

    @FXML
    private TableColumn<HistogramPresenter, String> columnValue;

    @FXML
    private ComboBox<String> comboBoxEntityName;

    @FXML
    private ComboBox<String> comboBoxEntityProperty;

    @FXML
    private TableView<HistogramPresenter> tableViewHistogram;
    @FXML
    private HBox hboxFinalDetails;
    @FXML
    private BarChart<String, Integer> barchartPopulation;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.obsListEntities = FXCollections.observableArrayList();
        this.obsListSimulations = FXCollections.observableArrayList();
        this.obsListEntityNames = FXCollections.observableArrayList();
        this.obsListPropertyNames = FXCollections.observableArrayList();
        this.obsListHistogram = FXCollections.observableArrayList();
        this.tableColumnEntity.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        this.tableColumnPopulation.setCellValueFactory(new PropertyValueFactory<>("population"));
        this.columnValue.setCellValueFactory(new PropertyValueFactory<>("propertyValue"));
        this.columnCount.setCellValueFactory(new PropertyValueFactory<>("countOfProperty"));
        this.tableViewEntities.setItems(this.obsListEntities);
        this.listViewSimulations.setItems(this.obsListSimulations);
        this.tableViewHistogram.setItems(this.obsListHistogram);
        this.listViewSimulations.setCellFactory(param -> new CustomItemCell());
        this.comboBoxEntityName.setItems(this.obsListEntityNames);
        this.comboBoxEntityProperty.setItems(this.obsListPropertyNames);
        this.hboxFinalDetails.setVisible(false);
        this.buttonRerun.setDisable(true);
        this.buttonPause.setDisable(true);
        this.buttonResume.setDisable(true);
        this.buttonStop.setDisable(true);
    }

    public void setSceneMenu(SceneMenu sceneMenu) {
        this.sceneMenu = sceneMenu;
    }

    @FXML
    private void selectedItem(){
        this.obsListEntityNames.clear();
        this.obsListPropertyNames.clear();
        this.obsListHistogram.clear();
        this.comboBoxEntityProperty.setDisable(true);
        if(this.listViewSimulations.getSelectionModel().getSelectedItem() != null) {
            this.currSimulationPresenter = this.listViewSimulations.getSelectionModel().getSelectedItem();
            DtoSimulationDetails currentDetailsForSimulation = this.interfaceMenu.getSimulationById(this.currSimulationPresenter.getSimulationId());
            this.buttonRerun.setDisable(false);
            this.buttonPause.setDisable(false);
            this.buttonResume.setDisable(false);
            this.buttonStop.setDisable(false);
            if (!currentDetailsForSimulation.isSimulationFinished()) {
                this.hboxFinalDetails.setVisible(false);
                Thread bringDetailsThread = new Thread(() -> {
                    while (this.currSimulationPresenter == this.listViewSimulations.getSelectionModel().getSelectedItem()) {
                        Platform.runLater(this::presentSelectedSimulationInfo);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                bringDetailsThread.start();
            } else {
                handleSimulationAfterFinish(currentDetailsForSimulation);
                presentSelectedSimulationInfo();
            }
        }
    }

    private void presentSelectedSimulationInfo() {
        if (this.interfaceMenu.getNumberOfSimulation() > 0){
            DtoSimulationDetails currentDetails = this.interfaceMenu.getSimulationById(this.currSimulationPresenter.getSimulationId());
            int population1 = currentDetails.getEntity1Population();
            int population2 = currentDetails.getEntity2Population();


            this.labelCurrTick.setText(Integer.toString(currentDetails.getSimulationTick()));
            this.labelCurrTimer.setText(Long.toString(currentDetails.getSimulationTimePassed()));
            this.labelIdSimulation.setText(Integer.toString(currentDetails.getSimulationId()));
            this.labelSimulationStatus.setText(getModeOfCurrentSimulation(currentDetails));
            this.obsListEntities.clear();
            this.obsListEntities.add(new EntityPresenter(currentDetails.getEntity1Name(), population1));
            if (population2 != -1 && !currentDetails.getEntity2Name().equalsIgnoreCase("")) {
                this.obsListEntities.add(new EntityPresenter(currentDetails.getEntity2Name(), population2));
            }
            this.buttonRerun.setDisable(!currentDetails.isSimulationFinished());
        }
    }

    private void handleSimulationAfterFinish(DtoSimulationDetails currentDetailsForSimulation) {
        this.obsListEntityNames.add(currentDetailsForSimulation.getEntity1Name());
        if(!currentDetailsForSimulation.getEntity2Name().equalsIgnoreCase("")){
            this.obsListEntityNames.add(currentDetailsForSimulation.getEntity2Name());
        }
        this.hboxFinalDetails.setVisible(true);
        initializeBarChart();
    }

    @FXML
    void onSelectedComboBoxEntitiesItem(ActionEvent event) {
        this.obsListPropertyNames.clear();
        String selectedItem = this.comboBoxEntityName.getValue();
        if(selectedItem != null) {
            int simulationId = this.currSimulationPresenter.getSimulationId();
            this.comboBoxEntityProperty.setDisable(false);
            List<String> allPropertyNames = interfaceMenu.bringPropertiesByEntityName(simulationId, selectedItem);
            for(String name: allPropertyNames){
                this.obsListPropertyNames.add(name);
            }
        }
    }

    @FXML
    void onSelectedComboBoxPropertyItem(ActionEvent event) {
        this.obsListHistogram.clear();
        this.avgTickValue.setText("");
        this.avgPropertyValue.setText("");
        String selectedItem = this.comboBoxEntityProperty.getValue();
        if(selectedItem != null){
            int simulationId = this.currSimulationPresenter.getSimulationId();
            DtoHistogramInfo dtoHistogramInfo = this.interfaceMenu.fetchInfoOnChosenProperty(simulationId, this.comboBoxEntityName.getValue(), selectedItem);
            if(dtoHistogramInfo.getAvgInFinalPopulation() != -1) {
                this.avgPropertyValue.setText(String.valueOf(dtoHistogramInfo.getAvgInFinalPopulation()));
            }else {
                this.avgPropertyValue.setText("The property is not float value \n/ the population is 0.");
            }
            if (dtoHistogramInfo.getAvgChangeInTicks() != -1){
                this.avgTickValue.setText(String.valueOf(dtoHistogramInfo.getAvgChangeInTicks()));
            } else {
                this.avgTickValue.setText("The population is 0.");
            }

            for (String str : dtoHistogramInfo.getValue2Count().keySet()) {
                Integer countOfProperty = dtoHistogramInfo.getValue2Count().get(str);
                obsListHistogram.add(new HistogramPresenter(str, countOfProperty));
            }
        }
    }

    private void initializeBarChart() {
        this.barchartPopulation.getData().clear();
        List<DtoCountTickPopulation> countTickPopulationList = this.interfaceMenu.getSimulationListOfPopulationPerTick(this.currSimulationPresenter.getSimulationId());
        int ratio = countTickPopulationList.size() / 50;
        if (ratio <= 0){
            ratio = 1;
        }
        int count = 0;
        boolean isSecondEntityFound = false;
        XYChart.Series<String, Integer> seriesEntity1 = new XYChart.Series();
        XYChart.Series<String, Integer> seriesEntity2 = new XYChart.Series();
        for (DtoCountTickPopulation dtoCountTickPopulation : countTickPopulationList){
            if (count % ratio == 0 ){
                isSecondEntityFound = true;
                // we have 2 entities
                if (dtoCountTickPopulation.getPopulationEntity2() != -1){
                    seriesEntity1.setName(dtoCountTickPopulation.getEntity1Name());
                    seriesEntity1.getData().add(new XYChart.Data<>(String.valueOf(dtoCountTickPopulation.getCurrentTick()), dtoCountTickPopulation.getPopulationEntity1()));
                    seriesEntity2.setName(dtoCountTickPopulation.getEntity2Name());
                    seriesEntity2.getData().add(new XYChart.Data<>(String.valueOf(dtoCountTickPopulation.getCurrentTick()), dtoCountTickPopulation.getPopulationEntity2()));
                }
                else{
                    seriesEntity1.setName(dtoCountTickPopulation.getEntity1Name());
                    seriesEntity1.getData().add(new XYChart.Data<>(String.valueOf(dtoCountTickPopulation.getCurrentTick()), dtoCountTickPopulation.getPopulationEntity1()));
                }

            }
            count++;
        }
        if (isSecondEntityFound){
            this.barchartPopulation.getData().addAll(seriesEntity1, seriesEntity2);
        }
        else {
            this.barchartPopulation.getData().addAll(seriesEntity1);
        }
    }

    private static String getModeOfCurrentSimulation(DtoSimulationDetails currentDetails) {
        if (currentDetails.isSimulationFinished()){
            return "Finished";
        }
        else if (!currentDetails.isSimulationPaused()){
            return "Running";
        }
        else{
            return "Waiting";
        }
    }

    public void loadFromWorldDef(InterfaceMenu interfaceMenu) {
        this.interfaceMenu = interfaceMenu;
        //this.executionerManager = this.interfaceMenu.getExecutionsManager();
    }

    @FXML
    void ReRunSimulation(ActionEvent event) {
        this.sceneMenu.navigateToNewExecutionTab(this.currSimulationPresenter.getSimulationId());
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
        if(this.currSimulationPresenter != null){
            this.interfaceMenu.resumeCurretnSimulation(currSimulationPresenter.getSimulationId());
        }
    }

    public void onStopPressed(){
        if(this.currSimulationPresenter != null){
            this.interfaceMenu.stopCurrentSimulation(currSimulationPresenter.getSimulationId());
        }
    }


    public void clearScreen() {
        this.obsListEntities.clear();
        this.obsListSimulations.clear();
    }

    public void resetAllComponent() {
        this.labelCurrTick.setText("");
        this.labelCurrTimer.setText("");
        this.labelIdSimulation.setText("");
        this.labelSimulationStatus.setText("");
        this.hboxFinalDetails.setVisible(false);
        this.buttonRerun.setDisable(true);
        this.buttonPause.setDisable(true);
        this.buttonResume.setDisable(true);
        this.buttonStop.setDisable(true);
    }
}
