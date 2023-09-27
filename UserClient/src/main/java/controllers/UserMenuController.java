package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserMenuController implements Initializable {
    private DetailsController detailsController;
    private RequestsController requestsController;
    private ResultsController resultsController;
    private NewExecutionController newExecutionController;
    @FXML
    private Tab tabOfDetails;
    @FXML private Tab tabOfNewExecution;
    @FXML private Tab tabOfResults;
    @FXML private Tab tabOfRequests;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ScrollPane nestedControllersContainer;
        FXMLLoader loaderDetail = new FXMLLoader();
        URL mainFXML = getClass().getResource("/scenes/details.fxml");
        loaderDetail.setLocation(mainFXML);
        try {
            nestedControllersContainer = loaderDetail.load();
            this.tabOfDetails.setContent(nestedControllersContainer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.detailsController = loaderDetail.getController();
        FXMLLoader loaderRequests = new FXMLLoader();
        mainFXML = getClass().getResource("/scenes/requests.fxml");
        loaderRequests.setLocation(mainFXML);
        try {
            nestedControllersContainer = loaderRequests.load();
            this.tabOfRequests.setContent(nestedControllersContainer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.requestsController = loaderRequests.getController();

        FXMLLoader loaderNewEeecution = new FXMLLoader();
        mainFXML = getClass().getResource("/scenes/newExecution.fxml");
        loaderNewEeecution.setLocation(mainFXML);
        try{
            nestedControllersContainer = loaderNewEeecution.load();
            this.tabOfNewExecution.setContent(nestedControllersContainer);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.newExecutionController = loaderNewEeecution.getController();

        FXMLLoader loaderResults = new FXMLLoader();
        mainFXML = getClass().getResource("/scenes/results.fxml");
        loaderResults.setLocation(mainFXML);
        try{
            nestedControllersContainer = loaderResults.load();
            this.tabOfResults.setContent(nestedControllersContainer);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.resultsController = loaderResults.getController();
    }
}
