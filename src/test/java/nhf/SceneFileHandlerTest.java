package nhf;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

public class SceneFileHandlerTest {
    @Test
    void readTest() {
        // Use the class loader to get the resource URL
        URL resource = getClass().getClassLoader().getResource("nhf/treeScene.xml");
        assertNotEquals(resource, null);

        // Convert URL to File
        File file = new File(resource.getFile());
        Scene s = SceneFileHandler.loadSceneFromXML(file);

        assertEquals(19, s.getLines().size());
    }

    @Test
    void saveTest() throws Exception {
        // Load the input scene
        URL inputResource = getClass().getClassLoader().getResource("nhf/treeScene.xml");
        assertNotEquals(inputResource, null);

        File inputFile = new File(inputResource.getFile());
        Scene scene = SceneFileHandler.loadSceneFromXML(inputFile);

        // Define the output file path
        File outputFile = new File("src/test/resources/nhf/testOutput.xml");

        // Save the scene to the output file
        SceneFileHandler.saveSceneToXML(scene, outputFile);

        // Assert that the saved file has 24 lines
        long lineCount = Files.lines(outputFile.toPath()).count();
        assertEquals(24, lineCount, "The output file should contain 24 lines.");

        // Clean up: delete the output file
        boolean deleted = outputFile.delete();
        assertEquals(deleted, true);
    }
}
