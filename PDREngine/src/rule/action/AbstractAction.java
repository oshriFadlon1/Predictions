package rule.action;

import entity.EntityDefinition;
import entity.SecondEntity;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;

import javax.swing.text.html.parser.Entity;
import java.io.Serializable;

public abstract class AbstractAction implements IAction, Serializable {
    private final Operation operationType;
    private final EntityDefinition entityDefinition;
    private SecondEntity secondaryEntity;

    public AbstractAction(Operation operationType, EntityDefinition entityDefinition) {
        this.operationType = operationType;
        this.entityDefinition = entityDefinition;
        this.secondaryEntity = null;
    }

    public AbstractAction(Operation operationType, EntityDefinition entityDefinition, SecondEntity secondaryEntity) {
        this.operationType = operationType;
        this.entityDefinition = entityDefinition;
        this.secondaryEntity = secondaryEntity;
    }

    public SecondEntity getSecondaryEntity() {
        return secondaryEntity;
    }


    public Operation getOperationType() {
        return this.operationType;
    }

    public EntityDefinition getEntityDefinition() {
        return entityDefinition;
    }

    public abstract void invoke(NecessaryVariablesImpl context) throws GeneralException;

    @Override
    public void SetSecondEntity(SecondEntity secondEntity) {
        this.secondaryEntity = secondEntity;
    }
}
