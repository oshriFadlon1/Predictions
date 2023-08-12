package rule.action;

import entity.EntityDefinition;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;

public interface IAction {
        public void invoke(NecessaryVariablesImpl context) throws GeneralException;
        public Operation getOperationType();
        public EntityDefinition getContextEntity();
}