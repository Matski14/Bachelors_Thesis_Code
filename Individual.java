import java.util.Random;
import java.util.HashSet;

public class Individual implements Comparable<Individual> {

  private Random random = new Random();
  private int[] permutation;
  private double fitness;

  public Individual(int[] permutation) {
    this.permutation = permutation;
    setFitness();
  }

  public Individual(int[] permutation, double mutationRate) {
    if((0.0 + random.nextInt(100))/100.0 < mutationRate) {
      int rand = random.nextInt(3);
      if(rand == 0) {
        this.permutation = insertionMutation(permutation);
      } else if(rand == 1) {
        this.permutation = displacementMutation(permutation);
      } else {
        this.permutation = inversionMutation(permutation);
      }
    } else {
      this.permutation = permutation;
    }
    setFitness();
  }

  /**
  * Insertion mutation
  */
  private int[] insertionMutation(int[] permutation) {
    int randomCity = random.nextInt(permutation.length);
    int insert = random.nextInt(permutation.length);
    int[] mutatedPermutation = new int[permutation.length];

    for(int i = 0; i < permutation.length; ++i) {
      if(i < randomCity) {
        mutatedPermutation[i] = permutation[i];
      } else if(i > randomCity) {
        mutatedPermutation[i-1] = permutation[i];
      }
    }
    for(int i = permutation.length-1; i > insert; --i) {
      mutatedPermutation[i] = mutatedPermutation[i-1];
    }
    mutatedPermutation[insert] = permutation[randomCity];
    return mutatedPermutation;
  }

  /**
  * Displacement mutation
  */
  private int[] displacementMutation(int[] permutation) {
    int[] mutatedPermutation = new int[permutation.length];
    int left = random.nextInt(permutation.length);
    int right = random.nextInt(permutation.length);
    if(left > right) {
      int temp = left;
      left = right;
      right = temp;
    }
    for(int i = 0; i < permutation.length; ++i) {
      if(i < left) {
        mutatedPermutation[i] = permutation[i];
      } else if(i > right) {
        mutatedPermutation[i-(right-left)-1] = permutation[i];
      }
    }
    int insert = random.nextInt(permutation.length - (right - left));

    for(int i = 0; i < permutation.length - (right-left+1) - insert; ++i) {
      mutatedPermutation[permutation.length-1-i] = mutatedPermutation[permutation.length-1-i-(right-left+1)];
    }
    for(int i = 0; i < right-left+1; ++i) {
      mutatedPermutation[insert + i] = permutation[left + i];
    }
    return mutatedPermutation;
  }

  /**
  * Inversion mutation
  */
  private int[] inversionMutation(int[] permutation) {
    int[] mutatedPermutation = new int[permutation.length];
    int left = random.nextInt(permutation.length);
    int right = random.nextInt(permutation.length);
    if(left > right) {
      int temp = left;
      left = right;
      right = temp;
    }
    for(int i = 0; i < permutation.length; ++i) {
      if(i < left) {
        mutatedPermutation[i] = permutation[i];
      } else if(i > right) {
        mutatedPermutation[i-(right-left)-1] = permutation[i];
      }
    }
    int insert = random.nextInt(permutation.length - (right - left));

    for(int i = 0; i < permutation.length - (right-left+1) - insert; ++i) {
      mutatedPermutation[permutation.length-1-i] = mutatedPermutation[permutation.length-1-i-(right-left+1)];
    }
    for(int i = 0; i < right-left+1; ++i) {
      mutatedPermutation[insert + (right-left) - i] = permutation[left + i];
    }
    return mutatedPermutation;
  }

  private void setFitness() {
    fitness = 0.0;
    for(int i = 0; i < permutation.length-1; ++i) {
      int next = permutation[i];
      fitness += DistanceMatrix.distanceMatrix[permutation[i]][permutation[i+1]];
    }
    fitness += DistanceMatrix.distanceMatrix[permutation[permutation.length-1]][permutation[0]];
  }

  public double getFitness() {
    return fitness;
  }

  public int[] getPermutation() {
    return permutation;
  }

  @Override
  public int compareTo(Individual ind) {
    Double d1 = getFitness();
    Double d2 = ind.getFitness();
    return d1.compareTo(d2);
  }

  public String toString() {
      StringBuilder sb = new StringBuilder();
      for(int i : permutation) {
        sb.append("" + i + " ");
      }
      sb.append("\nFitness: " + fitness);
      sb.append("\n--------------------------------------------------");
      return sb.toString();
  }
}
