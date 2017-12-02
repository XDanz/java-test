package com.terraware.trade;

import java.math.BigDecimal;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public interface TradeService {

    String addOrder(String orderBook, Side side, BigDecimal price, BigDecimal volume);

    void modifyOrder(String orderId, Side side, BigDecimal price, BigDecimal volume);

    void deleteOrder(String orderId);
}
