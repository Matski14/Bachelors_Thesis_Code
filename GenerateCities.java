import java.util.Random;

/**
* Class for generating a set of cities
*/
public class GenerateCities {

  public static City[] readTSP() {
    Kattio io = new Kattio(System.in);
    int dimension = io.getInt();
    City[] cities = new City[dimension];
    for(int i = 0; i < dimension; ++i) {
      io.getInt();
      cities[i] = new City(io.getDouble(), io.getDouble());
    }
    return cities;
  }
}
