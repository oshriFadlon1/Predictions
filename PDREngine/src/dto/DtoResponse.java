package dto;

import world.World;

public class DtoResponse {
    private World currentWorldSimulation;
    private String response;

    public DtoResponse(World currentWorldSimulation, String response) {
        this.currentWorldSimulation = currentWorldSimulation;
        this.response = response;
    }

    public DtoResponse(String response) {
        this.currentWorldSimulation = null;
        this.response = response;
    }

    public World getCurrentWorldSimulation() {
        return currentWorldSimulation;
    }

    public void setCurrentWorldSimulation(World currentWorldSimulation) {
        this.currentWorldSimulation = currentWorldSimulation;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
