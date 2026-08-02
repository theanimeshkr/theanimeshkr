#!/usr/bin/env python3
"""
Maximize the number of bags filled to full capacity.

You may optionally empty at most one bag first (adding its rocks to the
additional-rocks pool), then distribute the additional rocks greedily.
"""

from __future__ import annotations

import bisect
import sys
from typing import List


def max_full_bags(
    capacity: List[int], rocks: List[int], additional_rocks: int
) -> int:
    """Return the maximum number of bags that can be filled to capacity.

    Strategy:
      1. Never empty a bag — greedily fill by ascending remaining capacity.
      2. Try emptying each bag with rocks — reclaim them into the pool,
         reset that bag to empty, then greedily fill again.

    Uses sorted needs + prefix sums so each emptying candidate is evaluated
    in O(log N). Overall time O(N log N), space O(N).
    """
    n = len(capacity)
    if n == 0:
        return 0

    needs = [capacity[i] - rocks[i] for i in range(n)]
    order = sorted(range(n), key=lambda i: needs[i])
    sorted_needs = [needs[i] for i in order]
    pos = [0] * n
    for rank, i in enumerate(order):
        pos[i] = rank

    prefix = [0] * (n + 1)
    for k in range(n):
        prefix[k + 1] = prefix[k] + sorted_needs[k]

    def max_fillable_from_prefix(budget: int) -> int:
        # Largest k with prefix[k] <= budget.
        lo, hi = 0, n
        while lo < hi:
            mid = (lo + hi + 1) // 2
            if prefix[mid] <= budget:
                lo = mid
            else:
                hi = mid - 1
        return lo

    best = max_fillable_from_prefix(additional_rocks)
    if best == n:
        return n

    def sum_first_k_others(k: int, removed_pos: int) -> int:
        """Sum of the k smallest needs excluding index removed_pos."""
        if k <= 0:
            return 0
        if k <= removed_pos:
            return prefix[k]
        # First removed_pos elements, then skip removed_pos, take the rest.
        return prefix[k + 1] - sorted_needs[removed_pos]

    def insert_pos_among_others(value: int, removed_pos: int) -> int:
        """Lower-bound index of value in sorted_needs with removed_pos deleted."""
        ins = bisect.bisect_left(sorted_needs, value)
        if ins <= removed_pos:
            return ins
        return ins - 1

    def cost_of_k_after_empty(k: int, removed_pos: int, emptied_need: int) -> int:
        """Cost to fill k bags after replacing needs[removed] with emptied_need."""
        if k <= 0:
            return 0
        ip = insert_pos_among_others(emptied_need, removed_pos)
        if ip >= k:
            # emptied bag is not among the k cheapest
            return sum_first_k_others(k, removed_pos)
        # emptied bag is among the k cheapest
        return emptied_need + sum_first_k_others(k - 1, removed_pos)

    for j in range(n):
        if rocks[j] == 0:
            continue

        budget = additional_rocks + rocks[j]
        emptied_need = capacity[j]
        removed_pos = pos[j]

        lo, hi = 0, n
        while lo < hi:
            mid = (lo + hi + 1) // 2
            if cost_of_k_after_empty(mid, removed_pos, emptied_need) <= budget:
                lo = mid
            else:
                hi = mid - 1
        best = max(best, lo)
        if best == n:
            return n

    return best


def parse_input(data: str) -> tuple[List[int], List[int], int]:
    """Parse problem input into (capacity, rocks, additional_rocks)."""
    tokens = data.split()
    if not tokens:
        raise ValueError("empty input")

    idx = 0
    n = int(tokens[idx])
    idx += 1

    if idx + 2 * n + 1 > len(tokens):
        raise ValueError(
            f"expected {2 * n + 2} tokens (N, {n} capacities, "
            f"{n} rocks, additionalRocks), got {len(tokens)}"
        )

    capacity = [int(tokens[idx + i]) for i in range(n)]
    idx += n
    rocks_arr = [int(tokens[idx + i]) for i in range(n)]
    idx += n
    additional_rocks = int(tokens[idx])
    return capacity, rocks_arr, additional_rocks


def main() -> None:
    data = sys.stdin.read()
    capacity, rocks_arr, additional_rocks = parse_input(data)
    print(max_full_bags(capacity, rocks_arr, additional_rocks))


if __name__ == "__main__":
    main()
