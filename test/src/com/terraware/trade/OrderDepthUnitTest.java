package com.terraware.trade;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.NavigableMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class OrderDepthUnitTest {

    private BigDecimal _1_25 = BigDecimal.valueOf(1.25d);
    private BigDecimal _10 = BigDecimal.valueOf(10L);
    private BigDecimal _20 = BigDecimal.valueOf(20L);

    @Test
    void testAddOneOrder() {
        OrderDepth orderDepth = new OrderDepth("2X7");
        orderDepth.add(Side.BID, _1_25, _10);
        orderDepth.addOrder(_1_25, Side.BID);

        NavigableMap<BigDecimal, LevelInfo> bids = orderDepth.getSide(Side.BID);
        assertNotNull(bids);
        assertEquals(1, bids.size());
        assertEquals(Integer.valueOf(1), orderDepth.getOrders(_1_25, Side.BID));
        assertEquals(_10, bids.get(_1_25).getVolume());

        orderDepth.add(Side.BID, _1_25, _10);
        orderDepth.addOrder(_1_25, Side.BID);
        assertEquals(1, bids.size());
        assertEquals(Integer.valueOf(2), orderDepth.getOrders(_1_25, Side.BID));
        assertEquals(_20, bids.get(_1_25).getVolume());



//
//        orderDepth.add(Side.BID, BigDecimal.valueOf(1.25d), BigDecimal.valueOf(10L));
//
//        orderDepth.add(Side.BID, BigDecimal.valueOf(1.35d), BigDecimal.valueOf(100L));
//        NavigableMap<BigDecimal, LevelInfo> pricesByEntryType = orderDepth.getSide(Side.BID);
//
//        for (Map.Entry<BigDecimal, LevelInfo> bigDecimalLevelInfoEntry : pricesByEntryType.entrySet()) {
//            System.out.println("bigDecimalLevelInfoEntry = " + bigDecimalLevelInfoEntry);
//
//        }

    }
}
