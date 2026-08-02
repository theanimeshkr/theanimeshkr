#!/usr/bin/env python3
"""Tests for maximum full bags with optional empty operation."""

import unittest

from solution import max_full_bags, parse_input


class TestMaxFullBags(unittest.TestCase):
    def test_example_1(self):
        # Empty one bag (gain 5), fill another bag that needs 5 → 1 full bag.
        self.assertEqual(max_full_bags([10, 10, 10], [5, 5, 5], 0), 1)

    def test_no_empty_needed(self):
        # Classic fill without emptying: needs 1,1,1 with 2 extra → 2 full.
        self.assertEqual(max_full_bags([2, 3, 4, 5], [1, 2, 4, 4], 2), 3)

    def test_already_full(self):
        self.assertEqual(max_full_bags([5, 5, 5], [5, 5, 5], 0), 3)

    def test_empty_helps_fill_two(self):
        # Needs 6, 1, 1 with 0 extra → fill none.
        # Empty first (gain 4); needs become 10, 1, 1 with extra 4 → fill both small bags.
        self.assertEqual(max_full_bags([10, 2, 2], [4, 1, 1], 0), 2)

    def test_empty_almost_full_bag(self):
        # Empty bag with 9 rocks to fill a bag that needs 9.
        # capacity=[10,10], rocks=[9,1], extra=0
        # Without empty: need 1 and 9 → fill 0.
        # Empty first: gain 9, needs become [10, 9] → fill second → 1.
        # Empty second: gain 1, needs [1, 10] → fill first → 1.
        self.assertEqual(max_full_bags([10, 10], [9, 1], 0), 1)

    def test_cannot_fill_any(self):
        self.assertEqual(max_full_bags([100, 100], [0, 0], 1), 0)

    def test_fill_all_with_extra(self):
        self.assertEqual(max_full_bags([3, 3, 3], [1, 1, 1], 6), 3)

    def test_empty_then_refill_same_bag(self):
        # Emptying and refilling the same bag alone is never better than
        # not emptying, but with other bags it can still be optimal overall.
        # Here: empty bag 0 (gain 5), use 5 to fill bag 0 again → still 1,
        # same as filling bag 0 without emptying (need 0? wait rocks=5 need=5).
        # capacity=[5], rocks=[5], extra=0 → already full → 1
        self.assertEqual(max_full_bags([5], [5], 0), 1)
        self.assertEqual(max_full_bags([5], [0], 5), 1)
        self.assertEqual(max_full_bags([5], [0], 0), 0)

    def test_skip_empty_zero_rocks(self):
        # Bags with 0 rocks: emptying them changes nothing.
        self.assertEqual(max_full_bags([4, 4], [0, 3], 1), 1)

    def test_large_capacities(self):
        self.assertEqual(
            max_full_bags([10**9, 10**9], [10**9 - 1, 0], 1),
            1,
        )

    def test_parse_example_input(self):
        raw = "3\n10 10 10\n5 5 5\n0\n"
        capacity, rocks, extra = parse_input(raw)
        self.assertEqual(capacity, [10, 10, 10])
        self.assertEqual(rocks, [5, 5, 5])
        self.assertEqual(extra, 0)
        self.assertEqual(max_full_bags(capacity, rocks, extra), 1)

    def test_parse_multiline_values(self):
        raw = "3\n10\n10\n10\n5\n5\n5\n0\n"
        capacity, rocks, extra = parse_input(raw)
        self.assertEqual(capacity, [10, 10, 10])
        self.assertEqual(rocks, [5, 5, 5])
        self.assertEqual(extra, 0)


class TestEmptyVsNoEmpty(unittest.TestCase):
    def test_emptying_strictly_better(self):
        # Without empty: needs [5,5,5], extra 0 → 0
        # With empty: → 1
        self.assertEqual(max_full_bags([10, 10, 10], [5, 5, 5], 0), 1)

    def test_not_emptying_is_best(self):
        # Already can fill two cheap bags; emptying would waste progress.
        # needs: 1, 1, 100; extra 2 → fill 2 without emptying.
        self.assertEqual(max_full_bags([2, 2, 100], [1, 1, 50], 2), 2)


class TestBruteAgreement(unittest.TestCase):
    def test_random_small_cases_match_brute(self):
        import random

        def brute(capacity, rocks, extra):
            n = len(capacity)
            needs = [capacity[i] - rocks[i] for i in range(n)]

            def count(need_list, e):
                filled = 0
                rem = e
                for need in sorted(need_list):
                    if rem < need:
                        break
                    rem -= need
                    filled += 1
                return filled

            best = count(needs, extra)
            for j in range(n):
                new_needs = needs[:]
                new_needs[j] = capacity[j]
                best = max(best, count(new_needs, extra + rocks[j]))
            return best

        rng = random.Random(42)
        for _ in range(500):
            n = rng.randint(1, 10)
            capacity = [rng.randint(1, 25) for _ in range(n)]
            rocks = [rng.randint(0, c) for c in capacity]
            extra = rng.randint(0, 30)
            self.assertEqual(
                max_full_bags(capacity, rocks, extra),
                brute(capacity, rocks, extra),
            )


if __name__ == "__main__":
    unittest.main()
