import javax.swing.*;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Visual {

  private Panel panel;

  public Visual(City[] cities) {
    JFrame frame = new JFrame();
    frame.setSize(900,900);
    panel = new Panel(cities);
    frame.add(panel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  public void setPath(int[] path) {
    panel.setPath(path);
    panel.setBackground(new java.awt.Color(255, 255, 255));
    panel.repaint();
  }

  class Panel extends JPanel {

    City[] cities;
    int[] path;

    Panel(City[] cities) {
      City[] scaledCities = new City[cities.length];
      double minX = 1000000.0;
      double maxX = 0.0;
      double minY = 1000000.0;
      double maxY = 0.0;
      for(City c : cities) {
        minX = c.getX() < minX ? c.getX() : minX;
        maxX = c.getX() > maxX ? c.getX() : maxX;
        minY = c.getY() < minY ? c.getY() : minY;
        maxY = c.getY() > maxY ? c.getY() : maxY;
      }
      double scaleX = maxX - minX;
      double scaleY = maxY - minY;
      for(int i = 0; i < cities.length; ++i) {
        City c = cities[i];
        double x = (c.getX()-minX) / (maxX-minX) * (900-0) + 0;
        double y = (c.getY()-minY) / (maxY-minY) * (900-0) + 0;
        y = 900 - y;
        scaledCities[i] = new City(x, y);
      }
      this.cities = scaledCities;
    }

    public void setPath(int[] path) {
      this.path = path;
    }

    @Override
    protected void paintComponent(Graphics g) {
      if(path != null) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new java.awt.Color(0,0,0));
        g2.setStroke(new BasicStroke(3));
        for(int i = 0; i < path.length-1; ++i) {
          City from = cities[path[i]];
          City to = cities[path[i+1]];
          int fromX = (int) from.getX();
          int fromY = (int) from.getY();
          int toX = (int) to.getX();
          int toY = (int) to.getY();
          g2.drawLine(fromX, fromY, toX, toY);
        }
        City from = cities[path[path.length-1]];
        City to = cities[path[0]];
        int fromX = (int) from.getX();
        int fromY = (int) from.getY();
        int toX = (int) to.getX();
        int toY = (int) to.getY();
        g2.drawLine(fromX, fromY, toX, toY);
        g2.setColor(new java.awt.Color(255,0,0));
        for(City c : cities) {
          g2.fillOval((int) c.getX()-7, (int) c.getY()-7, 14, 14);
        }
      }
    }
  }
}
