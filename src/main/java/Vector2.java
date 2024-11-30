import java.awt.*;

public class Vector2 {
   public double x;
   public double y;
   public Vector2() {
      this.x = 0;
      this.y = 0;
   }
   public Vector2(double x, double y) {
      this.x = x;
      this.y = y;
   }
   public Vector2(Vector2 v) {
      this.x = v.x;
      this.y = v.y;
   }
   public Vector2(Point p) {
      this.x = p.x;
      this.y = p.y;
   }
   public Vector2 add(Vector2 v) {
      return new Vector2(x + v.x, y + v.y);
   }
   public Vector2 sub(Vector2 v) {
      return new Vector2(x - v.x, y - v.y);
   }
   public Vector2 mul(double v) {
      return new Vector2(x * v, y * v);
   }
   public double angle() {
      return Math.atan2(y, x);
   }
   public double angle(Vector2 v) {
      return angle() - v.angle();
   }
   public double dot(Vector2 v) {
      return this.x * v.x + this.y * v.y;
   }
   public double length() {
      return Math.sqrt(this.x * this.x + this.y * this.y);
   }
   public Vector2 normalize() {
      return new Vector2(this.x / this.length(), this.y / this.length());
   }
}
