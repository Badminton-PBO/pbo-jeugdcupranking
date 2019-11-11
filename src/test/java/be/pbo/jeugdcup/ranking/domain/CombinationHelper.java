package be.pbo.jeugdcup.ranking.domain;

import java.util.ArrayList;
import java.util.List;

final class CombinationHelper {

    public static List<int[]> generate(final int n, final int r) {
        final List<int[]> combinations = new ArrayList<>();
        helper(combinations, new int[r], 0, n - 1, 0);
        return combinations;
    }

    private static void helper(final List<int[]> combinations, final int data[], final int start, final int end, final int index) {
        if (index == data.length) {
            final int[] combination = data.clone();
            combinations.add(combination);
        } else {
            final int max = Math.min(end, end + 1 - data.length + index);
            for (int i = start; i <= max; i++) {
                data[index] = i;
                helper(combinations, data, i + 1, end, index + 1);
            }
        }
    }
}
