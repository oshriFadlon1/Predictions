package potentialerror;

import entity.EntityDefinition;
import property.PropertyDefinition;
import property.PropertyDefinitionEntity;

import javax.swing.text.html.parser.Entity;

public class PotentionalError {
    private String errorMsg;
    private EntityDefinition entittyDef;
    private PropertyDefinitionEntity propertyDef;

    public PotentionalError(){this.errorMsg = "";}

    public String getErrorMsg(){
        return this.errorMsg;
    }

    public PropertyDefinitionEntity getPropertyDef(){
        return this.propertyDef;
    }

    public EntityDefinition getEntittyDef(){
        return this.entittyDef;
    }

    public void setErrorMsg(String errorMsg){
        this.errorMsg = errorMsg;
    }

    public void setEntittyDef(EntityDefinition entityDef){
        this.entittyDef = entityDef;
    }

    public void setPropertyDef(PropertyDefinitionEntity propDef){
        this.propertyDef = propDef;
    }

}
