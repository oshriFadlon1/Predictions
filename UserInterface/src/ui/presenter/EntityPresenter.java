package ui.presenter;

public class EntityPresenter {
    private String entityName;
    private int population;

    public EntityPresenter(String entityName, int population) {
        this.entityName = entityName;
        this.population = population;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
