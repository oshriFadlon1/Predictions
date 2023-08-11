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
    private List<Action> trueResult;
    private List<Action> falseResult;

    public ActionCondition(EntityDefinition entityDefinition, IConditionComponent theCondition) {
        super(Operation.CONDITION, entityDefinition);
        this.theCondition = theCondition;
        this.trueResult = new ArrayList<>();
        this.falseResult = new ArrayList<>();
    }

    public List<Action> getTrueResult() {
        return trueResult;
    }

    public void setTrueResult(List<Action> trueResult) {
        this.trueResult = trueResult;
    }

    public List<Action> getFalseResult() {
        return falseResult;
    }

    public void setFalseResult(List<Action> falseResult) {
        this.falseResult = falseResult;
    }

    @Override
    public void invoke(NecessaryVariablesImpl context) throws GeneralException {
        try {
           boolean result = this.theCondition.getResultFromCondition(context);
            if (result) {
                for (Action action : this.trueResult) {
                    action.invoke(context);
                }
            } else {
                for (Action action : this.falseResult) {
                    action.invoke(context);
                }
            }
        } catch (GeneralException e) {
            throw e;
        }
    }

    @Override
    public Operation getActionType() {
        return Operation.CONDITION;
    }

    @Override
    public EntityDefinition getContextEntity() {
        return super.getEntityDefinition();
    }
}
