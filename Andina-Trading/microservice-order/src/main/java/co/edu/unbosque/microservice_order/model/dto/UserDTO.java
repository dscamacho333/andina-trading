package co.edu.unbosque.microservice_order.model.dto;


import co.edu.unbosque.microservice_order.model.enums.AccountStatus;
import co.edu.unbosque.microservice_order.model.enums.OrderType;
import co.edu.unbosque.microservice_order.model.enums.Role;

import java.time.LocalDateTime;

public class UserDTO {

    private Integer userId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String passwordHash;
    private Role role;
    private AccountStatus accountStatus;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccess;
    private Double commissionRate;
    private Boolean hasSubscription;
    private String alpacaStatus;
    private String alpacaAccountId;

    private Integer dailyOrderLimit;
    private OrderType defaultOrderType;
    private String balance;
    private String bankRelationshipId;


    public UserDTO() {
    }

    // Constructor con todos los campos
    public UserDTO(Integer id, String name, String email, String phone, Role role,
                   AccountStatus accountStatus, LocalDateTime createdAt, LocalDateTime lastAccess,
                   Double commissionRate, Boolean hasSubscription, String alpacaStatus,
                   String alpacaAccountId, Integer dailyOrderLimit, OrderType defaultOrderType) {
        this.userId = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.accountStatus = accountStatus;
        this.createdAt = createdAt;
        this.lastAccess = lastAccess;
        this.commissionRate = commissionRate;
        this.hasSubscription = hasSubscription;
        this.alpacaStatus = alpacaStatus;
        this.alpacaAccountId = alpacaAccountId;
        this.dailyOrderLimit = dailyOrderLimit;
        this.defaultOrderType = defaultOrderType;
        this.password = password;
        this.passwordHash = passwordHash;
    }

    // Getters and setters...


    public String getBankRelationshipId() {
        return bankRelationshipId;
    }

    public void setBankRelationshipId(String bankRelationshipId) {
        this.bankRelationshipId = bankRelationshipId;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Integer getId() {
        return userId;
    }

    public void setId(Integer id) {
        this.userId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(LocalDateTime lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(Double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Boolean getHasSubscription() {
        return hasSubscription;
    }

    public void setHasSubscription(Boolean hasSubscription) {
        this.hasSubscription = hasSubscription;
    }

    public String getAlpacaStatus() {
        return alpacaStatus;
    }

    public void setAlpacaStatus(String alpacaStatus) {
        this.alpacaStatus = alpacaStatus;
    }

    public String getAlpacaAccountId() {
        return alpacaAccountId;
    }

    public void setAlpacaAccountId(String alpacaAccountId) {
        this.alpacaAccountId = alpacaAccountId;
    }

    public Integer getDailyOrderLimit() {
        return dailyOrderLimit;
    }

    public void setDailyOrderLimit(Integer dailyOrderLimit) {
        this.dailyOrderLimit = dailyOrderLimit;
    }

    public OrderType getDefaultOrderType() {
        return defaultOrderType;
    }

    public void setDefaultOrderType(OrderType defaultOrderType) {
        this.defaultOrderType = defaultOrderType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }


}
