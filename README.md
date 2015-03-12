# eight-queens
A Java implementation of a genetic algorithm for solving the 'eight queens' chess puzzle.

The 'eight queens' chess puzzle asks how one can position eight queens on an 8 by 8 chessboard such that no queen threatens another.

Each possible solution is represented by an eight dimensional vector, where the ith component represents the row position of the queen in the ith column (noting that only one queen can appear in each).

This program uses a genetic algorithm to iterate on an inital population of 500 random solutions. After each iteration the best solution is printed. If a correct ('optimal') solution is found then it is printed along with a message 'OPTIMAL SOLUTION FOUND' and the program terminates. If a correct solution is not found within 30 iterations of the algorithm then the program terminates.

The genetic algorithm acts on each population as follows:

  1. A 'fitness' rating is calculated for each solution, based on the number of threatening queens and random generation The degree of random generation is currently at 50 % but may be adjusted.
  2. The most fit 150 solutions (of the 500 in the population) are retained. This quantity may also be adjusted.
  3. These 150 solutions compete in a 'tournament' in which new fitness ratings are calculated but with a 30 % degree of randomness (adjustable).
  2. New solutions are created from the surviving 150 using a technique called 'crossover'. This entails dividing each vector into seven (adjustable) segments and then recombining them with segment from another vector. The division points are 
  3. 


