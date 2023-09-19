package ui.sceneController;

import dto.DtoQueueManagerInfo;
import dto.DtoResponse;
import dto.DtoResponsePreview;
import engine.MainEngine;
import interfaces.InterfaceMenu;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.sceneController.detailsController.DetailsController;
import ui.sceneController.newExecutionController.NewExecutionController;
import ui.sceneController.resultsController.ResultsController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SceneMenu implements Initializable {

    private InterfaceMenu interfaceMenu = null;
    private DetailsController detailsController;
    private NewExecutionController newExecutionController;
    private ResultsController resultsController;
    private Stage primaryStage;

    private Thread queueManager;

    @FXML private Tab tabOfDetails;
    @FXML private Tab tabOfNewExecution;
    @FXML private Tab tabOfResults;

    @FXML private Button ButtonLoadFile;
    @FXML private TextField textFilePath;
    @FXML
    private Label fileStatus;
    @FXML
    private TabPane tabPaneManager;

    @FXML
    private Label endedSimulation;
    private SimpleStringProperty endedSimulationCountProperty;

    @FXML
    private Label numberOfSimulationInProgress;
    private SimpleStringProperty numberOfSimulationInProgressProperty;

    @FXML
    private Label simulationInWaiting;
    private SimpleStringProperty  simulationInWaitingCountProperty;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    @FXML
    public void onClickButtonLoadFile(ActionEvent e){
        if (this.interfaceMenu == null){
            this.interfaceMenu = new MainEngine();
        }
        fileStatus.setText("File Status: ");
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("XML Files", "*.xml");
        fileChooser.getExtensionFilters().add(xmlFilter);
        File selectedFile;
        selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null){
            return;
        }

        clearAllComponemts();
        String absolutePath = selectedFile.getAbsolutePath();

        DtoResponse dtoResponse = interfaceMenu.createWorldDefinition(absolutePath);
        if (dtoResponse.isSucceeded())
        {
            DtoResponsePreview wrldDef = interfaceMenu.showCurrentSimulation();
            loadEverythingFromWorldDefinition(wrldDef);
            textFilePath.setText(absolutePath);
            fileStatus.setText(fileStatus.getText() + dtoResponse.getResponse());
        }else {
            fileStatus.setText(fileStatus.getText() + dtoResponse.getResponse());
        }
    }

    private void clearAllComponemts() {
        this.interfaceMenu.clearAllInformation();
        this.resultsController.clearScreen();
        this.newExecutionController.clearScreen();
        this.detailsController.clearScreen();
    }

    private void loadEverythingFromWorldDefinition(DtoResponsePreview wrldDef) {
        this.detailsController.loadFromWorldDef(wrldDef);
        this.detailsController.setSceneMenu(this);
        this.newExecutionController.loadFromWorldDef(wrldDef,interfaceMenu);
        this.newExecutionController.setSceneMenu(this);
        this.resultsController.loadFromWorldDef(interfaceMenu);
        this.resultsController.setSceneMenu(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.comboBoxSkins.getItems().addAll("Skin 1", "Skin 2", "Skin 3");
        // Load the FXML of the nested controller
        ScrollPane nestedControllersContainer;
        FXMLLoader loaderDetail = new FXMLLoader();
        URL mainFXML = getClass().getResource("/ui/javaFx/scenes/sceneDetails/Details.fxml");
        loaderDetail.setLocation(mainFXML);
        try {
            nestedControllersContainer = loaderDetail.load();
            this.tabOfDetails.setContent(nestedControllersContainer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.detailsController = loaderDetail.getController();
        FXMLLoader loaderNewExecution = new FXMLLoader();
        mainFXML = getClass().getResource("/ui/javaFx/scenes/sceneNewExecution/newExecution.fxml");
        loaderNewExecution.setLocation(mainFXML);
        try {
            nestedControllersContainer = loaderNewExecution.load();
            this.tabOfNewExecution.setContent(nestedControllersContainer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.newExecutionController = loaderNewExecution.getController();

        FXMLLoader loaderResults = new FXMLLoader();
        mainFXML = getClass().getResource("/ui/javaFx/scenes/sceneResults/Results.fxml");
        loaderResults.setLocation(mainFXML);
        try{
            nestedControllersContainer = loaderResults.load();
            this.tabOfResults.setContent(nestedControllersContainer);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.resultsController = loaderResults.getController();

        this.endedSimulationCountProperty = new SimpleStringProperty();
        this.endedSimulation.textProperty().bind(this.endedSimulationCountProperty);

        this.numberOfSimulationInProgressProperty = new SimpleStringProperty();
        this.numberOfSimulationInProgress.textProperty().bind(this.numberOfSimulationInProgressProperty);

        this.simulationInWaitingCountProperty = new SimpleStringProperty();
        this.simulationInWaiting.textProperty().bind(this.simulationInWaitingCountProperty);
        createDaemonThreadToQueueManager();
    }

    private void createDaemonThreadToQueueManager() {
        this.queueManager = new Thread(() -> {
            while(true){
                if (this.interfaceMenu != null){
                    Platform.runLater(()-> {
                        DtoQueueManagerInfo simulationsRunningStateInfo = this.interfaceMenu.getQueueManagerInfo();
                        if (simulationsRunningStateInfo != null){
                            this.endedSimulationCountProperty.setValue(simulationsRunningStateInfo.getCountOfSimulationEnded());
                            this.simulationInWaitingCountProperty.setValue(simulationsRunningStateInfo.getCountOfSimulationsPending());
                            this.numberOfSimulationInProgressProperty.setValue(simulationsRunningStateInfo.getCountOfSimulationInProgress());
                        }
                    });}
                try {
                    // Sleep for 1 second
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        this.queueManager.setDaemon(true);
        this.queueManager.start();
    }

    public void navigateToResultTab() {
        this.tabPaneManager.getSelectionModel().select(tabOfResults);
        new Thread(()->{
            while(this.interfaceMenu.getAllSimulations().getMapOfAllSimulations().size() >= 1) {
                Platform.runLater(()-> {
                    this.resultsController.fetchAllSimulations();
                });
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @FXML
    void onSelectedComboBoxSkins(ActionEvent event) {
        String selectedSkin = this.comboBoxSkins.getValue();
        switch(selectedSkin){
            case "Skin 1":
                switchCSS("/ui/cssDesign/homePageDesign.css",this.primaryStage.getScene());
                break;
            case "Skin 2":
                switchCSS("/ui/cssDesign/homePageDesign2.css",this.primaryStage.getScene());
                break;
            case "Skin 3":
                switchCSS("/ui/cssDesign/homePageDesign3.css",this.primaryStage.getScene());
                break;
        }
    }

    public void navigateToNewExecutionTab(int idOfSimulation){
        this.newExecutionController.loadSimulationDetailsAgain(idOfSimulation);
        this.tabPaneManager.getSelectionModel().select(tabOfNewExecution);
    }


}
