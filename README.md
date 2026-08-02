# Maximum Full Bags (Java)

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

Whitespace is flexible (values may also appear one-per-line).

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

## Build & run

```bash
javac Solution.java
java Solution < examples/example1.txt

javac Solution.java SolutionTest.java
java SolutionTest
```
