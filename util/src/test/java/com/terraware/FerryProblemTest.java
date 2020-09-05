package com.terraware;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.terraware.FerryProblem.getNextArrivalTime;
import static com.terraware.FerryProblem.getNextEvents;
import static org.assertj.core.api.Assertions.assertThat;

public class FerryProblemTest {

    @ParameterizedTest
    @MethodSource("provider")
    void get_next_arrivalTime(int idx, int expected) {
        FerryProblem.Evt[] events = new FerryProblem.Evt[]
            {
                null, // 0
                null, // 1
                new FerryProblem.Evt(5, FerryProblem.Dir.LEFT), // 2
                new FerryProblem.Evt(5, FerryProblem.Dir.LEFT), // 3
                new FerryProblem.Evt(6, FerryProblem.Dir.LEFT),// 4
                new FerryProblem.Evt(7, FerryProblem.Dir.LEFT),// 5
                null, //6
                new FerryProblem.Evt(8, FerryProblem.Dir.LEFT), //7
                new FerryProblem.Evt(9, FerryProblem.Dir.LEFT), // 8
                new FerryProblem.Evt(10, FerryProblem.Dir.LEFT), // 9
            };
        int res = getNextArrivalTime(events, idx);
        assertThat(res).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provider2")
    void getNextEvents_test(int ctime, List<Integer> expected) {
        FerryProblem.Evt[] events = new FerryProblem.Evt[]
            {
                null, // 0
                null, // 1
                new FerryProblem.Evt(5, FerryProblem.Dir.LEFT), // 2
                new FerryProblem.Evt(5, FerryProblem.Dir.LEFT), // 3
                new FerryProblem.Evt(6, FerryProblem.Dir.LEFT),// 4
                new FerryProblem.Evt(7, FerryProblem.Dir.LEFT),// 5
                null, //6
                new FerryProblem.Evt(8, FerryProblem.Dir.LEFT), //7
                new FerryProblem.Evt(9, FerryProblem.Dir.LEFT), // 8
                new FerryProblem.Evt(10, FerryProblem.Dir.LEFT), // 9
            };
        List<Integer> res = getNextEvents(events, ctime, 0);
        assertThat(res).isEqualTo(expected);
    }
    private static Stream<Arguments> provider2() {
        return Stream.of(
            Arguments.of(0, List.of()),
            Arguments.of(1, List.of()),
            Arguments.of(2, List.of()),
            Arguments.of(4, List.of()),
            Arguments.of(5, List.of(2,3)),
            Arguments.of(6, List.of(2,3,4)),
            Arguments.of(7, List.of(2,3,4,5)),
            Arguments.of(8, List.of(2,3,4,5,7))
//                ,
//            Arguments.of(3, 5),
//            Arguments.of(4, 6),
//            Arguments.of(5, 7),
//            Arguments.of(6, 8),
//            Arguments.of(7, 8),
//            Arguments.of(8, 9),
//            Arguments.of(9, 10),
//            Arguments.of(10, -1)
            );
    }

    private static Stream<Arguments> provider() {
        return Stream.of(
            Arguments.of(0, 5),
            Arguments.of(1, 5),
            Arguments.of(2, 5),
            Arguments.of(3, 5),
            Arguments.of(3, 5),
            Arguments.of(4, 6),
            Arguments.of(5, 7),
            Arguments.of(6, 8),
            Arguments.of(7, 8),
            Arguments.of(8, 9),
            Arguments.of(9, 10),
            Arguments.of(10, -1));
    }


}



