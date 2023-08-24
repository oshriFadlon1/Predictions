package ui.sceneController.detailsController;

import entity.EntityDefinition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import rule.Rule;
import termination.Termination;
import world.WorldDefinition;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailsController implements Initializable {

    @FXML
    private TreeView<String> treeView;
    @FXML private Label titleEnv;
    @FXML private Label nameEnv;
    @FXML private Label typeEnv;
    @FXML private Label rangeEnv;

    @FXML private Label titleEnt;
    @FXML private Label propName;
    @FXML private Label propType;
    @FXML private Label propRange;
    @FXML private Label propInit;

    @FXML private Label titleRule;
    @FXML private Label ruleAcvition;

    @FXML private Label titleTerm;
    @FXML private Label termUser;
    @FXML private Label termSec;
    @FXML private Label termTicks;


    public void loadFromWorldDef(WorldDefinition worldDefinition){
        setTreeViewFromWorld(worldDefinition);
    }

    private void setTreeViewFromWorld(WorldDefinition worldDefinition) {
        TreeItem<String> rootItem = treeView.getRoot();
        // Create child items
        TreeItem<String> Env = new TreeItem<>("Env");
        for (String name : worldDefinition.getAllEnvironments().keySet()) {
            TreeItem<String> EnvName = new TreeItem<>(name);
            Env.getChildren().addAll(EnvName);
        }

        TreeItem<String> Ent = new TreeItem<>("Entity");
        for (EntityDefinition entityDefinition: worldDefinition.getEntityDefinitions()) {
            TreeItem<String> EntName = new TreeItem<>(entityDefinition.getEntityName());
            Ent.getChildren().addAll(EntName);
        }
        TreeItem<String> Rule = new TreeItem<>("Rules");
        for (Rule rule : worldDefinition.getRules()) {
            TreeItem<String> RuleName = new TreeItem<>(rule.getRuleName());
            Rule.getChildren().addAll(RuleName);
        }
        TreeItem<String> Term = new TreeItem<>("Termination");
        Termination termination = worldDefinition.getTermination();
        TreeItem<String> terminationInfo = new TreeItem<>("Ticks: " + termination.getTicks() + ", Seconds: " + termination.getSeconds());
        Term.getChildren().addAll(terminationInfo);

        // Add child items to root item
        rootItem.getChildren().addAll(Env, Ent,Rule,Term);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> rootItem = new TreeItem<>("World");
        treeView.setRoot(rootItem);
        treeView.setEditable(true);
    }
}
