package rule.action;

import entity.EntityDefinition;
import entity.EntityInstance;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;

public abstract class ActionCalculation extends AbstractAction {

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
