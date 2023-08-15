package dto;

public class DtoResponseSimulationEnded {
    private DtoResponseTermination endingCause;
    private int simulationId;
    private String errorMsg;//in case of an error while running the simulation

    public DtoResponseSimulationEnded(DtoResponseTermination endingCause, int simulationId) {
        this.endingCause = endingCause;
        this.simulationId = simulationId;
    }

    public DtoResponseSimulationEnded(String errorMsg){this.errorMsg = errorMsg;}

    public DtoResponseTermination getEndingCause() {
        return endingCause;
    }

    public void setEndingCause(DtoResponseTermination endingCause) {
        this.endingCause = endingCause;
    }

    public int getSimulationId() {
        return simulationId;
    }

    public void setSimulationId(int simulationId) {
        this.simulationId = simulationId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
