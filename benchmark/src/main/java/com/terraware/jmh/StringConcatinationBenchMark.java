package com.terraware.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Thread)
@BenchmarkMode(Mode.SingleShotTime)
@Measurement(batchSize = 100000, iterations = 20)
@Warmup(batchSize = 100000, iterations = 10)
@Fork(5)
public class StringConcatinationBenchMark {

    private String string;
    private String stringConcat;
    private StringBuilder stringBuilder;
    private StringBuffer stringBuffer;

    @Setup(Level.Iteration)
    public void setup() {
        string = "";
        stringConcat = "";
        stringBuilder = new StringBuilder();
        stringBuffer = new StringBuffer();
    }

    @Benchmark
    public void stringConcatenation() {
        string += "some more data";
    }

    @Benchmark
    public void stringConcatConcatenation() {
        stringConcat = stringConcat.concat("some more data");
    }

    @Benchmark
    public void stringBuilderConcatenation() {
        stringBuilder.append("some more data");
    }

    @Benchmark
    public void stringBufferConcatenation() {
        stringBuffer.append("some more data");
    }
}
