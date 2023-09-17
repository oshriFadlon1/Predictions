package dto;

public class DtoCountTickPopulation {
    private String Entity1Name;
    private int populationEntity1;
    private String Entity2Name;
    private int populationEntity2;
    private int currentTick;

    public DtoCountTickPopulation(String entity1Name, int populationEntity1, String entity2Name, int populationEntity2, int currentTick) {
        Entity1Name = entity1Name;
        this.populationEntity1 = populationEntity1;
        Entity2Name = entity2Name;
        this.populationEntity2 = populationEntity2;
        this.currentTick = currentTick;
    }

    public String getEntity1Name() {
        return Entity1Name;
    }

    public void setEntity1Name(String entity1Name) {
        Entity1Name = entity1Name;
    }

    public int getPopulationEntity1() {
        return populationEntity1;
    }

    public void setPopulationEntity1(int populationEntity1) {
        this.populationEntity1 = populationEntity1;
    }

    public String getEntity2Name() {
        return Entity2Name;
    }

    public void setEntity2Name(String entity2Name) {
        Entity2Name = entity2Name;
    }

    public int getPopulationEntity2() {
        return populationEntity2;
    }

    public void setPopulationEntity2(int populationEntity2) {
        this.populationEntity2 = populationEntity2;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }
}
