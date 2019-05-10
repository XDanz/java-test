package com.terraware.jmh;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
public class DateSortingBenchMark {

    @Param({"1000"})
    private int N;
    private List<DataObj> dataObjs;
    private LocalDate localDate ;
    private int lim = 5;

    @Setup
    public void setup() {
        dataObjs = createData();
    }

    public List<DataObj> createData() {
        List<DataObj> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < N; i++) {
            LocalDate next = today.plusDays(i);
            list.add(new DataObj(next, "tt:" + i));
        }
        Collections.shuffle(list);
        localDate = today.plusDays(N/2);
        return list;
    }

    @Benchmark
    public void measureTreSet(Blackhole bh) {
        Collection<DataObj> collect = dataObjs.stream()
                .filter(o -> o.expire.isBefore(localDate))
                .collect(Collectors.toCollection(() -> {
                    Comparator<DataObj> comparing = Comparator.comparing(obj -> obj.expire);
                    Comparator<DataObj> reversed = comparing.reversed();
                    return new TreeSet<>(reversed);
                }));

        List<DataObj> collect1 = collect.stream().limit(lim).collect(Collectors.toList());
        bh.consume(collect1);
    }

    @Benchmark
    public void measureStream(Blackhole bh) {
        Collection<DataObj> collect = dataObjs.stream()
                .filter(o -> o.expire.isBefore(localDate))
                .sorted((o1, o2) -> -1*o1.expire.compareTo(o2.expire)).limit(lim).collect(Collectors.toList());

        bh.consume(collect);
    }

    @Benchmark
    public void measureProc(Blackhole bh) {
        Comparator<DataObj> comparing = Comparator.comparing(obj -> obj.expire);
        Comparator<DataObj> reversed = comparing.reversed();
        TreeSet<DataObj> objs = new TreeSet<>(reversed);
        for (DataObj obj : dataObjs) {
            if (obj.expire.isBefore(localDate)) objs.add(obj);
        }

        List<DataObj> list = new ArrayList<>();
        int i =0;
        for(Iterator<DataObj> iterator = objs.iterator(); iterator.hasNext() && i < lim;) {
            list.add(iterator.next());
            i++;
        }
        bh.consume(list);
    }

//    public static void main(String[] args) throws RunnerException {
//        Options opt = new OptionsBuilder()
//                .include(".*" + DateSortingBenchMark.class.getSimpleName() + ".*")
//                .forks(0)
//                .build();
//
//        new Runner(opt).run();
//    }

    private static class DataObj {
        LocalDate expire;
        String name;

        public DataObj(final LocalDate expire, final String name) {
            this.expire = expire;
            this.name = name;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", DataObj.class.getSimpleName() + "[", "]")
                    .add("expire=" + expire)
                    .add("name='" + name + "'")
                    .toString();
        }
    }
}
