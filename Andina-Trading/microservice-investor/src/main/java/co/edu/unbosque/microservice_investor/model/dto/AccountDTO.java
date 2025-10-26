package co.edu.unbosque.microservice_investor.model.dto;

public class AccountDTO {

    private String accountId;     // ID de la cuenta en Alpaca
    private String status;        // Estado de la cuenta (ACTIVE, etc.)
    private String message;       // Mensaje descriptivo
    private boolean success;      // Indicador de éxito

    // Constructor vacío
    public AccountDTO() {}

    // Constructor con todos los campos
    public AccountDTO(String accountId, String status, String message, boolean success) {
        this.accountId = accountId;
        this.status = status;
        this.message = message;
        this.success = success;
    }

    // Getters y Setters
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

