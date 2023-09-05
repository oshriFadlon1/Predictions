package rule.action;

import exceptions.GeneralException;
import interfaces.IConditionComponent;
import necessaryVariables.NecessaryVariablesImpl;

import java.io.Serializable;
import java.util.List;

public class MultipleCondition implements IConditionComponent, Serializable {

    private String logical;
    private List<IConditionComponent> subConditions;

    public MultipleCondition(String logical, List<IConditionComponent> subConditions) {
        this.logical = logical;
        this.subConditions = subConditions;
    }

    public String getLogical() {
        return logical;
    }

    public void setLogical(String logical) {
        this.logical = logical;
    }

    public List<IConditionComponent> getSubConditions() {
        return subConditions;
    }

    public void setSubConditions(List<IConditionComponent> subConditions) {
        this.subConditions = subConditions;
    }

    @Override
    public int getNumberOfCondition() {
        int numberOfSubCondition = 1;
        for (IConditionComponent conditionComponent : subConditions) {
            numberOfSubCondition += conditionComponent.getNumberOfCondition();
        }
        return numberOfSubCondition;
    }

    @Override
    public boolean getResultFromCondition(NecessaryVariablesImpl context) throws GeneralException {
        boolean result = true;
        try{
            if (logical.equals("and"))
            {

                for (IConditionComponent conditionComponent:this.subConditions) {
                    result = result && conditionComponent.getResultFromCondition(context);
                }
            }
            if (logical.equals("or"))
            {
                result = false;
                for (IConditionComponent conditionComponent:this.subConditions) {
                    result = result || conditionComponent.getResultFromCondition(context);
                }
            }

        } catch (GeneralException e) {
            throw e;
        }
        return result;
    }

}
