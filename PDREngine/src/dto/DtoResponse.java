package dto;

import world.WorldInstance;

public class DtoResponse {
    private WorldInstance currentWorldSimulation;
    private String response;

    public DtoResponse(WorldInstance currentWorldSimulation, String response) {
        this.currentWorldSimulation = currentWorldSimulation;
        this.response = response;
    }

    public DtoResponse(String response) {
        this.currentWorldSimulation = null;
        this.response = response;
    }

    public WorldInstance getCurrentWorldSimulation() {
        return currentWorldSimulation;
    }

    public void setCurrentWorldSimulation(WorldInstance currentWorldSimulation) {
        this.currentWorldSimulation = currentWorldSimulation;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
