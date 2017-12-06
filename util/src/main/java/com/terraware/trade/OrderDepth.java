package com.terraware.trade;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class OrderDepth {

    private String securityId;
    private NavigableMap<BigDecimal, LevelInfo> bids;
    private NavigableMap<BigDecimal, LevelInfo> offers;

    OrderDepth(String securityId) {
        this.securityId = Objects.requireNonNull(securityId, "spaceOrderDepthInfoId");
        this.bids = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
        this.offers = new ConcurrentSkipListMap<>(Comparator.naturalOrder());
    }

    public String getId() {
        return securityId;
    }

    public String getSpaceOrderDepthInfoId() {
        return securityId;
    }

    private void setSpaceOrderDepthInfoId(String securityId) {
        this.securityId = securityId;
    }

    /**
     * Add the volume amount from the order with ngmMdEntryType (BID/OFFER)
     * to the specified price.
     *
     * @param side BID or OFFER
     * @param price The price
     * @param volume The amount of volume to remove from the order
     */
    public void add(Side side, BigDecimal price, BigDecimal volume) {
        NavigableMap<BigDecimal, LevelInfo> pricesByEntryType = getSide(side);
        if (!Objects.isNull(pricesByEntryType)) {
            if (pricesByEntryType.containsKey(price)) {
                LevelInfo levelInfo = pricesByEntryType.get(price);
                levelInfo.setVolume(levelInfo.getVolume().add(volume));
            } else {
                pricesByEntryType.put(price, new LevelInfo(volume));
            }
        }
    }

    public Integer getOrders(BigDecimal price, Side ngmMdSide) {
        NavigableMap<BigDecimal, LevelInfo> pricesByEntryType = getSide(ngmMdSide);
        if (!Objects.isNull(pricesByEntryType)) {
            if (pricesByEntryType.containsKey(price)) {
                return pricesByEntryType.get(price).getOrders();
            }
        }
        return 0;
    }

    public void addOrder(BigDecimal price, Side ngmMdSide) {
        NavigableMap<BigDecimal, LevelInfo> pricesByEntryType = getSide(ngmMdSide);
        if (!Objects.isNull(pricesByEntryType)) {
            if (pricesByEntryType.containsKey(price)) {
                LevelInfo levelInfo = pricesByEntryType.get(price);
                levelInfo.addOrderCount();
            }
        }
    }

    public void removeOrder(BigDecimal price, Side side) {
        NavigableMap<BigDecimal, LevelInfo> pricesByEntryType = getSide(side);
        if (!Objects.isNull(pricesByEntryType)) {
            if (pricesByEntryType.containsKey(price)) {
                LevelInfo levelInfo = pricesByEntryType.get(price);
                levelInfo.removeOrderCount();
            }
        }
    }

    /**
     * Subtract the volume amount from the order with ngmMdEntryType (BID/OFFER)
     * to the specified price.
     *
     * @param side BID or OFFER
     * @param price The price
     * @param volume The amount of volume to remove from the order
     */
    public void subtract(Side side, BigDecimal price, BigDecimal volume) {
        NavigableMap<BigDecimal, LevelInfo> pricesByEntryType = getSide(side);
        if (!Objects.isNull(pricesByEntryType)) {
            if (pricesByEntryType.containsKey(price)) {
                LevelInfo levelInfo = pricesByEntryType.get(price);
                levelInfo.setVolume(levelInfo.getVolume().subtract(volume));
            } else {
                pricesByEntryType.put(price, new LevelInfo(volume));
            }

            if (pricesByEntryType.get(price).getVolume().equals(BigDecimal.ZERO)) {
                pricesByEntryType.remove(price);
            } else if (pricesByEntryType.get(price).getVolume().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalStateException(" Big time error! delta volume could not be negative");
            }
        }
    }

    public NavigableMap<BigDecimal,LevelInfo> getSide(Side side) {
        switch (side) {
            case BID:
                return getBids();
            case OFFER:
                return getOffers();
            default:
                return null;
        }
    }

    public NavigableMap<BigDecimal, LevelInfo> getBids() {
        return bids;
    }

    private void setBids(NavigableMap<BigDecimal, LevelInfo> bids) {
        this.bids = bids;
    }

    public NavigableMap<BigDecimal, LevelInfo> getOffers() {
        return offers;
    }

    private void setOffers(NavigableMap<BigDecimal, LevelInfo> offers) {
        this.offers = offers;
    }


}
