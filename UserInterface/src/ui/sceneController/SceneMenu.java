package ui.sceneController;

import exceptions.GeneralException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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


    private DetailsController detailsController;
    private NewExecutionController newExecutionController;
    private ResultsController resultsController;
    private Stage primaryStage;

    @FXML private Button ButtonLoadFile;
    @FXML private TextField textFilePath;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void onClickButtonLoadFile(ActionEvent e){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("XML Files", "*.xml");
        fileChooser.getExtensionFilters().add(xmlFilter);
        File selectedFile;
        do {
            selectedFile = fileChooser.showOpenDialog(primaryStage);
        }while (selectedFile == null);

        String absolutePath = selectedFile.getAbsolutePath();
        textFilePath.setText(absolutePath);
        XmlParser xmlParser = new XmlParser(absolutePath);
        try {
            WorldDefinition wrldDef = xmlParser.tryToReadXml();
            loadEverythingFromWorldDefinition(wrldDef);
        }
        catch(GeneralException | JAXBException | IOException ex){

        }
    }

    private void loadEverythingFromWorldDefinition(WorldDefinition wrldDef) {
        this.detailsController.loadFromWorldDef(wrldDef);
        this.newExecutionController.loadFromWorldDef(wrldDef);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        detailsController = loaderDetail.getController();
        FXMLLoader loaderNewExecution = new FXMLLoader();
        mainFXML = getClass().getResource("/ui/javaFx/scenes/sceneNewExecution/newExecution.fxml");
        loaderNewExecution.setLocation(mainFXML);
        try {
            nestedControllersContainer = loaderNewExecution.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newExecutionController = loaderNewExecution.getController();
    }
}
