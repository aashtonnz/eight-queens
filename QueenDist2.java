/* File: QueenDist2.java - April 2014 */
package asgn2;

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Represents a distribution of queens on an eight by eight chessboard such that
 * every column has one queen in it. For crossover the position of each split
 * point is chosen randomly.
 *
 * @author Andrew Ashton
 */

public class QueenDist2 implements Comparable<QueenDist2> {

   // The number of columns/rows/queens.
   public static final int N = 8;
   
   /* The position vector for the queen distribution (i.e. vector[i] = j implies
      that for column i the queen is in row j). */
   private int[] vector;
   
   private double survivalRating = 0;
   
   private static Random rnd = new Random();
   
   /**
    * Creates a QueenDist2 for which each queen is in a random row.
    */
   public QueenDist2() {
      this.vector = new int[N];
      
      for(int i = 0; i < N; i++) {
         this.vector[i] = 1 + rnd.nextInt(N);
      }
   }
   
   /**
    * Creates a QueenDist2 specified by a given position vector.
    *
    * @param vector the position vector of length N for this QueenDist2
    */
   private QueenDist2(int[] vector) {
      this.vector = new int[N];
      this.vector = vector;
   }
   
   /**
    * Returns the row position of the Queen in the given column.
    *
    * @param col a column
    * @returns the row position of the queen in the given column
    */
   public int get(int col) {
      return vector[col];
   }
   
   /**
    * Derives a new QueenDist2 by splitting this QueenDist2 vector and another
    * after a given point and then combining the first half of this vector with
    * the second half of the other.
    *
    * @param splitPoint the point after which to split both QueenDist2 vectors
    * @param other      the QueenDist2 to be combined with this QueenDist2
    * @returns the derived QueenDist2
    */
   private QueenDist2 combine(int splitPoint, QueenDist2 other)
   {
      int[] newVector = new int[N];
      
      for(int i = 0; i < splitPoint; i++) {
         newVector[i] = this.get(i);
      }
      for(int i = splitPoint; i < N; i++) {
         newVector[i] = other.get(i);
      }
      return new QueenDist2(newVector);
   }
   
   /**
    * May derive two new QueenDist2s by crossing over between this QueenDist2
    * and the other at a given number of random split points. Otherwise a copy
    * of this and the given QueenDist2 are returned.
    *
    * @param prob   the probability of deriving new QueenDist2s
    * @param splits the number of split points at which to crossover
    * @param other  the QueenDist2 to crossover with this QueenDist2
    * @returns an array of two QueenDist2s
    */
   public QueenDist2[] crossover(double prob, int splits, QueenDist2 other) {
     QueenDist2[] children = new QueenDist2[2];

     if(rnd.nextDouble() < prob) {
         ArrayList<Integer> list = new ArrayList<Integer>();
         int splitPoint;
         /* Add split points to a list, making sure each is distinct from the
            others, until it has the required number of splits. */
         while(list.size() < splits) {
            do {
               splitPoint = rnd.nextInt(QueenDist2.N - 1) + 1;
            } while(list.contains(splitPoint));
            list.add(splitPoint);
         }
         Integer[] splitPoints = new Integer[list.size()];
         splitPoints = list.toArray(splitPoints);
         
         //Ensure the crossovers occur in the correct order.
         Arrays.sort(splitPoints);
         
         children[0] = this.combine(splitPoints[0], other);
         children[1] = other.combine(splitPoints[0], this);
         
         /* Switch the QueenDist to be combined with each child after each split
            point. */
         for(int i = 1; i < splitPoints.length; i++) {
            if(i % 2 == 0) {
               children[0] = children[0].combine(splitPoints[i], other);
               children[1] = children[1].combine(splitPoints[i], this);
            } else {
               children[0] = children[0].combine(splitPoints[i], this);
               children[1] = children[1].combine(splitPoints[i], other);
            }
         }
      } else {
         int[] vector0 = new int[N];
         int[] vector1 = new int[N];
         
         for(int i = 0; i < N; i++) {
            vector0[i] = this.get(i);
            vector1[i] = other.get(i);
         }
         children[0] = new QueenDist2(vector0);
         children[1] = new QueenDist2(vector1);
      }
      return children;
   }
   
   /**
    * May randomly change the row of the queen in a randomly chosen column.
    *
    * @param prob the probability of a change occurring
    */
   public void mutate(double prob) {
      if(rnd.nextDouble() < prob) {
         vector[rnd.nextInt(N)] = 1 + rnd.nextInt(N);
      }
   }
   
   /**
    * Determines the number of attacking pairs of queens for the QueenDist2.
    *
    * @returns the number of attacking pairs
    */
   public int attackingPairs() {
      int pairs = 0;
      
      /* Consider the ith column. If the jth column to the right of the column
         has a queen in the same row, j rows above (i.e. on the upwards
         diagonal), or j rows below (i.e. on the downwards diagonal), then there
         is another attacking pair. */
      for(int i = 0; i < N; i++) {
         for(int j = 1; j < N - i; j++) {
            if(get(i + j) == get(i)) {
               pairs++;
            }
            if(get(i + j) == get(i) + j) {
               pairs++;
            }
            if(get(i + j) == get(i) - j) {
               pairs++;
            }
         }
      }
      return pairs;
   }
   
  /**
   * Measures the number of attacking pairs as a double from 0 (maximum possible
   * attacking pairs) to 1 (no attacking pairs).
   *
   * @returns a measure of the number of attacking pairs
   */
   public double fitness() {
   
      /* The maximum possible number of attacking pairs (i.e. all queens are in
         the same row or diagonal). The first queen can be paired with seven
         others, the second with six others, and so on. Therefore the maximum is
         the sum from 1 to N - 1, which is ((N - 1) * N) / 2 */
      final double MAX = ((N - 1) * N) / 2;
      
      return 1 - attackingPairs() / MAX;
   }
   
   /**
    * Updates the survival rating for this QueenDist2 using a given constant,
    * the highest fitness in the population and a random number.
    *
    * @param survivalConst  determines the randomness of the rating (0 gives
    *                       complete randomness, 1 gives no randomness)
    * @param highestFitness the highest fitness in the population
    */
   public void calcSurvivalRating(double survivalConst, double
                                  highestFitness) {
      if(highestFitness == 0) {
         survivalRating = 0;
      } else {
         survivalRating = survivalConst * this.fitness() / highestFitness
                           + (1 - survivalConst) * rnd.nextDouble();
      }
   }
   
   /**
    * Returns the survival rating previously calculated, or 0 if it has not been
    * calculated.
    *
    * @returns the survival rating
    */
   public double getSurvivalRating() {
      return survivalRating;
   }
   
   /**
    * Returns the difference between the fitness of the other QueenDist2 and
    * this QueenDist2.
    *
    * @param other the QueenDist2 to be compared to
    *
    * @returns the fitness of the other QueenDist2 minus the fitness of this
    *          QueenDist2.
    */
   public int compareTo(QueenDist2 other) {
      return this.attackingPairs() - other.attackingPairs();
   }
   
   /**
    * Returns the position vector for the QueenDist2.
    *
    * @returns the components of the position vector in brackets
    */
   public String toString() {
      StringBuilder result = new StringBuilder();
      result.append("(");
      
      for(int i = 0; i < N - 1; i++) {
         result.append(get(i) + ", ");
      }
      result.append(get(N - 1) + ")");
      
      return result.toString();
   }

}
