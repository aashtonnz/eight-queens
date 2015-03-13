/* File: TournamentGA2.java - April 2014 */
package asgn2;

import java.util.*;

/**
 * Represents a genetic algorithm for finding a QueenDist2 of optimal fitness,
 * using the tournament approach to selecting parents.
 *
 * @author Andrew Ashton
 */
public class TournamentGA2 {

   private ArrayList<QueenDist2> dists;
   private static Random rnd = new Random();
   
   private int survivors;
   private double survivalConst;
   private int splits;
   private double crossoverProb;
   private double mutationProb;
   private int tournSize;
   private double tournConst;
 
   /**
    * Creates a new TournamentGA2.
    *
    * @param population     the number of QueenDists for any given generation
    * @param survivors      the number of QueenDists retained between
                            generations
    * @pararm survivalConst the level of randomness (0 for complete, 1 for none)
    *                       involved in selecting survivors
    * @param splits         the number of split points for crossover
    * @param crossoverProb  the probability of successful crossover
    * @param mutationProb   the probability of mutations
    * @param tournSize      the number of participants chosen to compete in a
    *                       tournament
    * @param tournConst     the level of randomness involved in selecting
    *                       tournament winners
    */
   public TournamentGA2(int population, int survivors, double survivalConst,
                        int splits, double crossoverProb, double mutationProb,
                        int tournSize, double tournConst) {
      this.survivors = survivors;
      this.survivalConst = survivalConst;
      this.splits = splits;
      this.crossoverProb = crossoverProb;
      this.mutationProb = mutationProb;
      
      this.tournSize = tournSize;
      this.tournConst = tournConst;
      
      generate(population);
   }
  
  /**
    * Generates a random population of QueenDists.
    *
    * @param the size of the population to be generated
    */
   private void generate(int population) {
      dists = new ArrayList<QueenDist2>();
      
      while(dists.size() < population) {
         dists.add(new QueenDist2());
      }
   }
   
   /**
    * Sorts the population (as stored in the ArrayList) from most fit to least
    * fit.
    */
   private void sort() {
      QueenDist2[] temp = new QueenDist2[dists.size()];
      temp = dists.toArray(temp);
      Arrays.sort(temp);
      
      dists = new ArrayList<QueenDist2>();
      
      for(int i = 0; i < temp.length; i++) {
         dists.add(temp[i]);
      }
   }
   
   /**
    * Calculates survival ratings and then sorts the population
    * from highest survival rating to lowest.
    *
    * @param constant the constant used to determine the randomness of the
    *                 survival ratings
    */
   private void survivalSort(double constant) {
      sort();
      final double HIGHEST_FITNESS = dists.get(0).fitness();
      
      for(int i = 0; i < dists.size(); i++) {
         dists.get(i).calcSurvivalRating(constant, HIGHEST_FITNESS);
      }
      
      Collections.sort(dists, new Comparator<QueenDist2>() {
         @Override
         public int compare(QueenDist2 dist1, QueenDist2 dist2)
         {
            if(dist1.getSurvivalRating() > dist2.getSurvivalRating()) {
               return -1;
            }
            if (dist1.getSurvivalRating() < dist2.getSurvivalRating()) {
               return 1;
            }
            return 0;
         }
      });
   }
   
   /**
    * Returns those QueenDists to be retained between populations.
    *
    * @return an ArrayList of surviving QueenDists
    */
   private ArrayList<QueenDist2> getSurvivors() {
      ArrayList<QueenDist2> survivorDists = new ArrayList<QueenDist2>();
      survivalSort(survivalConst);
      
      for(int i = 0; survivorDists.size() < survivors; i++) {
         survivorDists.add(dists.get(i));
      }
      return survivorDists;
   }
   
   /**
    * Returns a parent for crossover, using a tournament.
    *
    * @return a parent for crossover
    */
   private QueenDist2 selectParent() {
      survivalSort(tournConst);
      ArrayList<QueenDist2> competitors = new ArrayList<QueenDist2>(dists);
      
      while(competitors.size() > tournSize) {
         competitors.remove(rnd.nextInt(competitors.size()));
      }
      return competitors.get(0);
   }
   
   /**
    * Returns the children which, along with the survivors, will populate the
    * next generation.
    *
    * @return the children
    */
   private ArrayList<QueenDist2> getChildren() {
      ArrayList<QueenDist2> allChildren = new ArrayList<QueenDist2>();
      
      while(allChildren.size() < dists.size() - survivors) {
         QueenDist2[] children = selectParent().crossover(crossoverProb, splits,
                                                          selectParent());
         children[0].mutate(mutationProb);
         children[1].mutate(mutationProb);
         
         allChildren.add(children[0]);
         
         if(allChildren.size() < dists.size() - survivors) {
            allChildren.add(children[1]);
         }
      }
      return allChildren;
   }
   
   /**
    * Repopulates the ArrayList with a new generation of QueenDists composed of
    * the survivors and children.
    */
   public void nextGen() {
      ArrayList<QueenDist2> survivorDists = getSurvivors();
      ArrayList<QueenDist2> children = getChildren();
      
      dists = new ArrayList<QueenDist2>();
      
      for(int i = 0; i < survivorDists.size(); i++) {
         dists.add(survivorDists.get(i));
      }
      for(int i = 0; i < children.size(); i++) {
         dists.add(children.get(i));
      }
   }
   
   /**
    * Returns true if the current generation of QueenDists contains one with
    * perfect fitness.
    *
    * @return true if the current generation contains a QueenDist with fitness
    *          of 1
    */
   public boolean isOptimal() {
      sort();
      return dists.get(0).fitness() == 1;
   }
   
   /**
    * Returns a string containing the position vector of the most fit QueenDist
    * in the population and its fitness.
    *
    * @return the best solution and its fitness
    */
   public String toString() {
      sort();
      return "Solution: " + dists.get(0)
             + " (fitness: " + dists.get(0).fitness() + ")";
   }
   
   /**
    * Prints a list, ordered by fitness, of position vectors of all QueenDists
    * in the current generation, their fitness, and their position in the list.
    */
   public void print() {
      sort();
      for(int i = 0; i < dists.size(); i++) {
         System.out.print(dists.get(i) + " ");
         System.out.printf("%.5f", dists.get(i).fitness());
         System.out.println(" " + (i + 1));
      }
   }

}
