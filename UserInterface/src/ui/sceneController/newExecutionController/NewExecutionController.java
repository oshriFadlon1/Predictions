package ui.sceneController.newExecutionController;

import dto.DtoResponseEntities;
import dto.DtoResponsePreview;
import entity.EntityDefinition;
import environment.EnvironmentDefinition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import range.Range;
import world.WorldDefinition;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class NewExecutionController implements Initializable{

    @FXML
    private Label labelError;

    @FXML
    private Spinner<Integer> spinnerOfEntity1;
    @FXML
    private Spinner<Integer> spinnerOfEntity2;
    @FXML
    private Label entity1Label;
    @FXML
    private  Label entity2Label;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private TextField textFieldValue;
    @FXML
    private Label labelValue;





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelError.setText("Label error");
    }

    public void loadFromWorldDef(DtoResponsePreview worldDef){
        Map<String, EnvironmentDefinition> allEnv = worldDef.getDtoEnvironments().getEnvironmentDefinitions();
        TreeItem<String>mainRootItem = new TreeItem<>("Environments");
        treeView.setRoot(mainRootItem);
        for(String envName: allEnv.keySet()){
            TreeItem<String> rootItemEnv = new TreeItem<>(envName);
            String propType = allEnv.get(envName).getEnvPropertyDefinition().getPropertyType();
            TreeItem<String> rootItemType = new TreeItem<>(propType);
            Range rangeOfProp = allEnv.get(envName).getEnvPropertyDefinition().getPropertyRange();
            rootItemEnv.getChildren().add(rootItemType);
            if(rangeOfProp != null){
                TreeItem<String> rootItemRange = new TreeItem<>(allEnv.get(envName).getEnvPropertyDefinition().getPropertyRange().toString());
                rootItemEnv.getChildren().add(rootItemRange);
                TreeItem<String> rootForValue = new TreeItem<>("Please state your value in the text field");
                rootItemEnv.getChildren().add(rootForValue);
            }
            else{
                TreeItem<String> rootItemBoolString;
                if(propType.equalsIgnoreCase("string")){
                    rootItemBoolString = new TreeItem<>("Please enter a string as you wish in the text field");
                }
                else{
                    rootItemBoolString = new TreeItem<>("Please enter a value true/false in the text field");
                }

                rootItemEnv.getChildren().add(rootItemBoolString);
            }

            mainRootItem.getChildren().addAll(rootItemEnv);

        }
        List<DtoResponseEntities> entities = worldDef.getDtoResponseEntities();
        entity1Label.setText(entities.get(0).getEntityName());
        if(entities.size() == 2){
            entity2Label.setDisable(false);
            entity2Label.setText(entities.get(1).getEntityName());
            spinnerOfEntity2.setEditable(true);
        }
    }

    public void selectItem(){
        TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
        if(item != null){
            if(item.getValue().substring(0, 6).equalsIgnoreCase("please")){
                textFieldValue.setVisible(true);
                textFieldValue.setDisable(false);
                labelValue.setVisible(true);
            }
            else{
                textFieldValue.setVisible(false);
                textFieldValue.setDisable(true);
                labelValue.setVisible(false);
            }
        }
    }
}

