package rule.action;

import entity.EntityDefinition;
import enums.Operation;
import exceptions.GeneralException;
import interfaces.IConditionComponent;
import necessaryVariables.NecessaryVariablesImpl;

import java.util.ArrayList;
import java.util.List;

public class ActionCondition extends AbstractAction {
    private IConditionComponent theCondition;
    private List<IAction> trueResult;
    private List<IAction> falseResult;

    public ActionCondition(EntityDefinition entityDefinition, IConditionComponent theCondition, List<IAction> trueResult, List<IAction> falseResult) {
        super(Operation.CONDITION, entityDefinition);
        this.theCondition = theCondition;
        this.trueResult = trueResult;
        this.falseResult = falseResult;
    }

    public IConditionComponent getTheCondition() {
        return theCondition;
    }

    public void setTheCondition(IConditionComponent theCondition) {
        this.theCondition = theCondition;
    }

    public List<IAction> getTrueResult() {
        return trueResult;
    }

    public void setTrueResult(List<IAction> trueResult) {
        this.trueResult = trueResult;
    }

    public List<IAction> getFalseResult() {
        return falseResult;
    }

    public void setFalseResult(List<IAction> falseResult) {
        this.falseResult = falseResult;
    }

    @Override
    public void invoke(NecessaryVariablesImpl context) throws GeneralException {
        try {
           boolean result = this.theCondition.getResultFromCondition(context);
            if (result) {
                for (IAction action : this.trueResult) {
                    action.invoke(context);
                }
            } else {
                for (IAction action : this.falseResult) {
                    action.invoke(context);
                }
            }
        } catch (GeneralException e) {
            throw e;
        }
    }

    @Override
    public Operation getOperationType() {
        return Operation.CONDITION;
    }

    @Override
    public EntityDefinition getContextEntity() {
        return super.getEntityDefinition();
    }
}