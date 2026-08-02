import java.util.*;

class Main {

    public static int solve(int N) {
        final int MOD = 1_000_000_007;

        // dp[i][r][g] = ways to fill i frames with Red parity r and Green parity g
        long[][][] dp = new long[N + 1][2][2];
        dp[0][0][0] = 1;

        for (int i = 0; i < N; i++) {
            for (int r = 0; r < 2; r++) {
                for (int g = 0; g < 2; g++) {
                    long cur = dp[i][r][g];
                    if (cur == 0) {
                        continue;
                    }

                    // R: 1 frame, flip red parity
                    if (i + 1 <= N) {
                        dp[i + 1][r ^ 1][g] = (dp[i + 1][r ^ 1][g] + cur) % MOD;
                    }

                    // G: 1 frame, flip green parity
                    if (i + 1 <= N) {
                        dp[i + 1][r][g ^ 1] = (dp[i + 1][r][g ^ 1] + cur) % MOD;
                    }

                    // B: 2 frames, parity unchanged
                    if (i + 2 <= N) {
                        dp[i + 2][r][g] = (dp[i + 2][r][g] + cur) % MOD;
                    }
                }
            }
        }

        return (int) dp[N][0][0];
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int result = solve(N);
        System.out.println(result);
    }
}
