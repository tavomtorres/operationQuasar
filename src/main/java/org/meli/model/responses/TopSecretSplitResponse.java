package org.meli.model.responses;

public class TopSecretSplitResponse {
    private String message;

    private int httpStatus;

    public void setHttpStatus(int status){
        this.httpStatus= status;
    }

    public int getHttpStatus(){
        return httpStatus;
    }

    public void setMessage(String message){
        this.message=message;
    }

    public String getMessage(){
        return message;
    }

}
