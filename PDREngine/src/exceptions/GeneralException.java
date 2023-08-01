package exceptions;

public class GeneralException extends Exception{

    private String errorMsg;


    public GeneralException(String errorMsg) {
        this.errorMsg = errorMsg;
    }


    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
