
package com.example.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @NotNull(message = "tradeId is required")
    @Column(name = "trade_id")
    private Long tradeId;

    @NotNull(message = "version is required")
    @Min(value = 1, message = "version must be >= 1")
    @Column(name = "version")
    private Integer version;

    @NotBlank(message = "securityCode is required")
    @Column(name = "security_code")
    private String securityCode;

    @NotNull(message = "quantity is required")
    @Column(name = "quantity")
    private Integer quantity;

    @NotBlank(message = "action is required")
    @Column(name = "action")
    private String action; // INSERT, UPDATE, CANCEL

    @NotBlank(message = "direction is required")
    @Column(name = "direction")
    private String direction; // Buy, Sell

    public Transaction() {}

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
