package ui.sceneController;

import dto.DtoResponse;
import dto.DtoResponsePreview;
import engine.MainEngine;
import exceptions.GeneralException;
import interfaces.InterfaceMenu;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.sceneController.detailsController.DetailsController;
import ui.sceneController.newExecutionController.NewExecutionController;
import ui.sceneController.resultsController.ResultsController;
import world.WorldDefinition;
import xmlParser.XmlParser;

import javax.xml.bind.JAXBException;
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


    @FXML private Tab tabOfDetails;
    @FXML private Tab tabOfNewExecution;
    @FXML private Tab tabOfResults;

    @FXML private Button ButtonLoadFile;
    @FXML private TextField textFilePath;
    @FXML
    private Label fileStatus;
    @FXML
    private TabPane tabPaneManager;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void onClickButtonLoadFile(ActionEvent e){
        if (this.interfaceMenu == null){
            this.interfaceMenu = new MainEngine();
        }
        fileStatus.setText("File Status: ");
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("XML Files", "*.xml");
        fileChooser.getExtensionFilters().add(xmlFilter);
        File selectedFile;
        do {
            selectedFile = fileChooser.showOpenDialog(primaryStage);
        }while (selectedFile == null);

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

        //this.tabPaneManager.getTabs().addAll(this.tabOfDetails,this.tabOfNewExecution,this.tabOfResults);
    }

    public void navigateToResultTab() {
        this.tabPaneManager.getSelectionModel().select(tabOfResults);
    }

    public void navigateToNewExecutionTab(){

    }
}
