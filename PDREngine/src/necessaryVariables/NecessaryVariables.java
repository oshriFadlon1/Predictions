package necessaryVariables;

import entity.EntityInstance;
import environment.EnvironmentInstance;

public interface NecessaryVariables {
    EntityInstance getPrimaryEntityInstance();
    void removeEntity(EntityInstance entityInstance);
    EnvironmentInstance getEnvironmentVariable(String name);
}
