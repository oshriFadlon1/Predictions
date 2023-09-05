package rule.action;

import dto.DtoActionResponse;
import entity.EntityDefinition;
import entity.SecondEntity;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;

public interface IAction {
         void invoke(NecessaryVariablesImpl context) throws GeneralException;
         Operation getOperationType();
         EntityDefinition getContextEntity();
         void SetSecondEntity(SecondEntity secondEntity);
         SecondEntity getSecondaryEntity();

         DtoActionResponse getActionResponse();

}