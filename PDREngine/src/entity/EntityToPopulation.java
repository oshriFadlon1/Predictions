package entity;

public class EntityToPopulation {
    private EntityDefinition currEntityDef;
    private int currEntityPopulation;

    public EntityToPopulation(EntityDefinition currEntityDef, int currEntityPopulation) {
        this.currEntityDef = currEntityDef;
        this.currEntityPopulation = currEntityPopulation;
    }

    public EntityDefinition getCurrEntityDef() {
        return currEntityDef;
    }

    public void setCurrEntityDef(EntityDefinition currEntityDef) {
        this.currEntityDef = currEntityDef;
    }

    public int getCurrEntityPopulation() {
        return currEntityPopulation;
    }

    public void setCurrEntityPopulation(int currEntityPopulation) {
        this.currEntityPopulation = currEntityPopulation;
    }

    @Override
    public String toString() {
        return "EntityToPopulation{" +
                "currEntityDef=" + currEntityDef +
                ", currEntityPopulation=" + currEntityPopulation +
                '}';
    }
}
