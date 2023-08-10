package dto;

import java.util.List;

public class DtoResponsePreview {
    private DtoResponseEntities dtoResponseEntities;

    private List<DtoResponseRules> dtoResponseRules;
    private DtoResponsePreviewTermination dtoResponseTermination;

    public DtoResponsePreview(DtoResponseEntities dtoResponseEntities, List<DtoResponseRules> dtoResponseRules, DtoResponsePreviewTermination dtoResponseTermination) {
        this.dtoResponseEntities = dtoResponseEntities;
        this.dtoResponseRules = dtoResponseRules;
        this.dtoResponseTermination = dtoResponseTermination;
    }

    public DtoResponseEntities getDtoResponseEntities() {
        return dtoResponseEntities;
    }

    public void setDtoResponseEntities(DtoResponseEntities dtoResponseEntities) {
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

    @Override
    public String toString() {
        return "Entities: " + dtoResponseEntities.toString() +
                "\r\nRules: " + dtoResponseRules.toString() +
                "\r\nTermination: " + dtoResponseTermination.toString();
    }
}
