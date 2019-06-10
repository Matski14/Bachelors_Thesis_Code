public class City {

  private double xCoordinate;
  private double yCoordinate;

  public City(double xCoordinate, double yCoordinate) {
    this.xCoordinate = xCoordinate;
    this.yCoordinate = yCoordinate;
  }

  public double getX() {
    return xCoordinate;
  }

  public double getY() {
    return yCoordinate;
  }

  public String toString() {
    return "X: " + xCoordinate + "\tY: " + yCoordinate;
  }
}
