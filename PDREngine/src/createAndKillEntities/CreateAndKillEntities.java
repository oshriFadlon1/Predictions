package createAndKillEntities;

import entity.EntityDefinition;
import entity.EntityInstance;
import enums.CreationType;

public class CreateAndKillEntities {
    private EntityInstance Kill;
    private EntityDefinition create;
    private CreationType creationType;

    public CreateAndKillEntities(EntityInstance kill, EntityDefinition create, CreationType creationType) {
        this.Kill = kill;
        this.create = create;
        this.creationType = creationType;
    }
    public CreateAndKillEntities() {
        this.Kill = null;
        this.create = null;
        this.creationType = null;
    }

    public EntityInstance getKill() {
        return Kill;
    }

    public void setKill(EntityInstance kill) {
        Kill = kill;
    }

    public EntityDefinition getCreate() {
        return create;
    }

    public void setCreate(EntityDefinition create) {
        this.create = create;
    }

    public CreationType getCreationType() {
        return creationType;
    }

    public void setCreationType(CreationType creationType) {
        this.creationType = creationType;
    }

    public void resetCreateAndKill(){
        this.Kill = null;
        this.create = null;
        this.creationType = null;
    }
}
