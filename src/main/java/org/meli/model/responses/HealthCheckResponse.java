package org.meli.model.responses;

public class HealthCheckResponse {
    
    private String version;
    private String appName;
    private String status;

    public void setAppName(String appName){
        this.appName= appName;
    }

    public String getAppName(){
        return appName;
    }

    public void setVersion(String version){
        this.version=version;
    }

    public String getVersion(){
        return version;
    }
    
    public void setStatus(String status){
        this.status=status;
    }

    public String getStatus(){
        return status;
    }

}
