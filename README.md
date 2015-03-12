# eight-queens
A Java implementation of a genetic algorithm for solving the 'eight queens' chess puzzle.

The 'eight queens' chess puzzle asks how one can position eight queens on an 8 by 8 chessboard such that no queen threatens another.

Each possible solution is represented by an eight dimensional vector, where the ith component represents the row position of the queen in the ith column (noting that only one queen can appear in each).

This program uses a genetic algorithm to iterate on an inital population of 500 random solutions. After each iteration the best solution is printed. If a correct ('optimal') solution is found then it is printed along with a message 'OPTIMAL SOLUTION FOUND' and the program terminates. If a correct solution is not found within 30 iterations of the algorithm then the program terminates.

One iteration of the genetic algorithm acts on the population as follows:

  1. A 'fitness' rating is calculated for each solution based on the number of threatening queens and random generation. The degree of random generation is currently at 50% but may be adjusted.
  2. Of the 500 population members, the most fit 150 solutions are retained. This quantity may also be adjusted.
  3. A parent is selected from the remaining 150 solutions using a 'tournament'. This entails calculating new fitness ratings for each solution using a 30% degree of randomness (adjustable). Of the 150 solutions, 120 (adjustable) are randomly selected, and from these the solution with the highest fitness is selected as a parent.
  4. Another parent is selected using step (3).
  5. Two child solutions are created from the two parents using 'crossover'. This entails dividing each vector at six split points, and then recombining them. Currently crossover is always successful, however one may adjust the success probability. The number of split points is also adjustable. Finally, there is a 0.1% chance that a vector component of a child will be randomly changed (or 'mutated').
  6. Steps (3) to (5) are repeated until the population contains 500 solutions again.
  7. If the population contains an optimal solution then it is printed.

The adjustable parameters have been set to values determined through experimentation. However the intention of this project was to implement a genetic algorithm, so certain parameters were restricted. For example, one might increase the mutation rate to 100%, but this would not be consistent with natural evolution.
