package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    @FXML
    private Tab tabOfManagement;
    @FXML
    private Tab tabOfAllocations;
    @FXML
    private Tab tabOfExecutionsHistory;
    private Stage primaryStage;
    private AllocationsController allocationsController;
    private AdminManagementController adminManagementController;
    private ExecutionsHistoryController executionsHistoryController;
    private SimulationDetailsController simulationDetailsController;
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ScrollPane nestedControllersContainer;
        FXMLLoader loaderManagement = new FXMLLoader();
        URL mainFXML = getClass().getResource("/scenes/admin Management part.fxml");
        loaderManagement.setLocation(mainFXML);
        try {
            nestedControllersContainer = loaderManagement.load();
            this.tabOfManagement.setContent(nestedControllersContainer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.adminManagementController = loaderManagement.getController();
        FXMLLoader loaderAllocations = new FXMLLoader();
        mainFXML = getClass().getResource("/scenes/Allocations.fxml");
        loaderAllocations.setLocation(mainFXML);
        try {
            nestedControllersContainer = loaderAllocations.load();
            this.tabOfAllocations.setContent(nestedControllersContainer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.allocationsController = loaderAllocations.getController();

        FXMLLoader loaderExecutionsHistory = new FXMLLoader();
        mainFXML = getClass().getResource("/scenes/Executions History.fxml");
        loaderExecutionsHistory.setLocation(mainFXML);
        try{
            nestedControllersContainer = loaderExecutionsHistory.load();
            this.tabOfExecutionsHistory.setContent(nestedControllersContainer);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.executionsHistoryController = loaderExecutionsHistory.getController();
    }
}
