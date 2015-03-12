/* File: GAApp.java - April 2014 */
package asgn2;

/**
 * Runs TournamentGA2 until it is optimal or 30 generations have passed.
 *
 * @author Andrew Ashton
 */
public class GAApp {

   /**
    * Runs the genetic algorithm, printing out the generation number, and the
    * best QueenDist and its fitness for each generation. If an generation is
    * optimal then it stops and prints a notification, otherwise it continues
    * until 30 generations have passed.
    *
    * @param args not used
    */
   public static void main(String[] args) {
      TournamentGA2 ga = new TournamentGA2(500, 150, 0.5, 6, 1.0, 0.001, 120,
                                           0.3);
      int gen;
      
      System.out.println();
      
      for(gen = 1; gen < 30 && !ga.isOptimal() ; gen++) {
         System.out.println("Generation: " + gen);
         System.out.println(ga);
         System.out.println();
         ga.nextGen();
      }
      
      System.out.println("Generation: " + gen);
      System.out.println(ga);
      
      if( ga.isOptimal() ) {
         System.out.println();
         System.out.println("OPTIMAL SOLUTION FOUND");
      }
   }

}
