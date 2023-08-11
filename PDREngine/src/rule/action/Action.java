package rule.action;

import entity.EntityDefinition;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;

public interface Action {
        public void invoke(NecessaryVariablesImpl context) throws GeneralException;
        public Operation getActionType();
        public EntityDefinition getContextEntity();
}