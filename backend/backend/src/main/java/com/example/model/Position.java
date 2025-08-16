package com.example.model;

public class Position {
    private String securityCode;
    private Integer netQuantity;

    public Position(String securityCode, Integer netQuantity) {
        this.securityCode = securityCode;
        this.netQuantity = netQuantity;
    }

    public String getSecurityCode() { return securityCode; }
    public void setSecurityCode(String securityCode) { this.securityCode = securityCode; }
    public Integer getNetQuantity() { return netQuantity; }
    public void setNetQuantity(Integer netQuantity) { this.netQuantity = netQuantity; }
}
