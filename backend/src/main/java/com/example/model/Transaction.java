package com.example.model;

public class Transaction {
    private Long transactionId;
    private Long tradeId;
    private Integer version;
    private String securityCode;
    private Integer quantity;
    private String action; // INSERT, UPDATE, CANCEL
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
