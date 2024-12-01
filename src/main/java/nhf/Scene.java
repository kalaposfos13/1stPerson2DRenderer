package nhf;

import java.util.ArrayList;
import java.util.List;


public class Scene {
   private String version;
   private final List<Line> lines;

   public Scene(String version) {
      this.version = version;
      this.lines = new ArrayList<>();
   }

   public String getVersion() {
      return version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public List<Line> getLines() {
      return lines;
   }
   public void removeLine(Line line) {
      lines.remove(line);
   }

   public void addLine(Line line) {
      lines.add(line);
   }
}