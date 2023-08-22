package ui.sceneController.newExecutionController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.util.ResourceBundle;

public class NewExecutionController implements Initializable {
    @FXML
    private TreeView treeView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> rootItem = new TreeItem<>("Environments");

        TreeItem<String> branchItem1 = new TreeItem<>("Environment 1");
        TreeItem<String> branchItem2 = new TreeItem<>("Environment 2");
        TreeItem<String> branchItem3 = new TreeItem<>("Environment 3");

        rootItem.getChildren().addAll(branchItem1, branchItem2, branchItem3);
        treeView.setRoot(rootItem);
    }



}
