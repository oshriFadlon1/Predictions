package dto;

public class DtoResponse {
    private String response;
    private boolean isSucceeded;

    public DtoResponse(String response, boolean isSucceeded) {
        this.response = response;
        this.isSucceeded = isSucceeded;
    }

    public String getResponse() {
        return response;
    }

    public boolean isSucceeded() {
        return isSucceeded;
    }
}
