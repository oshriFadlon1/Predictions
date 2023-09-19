package ui.sceneController.detailsController;

import environment.EnvironmentDefinition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import property.PropertyDefinition;

public class EnvironmentComponentController {

    @FXML
    private Label envName;

    @FXML
    private Label envRange;

    @FXML
    private Label envType;

    @FXML
    private Label valueOfName;

    @FXML
    private Label valueOfRange;

    @FXML
    private Label valueOfType;

        public void updateLabelEnv(EnvironmentDefinition environmentDefinition){
        if (environmentDefinition == null){
            return;
        }
        PropertyDefinition propertyDefinition = environmentDefinition.getEnvPropertyDefinition();
            valueOfName.setText(propertyDefinition.getPropertyName());
            valueOfType.setText(propertyDefinition.getPropertyType());
        if (propertyDefinition.getPropertyRange() == null) {
            valueOfRange.setText("The following environment variable has no range of values.");
        }
        else {
            valueOfRange.setText(propertyDefinition.getPropertyRange().getFrom()+" -> "+propertyDefinition.getPropertyRange().getTo());
        }

    }

}
