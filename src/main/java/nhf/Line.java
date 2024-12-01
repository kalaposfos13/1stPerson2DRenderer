package nhf;

import java.awt.*;
import java.awt.geom.Point2D;
 

public class Line {
   public Vector2 p1;
   public Vector2 p2;
   public Color color;


   public Line(double p1x, double p1y, double p2x, double p2y, Color color) {
      this.p1 = new Vector2(p1x, p1y);
      this.p2 = new Vector2(p2x, p2y);
      this.color = color;
   }
   public Line(Point2D p1, Point2D p2, Color color) {
      this.p1 = new Vector2(p1.getX(), p1.getY());
      this.p2 = new Vector2(p2.getX(), p2.getY());
      this.color = color;
   }
   public Line(Vector2 p1, Vector2 p2, Color color) {
      this.p1 = p1;
      this.p2 = p2;
      this.color = color;
   }

   // Convert color to hex string
   public static String getColorHex(Color color) {
      return String.format("#%02x%02x%02x%02x", color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
   }
   public static Color getColorFromHex(String hex) {
      return new Color((int)Long.parseLong(hex.substring(1), 16), true);
   }
}