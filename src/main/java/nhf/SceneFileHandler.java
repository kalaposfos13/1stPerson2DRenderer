package nhf;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;


public class SceneFileHandler {

   public static Scene loadSceneFromXML(File file) {
      Scene scene = null;
      try {
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(file);
         doc.getDocumentElement().normalize();

         // Check scene version
         String version = doc.getDocumentElement().getAttribute("version");
         System.out.println("Scene version: " + version);
         scene = new Scene(version);

         // Locate the <Lines> element
         NodeList linesNodeList = doc.getElementsByTagName("Lines");
         if (linesNodeList.getLength() > 0) {
            Element linesElement = (Element) linesNodeList.item(0);

            // Get all <Line> elements within <Lines>
            NodeList lineNodes = linesElement.getElementsByTagName("Line");

            for (int i = 0; i < lineNodes.getLength(); i++) {
               Node lineNode = lineNodes.item(i);

               if (lineNode.getNodeType() == Node.ELEMENT_NODE) {
                  Element lineElement = (Element) lineNode;

                  double x1 = Double.parseDouble(lineElement.getAttribute("x1"));
                  double y1 = Double.parseDouble(lineElement.getAttribute("y1"));
                  double x2 = Double.parseDouble(lineElement.getAttribute("x2"));
                  double y2 = Double.parseDouble(lineElement.getAttribute("y2"));
                  String color = lineElement.getAttribute("color");

                  System.out.println("Read line: (" + x1 + ", " + y1 + ") to (" + x2 + ", " + y2 + ") with color " + color);

                  // Create and add line to scene
                  Line line = new Line(x1, y1, x2, y2, Line.getColorFromHex(color));
                  scene.addLine(line);
               }
            }
         }

         System.out.println("Finished loading file. Scene has " + scene.getLines().size() + " lines.");

      } catch (Exception e) {
         e.printStackTrace();
      }

      return scene;
   }


   public static void saveSceneToXML(Scene scene, File file) throws Exception {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

      // Root element <Scene>
      Document doc = docBuilder.newDocument();
      Element rootElement = doc.createElement("Scene");
      rootElement.setAttribute("version", scene.getVersion());
      doc.appendChild(rootElement);

      // <Lines> element
      Element linesElement = doc.createElement("Lines");
      rootElement.appendChild(linesElement);

      // Iterate over lines and create <Line> elements
      for (Line line : scene.getLines()) {
         Element lineElement = doc.createElement("Line");

         lineElement.setAttribute("x1", Double.toString(line.p1.x));
         lineElement.setAttribute("y1", Double.toString(line.p1.y));
         lineElement.setAttribute("x2", Double.toString(line.p2.x));
         lineElement.setAttribute("y2", Double.toString(line.p2.y));
         lineElement.setAttribute("color", Line.getColorHex(line.color));

         linesElement.appendChild(lineElement);
      }

      // Write the content into an XML file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(file);

      transformer.transform(source, result);
   }
}