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
            case "set":
            case "replace":
            case "proximity":
                this.actionName.setText(DtoActionResponse.getActionName());
                this.actionEntity.setText(DtoActionResponse.getPrimEntityName());
                this.EntitySecond.setText(DtoActionResponse.getSecEntityName());
                this.actionProperty.setText(DtoActionResponse.getActionProperty());
                this.actionValue.setText(DtoActionResponse.getActionValue());
                this.actionArg2.setText("");
                this.ActionElse.setText("");
                break;
            case "kill":
                this.actionName.setText(DtoActionResponse.getActionName());
                this.actionEntity.setText(DtoActionResponse.getPrimEntityName());
                this.EntitySecond.setText("");
                this.actionProperty.setText("");
                this.actionValue.setText("");
                this.actionArg2.setText("");
                this.ActionElse.setText("");
                break;
            case "calculation divide":
            case "calculation multiply":
            case "single condition":
                this.actionName.setText(DtoActionResponse.getActionName());
                this.actionEntity.setText(DtoActionResponse.getPrimEntityName());
                this.EntitySecond.setText(DtoActionResponse.getSecEntityName());
                this.actionProperty.setText(DtoActionResponse.getActionProperty());
                this.actionValue.setText(DtoActionResponse.getActionValue());
                this.actionArg2.setText(DtoActionResponse.getArg2());
                this.ActionElse.setText("");
                break;
            case "multiple condition":
                this.actionName.setText(DtoActionResponse.getActionName());
                this.actionEntity.setText(DtoActionResponse.getPrimEntityName());
                this.EntitySecond.setText(DtoActionResponse.getSecEntityName());
                this.actionProperty.setText(DtoActionResponse.getActionProperty());
                this.actionValue.setText(DtoActionResponse.getActionValue());
                this.actionArg2.setText(DtoActionResponse.getArg2());
                this.ActionElse.setText(DtoActionResponse.getActionElse());
                break;
        }
    }
}
