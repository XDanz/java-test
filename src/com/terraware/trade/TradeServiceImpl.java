package com.terraware.trade;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class TradeServiceImpl implements TradeService {

    private Map<String, OrderDepth> orderBooks = new ConcurrentHashMap<>();
    private Map<String, Order> orders = new ConcurrentHashMap<>();
    private AtomicInteger orderCount = new AtomicInteger(0);

    TradeServiceImpl() {
        orderBooks.put("2X7", new OrderDepth("2X7"));
        orderBooks.put("1DHN", new OrderDepth("1DHN"));
    }

    @Override
    public String addOrder(String orderBook, Side side, BigDecimal price, BigDecimal volume) {
        String orderId = null;
        if (orderBooks.containsKey(orderBook)) {
            orderId = generateOrderId(orderBook);
            orders.put(orderId, new Order(volume, price,orderBook, side));
            OrderDepth orderDepth = orderBooks.get(orderBook);
            orderDepth.add(side, price, volume);
            orderDepth.addOrder(price, side);
        }
        return orderId;
    }

    @Override
    public void modifyOrder(String orderId, Side side, BigDecimal price, BigDecimal volume) {
        if (orders.containsKey(orderId)) {
            Order order = orders.get(orderId);
            OrderDepth orderDepth = orderBooks.get(order.getSecurityId());
            if (!order.getVolume().equals(volume) && !order.getPrice().equals(price)) {
                handleVolumeAndPriceChange(side, price, volume, order, orderDepth);
            } else if (!order.getVolume().equals(volume)) {
                handleVolumeChange(side, price, volume, order, orderDepth);
            } else {
                handlePriceChange(side,price,volume,order,orderDepth);
            }
        }

    }

    private void handlePriceChange(Side side, BigDecimal price, BigDecimal volume,
                                   Order order,
                                   OrderDepth orderDepthInfo) {

        orderDepthInfo.add(side, price, volume);
        orderDepthInfo.addOrder(price, side);

        orderDepthInfo.subtract(side, price, volume);
        orderDepthInfo.removeOrder(price, side);

        order.setVolume(volume);
        order.setPrice(price);
    }

    private void handleVolumeChange(Side side, BigDecimal price, BigDecimal volume,
                                    Order oldOrder,
                                    OrderDepth orderDepthInfo) {
        // Volume has change..
        BigDecimal delta = oldOrder.getVolume().subtract(volume).abs();
        if (oldOrder.getVolume().compareTo(volume) > 0) {
            // the order has been decreased by delta amount, subtract the delta from
            // the total volume for that price
            orderDepthInfo.subtract(side, price, delta);

            // set the new volume on that order
            oldOrder.setVolume(volume);
        } else {
            // the order has been increased by delta amount, add the delta
            // to the total volume
            orderDepthInfo.add(side, price, delta);
            // set the new volume on that order
            oldOrder.setVolume(volume);
        }
    }


    private void handleVolumeAndPriceChange(Side side, BigDecimal price, BigDecimal volume,
                                            Order oldOrder,
                                            OrderDepth orderDepth) {
        // First add the new price and volume
        orderDepth.add(side, price, volume);
        orderDepth.addOrder(price, side);

        // Then we need to remove the old price and the old volume
        orderDepth.subtract(side, oldOrder.getPrice(), oldOrder.getVolume());
        orderDepth.removeOrder(oldOrder.getPrice(), oldOrder.getEntryType());

        oldOrder.setVolume(volume);
        oldOrder.setPrice(price);
    }

    @Override
    public void deleteOrder(String orderId) {

    }

    private String generateOrderId(String orderBook) {
        return orderBook + "+" + orderCount.incrementAndGet();
    }
}
