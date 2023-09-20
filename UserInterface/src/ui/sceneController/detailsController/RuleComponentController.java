package ui.sceneController.detailsController;

import dto.DtoActionResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

public class RuleComponentController {
    @FXML
    private Label actionEntity;

    @FXML
    private Label actionName;

    @FXML
    private Label actionProperty;

    @FXML
    private Label actionValue;

    @FXML
    private Label actionArg2;

    @FXML
    private Label ActionElse;

    @FXML
    private Label EntitySecond;


    public void updateLabelRule(DtoActionResponse DtoActionResponse){
        switch(DtoActionResponse.getActionName().toLowerCase()){
            case "increase":
            case "decrease":
                this.actionName.setText("Action name: " + DtoActionResponse.getActionName());
                this.actionEntity.setText("Primary entity: " + DtoActionResponse.getPrimEntityName());
                this.EntitySecond.setText("Secondary entity: " + DtoActionResponse.getSecEntityName());
                this.actionProperty.setText("Action property: " + DtoActionResponse.getActionProperty());
                this.actionValue.setText("By: " + DtoActionResponse.getActionValue());
                this.actionArg2.setText("");
                this.ActionElse.setText("");
                break;
            case "set":
                this.actionName.setText("Action name: " + DtoActionResponse.getActionName());
                this.actionEntity.setText("Primary entity: " + DtoActionResponse.getPrimEntityName());
                this.EntitySecond.setText("Secondary entity: " + DtoActionResponse.getSecEntityName());
                this.actionProperty.setText("Action property: " + DtoActionResponse.getActionProperty());
                this.actionValue.setText("Value: " + DtoActionResponse.getActionValue());
                this.actionArg2.setText("");
                this.ActionElse.setText("");
                break;
            case "replace":
                this.actionName.setText("Action name: " + DtoActionResponse.getActionName());
                this.actionEntity.setText("Entity to Kill: " + DtoActionResponse.getPrimEntityName());
                this.EntitySecond.setText("Entity to create: " + DtoActionResponse.getSecEntityName());
                this.actionProperty.setText("In mode: " + DtoActionResponse.getActionValue());
                this.actionValue.setText("");
                this.actionArg2.setText("");
                this.ActionElse.setText("");
                break;
            case "proximity":
                this.actionName.setText("Action name: " + DtoActionResponse.getActionName());
                this.actionEntity.setText("Primary entity: " + DtoActionResponse.getPrimEntityName());
                this.EntitySecond.setText("Secondary entity: " + DtoActionResponse.getSecEntityName());
                this.actionProperty.setText("Depth: " + DtoActionResponse.getActionProperty());
                this.actionValue.setText("");
                this.actionArg2.setText("");
                this.ActionElse.setText("");
                break;
            case "kill":
                this.actionName.setText("Action name: " + DtoActionResponse.getActionName());
                this.actionEntity.setText("Entity to kill: " + DtoActionResponse.getPrimEntityName());
                this.EntitySecond.setText("");
                this.actionProperty.setText("");
                this.actionValue.setText("");
                this.actionArg2.setText("");
                this.ActionElse.setText("");
                break;
            case "calculation divide":
            case "calculation multiply":
                this.actionName.setText("Action name: " + DtoActionResponse.getActionName());
                this.actionEntity.setText("Primary entity: " + DtoActionResponse.getPrimEntityName());
                this.EntitySecond.setText("Secondary entity: " + DtoActionResponse.getSecEntityName());
                this.actionProperty.setText("Property result: " + DtoActionResponse.getActionProperty());
                this.actionValue.setText("Arg 1: " + DtoActionResponse.getActionValue());
                this.actionArg2.setText("Arg 2: " + DtoActionResponse.getArg2());
                this.ActionElse.setText("");
                break;
            case "single condition":
                this.actionName.setText("Action name: " + DtoActionResponse.getActionName());
                this.actionEntity.setText("Primary entity: " + DtoActionResponse.getPrimEntityName());
                this.EntitySecond.setText("Secondary entity: " + DtoActionResponse.getSecEntityName());
                this.actionProperty.setText("Property: " + DtoActionResponse.getActionProperty());
                this.actionValue.setText( "Operator: " + DtoActionResponse.getActionValue());
                this.actionArg2.setText("Value: "+DtoActionResponse.getArg2());
                this.ActionElse.setText("");
                break;
            case "multiple condition":
                this.actionName.setText("Action name: " + DtoActionResponse.getActionName());
                this.actionEntity.setText("Primary entity: " + DtoActionResponse.getPrimEntityName());
                this.EntitySecond.setText("Secondary entity: " + DtoActionResponse.getSecEntityName());
                this.actionProperty.setText("Logical: " + DtoActionResponse.getActionProperty());
                this.actionValue.setText("Number of condition: " + DtoActionResponse.getActionValue());
                this.actionArg2.setText("Number of action in then: " + DtoActionResponse.getArg2());
                this.ActionElse.setText("Number of action in else: " + DtoActionResponse.getActionElse());
                break;
        }
    }
}
