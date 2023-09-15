package rule.action;

import dto.DtoActionResponse;
import entity.EntityDefinition;
import entity.SecondEntity;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;
import pointCoord.PointCoord;

import java.util.List;
import java.util.Set;

public class ActionProximity extends AbstractAction {
    private String envDepth;
    private List<IAction> actionIfTrue;

    public ActionProximity(EntityDefinition entityDefinition, String envDepth, List<IAction> actionIfTrue) {
        super(Operation.PROXIMITY, entityDefinition);
        this.envDepth = envDepth;
        this.actionIfTrue = actionIfTrue;
    }

    public ActionProximity(EntityDefinition entityDefinition, SecondEntity secondaryEntity, String envDepth, List<IAction> actionIfTrue) {
        super(Operation.PROXIMITY, entityDefinition, secondaryEntity);
        this.envDepth = envDepth;
        this.actionIfTrue = actionIfTrue;
    }

    @Override
    public void invoke(NecessaryVariablesImpl context) throws GeneralException {
        if (!context.getPrimaryEntityInstance().getDefinitionOfEntity().getEntityName().equalsIgnoreCase(super.getEntityDefinition().getEntityName())){
            return;
        }
        PointCoord entity1point = context.getPrimaryEntityInstance().getPositionInWorld();
        PointCoord entity2point = context.getSecondaryEntityInstance().getPositionInWorld();
        boolean findTheSecEntity = false;
        int depth = ((Number)context.getValueFromString(this.envDepth)).intValue();

        // send to find all the cell in the depth by sending point1entity and depth
        Set<PointCoord> allTheCell = context.getWorldPhysicalSpace().findEnvironmentCells(entity1point, depth);
        // check if entity2point is in the set
        for (PointCoord pointCoord : allTheCell) {
            if (pointCoord.equals(entity2point)){
                findTheSecEntity = true;
                context.setSecondaryEntityDefinition(context.getSecondaryEntityInstance().getDefinitionOfEntity());
                break;
            }
        }

        if (findTheSecEntity){
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
}
