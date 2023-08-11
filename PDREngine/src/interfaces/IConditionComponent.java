package interfaces;

import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;

public interface IConditionComponent {
    boolean getResultFromCondition(NecessaryVariablesImpl context) throws GeneralException;
}
