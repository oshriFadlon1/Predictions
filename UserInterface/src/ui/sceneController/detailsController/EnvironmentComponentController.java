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

        public void updateLabelEnv(EnvironmentDefinition environmentDefinition){
        if (environmentDefinition == null){
            return;
        }
        PropertyDefinition propertyDefinition = environmentDefinition.getEnvPropertyDefinition();
        envName.setText(propertyDefinition.getPropertyName());
        envType.setText(propertyDefinition.getPropertyType());
        if (propertyDefinition.getPropertyRange() == null) {
            envRange.setText("");
        }
        else {

            envRange.setText("Range: "+propertyDefinition.getPropertyRange().getFrom()+" -> "+propertyDefinition.getPropertyRange().getTo());
        }

    }

}
