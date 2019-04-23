package com.terraware.jmh;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
public class SortingBenchMark {

    private List<String> DATA_FOR_TESTING;

    @Setup
    public void setup() {
        DATA_FOR_TESTING = createData();
    }

    @Ignore
    @Benchmark
    public void sortFirst(Blackhole bh) {
        List<String> cpy = new ArrayList<>(DATA_FOR_TESTING);
        Collections.sort(cpy);
        Map<Character, List<String>> collect = cpy.stream().collect(Collectors.groupingBy(s -> s.charAt(0)));
        bh.consume(collect);
    }

    @Ignore
    @Benchmark
    public void loopWhile(Blackhole bh) {
        List<String> cpy = new ArrayList<>(DATA_FOR_TESTING);
        Map<Character, List<String>> collect = cpy.stream().sorted().collect(Collectors.groupingBy(s -> s.charAt(0)));
        bh.consume(collect);
    }

    @Benchmark
    public void customCollector(Blackhole bh) {
        List<String> cpy = new ArrayList<>(DATA_FOR_TESTING);
        Map<Character, List<String>> collect = cpy.stream().collect(Collectors.groupingBy(s -> s.charAt(0),
                TreeMap::new,
                new Collector<String, TreeSet<String>, List<String>>() {
                    @Override
                    public Supplier<TreeSet<String>> supplier() {
                        return TreeSet::new;
                    }

                    @Override
                    public BiConsumer<TreeSet<String>, String> accumulator() {
                        return TreeSet::add;
                    }

                    @Override
                    public BinaryOperator<TreeSet<String>> combiner() {
                        return (strings, strings2) -> {
                            strings.addAll(strings2);
                            return strings;
                        };
                    }

                    @Override
                    public Function<TreeSet<String>, List<String>> finisher() {
                        return ArrayList::new;
                    }

                    @Override
                    public Set<Characteristics> characteristics() {
                        return EnumSet.of(Characteristics.UNORDERED);
                    }
                }));
        bh.consume(collect);
    }

    @Benchmark
    public void plainOld(Blackhole bh) {
        List<String> cpy = new ArrayList<>(DATA_FOR_TESTING);

        Map<Character, SortedSet<String>> map = new TreeMap<>();

        for (String s : cpy) {
            char c = s.charAt(0);
            map.compute(c, (character, strings) -> {
                SortedSet<String> list = new TreeSet<>();
                if(strings != null) {
                    strings.add(s);
                    list = strings;
                }
                return list;
            });
        }
        Map<Character, List<String>> map2 = new TreeMap<>();

        for (Map.Entry<Character, SortedSet<String>> entry : map.entrySet()) {
            map2.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        bh.consume(map2);
    }

    @Benchmark
    public void plainOld2(Blackhole bh) {
        List<String> cpy = new ArrayList<>(DATA_FOR_TESTING);

        SortedSet<String> map = new TreeSet<>(cpy);
        Map<Character, List<String>> map2 = new TreeMap<>();
        for (String s : map) {
            char c = s.charAt(0);
            map2.compute(c, (character, strings) -> {
                List<String> list = new ArrayList<>();
                if(strings != null) {
                    strings.add(s);
                    list = strings;
                }
                return list;
            });
        }
        bh.consume(map2);
    }

    @Benchmark
    public void sortedSetInsert(Blackhole bh) {
        List<String> cpy = new ArrayList<>(DATA_FOR_TESTING);
        TreeSet<String> set = new TreeSet<>(cpy);
        Map<Character, List<String>> collect = set.stream().collect(Collectors.groupingBy(s -> s.charAt(0)));
        bh.consume(collect);
    }

    @Benchmark
    public void sortedSetInsert2(Blackhole bh) {
        List<String> cpy = new ArrayList<>(DATA_FOR_TESTING);
        TreeSet<String> set = new TreeSet<>(cpy);
        Map<Character, List<String>> collect = new HashMap<>();
        for (String s : set) {
            collect.computeIfAbsent(s.charAt(0), k -> new ArrayList<>()).add(s);
        }
        bh.consume(collect);
    }

    private List<String> createData() {
        List<String> data = null;
        try {
            data = Files.lines(Paths.get("/home/daniel/wlist")).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
