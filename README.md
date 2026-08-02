# Maximum Full Bags

Given bag capacities, current rock counts, and `additionalRocks`, maximize
how many bags can be filled to capacity.

You may empty **at most one** bag before distributing rocks: remove all rocks
from that bag, add them to your additional pool, and leave the bag empty.

## Input

```
N
capacity[0] capacity[1] ... capacity[N-1]
rocks[0] rocks[1] ... rocks[N-1]
additionalRocks
```

Values may also be provided one-per-line; whitespace is flexible.

## Output

A single integer: the maximum number of full bags.

## Example

```
3
10 10 10
5 5 5
0
```

Output: `1`

## Run

```bash
python3 solution.py < input.txt
python3 -m unittest tests.test_solution -v
```
