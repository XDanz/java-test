package com.terraware;

import java.util.function.Function;

public class MappingFunction implements Function<TradeReport, Container<TradeReport>> {
    @Override
    public Container<TradeReport> apply(TradeReport tradeReport) {
        return new Container<>(tradeReport);
    }
}
