package ui.sceneController.detailsController;

import dto.DtoResponseEntities;
import dto.DtoResponsePreview;
import dto.DtoResponsePreviewTermination;
import dto.DtoResponseRules;
import environment.EnvironmentDefinition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import property.PropertyDefinition;
import property.PropertyDefinitionEntity;
import range.Range;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class DetailsController implements Initializable {

    private DtoResponsePreview worldPreview;

    @FXML private Label propInit;

    @FXML private Label propRange;

    @FXML private Label propType;

    @FXML private Label rangeEnv;

    @FXML private Label primeEntRule;
    @FXML private Label ruleActivation;

    @FXML private Label termSec;

    @FXML private Label termTicks;

    @FXML private Label termUser;

    @FXML private Label titleEnt;

    @FXML private Label titleEnv;

    @FXML private Label titleRule;

    @FXML private Label titleTerm;

    @FXML private TreeView<String> treeView;

    @FXML private Label typeEnv;

    public void loadFromWorldDef(DtoResponsePreview worldDefinition){
        this.worldPreview = worldDefinition;
        setTreeViewFromWorld();
    }

    private void setTreeViewFromWorld() {
        TreeItem<String> rootItem = treeView.getRoot();
        // Create child items
        TreeItem<String> Env = rootItem.getChildren().get(0);
        for (String name : this.worldPreview.getDtoEnvironments().getEnvironmentDefinitions().keySet()) {
            TreeItem<String> EnvName = new TreeItem<>(name);
            Env.getChildren().addAll(EnvName);
        }

        TreeItem<String> Ent = rootItem.getChildren().get(1);
        for (DtoResponseEntities entityDefinition: this.worldPreview.getDtoResponseEntities()) {
            TreeItem<String> EntName = new TreeItem<>(entityDefinition.getEntityName());
            for (PropertyDefinitionEntity propertyDefinitionEntity:entityDefinition.getPropertyDefinitionEntityList()) {
                TreeItem<String> entPropName = new TreeItem<>(propertyDefinitionEntity.getPropertyDefinition().getPropertyName());
                EntName.getChildren().addAll(entPropName);
            }
            Ent.getChildren().addAll(EntName);
        }
        TreeItem<String> Rule = rootItem.getChildren().get(2);
        for (DtoResponseRules rule : this.worldPreview.getDtoResponseRules()) {
            TreeItem<String> RuleName = new TreeItem<>(rule.getRuleName());
            Rule.getChildren().addAll(RuleName);
        }
        updateLabelTerm(this.worldPreview.getDtoResponseTermination());
        treeView.setShowRoot(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> rootItem = new TreeItem<>("World");
        TreeItem<String> Env = new TreeItem<>("Environment");
        TreeItem<String> Ent = new TreeItem<>("Entity");
        TreeItem<String> Rule = new TreeItem<>("Rules");
        rootItem.getChildren().addAll(Env, Ent,Rule);
        treeView.setRoot(rootItem);
        treeView.setEditable(true);
        treeView.setShowRoot(false);
    }

    public void SelectedItem(){
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null || this.worldPreview == null || selectedItem.getValue().equalsIgnoreCase("World")){
            return;
        }

        if (selectedItem.getParent().getValue().equalsIgnoreCase("Environment")){
            updateLabelEnv(selectedItem.getValue());
        }
        updateLabelEnt(selectedItem);
        updateLabelRule(selectedItem);
    }

    private void updateLabelEnv(String selectedItem){
        Map<String, EnvironmentDefinition> environment = this.worldPreview.getDtoEnvironments().getEnvironmentDefinitions();
        EnvironmentDefinition environmentDefinition = environment.get(selectedItem);
        if (environmentDefinition == null){
            return;
        }
        PropertyDefinition propertyDefinition = environmentDefinition.getEnvPropertyDefinition();
        titleEnv.setText(propertyDefinition.getPropertyName());
        typeEnv.setText(propertyDefinition.getPropertyType());
        if (propertyDefinition.getPropertyRange() == null) {
            rangeEnv.setText("");
        }
        else {
            Range range = propertyDefinition.getPropertyRange();
            rangeEnv.setText("Range: "+range.getFrom()+" -> "+range.getTo());
        }

    }

    private void updateLabelEnt(TreeItem<String> selectedItem){
        PropertyDefinitionEntity propertyDefinitionChoose = getSelectedProperty(selectedItem);
        if (propertyDefinitionChoose == null){
            return;
        }

        titleEnt.setText(propertyDefinitionChoose.getPropertyDefinition().getPropertyName());
        propType.setText(propertyDefinitionChoose.getPropertyDefinition().getPropertyType());
        if (propertyDefinitionChoose.getPropertyDefinition().getPropertyRange() == null) {
            propRange.setText("");
        }
        else {
            Range range = propertyDefinitionChoose.getPropertyDefinition().getPropertyRange();
            propRange.setText("Range: "+range.getFrom()+" -> "+range.getTo());
        }
        boolean randomInit = propertyDefinitionChoose.getPropValue().getRandomInit();
        String initTo = propertyDefinitionChoose.getPropValue().getInit();
        if (randomInit){
            propInit.setText("Random-initialize");
        }else{
            propInit.setText("initialize to " + initTo);
        }
    }

    private PropertyDefinitionEntity getSelectedProperty(TreeItem<String> selectedItem) {
        String parentName = selectedItem.getParent().getValue();
        PropertyDefinitionEntity propertyDefinitionChoose = null;
        DtoResponseEntities entityChoose = null;
        for (DtoResponseEntities entities:worldPreview.getDtoResponseEntities()) {
            if (entities.getEntityName().equalsIgnoreCase(parentName)){
                entityChoose = entities;
                break;
            }
        }
        if (entityChoose == null){
            return null;
        }
        for (PropertyDefinitionEntity propertyDefinitionEntity:entityChoose.getPropertyDefinitionEntityList()) {
            if (propertyDefinitionEntity.getPropertyDefinition().getPropertyName().equalsIgnoreCase(selectedItem.getValue())){
                propertyDefinitionChoose = propertyDefinitionEntity;
            }
        }
        return propertyDefinitionChoose;
    }

    private void updateLabelRule(TreeItem<String> selectedItem){

    }
    private void updateLabelTerm(DtoResponsePreviewTermination termination){
        titleTerm.setText("Termination");
        if (termination.getTicks() == -1) {
            termTicks.setText("");
        }
        else {
            termTicks.setText("Ticks: " + termination.getTicks());
        }
        if (termination.getSeconds() == -1) {
            termSec.setText("");
        }
        else {
            termSec.setText("Seconds: " + termination.getSeconds());
        }

        // need to add if user choice
        termUser.setText("");
    }
}
