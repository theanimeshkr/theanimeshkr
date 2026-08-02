/**
 * Simple checks for {@link Solution#maximumFullBags}.
 * Run: {@code javac Solution.java SolutionTest.java && java SolutionTest}
 */
public class SolutionTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        expect(1, Solution.maximumFullBags(new int[] {10, 10, 10}, new int[] {5, 5, 5}, 0));
        expect(3, Solution.maximumFullBags(new int[] {2, 3, 4, 5}, new int[] {1, 2, 4, 4}, 2));
        expect(3, Solution.maximumFullBags(new int[] {5, 5, 5}, new int[] {5, 5, 5}, 0));
        expect(2, Solution.maximumFullBags(new int[] {10, 2, 2}, new int[] {4, 1, 1}, 0));
        expect(1, Solution.maximumFullBags(new int[] {10, 10}, new int[] {9, 1}, 0));
        expect(0, Solution.maximumFullBags(new int[] {100, 100}, new int[] {0, 0}, 1));
        expect(3, Solution.maximumFullBags(new int[] {3, 3, 3}, new int[] {1, 1, 1}, 6));
        expect(1, Solution.maximumFullBags(new int[] {5}, new int[] {5}, 0));
        expect(1, Solution.maximumFullBags(new int[] {5}, new int[] {0}, 5));
        expect(0, Solution.maximumFullBags(new int[] {5}, new int[] {0}, 0));
        expect(1, Solution.maximumFullBags(new int[] {4, 4}, new int[] {0, 3}, 1));
        expect(2, Solution.maximumFullBags(new int[] {2, 2, 100}, new int[] {1, 1, 50}, 2));
        expect(
                1,
                Solution.maximumFullBags(
                        new int[] {1_000_000_000, 1_000_000_000},
                        new int[] {1_000_000_000 - 1, 0},
                        1));

        // Randomized agreement with brute force on small instances.
        java.util.Random rng = new java.util.Random(42);
        for (int trial = 0; trial < 500; trial++) {
            int n = 1 + rng.nextInt(10);
            int[] capacity = new int[n];
            int[] rocks = new int[n];
            for (int i = 0; i < n; i++) {
                capacity[i] = 1 + rng.nextInt(25);
                rocks[i] = rng.nextInt(capacity[i] + 1);
            }
            long extra = rng.nextInt(31);
            int got = Solution.maximumFullBags(capacity, rocks, extra);
            int want = brute(capacity, rocks, extra);
            if (got != want) {
                failed++;
                System.out.println(
                        "FAIL random trial "
                                + trial
                                + ": got "
                                + got
                                + " want "
                                + want
                                + " capacity="
                                + java.util.Arrays.toString(capacity)
                                + " rocks="
                                + java.util.Arrays.toString(rocks)
                                + " extra="
                                + extra);
                break;
            }
            passed++;
        }

        System.out.println("Passed: " + passed + ", Failed: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void expect(int want, int got) {
        if (got == want) {
            passed++;
        } else {
            failed++;
            System.out.println("FAIL: expected " + want + " but got " + got);
        }
    }

    private static int brute(int[] capacity, int[] rocks, long extra) {
        int n = capacity.length;
        long[] needs = new long[n];
        for (int i = 0; i < n; i++) {
            needs[i] = (long) capacity[i] - rocks[i];
        }
        int best = count(needs, extra);
        for (int j = 0; j < n; j++) {
            long[] copy = needs.clone();
            copy[j] = capacity[j];
            best = Math.max(best, count(copy, extra + rocks[j]));
        }
        return best;
    }

    private static int count(long[] needs, long extra) {
        long[] sorted = needs.clone();
        java.util.Arrays.sort(sorted);
        int filled = 0;
        long rem = extra;
        for (long need : sorted) {
            if (rem < need) {
                break;
            }
            rem -= need;
            filled++;
        }
        return filled;
    }
}
