import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.HashSet;

public class Population {

  int populationSize;
  Individual[] individuals;
  double mutationRate;
  Random random = new Random();

  /**
  * Generates a random population
  */
  public Population(int populationSize, int numberOfCities) {
    this.populationSize = populationSize;
    individuals = new Individual[populationSize];
    for(int i = 0; i < populationSize; ++i) {
      ArrayList<Integer> list = new ArrayList<Integer>();
      for(int j = 0; j < numberOfCities; ++j) {
        list.add(j);
      }
      Collections.shuffle(list);
      individuals[i] = new Individual(listToArray(list));
    }
    Arrays.sort(individuals);
  }

  /**
  * Generates a population based on crossover between
  */
  public Population(int populationSize, ArrayList<Individual> parents, double mutationRate) {
    individuals = new Individual[populationSize];
    this.mutationRate = mutationRate;
    this.populationSize = populationSize;
    for(int i = 0; i < populationSize; i += 2) {
      Individual parent1 = parents.get(random.nextInt(parents.size()-1));
      Individual parent2 = parents.get(random.nextInt(parents.size()-1));
      while(parent1 == parent2) {
        parent2 = parents.get(random.nextInt(parents.size()));
      }
      Individual[] offspring = ox1(parent1, parent2);
      individuals[i] = offspring[0];
      individuals[i+1] = offspring[1];
    }
    individuals[individuals.length-1] = parents.get(parents.size()-1);
    Arrays.sort(individuals);
  }

  /**
  * OX1 crossover
  */
  private Individual[] ox1(Individual parent1, Individual parent2) {
    Individual[] offspring = new Individual[2];
    int[] child1 = new int[parent1.getPermutation().length];
    int[] child2 = new int[parent1.getPermutation().length];
    //Sets cut points
    int left = random.nextInt(parent1.getPermutation().length);
    int right = random.nextInt(parent1.getPermutation().length);
    if(left > right) {
      int temp = left;
      left = right;
      right = temp;
    }

    //Keep track of witch cities already in child 1 and 2
    HashSet<Integer> hs1 = new HashSet<Integer>();
    HashSet<Integer> hs2 = new HashSet<Integer>();

    //Copy the subpath to the children
    for(int i = left; i < right; ++i) {
      child1[i] = parent1.getPermutation()[i];
      hs1.add(child1[i]);
      child2[i] = parent2.getPermutation()[i];
      hs2.add(child2[i]);
    }

    //Create the rest of child1
    for(int i = right; i < child1.length; ++i) {
      boolean found = false;
      for(int j = right; j < child1.length; ++j) {
        if(!hs1.contains(parent2.getPermutation()[j])) {
          child1[i] = parent2.getPermutation()[j];
          hs1.add(parent2.getPermutation()[j]);
          found = true;
          break;
        }
      }
      if(found) {
        continue;
      }
      for(int j = 0; j < right; ++j) {
        if(!hs1.contains(parent2.getPermutation()[j])) {
          child1[i] = parent2.getPermutation()[j];
          hs1.add(parent2.getPermutation()[j]);
          break;
        }
      }
    }
    for(int i = 0; i < left; ++i) {
      boolean found = false;
      for(int j = right; j < child1.length; ++j) {
        if(!hs1.contains(parent2.getPermutation()[j])) {
          child1[i] = parent2.getPermutation()[j];
          hs1.add(parent2.getPermutation()[j]);
          found = true;
          break;
        }
      }
      if(found) {
        continue;
      }
      for(int j = 0; j < right; ++j) {
        if(!hs1.contains(parent2.getPermutation()[j])) {
          child1[i] = parent2.getPermutation()[j];
          hs1.add(parent2.getPermutation()[j]);
          break;
        }
      }
    }

    //Create the rest of child2
    for(int i = right; i < child1.length; ++i) {
      boolean found = false;
      for(int j = right; j < child1.length; ++j) {
        if(!hs2.contains(parent1.getPermutation()[j])) {
          child2[i] = parent1.getPermutation()[j];
          hs2.add(parent1.getPermutation()[j]);
          found = true;
          break;
        }
      }
      if(found) {
        continue;
      }
      for(int j = 0; j < right; ++j) {
        if(!hs2.contains(parent1.getPermutation()[j])) {
          child2[i] = parent1.getPermutation()[j];
          hs2.add(parent1.getPermutation()[j]);
          break;
        }
      }
    }
    for(int i = 0; i < left; ++i) {
      boolean found = false;
      for(int j = right; j < child1.length; ++j) {
        if(!hs2.contains(parent1.getPermutation()[j])) {
          child2[i] = parent1.getPermutation()[j];
          hs2.add(parent1.getPermutation()[j]);
          found = true;
          break;
        }
      }
      if(found) {
        continue;
      }
      for(int j = 0; j < right; ++j) {
        if(!hs2.contains(parent1.getPermutation()[j])) {
          child2[i] = parent1.getPermutation()[j];
          hs2.add(parent1.getPermutation()[j]);
          break;
        }
      }
    }
    offspring[0] = new Individual(child1, mutationRate);
    offspring[1] = new Individual(child2, mutationRate);
    return offspring;
  }

  private int[] listToArray(ArrayList<Integer> list) {
    int[] arr = new int[list.size()];
    for(int i = 0; i < list.size(); ++i) {
      arr[i] = list.get(i);
    }
    return arr;
  }

  public double getGenerationMean() {
    double mean = 0.0;
    for(Individual ind : individuals) {
      mean += ind.getFitness();
    }
    return mean/populationSize;
  }

  public Individual[] getIndividuals() {
    return individuals;
  }
}
