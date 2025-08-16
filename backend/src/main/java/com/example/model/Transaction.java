package com.example.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class Transaction {
    private Long transactionId;

    @NotNull(message = "tradeId is required")
    private Long tradeId;

    @NotNull(message = "version is required")
    @Min(value = 1, message = "version must be >= 1")
    private Integer version;

    @NotBlank(message = "securityCode is required")
    private String securityCode;

    @NotNull(message = "quantity is required")
    private Integer quantity;

    @NotBlank(message = "action is required")
    private String action; // INSERT, UPDATE, CANCEL

    @NotBlank(message = "direction is required")
    private String direction; // Buy, Sell

    // Getters and Setters
    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public Long getTradeId() { return tradeId; }
    public void setTradeId(Long tradeId) { this.tradeId = tradeId; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getSecurityCode() { return securityCode; }
    public void setSecurityCode(String securityCode) { this.securityCode = securityCode; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
}
