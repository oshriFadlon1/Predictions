package ui.sceneController.detailsController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import property.PropertyDefinitionEntity;

public class EntityComponentController {

    @FXML
    private Label entPropName;

    @FXML
    private Label propNameValue;

    @FXML
    private Label propRange;

    @FXML
    private Label propType;

    @FXML
    private Label randomInit;

    @FXML
    private Label randomValue;

    @FXML
    private Label rangeValue;

    @FXML
    private Label typeValue;



    public void updateLabelEnt(PropertyDefinitionEntity propertyDefinitionChoose){
        if (propertyDefinitionChoose == null){
            return;
        }

        entPropName.setText("Entity property name: ");
        propNameValue.setText(propertyDefinitionChoose.getPropertyDefinition().getPropertyName());
        propType.setText("Entity property type: ");
        typeValue.setText(propertyDefinitionChoose.getPropertyDefinition().getPropertyType());
        if (propertyDefinitionChoose.getPropertyDefinition().getPropertyRange() != null) {
            rangeValue.setText(propertyDefinitionChoose.getPropertyDefinition().getPropertyRange().getFrom() + " -> " + propertyDefinitionChoose.getPropertyDefinition().getPropertyRange().getTo());
        }else {
            rangeValue.setText("The following property variable has no range of values.");

        }
        boolean randomInitialize = propertyDefinitionChoose.getPropValue().getRandomInit();
        String initTo = propertyDefinitionChoose.getPropValue().getInit();
        if (randomInitialize){
            randomValue.setText("Random-initialize");
        }else{
            randomValue.setText("initialize to " + initTo);
        }
    }

}
