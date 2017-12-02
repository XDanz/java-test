package com.terraware.trade;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class LevelInfo {
    private AtomicInteger orders = new AtomicInteger(0);
    private BigDecimal volume;

    public LevelInfo(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public int getOrders() {
        return orders.get();
    }

    public void addOrderCount() {
        orders.incrementAndGet();
    }

    public void removeOrderCount() {
        orders.decrementAndGet();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LevelInfo{");
        sb.append("orders=").append(orders);
        sb.append(", volume=").append(volume);
        sb.append('}');
        return sb.toString();
    }
}
