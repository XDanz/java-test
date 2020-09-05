package com.terraware;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringJoiner;

public class FerryProblem {

    enum Dir {
        LEFT,
        RIGHT
    }

    static class Evt {
        int arrival;
        Dir direction;

        public Evt(final int arrival, final Dir direction) {
            this.arrival = arrival;
            this.direction = direction;
        }

        public int getArrival() {
            return arrival;
        }

        public Dir getDirection() {
            return direction;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Evt evt = (Evt) o;
            return arrival == evt.arrival &&
                direction == evt.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(arrival, direction);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Evt.class.getSimpleName() + "[", "]")
                .add("arrival=" + arrival)
                .add("direction=" + direction)
                .toString();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int nCases = sc.nextInt();
        for (int i = 0; i < nCases; i++) {

            int cap = sc.nextInt();
            int time = sc.nextInt();
            int arrivals = sc.nextInt();

            Evt[] evts = new Evt[arrivals];
            for (int j = 0; j < arrivals; j++) {
                int arrival = sc.nextInt();
                String dir = sc.next();

                Evt ev = new Evt(arrival, dir.equals("left") ? Dir.LEFT : Dir.RIGHT);
                evts[j] = ev;
            }

            int[] output = trans2(evts, cap, time);

            for (int e : output) {
                System.out.println(e);
            }
            if (i < nCases - 1)
                System.out.println();
        }

    }

    static int[] trans2(Evt[] input, int cap, int time) {
        int[] output = new int[input.length];
        Deque<Integer> cargo = new ArrayDeque<>(cap);

        int ctime = 0;
        Dir curDir = Dir.LEFT;

        int minIdx = 0;
        int nextEvtTime = -1;
        while ((nextEvtTime = getNextArrivalTime(input,0 )) != -1) {

            while (!cargo.isEmpty()) {
                Integer pos = cargo.pop();
                output[pos] = ctime;
                input[pos] = null;
            }

            if (nextEvtTime > ctime) {
                ctime = nextEvtTime;
            }

            final List<Integer> events = getNextEvents(input, ctime, minIdx);

            for (Integer i : events) {
                if (isSameDirection(curDir, input[i].getDirection())) {
                    if (cargo.size() < cap) {
                        cargo.push(i);
                    }
                }
            }

            if (!events.isEmpty()) {
                curDir = (isSameDirection(Dir.LEFT, curDir)) ? Dir.RIGHT : Dir.LEFT;
                ctime += time;
            }

        }
        return output;
    }

    public static int getNextArrivalTime(final Evt[] input, final int startIdx) {
        int aTime = -1;
        for (int i = startIdx; i < input.length; i++) {
            Evt e = input[i];
            if (e != null) {
                aTime = e.getArrival();
                break;
            }
        }
        return aTime;
    }

    public static List<Integer> getNextEvents(final Evt[] input, int cTime, int startIdx) {
        final List<Integer> pos = new ArrayList<>();
        for (int i = startIdx; i < input.length; i++) {
            Evt e = input[i];
            if (e != null) {
                if (cTime >= e.getArrival()) {
                    pos.add(i);
                }
            }
        }
        return pos;
    }

    private static boolean isSameDirection(final Dir dir, final Dir direction) {
        return direction == dir;
    }

}
