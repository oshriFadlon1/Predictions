package rule.action;

import dto.DtoActionResponse;
import entity.EntityDefinition;
import entity.SecondEntity;
import enums.Operation;
import exceptions.GeneralException;
import interfaces.IConditionComponent;
import necessaryVariables.NecessaryVariablesImpl;

import java.io.Serializable;
import java.util.List;

public class ActionCondition extends AbstractAction implements Serializable {
    private IConditionComponent theCondition;
    private List<IAction> trueResult;
    private List<IAction> falseResult;

    public ActionCondition(EntityDefinition entityDefinition, IConditionComponent theCondition, List<IAction> trueResult, List<IAction> falseResult, SecondEntity secondEntity) {
        super(Operation.CONDITION, entityDefinition, secondEntity);
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
        if (!context.getPrimaryEntityInstance().getDefinitionOfEntity().getEntityName().equalsIgnoreCase(super.getEntityDefinition().getEntityName())){
            return;
        }

        try {
           boolean result = this.theCondition.getResultFromCondition(context);
            if (result) {
                for (IAction action : this.trueResult) {
                    action.invoke(context);
                }
            } else {
                if (this.falseResult != null){
                    for (IAction action : this.falseResult) {
                        action.invoke(context);
                    }
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

    @Override
    public DtoActionResponse getActionResponse() {
        DtoActionResponse actionResponse = super.getActionResponse();
        MultipleCondition multipleCondition = (MultipleCondition)this.theCondition;
        SingleCondition singleCondition;
        if(multipleCondition.getSubConditions().size() == 1){
            singleCondition = (SingleCondition) (multipleCondition.getSubConditions().get(0));
            actionResponse.setActionName("Single condition");
            actionResponse.setActionProperty(singleCondition.getExpressionValue());
            actionResponse.setActionValue(singleCondition.getOperator());
            actionResponse.setArg2(singleCondition.getValueToCompare());
        } else {
            actionResponse.setActionName("Multiple condition");
            actionResponse.setActionProperty(multipleCondition.getLogical());
            actionResponse.setActionValue(String.valueOf(multipleCondition.getNumberOfCondition()));
            actionResponse.setArg2(String.valueOf(this.trueResult.size()));
            if (this.falseResult == null){
                actionResponse.setActionElse(String.valueOf("0"));
            }else{
                actionResponse.setActionElse(String.valueOf(this.falseResult.size()));
            }

        }

        return actionResponse;
    }
}
