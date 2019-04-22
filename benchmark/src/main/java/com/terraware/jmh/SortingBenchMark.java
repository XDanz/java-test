package com.terraware.jmh;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
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
    public void sortedSetInsert(Blackhole bh) {
        List<String> cpy = new ArrayList<>(DATA_FOR_TESTING);
        TreeSet<String> set = new TreeSet<>(cpy);
        Map<Character, List<String>> collect = set.stream().sorted().collect(Collectors.groupingBy(s -> s.charAt(0)));
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
