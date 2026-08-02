import java.util.*;

class Main {

    public static int solve(int N, int[] capacity, int[] rocks, int additionalRocks) {
        int best = fillCount(buildNeeds(N, capacity, rocks, -1), additionalRocks);

        for (int j = 0; j < N; j++) {
            if (rocks[j] == 0) {
                continue;
            }
            long[] needs = buildNeeds(N, capacity, rocks, j);
            long extra = (long) additionalRocks + rocks[j];
            best = Math.max(best, fillCount(needs, extra));
            if (best == N) {
                return N;
            }
        }
        return best;
    }

    // emptiedIndex < 0 means do not empty any bag.
    private static long[] buildNeeds(int N, int[] capacity, int[] rocks, int emptiedIndex) {
        long[] needs = new long[N];
        for (int i = 0; i < N; i++) {
            if (i == emptiedIndex) {
                needs[i] = capacity[i];
            } else {
                needs[i] = (long) capacity[i] - rocks[i];
            }
        }
        return needs;
    }

    private static int fillCount(long[] needs, long extra) {
        Arrays.sort(needs);
        int filled = 0;
        for (long need : needs) {
            if (extra < need) {
                break;
            }
            extra -= need;
            filled++;
        }
        return filled;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int N = sc.nextInt();

        int[] capacity = new int[N];
        for (int i = 0; i < N; i++) {
            capacity[i] = sc.nextInt();
        }

        int[] rocks = new int[N];
        for (int i = 0; i < N; i++) {
            rocks[i] = sc.nextInt();
        }

        int additionalRocks = sc.nextInt();

        int result = solve(N, capacity, rocks, additionalRocks);
        System.out.println(result);
    }
}
