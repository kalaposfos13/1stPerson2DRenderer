package nhf;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ShaderUtil {
   public int initShaders(GL3 gl, String vertexShaderFile, String fragmentShaderFile) throws IOException {
      String vertexShaderSource = loadShaderSource(vertexShaderFile);
      String fragmentShaderSource = loadShaderSource(fragmentShaderFile);

      int vertexShader = createAndCompileShader(gl, GL3.GL_VERTEX_SHADER, vertexShaderSource);
      int fragmentShader = createAndCompileShader(gl, GL3.GL_FRAGMENT_SHADER, fragmentShaderSource);

      int shaderProgram = gl.glCreateProgram();
      gl.glAttachShader(shaderProgram, vertexShader);
      gl.glAttachShader(shaderProgram, fragmentShader);
      gl.glLinkProgram(shaderProgram);

      checkLinkStatus(gl, shaderProgram);
      gl.glDeleteShader(vertexShader);
      gl.glDeleteShader(fragmentShader);

      return shaderProgram;
   }

   private String loadShaderSource(String filename) throws IOException {
      Path shaderPath = Paths.get("shaders", filename);
      try (FileInputStream shaderStream = new FileInputStream(shaderPath.toFile())) {
         return new String(shaderStream.readAllBytes());
      }
   }

   public int createAndCompileShader(GL3 gl, int shaderType, String shaderSource) {
      int shader = gl.glCreateShader(shaderType);
      gl.glShaderSource(shader, 1, new String[]{shaderSource}, null);
      gl.glCompileShader(shader);

      int[] compileStatus = new int[1];
      compileStatus[0] = GL3.GL_TRUE;
      gl.glGetShaderiv(shader, GL3.GL_COMPILE_STATUS, compileStatus, 0);
      if (compileStatus[0] == GL3.GL_FALSE) {
         byte[] infoLog = new byte[512];
         gl.glGetShaderInfoLog(shader, infoLog.length, null, 0, infoLog, 0);
         throw new GLException("Shader compilation failed:\n\"" + new String(infoLog) + "\"");
      }
      return shader;
   }

   private void checkLinkStatus(GL3 gl, int shaderProgram) {
      int[] linkStatus = new int[1];
      linkStatus[0] = 1;
      gl.glGetProgramiv(shaderProgram, GL3.GL_LINK_STATUS, linkStatus, 0);
      if (linkStatus[0] == GL3.GL_FALSE) {
         byte[] infoLog = new byte[512];
         gl.glGetProgramInfoLog(shaderProgram, infoLog.length, null, 0, infoLog, 0);
         throw new GLException("Shader program linking failed:\n\"" + new String(infoLog) + "\"");
      }
   }
}
