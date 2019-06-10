import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithmTSP {

  private static int numberOfCities;
  private static City[] cities;
  private static long time = System.currentTimeMillis();
  private static Random random = new Random();
  private static int tournamentSize;

  public static void initalizeDistanceMatrix() {
    for(int i = 0; i < numberOfCities; ++i) {
      for(int j = 0; j < numberOfCities; ++j) {
        DistanceMatrix.distanceMatrix[i][j] = Math.sqrt(
          ((cities[i].getX()-cities[j].getX())*
          (cities[i].getX()-cities[j].getX()))+
          ((cities[i].getY()-cities[j].getY())*
          (cities[i].getY()-cities[j].getY())));
      }
    }
  }

  public static ArrayList<Individual> tournamentSelection(Individual[] prevGen, double retain) {
    ArrayList<Individual> parents = new ArrayList<Individual>();
    Individual[] ind = new Individual[tournamentSize];
    for(int i = 0; i < Math.max(2, prevGen.length * retain); ++i) {
      for(int j = 0; j < tournamentSize; ++j) {
        ind[j] = prevGen[random.nextInt(prevGen.length)];
      }
      Individual best = ind[0];
      for(int j = 1; j < tournamentSize; ++j) {
        if(ind[j].getFitness() < best.getFitness()) {
          best = ind[j];
        }
      }
      parents.add(best);
    }
    parents.add(prevGen[0]);
    return parents;
  }

  public static ArrayList<Individual> selectParents(Individual[] prevGen, double retain) {
    ArrayList<Individual> parents = new ArrayList<Individual>();
    for(int i = 0; i < Math.max(2, prevGen.length * retain); ++i) {
      Individual ind = prevGen[i];
      parents.add(ind);
    }
    return parents;
  }

  public static void geneticAlgorithm(int populationSize, double mutationRate, double retain, double fitnessLimit, int generationLimit) {
    Visual v = new Visual(cities);
    int generation = 0;
    Population population = new Population(populationSize, numberOfCities);
    double lastBestFitness = population.getIndividuals()[0].getFitness();
    double bestFitness = population.getIndividuals()[0].getFitness();
    int sameValue = 0;
    while(true) {
      ++generation;
      ArrayList<Individual> parents = tournamentSelection(population.getIndividuals(), retain);
      population = new Population(populationSize, parents, mutationRate);
      bestFitness = population.getIndividuals()[0].getFitness();
      if(lastBestFitness == bestFitness) {
        ++sameValue;
      } else {
        sameValue = 0;
      }
      if(sameValue >= 20000) {
        break;
      }
      lastBestFitness = bestFitness;
      v.setPath(population.getIndividuals()[0].getPermutation());
      if(population.getIndividuals()[0].getFitness() <= fitnessLimit || generation > generationLimit) {
        break;
      }
      System.out.println(population.getIndividuals()[0].getFitness());
    }
    System.out.println(population.getIndividuals()[0].getFitness());
  }

  public static double temperature(double x) {
    return Math.pow(x-1, 8);
  }

  public static ArrayList<Individual> tournamentSelectionSA(Individual[] prevGen, double retain, double temperature) {
    ArrayList<Individual> parents = new ArrayList<Individual>();
    Individual[] ind = new Individual[tournamentSize];
    for(int i = 0; i < Math.max(2, prevGen.length * retain); ++i) {
      for(int j = 0; j < tournamentSize; ++j) {
        ind[j] = prevGen[random.nextInt(prevGen.length)];
      }
      Individual best = ind[0];
      for(int j = 1; j < tournamentSize; ++j) {
        if(ind[j].getFitness() < best.getFitness()) {
          best = ind[j];
        }
      }
      Individual randomIndividual = ind[random.nextInt(ind.length)];
      if(randomIndividual.getFitness() > best.getFitness()) {
        if((0.0 + random.nextInt(100))/100.0 < temperature) {
          parents.add(randomIndividual);
        } else {
          parents.add(best);
        }
      } else {
        parents.add(best);
      }
    }
    parents.add(prevGen[0]);
    return parents;
  }

  public static ArrayList<Individual> selectParentsSA(Individual[] prevGen, double retain, double temperature) {
    ArrayList<Individual> parents = new ArrayList<Individual>();
    while(parents.size() < prevGen.length) {
      int randomIndividual = random.nextInt(prevGen.length);
      if(randomIndividual > prevGen.length * retain) {
        if((0.0 + random.nextInt(100))/100.0 < temperature) {
          parents.add(prevGen[randomIndividual]);
        }
      } else {
        parents.add(prevGen[randomIndividual]);
      }
    }
    return parents;
  }

  public static void geneticAlgorithmSA(int populationSize, double mutationRate, double retain, double fitnessLimit, int generationLimit) {
    Visual v = new Visual(cities);
    int generation = 0;
    Population population = new Population(populationSize, numberOfCities);
    double lastBestFitness = population.getIndividuals()[0].getFitness();
    double bestFitness = population.getIndividuals()[0].getFitness();
    int sameValue = 0;
    while(true) {
      ++generation;
      double temperature = temperature((0.0+generation)/generationLimit);
      ArrayList<Individual> parents = tournamentSelectionSA(population.getIndividuals(), retain, temperature);
      population = new Population(populationSize, parents, mutationRate);
      bestFitness = population.getIndividuals()[0].getFitness();
      if(lastBestFitness == bestFitness) {
        ++sameValue;
      } else {
        sameValue = 0;
      }
      if(sameValue >= 20000) {
        break;
      }
      lastBestFitness = bestFitness;
      v.setPath(population.getIndividuals()[0].getPermutation());
      if(population.getIndividuals()[0].getFitness() <= fitnessLimit || generation > generationLimit) {
        break;
      }
      System.out.println(population.getIndividuals()[0].getFitness());
    }
    System.out.println(population.getIndividuals()[0].getFitness());
  }

  public static void main(String[] args) {
    cities = GenerateCities.readTSP();
    numberOfCities = cities.length;

    DistanceMatrix.distanceMatrix = new double[numberOfCities][numberOfCities];
    initalizeDistanceMatrix();

    tournamentSize = 4;
    int maxGenerations = 50000;
    int populationSize = numberOfCities * 4;
    double mutationRate = 0.2;
    double retain = 0.5;

    //Run the SA implementation
    geneticAlgorithmSA(populationSize, mutationRate, retain, 0.0, maxGenerations);
    System.out.println("SA Done");

    //Run the GA implementation
    //geneticAlgorithm(populationSize, mutationRate, retain, 0.0, maxGenerations);
    //System.out.println("GA Done");
  }
}
