package rule.action;

import entity.EntityDefinition;
import enums.Operation;
import necessaryVariables.NecessaryVariablesImpl;

public interface Action {
        public void invoke(NecessaryVariablesImpl context);
        public Operation getActionType();
        public EntityDefinition getContextEntity();
}
