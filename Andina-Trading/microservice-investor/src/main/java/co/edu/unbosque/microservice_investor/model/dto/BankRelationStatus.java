package co.edu.unbosque.microservice_investor.model.dto;


public class BankRelationStatus {
    private Boolean status;
    private String details;

    public BankRelationStatus(Boolean status, String details) {
        this.status = status;
        this.details = details;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
