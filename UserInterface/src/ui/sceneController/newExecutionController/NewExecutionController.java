package ui.sceneController.newExecutionController;

import dto.DtoResponseEntities;
import dto.DtoResponsePreview;
import dto.DtoUiToEngine;
import environment.EnvironmentDefinition;
import interfaces.InterfaceMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import range.Range;
import world.WorldDefinition;

import java.net.URL;
import java.util.*;

public class NewExecutionController implements Initializable{

    private int population1 = 0;
    private int population2 = 0;
    private Map<String, Object> EnvToValue;
    private DtoResponsePreview worldPreview;
    private InterfaceMenu interfaceMenu = null;
    private TreeItem<String> currentSelectedItem;
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

    public void loadFromWorldDef(DtoResponsePreview worldDef,InterfaceMenu i_interfaceMenu){
        this.worldPreview = worldDef;
        this.EnvToValue = new HashMap<>();
        if (this.interfaceMenu == null){this.interfaceMenu = i_interfaceMenu;}
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

    private void setTextFiledToEnvProp(boolean value, boolean value1) {
        textFieldValue.setVisible(value);
        textFieldValue.setDisable(value1);
        labelValue.setVisible(value);
        buttonValue.setVisible(value);
        buttonValue.setDisable(value1);
        labelError.setText("");
    }

    public void checkValidationEnvironment(){
        String valueToCheck = textFieldValue.getText();
        String envName = this.currentSelectedItem.getParent().getValue();
        Map<String, EnvironmentDefinition> envDefs = this.worldPreview.getDtoEnvironments().getEnvironmentDefinitions();
        EnvironmentDefinition envDef = envDefs.get(envName);
        String envType = envDef.getEnvPropertyDefinition().getPropertyType();
        if(!isTypeValid(envType, valueToCheck)){
            labelError.setVisible(true);
            labelError.setText("Environment value is not matching to it's type");
            return;
        }
        if(envType.equalsIgnoreCase("float")){
            if(!isRangeValid(envDef.getEnvPropertyDefinition().getPropertyRange(), valueToCheck)){
                labelError.setVisible(true);
                labelError.setText("Environment value is not in range");
                return;
            }
            this.EnvToValue.put(envName,Float.parseFloat(valueToCheck));

        }
        else if (envType.equalsIgnoreCase("boolean")){
            this.EnvToValue.put(envName,Boolean.parseBoolean(valueToCheck));
        }
        else {
            this.EnvToValue.put(envName,valueToCheck);
        }
        SaveEnvPropValueSuccessfully(valueToCheck, envName);
    }

    private void SaveEnvPropValueSuccessfully(String valueToCheck, String envName) {
        labelError.setVisible(true);
        labelError.setText("Value " + valueToCheck + " was set to environment " + envName + " succesfully");
        textFieldValue.setText("");
    }

    private boolean isRangeValid(Range propertyRange, String valueToCheck) {
        float parsedVal = Float.parseFloat(valueToCheck);
        return parsedVal >= propertyRange.getFrom() && parsedVal <= propertyRange.getTo();
    }

    private boolean isTypeValid(String propertyType,String value) {
        if(propertyType.equalsIgnoreCase("boolean")){
            if(!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")){
                return false;
            }
        }
        else if(propertyType.equalsIgnoreCase("float")){
            if(!Utilities.isFloat(value)){
                return false;
            }
        }

        return true;
    }

    @FXML
    void OnClickSetValueOfPopulationEntity1(ActionEvent event) {
        String valueOfTextField = textFieldEntity1.getText();
        int sizeOfWorld = this.worldPreview.getWorldSize().getRow() * this.worldPreview.getWorldSize().getCol();
        if(!Utilities.isInteger(valueOfTextField)){
            labelErrorEntity1.setText("Invalid value for population. Please enter a number between 0 to" + (sizeOfWorld-this.population2));
            labelErrorEntity1.setVisible(true);
            return;
        }
        else {
            int parsedValue = Integer.parseInt(valueOfTextField);
            if(parsedValue > sizeOfWorld - this.population2){
                labelErrorEntity1.setText("Quantity of population cannot be bigger than actual world size. Please enter a number between 0 to" + (sizeOfWorld-this.population2));
                labelErrorEntity1.setVisible(true);
            }
            else{
                labelErrorEntity1.setVisible(false);
                this.population1 = parsedValue;
            }
        }
    }

    @FXML
    void OnClickSetValueOfPopulationEntity2(ActionEvent event) {
        String valueOfTextField = textFieldEntity2.getText();
        int sizeOfWorld = this.worldPreview.getWorldSize().getRow() * this.worldPreview.getWorldSize().getCol();
        if(!Utilities.isInteger(valueOfTextField)){
            labelErrorEntity2.setText("Invalid value for population. Please enter a number between 0 to" + (sizeOfWorld - this.population1));
            labelErrorEntity2.setVisible(true);
        }
        else {
            int parsedValue = Integer.parseInt(valueOfTextField);
            if(parsedValue > sizeOfWorld - this.population1){
                labelErrorEntity2.setText("Quantity of population cannot be bigger than actual world size. Please enter a number between 0 to" + (sizeOfWorld - this.population1));
                labelErrorEntity2.setVisible(true);
            }
            else{
                labelErrorEntity2.setVisible(false);
                this.population2 = parsedValue;
            }
        }
    }

    @FXML
    void OnClickedSetValueOfEnvProp(ActionEvent event) {
        checkValidationEnvironment();
    }

    @FXML
    void OnClickedClear(ActionEvent event) {
        this.EnvToValue.clear();
        this.population1 = 0;
        this.population2 = 0;
        labelError.setText("All chosen environment and population values were removed");
    }


    @FXML
    void OnClickedStartSimulation(ActionEvent event) {
        Object valueInitToCurrentEnv = null;
        for (String envName : this.worldPreview.getDtoEnvironments().getEnvironmentDefinitions().keySet()) {
            if(!this.EnvToValue.containsKey(envName)) {
                valueInitToCurrentEnv = this.interfaceMenu.initializeRandomEnvironmentValues(this.worldPreview
                        .getDtoEnvironments()
                        .getEnvironmentDefinitions()
                        .get(envName).getEnvPropertyDefinition());
                this.EnvToValue.put(envName,valueInitToCurrentEnv);
            }
        }
        this.interfaceMenu.runSimulations(new DtoUiToEngine(this.EnvToValue,this.population1,this.population2));
    }
}

