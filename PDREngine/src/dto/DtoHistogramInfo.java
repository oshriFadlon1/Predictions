package dto;

import java.util.Map;

public class DtoHistogramInfo {
    Map<String, Integer> value2Count;
    private float avgInFinalPopulation;
    private float avgChangeInTicks;

    public DtoHistogramInfo(Map<String, Integer> value2Count, float avgInFinalPopulation, float avgChangeInTicks) {
        this.value2Count = value2Count;
        this.avgInFinalPopulation = avgInFinalPopulation;
        this.avgChangeInTicks = avgChangeInTicks;
    }

    public Map<String, Integer> getValue2Count() {
        return value2Count;
    }

    public void setValue2Count(Map<String, Integer> value2Count) {
        this.value2Count = value2Count;
    }

    public float getAvgInFinalPopulation() {
        return avgInFinalPopulation;
    }

    public void setAvgInFinalPopulation(float avgInFinalPopulation) {
        this.avgInFinalPopulation = avgInFinalPopulation;
    }

    public float getAvgChangeInTicks() {
        return avgChangeInTicks;
    }

    public void setAvgChangeInTicks(float avgChangeInTicks) {
        this.avgChangeInTicks = avgChangeInTicks;
    }
}
