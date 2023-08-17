package rule.action;

import entity.EntityDefinition;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;

import java.io.Serializable;

public abstract class ActionCalculation extends AbstractAction implements Serializable {

    private String propertyPlacement; // property name to set the value in

    public String getPropertyPlacement() {
        return propertyPlacement;
    }

    public void setPropertyPlacement(String propertyPlacement) {
        this.propertyPlacement = propertyPlacement;
    }

    public ActionCalculation(EntityDefinition entityDefinition, String propertyPlacement) {
        super(Operation.CALCULATION, entityDefinition);
        this.propertyPlacement = propertyPlacement;
    }

    public abstract void invoke(NecessaryVariablesImpl context) throws GeneralException;
}
