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
    private List<AbstractAction> trueResult;
    private List<AbstractAction> falseResult;

    public ActionCondition(EntityDefinition entityDefinition, IConditionComponent theCondition) {
        super(Operation.CONDITION, entityDefinition);
        this.theCondition = theCondition;
        this.trueResult = new ArrayList<>();
        this.falseResult = new ArrayList<>();
    }

    public List<AbstractAction> getTrueResult() {
        return trueResult;
    }

    public void setTrueResult(List<AbstractAction> trueResult) {
        this.trueResult = trueResult;
    }

    public List<AbstractAction> getFalseResult() {
        return falseResult;
    }

    public void setFalseResult(List<AbstractAction> falseResult) {
        this.falseResult = falseResult;
    }

    @Override
    public void invoke(NecessaryVariablesImpl context) throws GeneralException {
        try {
           boolean result = this.theCondition.getResultFromCondition(context);
            if (result) {
                for (AbstractAction action : this.trueResult) {
                    action.invoke(context);
                }
            } else {
                for (AbstractAction action : this.falseResult) {
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
