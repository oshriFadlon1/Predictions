package dto;

import pointCoord.PointCoord;

import java.util.List;

public class DtoResponsePreview {
    private DtoEnvironments dtoEnvironments;
    private List<DtoResponseEntities> dtoResponseEntities; //to change to list (for list to get the 2 entities)

    private List<DtoResponseRules> dtoResponseRules;
    private DtoResponsePreviewTermination dtoResponseTermination;
    private PointCoord worldSize;

    public DtoResponsePreview(DtoEnvironments dtoEnvironments, List<DtoResponseEntities> dtoResponseEntities, List<DtoResponseRules> dtoResponseRules, DtoResponsePreviewTermination dtoResponseTermination) {
        this.dtoEnvironments = dtoEnvironments;
        this.dtoResponseEntities = dtoResponseEntities;
        this.dtoResponseRules = dtoResponseRules;
        this.dtoResponseTermination = dtoResponseTermination;
        this.worldSize = new PointCoord(100,100);
    }

    public List<DtoResponseEntities> getDtoResponseEntities() {
        return dtoResponseEntities;
    }

    public void setDtoResponseEntities(List<DtoResponseEntities> dtoResponseEntities) {
        this.dtoResponseEntities = dtoResponseEntities;
    }

    public List<DtoResponseRules> getDtoResponseRules() {
        return dtoResponseRules;
    }

    public void setDtoResponseRules(List<DtoResponseRules> dtoResponseRules) {
        this.dtoResponseRules = dtoResponseRules;
    }

    public DtoResponsePreviewTermination getDtoResponseTermination() {
        return dtoResponseTermination;
    }

    public void setDtoResponseTermination(DtoResponsePreviewTermination dtoResponseTermination) {
        this.dtoResponseTermination = dtoResponseTermination;
    }

    public DtoEnvironments getDtoEnvironments() {
        return dtoEnvironments;
    }

    public void setDtoEnvironments(DtoEnvironments dtoEnvironments) {
        this.dtoEnvironments = dtoEnvironments;
    }

    public PointCoord getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(PointCoord worldSize) {
        this.worldSize = worldSize;
    }

    @Override
    public String toString() {
        return "Entities: " + dtoResponseEntities.toString() +
                "\r\nRules: " + dtoResponseRules.toString() +
                "\r\nTermination: " + dtoResponseTermination.toString();
    }
}
