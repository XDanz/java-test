package com.terraware.trade;

import java.math.BigDecimal;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class Order {

    private BigDecimal volume;
    private BigDecimal price;
    private String securityId;
    private Side entryType;

    public Order(BigDecimal volume, BigDecimal price, String securityId, Side entryType) {
        this.volume = volume;
        this.price = price;
        this.securityId = securityId;
        this.entryType = entryType;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSecurityId() {
        return securityId;
    }

    public Side getEntryType() {
        return entryType;
    }
}
