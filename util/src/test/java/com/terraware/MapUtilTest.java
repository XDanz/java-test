package com.terraware;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.reverse;
import static org.junit.Assert.assertEquals;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class MapUtilTest {

    @Test
    public void testSortAscByValue() {
        Map<Integer,String> map = new HashMap<>();
        map.put(1,"Xaniel");
        map.put(2, "Daniel");
        map.put(3, "Anna");
        Map<Integer, String> sortAscByValue = MapUtil.sortAscByValue(map);

        // A,D,X
        Integer expectedKey = map.size();
        for (Map.Entry<Integer, String> entry : sortAscByValue.entrySet()) {
            assertEquals(expectedKey--, entry.getKey());
        }
    }

    @Test
    public void testSortDescByValue() {
        Map<Integer,String> map = new HashMap<>();
        map.put(1,"Xaniel");
        map.put(2, "Daniel");
        map.put(3, "Anna");
        Map<Integer, String> sortAscByValue = MapUtil.sortDescByValue(map);

        // A,D,X
        Integer expectedKey = 1;
        for (Map.Entry<Integer, String> entry : sortAscByValue.entrySet()) {
            assertEquals(expectedKey++, entry.getKey());
        }
    }

    @Test
    public void testSortDescByValue2() {
        Map<Integer,String> map = new HashMap<>();
        map.put(1,"Xaniel");
        map.put(2, "Daniel");
        map.put(3, "Anna");

        map.keySet().stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
    }



    @Test
    public void test() {
        TradeReport[] trs  = {
          new TradeReport("1", TradeReportAction.SUBMIT),
          new TradeReport("1",  "1", TradeReportAction.CANCEL),
          new TradeReport("2", "1",  TradeReportAction.SUBMIT),
          new TradeReport("2", "1",  TradeReportAction.CANCEL),
          new TradeReport("3", "2",  TradeReportAction.SUBMIT),
          new TradeReport("3", "2",  TradeReportAction.CANCEL),
          new TradeReport("4", "3",  TradeReportAction.SUBMIT),
          new TradeReport("5",  TradeReportAction.SUBMIT),
          new TradeReport("5", "5", TradeReportAction.CANCEL),
          new TradeReport("6", "5", TradeReportAction.SUBMIT),
        };
        Map<String, List<Container<TradeReport>>> groupedTradeReports = TradeReport.getGroupedTradeReports(trs);
        assertEquals(2, groupedTradeReports.size());
        assertEquals(7, groupedTradeReports.get("1").size());
        assertEquals(3, groupedTradeReports.get("5").size());
        System.out.println("groupedTradeReports = " + groupedTradeReports);
    }

    @Test
    public void test2() {
        Map<String,List<Container>> map = new LinkedHashMap<>();
        map.put("bb", asList(new Container("BBtree"),new Container("BBtwo"),new Container("BBone") ));
        map.put("aa", asList(new Container("AAtree"),new Container("AAtwo"),new Container("AAone"),new Container("AAParent") ));
        map.put("cc", asList(new Container("CCtree"),new Container("CCtwo"),new Container("CCone") ));
        map.put("dd", asList(new Container("DDtree"),new Container("DDtwo"),new Container("DDone") ));

        List<Container> collect = map.entrySet().stream().peek(o -> reverse(o.getValue()))
                .map(o -> o.getValue().stream().reduce((container, container2) -> container.addChild(container2.getParent())))
                .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        System.out.println("collect = " + collect);
    }
}
