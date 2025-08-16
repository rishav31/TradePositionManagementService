
package com.example.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "positions")
public class Position {
    @Id
    @Column(name = "security_code", nullable = false, unique = true)
    private String securityCode;

    @Column(name = "net_quantity")
    private Integer netQuantity;

    public Position() {}

    public Position(String securityCode, Integer netQuantity) {
        this.securityCode = securityCode;
        this.netQuantity = netQuantity;
    }

    public String getSecurityCode() { return securityCode; }
    public void setSecurityCode(String securityCode) { this.securityCode = securityCode; }
    public Integer getNetQuantity() { return netQuantity; }
    public void setNetQuantity(Integer netQuantity) { this.netQuantity = netQuantity; }
}
