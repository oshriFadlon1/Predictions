package entity;

public class EntityDefinition {
    private String entityName;
    private int startPopulation;
    private int endPopulation;

    public EntityDefinition(String name, int population) {
        this.entityName = name;
        this.startPopulation = population;
        this.endPopulation = startPopulation;

    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getStartPopulation() {
        return startPopulation;
    }

    public int getEndPopulation() {
        return endPopulation;
    }

    public void setEndPopulation(int endPopulation) {
        this.endPopulation = endPopulation;
    }

    public void setStartPopulation(int startPopulation) {
        this.startPopulation = startPopulation;
    }


    @Override
    public String toString() {
        return "Name: " + entityName +
                "\npopulation: " + startPopulation;
    }
}
