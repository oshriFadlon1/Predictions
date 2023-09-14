package ui.sceneController.detailsController;

import dto.DtoActionResponse;
import dto.DtoResponseEntities;
import dto.DtoResponsePreview;
import dto.DtoResponseRules;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import property.PropertyDefinitionEntity;
import ui.sceneController.SceneMenu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DetailsController implements Initializable {

    private SceneMenu sceneMenu;

    @FXML private TreeView<String> treeView;

    @FXML private AnchorPane mainAnchorPane;
    private DtoResponsePreview worldPreview;
    private TreminationComponentController treminationComponentController;
    private EntityComponentController entityComponentController;
    private EnvironmentComponentController environmentComponentController;

    private RuleComponentController ruleComponentController;

    private final String world = "World";
    private final String environment = "Environment";
    private final String entities = "Entities";
    private final String general = "General";
    private String rules = "Rules";


    public void setSceneMenu(SceneMenu sceneMenu) {
        this.sceneMenu = sceneMenu;
    }

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
            int count = 1;
            TreeItem<String> RuleName = new TreeItem<>(rule.getRuleName());
            TreeItem<String> activation = new TreeItem<>("Ticks: " + rule.getTicks() + ", probability: " + rule.getProbability());
            for (DtoActionResponse actionResponse: rule.getActionNames()) {
                TreeItem<String> actionName = new TreeItem<>(count + ") " + actionResponse.getActionName());
                RuleName.getChildren().addAll(actionName);
                count++;
            }
            RuleName.getChildren().addAll(activation);
            Rule.getChildren().addAll(RuleName);
        }

        treeView.setShowRoot(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> rootItem = new TreeItem<>(world);
        addBasicTreeItems(rootItem);
        treeView.setRoot(rootItem);
        treeView.setEditable(true);
        treeView.setShowRoot(false);
    }

    private void addBasicTreeItems(TreeItem<String> rootItem) {
        TreeItem<String> Env = new TreeItem<>(environment);
        TreeItem<String> Ent = new TreeItem<>(entities);
        TreeItem<String> Rule = new TreeItem<>(rules);
        TreeItem<String> Term = new TreeItem<>(general);

        rootItem.getChildren().addAll(Env, Ent,Rule, Term);
    }

    public void SelectedItem(){
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
        // case choose world or not set the file yet.
        if (selectedItem == null || this.worldPreview == null || selectedItem.getValue().equalsIgnoreCase(world)){
            return;
        }

        // case choose general (termination and grid)
        if (selectedItem.getValue().equalsIgnoreCase(general)){
            loadAndAddFXML("/ui/javaFx/scenes/sceneDetails/detailsComponents/TerminationComponent.fxml", general);
            treminationComponentController.updateLabelTerm(this.worldPreview.getDtoResponseTermination(), this.worldPreview.getWorldSize());
            return;
        }

        // case choose environment property.
        if (selectedItem.getParent().getValue().equalsIgnoreCase(environment)){
            loadAndAddFXML("/ui/javaFx/scenes/sceneDetails/detailsComponents/EnvironmentComponent.fxml", environment);
            environmentComponentController.updateLabelEnv(this.worldPreview.getDtoEnvironments().getEnvironmentDefinitions().get(selectedItem.getValue()));
            return;
        }

        // case choose entity property.
        if (selectedItem.getParent().getParent().getValue().equalsIgnoreCase(entities)){
            loadAndAddFXML("/ui/javaFx/scenes/sceneDetails/detailsComponents/EntityComponent.fxml", "Entity");
            entityComponentController.updateLabelEnt(getSelectedPropertyEntity(selectedItem));
            return;
        }
        // case choose rule action
        if (selectedItem.getParent().getParent().getValue().equalsIgnoreCase(rules)){
            loadAndAddFXML("/ui/javaFx/scenes/sceneDetails/detailsComponents/RuleComponent.fxml", "Rule");
            this.ruleComponentController.updateLabelRule(getSelectedAction(selectedItem));
        }

    }

    private PropertyDefinitionEntity getSelectedPropertyEntity(TreeItem<String> selectedItem) {
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

    private DtoActionResponse getSelectedAction(TreeItem<String> selectedItem) {
        String parentName = selectedItem.getParent().getValue();
        String selectedActionName = selectedItem.getValue().toLowerCase();
        DtoActionResponse actionResponse = null;
        DtoResponseRules responseRule = null;
        for (DtoResponseRules dtoResponseRule : this.worldPreview.getDtoResponseRules()) {
            if (dtoResponseRule.getRuleName().equalsIgnoreCase(parentName)){
                responseRule = dtoResponseRule;
                break;
            }
        }
        int count = 1;
        for (DtoActionResponse dtoActionResponse : responseRule.getActionNames()) {
            if (selectedActionName.equalsIgnoreCase(count + ") " +dtoActionResponse.getActionName())){
                actionResponse = dtoActionResponse;
                break;
            }
            count ++;
        }
        return actionResponse;
    }

    private void loadAndAddFXML(String fxmlFileName, String whichController) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource(fxmlFileName);
            loader.setLocation(mainFXML);
            AnchorPane component = loader.load();
            switch (whichController.toLowerCase()){
                case "environment":
                    this.environmentComponentController = loader.getController();
                    break;
                case "entity":
                    this.entityComponentController = loader.getController();
                    break;
                case"rule":
                    this.ruleComponentController = loader.getController();
                    break;
                case "general":
                    this.treminationComponentController = loader.getController();
                    break;
            }

            mainAnchorPane.getChildren().setAll(component);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearScreen() {
        this.treeView.getRoot().getChildren().clear();
        addBasicTreeItems(this.treeView.getRoot());
    }
}
