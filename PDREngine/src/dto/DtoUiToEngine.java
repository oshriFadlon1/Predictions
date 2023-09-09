package dto;

import java.util.Map;

public class DtoUiToEngine {
    private  Map<String, Object> environmentToValue;
    private int population1;
    private int population2;
    private String primaryEntityName;
    private String secondaryEntityName;

    public DtoUiToEngine(Map<String, Object> environmentToValue, int population1, int population2, String primaryEntName, String secondaryEntName) {
        this.environmentToValue = environmentToValue;
        this.population1 = population1;
        this.population2 = population2;
        this.primaryEntityName = primaryEntName;
        this.secondaryEntityName = secondaryEntName;
    }

    public Map<String, Object> getEnvironmentToValue() {
        return environmentToValue;
    }

    public void setEnvironmentToValue(Map<String, Object> environmentToValue) {
        this.environmentToValue = environmentToValue;
    }

    public int getPopulation1() {
        return population1;
    }

    public void setPopulation1(int population1) {
        this.population1 = population1;
    }

    public int getPopulation2() {
        return population2;
    }

    public void setPopulation2(int population2) {
        this.population2 = population2;
    }

    public String getPrimaryEntityName() {
        return primaryEntityName;
    }

    public String getSecondaryEntityName() {
        return secondaryEntityName;
    }
}
