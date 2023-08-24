package ui.sceneController.newExecutionController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NewExecutionController implements Initializable{
    @FXML
    private ListView<String> listViewEnvironments;


    @FXML
    private VBox vboxTextFields;

    @FXML
    private Label labelError;

    @FXML
    private List<TextField> textFields;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFields = new ArrayList<>();
        listViewEnvironments.getItems().add("one");
        listViewEnvironments.getItems().add("two");
        listViewEnvironments.getItems().add("three");
        for(int i = 0; i < listViewEnvironments.getItems().stream().count(); i++){
            TextField txtField = new TextField();
            textFields.add(txtField);
            vboxTextFields.getChildren().addAll(txtField);
            labelError.setText("Label error");
        }
    }



}
