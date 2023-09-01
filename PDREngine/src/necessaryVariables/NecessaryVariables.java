package necessaryVariables;

import createAndKillEntities.CreateAndKillEntities;
import entity.EntityDefinition;
import entity.EntityInstance;
import enums.CreationType;
import environment.EnvironmentInstance;

public interface NecessaryVariables {
    EntityInstance getPrimaryEntityInstance();
    EntityInstance getSecondaryEntityInstance();
    void removeEntity(EntityInstance entityInstance);
    EnvironmentInstance getEnvironmentVariable(String name);
    void killAndCreateEntity(EntityInstance entityInstance, EntityDefinition secondatyInstance, CreationType creationType);

    EntityInstance getEntityToKill();
    CreateAndKillEntities getEntityToKillAndCreate();

    void resetKillAndCreateAndKill();
}
