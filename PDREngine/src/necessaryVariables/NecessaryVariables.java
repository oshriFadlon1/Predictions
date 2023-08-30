package necessaryVariables;

import entity.EntityInstance;
import enums.CreationType;
import environment.EnvironmentInstance;

public interface NecessaryVariables {
    EntityInstance getPrimaryEntityInstance();
    void removeEntity(EntityInstance entityInstance);
    EnvironmentInstance getEnvironmentVariable(String name);
    void killAndCreateEntity(EntityInstance entityInstance, String secondatyInstance, CreationType creationType);
}
