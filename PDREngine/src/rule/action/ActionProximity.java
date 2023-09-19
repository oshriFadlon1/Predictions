package rule.action;

import dto.DtoActionResponse;
import entity.EntityDefinition;
import entity.EntityInstance;
import entity.SecondEntity;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;
import pointCoord.PointCoord;
import utility.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ActionProximity extends AbstractAction {
    private String targetName;
    private String envDepth;
    private List<IAction> actionIfTrue;

//    public ActionProximity(EntityDefinition entityDefinition, String envDepth, List<IAction> actionIfTrue) {
//        super(Operation.PROXIMITY, entityDefinition);
//        this.envDepth = envDepth;
//        this.actionIfTrue = actionIfTrue;
//    }

    public ActionProximity(EntityDefinition entityDefinition, SecondEntity secondaryEntity, String targetEntity, String envDepth, List<IAction> actionIfTrue) {
        super(Operation.PROXIMITY, entityDefinition, secondaryEntity);
        this.targetName = targetEntity;
        this.envDepth = envDepth;
        this.actionIfTrue = actionIfTrue;
    }

    @Override
    public void invoke(NecessaryVariablesImpl context) throws GeneralException {

        PointCoord entity1point = context.getPrimaryEntityInstance().getPositionInWorld();
        int depth = ((Number)context.getValueFromString(this.envDepth)).intValue();

        List<EntityInstance> secondEntity = context.getWorldPhysicalSpace().getEntitiesInProximity(entity1point.getRow(), entity1point.getCol(), depth, this.targetName);

        if (secondEntity.size() > 0 ){
            context.setSecondaryEntityInstance(secondEntity.get(Utilities.initializeRandomInt(0,secondEntity.size() - 1)));
            for (IAction action: this.actionIfTrue) {
                action.invoke(context);
            }
        }
    }

    @Override
    public EntityDefinition getContextEntity() {
        return super.getEntityDefinition();
    }

    @Override
    public DtoActionResponse getActionResponse() {
        DtoActionResponse actionResponse = super.getActionResponse();
        actionResponse.setActionName("Proximity");
        actionResponse.setActionProperty(this.envDepth);
        actionResponse.setActionValue(String.valueOf(this.actionIfTrue.size()));
        return actionResponse;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
}
