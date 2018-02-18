package com.terraware;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class TradeReport {
    private String transId;
    private String originalTransId;
    private TradeReportAction action;


    public TradeReport(String transId, String originalTransId, TradeReportAction action) {
        this.transId = transId;
        this.action = action;
        this.originalTransId = originalTransId;
    }

    public TradeReport(String transId, TradeReportAction action) {
        this(transId,   null, action);
    }

    public static Map<String, List<Container<TradeReport>>> getGroupedTradeReports(TradeReport[] tradeReports) {
        Map<String,String> link = new HashMap<>();
        return Arrays.stream(tradeReports).map(new MappingFunction())
                .collect(groupingBy(o -> classify(o, link)));
    }

    @Override
    public String toString() {
        return "TradeReport{" +
                "transId='" + transId + '\'' +
                ", originalTransId='" + originalTransId + '\'' +
                ", action=" + action +
                '}';
    }

    private static String classify(Container<TradeReport> container, Map<String, String> link) {
        TradeReport tr = container.getParent();
        String groupKey;
        if (isReferred(tr)) {
            link.putIfAbsent(tr.transId, (groupKey = getGroupValue(link, tr)));
        } else {
            groupKey = tr.transId;
        }
        return groupKey;
    }

    private static boolean isReferred(TradeReport tr) {
        return tr.originalTransId != null & !Objects.equals(tr.originalTransId, tr.transId);
    }

    private static String getGroupValue(Map<String, String> link, TradeReport tr) {
        return (!link.containsKey(tr.originalTransId)) ? tr.originalTransId : link.get(tr.originalTransId);
    }
}
