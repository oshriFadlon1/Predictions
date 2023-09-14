package ui.sceneController.detailsController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import property.PropertyDefinitionEntity;

public class EntityComponentController {

    @FXML
    private Label entPropName;

    @FXML
    private Label propRange;

    @FXML
    private Label propType;

    @FXML
    private Label randomInit;


    public void updateLabelEnt(PropertyDefinitionEntity propertyDefinitionChoose){
        if (propertyDefinitionChoose == null){
            return;
        }

        entPropName.setText("Entity property name: " + propertyDefinitionChoose.getPropertyDefinition().getPropertyName());
        propType.setText("Entity property type: " +propertyDefinitionChoose.getPropertyDefinition().getPropertyType());
        if (propertyDefinitionChoose.getPropertyDefinition().getPropertyRange() == null) {
            propRange.setText("");
        }
        else {
            propRange.setText("Range: "+propertyDefinitionChoose.getPropertyDefinition().getPropertyRange().getFrom() + " -> " + propertyDefinitionChoose.getPropertyDefinition().getPropertyRange().getTo());
        }
        boolean randominitialize = propertyDefinitionChoose.getPropValue().getRandomInit();
        String initTo = propertyDefinitionChoose.getPropValue().getInit();
        if (randominitialize){
            randomInit.setText("Random-initialize");
        }else{
            randomInit.setText("initialize to " + initTo);
        }
    }

}
