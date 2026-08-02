import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Maximize the number of bags filled to full capacity.
 *
 * <p>Before distributing {@code additionalRocks}, you may empty at most one bag:
 * remove all of its rocks, add them to the additional pool, and leave that bag empty.
 *
 * <p>Time: O(N log N), Space: O(N).
 */
public class Solution {

    /**
     * Returns the maximum number of bags that can be filled to capacity.
     */
    public static int maximumFullBags(int[] capacity, int[] rocks, long additionalRocks) {
        int n = capacity.length;
        if (n == 0) {
            return 0;
        }

        long[] needs = new long[n];
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            needs[i] = (long) capacity[i] - rocks[i];
            order[i] = i;
        }

        Arrays.sort(order, (a, b) -> Long.compare(needs[a], needs[b]));

        long[] sortedNeeds = new long[n];
        int[] pos = new int[n];
        for (int rank = 0; rank < n; rank++) {
            int i = order[rank];
            sortedNeeds[rank] = needs[i];
            pos[i] = rank;
        }

        long[] prefix = new long[n + 1];
        for (int k = 0; k < n; k++) {
            prefix[k + 1] = prefix[k] + sortedNeeds[k];
        }

        int best = maxFillableFromPrefix(prefix, n, additionalRocks);
        if (best == n) {
            return n;
        }

        for (int j = 0; j < n; j++) {
            if (rocks[j] == 0) {
                continue;
            }

            long budget = additionalRocks + rocks[j];
            long emptiedNeed = capacity[j];
            int removedPos = pos[j];

            int lo = 0;
            int hi = n;
            while (lo < hi) {
                int mid = (lo + hi + 1) >>> 1;
                if (costOfKAfterEmpty(mid, removedPos, emptiedNeed, sortedNeeds, prefix)
                        <= budget) {
                    lo = mid;
                } else {
                    hi = mid - 1;
                }
            }
            best = Math.max(best, lo);
            if (best == n) {
                return n;
            }
        }

        return best;
    }

    private static int maxFillableFromPrefix(long[] prefix, int n, long budget) {
        int lo = 0;
        int hi = n;
        while (lo < hi) {
            int mid = (lo + hi + 1) >>> 1;
            if (prefix[mid] <= budget) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }
        return lo;
    }

    private static long sumFirstKOthers(int k, int removedPos, long[] sortedNeeds, long[] prefix) {
        if (k <= 0) {
            return 0L;
        }
        if (k <= removedPos) {
            return prefix[k];
        }
        return prefix[k + 1] - sortedNeeds[removedPos];
    }

    private static int insertPosAmongOthers(long value, int removedPos, long[] sortedNeeds) {
        int ins = lowerBound(sortedNeeds, value);
        if (ins <= removedPos) {
            return ins;
        }
        return ins - 1;
    }

    private static long costOfKAfterEmpty(
            int k,
            int removedPos,
            long emptiedNeed,
            long[] sortedNeeds,
            long[] prefix) {
        if (k <= 0) {
            return 0L;
        }
        int ip = insertPosAmongOthers(emptiedNeed, removedPos, sortedNeeds);
        if (ip >= k) {
            return sumFirstKOthers(k, removedPos, sortedNeeds, prefix);
        }
        return emptiedNeed + sumFirstKOthers(k - 1, removedPos, sortedNeeds, prefix);
    }

    private static int lowerBound(long[] a, long value) {
        int lo = 0;
        int hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] < value) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    public static void main(String[] args) throws IOException {
        FastScanner sc = new FastScanner();
        int n = sc.nextInt();
        int[] capacity = new int[n];
        int[] rocks = new int[n];
        for (int i = 0; i < n; i++) {
            capacity[i] = sc.nextInt();
        }
        for (int i = 0; i < n; i++) {
            rocks[i] = sc.nextInt();
        }
        long additionalRocks = sc.nextLong();
        System.out.println(maximumFullBags(capacity, rocks, additionalRocks));
    }

    /** Lightweight scanner for whitespace-separated tokens. */
    private static final class FastScanner {
        private final BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        private StringTokenizer tokenizer = new StringTokenizer("");

        String next() throws IOException {
            while (!tokenizer.hasMoreTokens()) {
                String line = reader.readLine();
                if (line == null) {
                    return null;
                }
                tokenizer = new StringTokenizer(line);
            }
            return tokenizer.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        long nextLong() throws IOException {
            return Long.parseLong(next());
        }
    }
}
