package ui.sceneController.newExecutionController;

import dto.DtoResponseEntities;
import dto.DtoResponsePreview;
import dto.DtoUiToEngine;
import entity.EntityDefinition;
import enums.Type;
import environment.EnvironmentDefinition;
import environment.EnvironmentInstance;
import interfaces.InterfaceMenu;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import range.Range;
import termination.Termination;
import ui.javaFx.scenes.sceneNewExecution.tableViewModel;
import ui.presenter.EntityPresenter;
import ui.presenter.EnvironmentPresenter;
import ui.sceneController.SceneMenu;
import utility.Utilities;
import world.GeneralInformation;
import world.WorldDefinition;
import world.WorldInstance;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class NewExecutionController implements Initializable{

    private int population1 = 0;
    private int population2 = 0;
    private Map<String, Object> EnvToValue;
    private DtoResponsePreview worldPreview;
    private InterfaceMenu interfaceMenu = null;
    private EnvironmentPresenter currentSelectedItem;
    private ObservableList<EntityPresenter> obsListEntities;
    private ObservableList<EnvironmentPresenter> obsListEnvironments;
    private ObservableList<EnvironmentPresenter> obsListEnvironmentsBefore;
    private SceneMenu sceneMenu;
    @FXML
    private Label labelError;
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
    @FXML
    private Button buttonValue;
    @FXML
    private Label labelErrorEntity1;
    @FXML
    private Label labelErrorEntity2;
    @FXML
    private Button buttonValueEntity1;
    @FXML
    private Button buttonValueEntity2;
    @FXML
    private TextField textFieldEntity1;
    @FXML
    private TextField textFieldEntity2;
    @FXML
    private Label subTitleEntity2;
    @FXML
    private TableView<EntityPresenter> tableEntities;
    @FXML
    private TableView<EnvironmentPresenter> tableEnvironmentsPresenter;
    @FXML
    private TableView<EnvironmentPresenter> tableEnvironments;
    @FXML
    private TableColumn<EnvironmentPresenter, Object> valueColumn;
    @FXML
    private TableColumn<EntityPresenter, Integer> populationColumn;
    @FXML
    private TableColumn<EntityPresenter, String> entityColumn;
    @FXML
    private TableColumn<EnvironmentPresenter, String> environmentColumn;
    @FXML
    private TableColumn<EnvironmentPresenter, String> envNameCol;
    @FXML
    private TableColumn<EnvironmentPresenter, Range> envRangeCol;
    @FXML
    private TableColumn<EnvironmentPresenter, String> envTypeCol;
    @FXML
    private Button buttonStart;
    @FXML
    private Button buttonClear;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn values = new TableColumn("Values");
        this.tableEnvironmentsPresenter.getColumns().add(values);
        this.obsListEnvironmentsBefore = FXCollections.observableArrayList();
        this.obsListEnvironments = FXCollections.observableArrayList();
        this.obsListEntities = FXCollections.observableArrayList();
        this.envNameCol.setCellValueFactory(new PropertyValueFactory<>("environmentName"));
        this.envRangeCol.setCellValueFactory(new PropertyValueFactory<>("environmentRange"));
        this.envTypeCol.setCellValueFactory(new PropertyValueFactory<>("environmentType"));
        //this.envValueCol.setCellValueFactory(new PropertyValueFactory<>("textFieldValue"));
        this.environmentColumn.setCellValueFactory(new PropertyValueFactory<>("environmentName"));
        this.valueColumn.setCellValueFactory(new PropertyValueFactory<>("environmentVal"));
        this.entityColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        this.populationColumn.setCellValueFactory(new PropertyValueFactory<>("population"));
        this.tableEntities.setItems(this.obsListEntities);
        this.tableEnvironments.setItems(this.obsListEnvironments);
        this.tableEnvironmentsPresenter.setItems(this.obsListEnvironmentsBefore);
        labelError.setText("Label error");

    }


    public void loadFromWorldDef(DtoResponsePreview worldDef,InterfaceMenu i_interfaceMenu){
        this.buttonStart.setDisable(false);
        this.buttonValue.setDisable(false);
        this.labelValue.setVisible(true);
        this.textFieldValue.setVisible(true);
        this.buttonValueEntity1.setDisable(false);
        this.buttonClear.setDisable(false);
        this.worldPreview = worldDef;
        this.EnvToValue = new HashMap<>();
        if (this.interfaceMenu == null){this.interfaceMenu = i_interfaceMenu;}
        Map<String, EnvironmentDefinition> allEnv = worldDef.getDtoEnvironments().getEnvironmentDefinitions();
        for(String envName: allEnv.keySet()){
            String propType = allEnv.get(envName).getEnvPropertyDefinition().getPropertyType();
            Range rangeOfProp = allEnv.get(envName).getEnvPropertyDefinition().getPropertyRange();
            EnvironmentPresenter newPresenter = new EnvironmentPresenter(envName, rangeOfProp, propType);
            this.obsListEnvironmentsBefore.add(newPresenter);
        }
        List<DtoResponseEntities> entities = worldDef.getDtoResponseEntities();
        entity1Label.setText(entities.get(0).getEntityName());
        if(entities.size() == 2){
            entity2Label.setVisible(true);
            entity2Label.setDisable(false);
            buttonValueEntity2.setDisable(false);
            buttonValueEntity2.setVisible(true);
            textFieldEntity2.setDisable(false);
            textFieldEntity2.setVisible(true);
            entity2Label.setText(entities.get(1).getEntityName());
            subTitleEntity2.setVisible(true);
        }
    }

    public void selectItem(){
        EnvironmentPresenter item = this.tableEnvironmentsPresenter.getSelectionModel().getSelectedItem();
        if(item != null){
            this.currentSelectedItem = item;
        }
    }

    public void checkValidationEnvironment(){
        if(this.currentSelectedItem == null){
            this.labelError.setVisible(true);
            this.labelError.setText("You haven't selected a value yet");
            return;
        }
        String valueToCheck = textFieldValue.getText();
        String envName = this.currentSelectedItem.getEnvironmentName();
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
        this.labelValue.setText("");
        textFieldValue.setText("");
        EnvironmentPresenter envPresenter = new EnvironmentPresenter(envName, valueToCheck);
        EnvironmentPresenter isEnvExist = isEnvNameExistsInList(envPresenter);
        if(isEnvNameExistsInList(envPresenter) == null) {
            this.obsListEnvironments.add(envPresenter);
        }
        else{
            this.obsListEnvironments.set(this.obsListEnvironments.indexOf(isEnvExist), envPresenter);
        }
        this.tableEnvironments.setVisible(true);
    }

    private EnvironmentPresenter isEnvNameExistsInList(EnvironmentPresenter environmentPresenter){
        for(EnvironmentPresenter currPresenter: this.obsListEnvironments){
            if(environmentPresenter.getEnvironmentName().equalsIgnoreCase(currPresenter.getEnvironmentName())){
                return currPresenter;
            }
        }
        return null;
    }

    private EntityPresenter isEntityNameExistsInList(EntityPresenter entityPresenter){
        for(EntityPresenter currPresenter: this.obsListEntities){
            if(entityPresenter.getEntityName().equalsIgnoreCase(currPresenter.getEntityName())){
                return currPresenter;
            }
        }
        return null;
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
                labelErrorEntity1.setVisible(true);
                this.population1 = parsedValue;
                this.labelErrorEntity1.setText(this.entity1Label.getText() + " population: " + this.population1);
                EntityPresenter entityPresenter = new EntityPresenter(this.entity1Label.getText(), this.population1);
                addEntityToObserverList(entityPresenter);
                this.population1 = entityPresenter.getPopulation();
                this.tableEntities.setVisible(true);
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
                labelErrorEntity2.setVisible(true);
                this.population2 = parsedValue;
                this.labelErrorEntity2.setText(this.entity2Label.getText() + " population: " + this.population2);
                EntityPresenter entityPresenter = new EntityPresenter(this.entity2Label.getText(), this.population2);
                addEntityToObserverList(entityPresenter);
                this.population2 = entityPresenter.getPopulation();
                this.tableEntities.setVisible(true);
            }
        }
    }

    private void addEntityToObserverList(EntityPresenter presenterToAdd){
        EntityPresenter isEntityExists = isEntityNameExistsInList(presenterToAdd);
        if(isEntityExists == null) {
            this.obsListEntities.add(presenterToAdd);
        }
        else{
            this.obsListEntities.set(this.obsListEntities.indexOf(isEntityExists), presenterToAdd);
        }
    }

    @FXML
    void OnClickedSetValueOfEnvProp(ActionEvent event) {
        checkValidationEnvironment();
    }

    @FXML
    void OnClickedClear(ActionEvent event) {
        this.EnvToValue.clear();
        this.labelErrorEntity1.setVisible(false);
        this.labelErrorEntity2.setVisible(false);
        this.textFieldEntity2.setText("");
        this.textFieldValue.setText("");
        this.textFieldEntity1.setText("");
        this.population1 = 0;
        this.population2 = 0;
        labelError.setText("All chosen environment and population values were removed");
        this.obsListEntities.clear();
        this.obsListEnvironments.clear();
        this.tableEntities.setVisible(false);
        this.tableEnvironments.setVisible(false);
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
        Map<String, Object> copyOfEnvToValue = createCopyFromEnvToNameMap();
        this.interfaceMenu.executeSimulation(new DtoUiToEngine(copyOfEnvToValue, this.population1, this.population2,entity1Label.getText(), entity2Label.getText()));
        this.sceneMenu.navigateToResultTab();

        //this.interfaceMenu.runSimulations(new DtoUiToEngine(this.EnvToValue,this.population1,this.population2));
    }

    private Map<String, Object> createCopyFromEnvToNameMap() {
        Map<String, Object> copyOfEnvToValue = new HashMap<>();
        for(String envName: this.EnvToValue.keySet()){
            Object copyOfValue = this.EnvToValue.get(envName);
            copyOfEnvToValue.put(envName, copyOfValue);
        }

        return copyOfEnvToValue;
    }

    public void setSceneMenu(SceneMenu sceneMenu) {
        this.sceneMenu = sceneMenu;
    }

    public void clearScreen(){
        this.obsListEntities.clear();
        this.obsListEnvironments.clear();
        this.obsListEnvironmentsBefore.clear();
    }

    public void loadSimulationDetailsAgain(int idOfSimulation) {
        DtoUiToEngine simulationStartingDetails = this.interfaceMenu.getSimulationStartingInfoById(idOfSimulation);
        this.obsListEntities.clear();
        this.textFieldEntity1.setText(Integer.toString(simulationStartingDetails.getPopulation1()));
        this.population1 = simulationStartingDetails.getPopulation1();
        this.obsListEntities.add(new EntityPresenter(this.entity1Label.getText(), simulationStartingDetails.getPopulation1()));
        if (simulationStartingDetails.getPopulation2() != -1) {
            this.population2 = simulationStartingDetails.getPopulation2();
            this.textFieldEntity2.setText(Integer.toString(simulationStartingDetails.getPopulation2()));
            this.obsListEntities.add(new EntityPresenter(this.entity2Label.getText(), simulationStartingDetails.getPopulation2()));
        }
        this.obsListEnvironments.clear();
        for (String envName : simulationStartingDetails.getEnvironmentToValue().keySet()) {
            EnvironmentPresenter startSimulationPresenter = new EnvironmentPresenter(envName, simulationStartingDetails.getEnvironmentToValue().get(envName));
            this.obsListEnvironments.add(startSimulationPresenter);
        }

        this.labelError.setText("");
        this.labelErrorEntity1.setText("");
        this.labelErrorEntity2.setText("");
    }
}

