package nhf;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;


public class RendererPanel extends JPanel implements GLEventListener {
   private int shaderProgram;
   private int[] vao = new int[1];  // VAO
   private int[] vbo = new int[1];  // VBO
   private int[] ebo = new int[1];  // EBO
   private int[] ssbo = new int[1];  // SSBO
   private GLCanvas glCanvas;
   private ShaderUtil shaderUtil;
   private Camera camera;
   private RendererInputHandler inputHandler;
   Timer frameTimer;
   private Scene scene;

   public RendererPanel(Scene s) {
      scene = s;
      glCanvas = new GLCanvas();
      glCanvas.addGLEventListener(this);

      shaderUtil = new ShaderUtil();
      camera = new Camera();
      inputHandler = new RendererInputHandler(camera);

      // Set layout and add the canvas
      setLayout(new BorderLayout());
      add(glCanvas, BorderLayout.CENTER);

      // Register input handler for the entire window
      glCanvas.addKeyListener(inputHandler);

      // Start a frame timer for continuous redraws
      frameTimer = new Timer(16, e -> glCanvas.display());
      frameTimer.start();

      // Initialize buffers after the OpenGL context is ready
      glCanvas.addGLEventListener(new GLEventListener() {
         @Override
         public void init(GLAutoDrawable drawable) {
            //initBuffers(scene, drawable.getGL().getGL3());
         }

         @Override
         public void dispose(GLAutoDrawable drawable) {}

         @Override
         public void display(GLAutoDrawable drawable) {
            //camera.updateCamera(inputHandler.getKeyStates());
         }

         @Override
         public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
      });
   }

   public void initBuffers(Scene scene, GL3 gl) {
      System.out.println("OpenGL Version: " + gl.glGetString(GL.GL_VERSION));

      // Step 1: Initialize screen quad
      float[] quadVertices = {
            // Positions    // Texture Coordinates
            -1.0f, -1.0f,   0.0f, 0.0f, // Bottom-left
            1.0f, -1.0f,   1.0f, 0.0f, // Bottom-right
            -1.0f,  1.0f,   0.0f, 1.0f, // Top-left
            1.0f,  1.0f,   1.0f, 1.0f  // Top-right
      };
      int[] quadIndices = { 0, 1, 2, 1, 2, 3 };

      FloatBuffer vertexBuffer = FloatBuffer.wrap(quadVertices);
      IntBuffer indexBuffer = IntBuffer.wrap(quadIndices);

      // Generate VBO and EBO
      int[] vbo = new int[1];
      int[] ebo = new int[1];
      gl.glGenBuffers(1, vbo, 0);
      gl.glGenBuffers(1, ebo, 0);

      // Upload vertex data
      gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
      gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertexBuffer.limit() * Float.BYTES, vertexBuffer, GL3.GL_STATIC_DRAW);

      // Upload index data
      gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, ebo[0]);
      gl.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.limit() * Integer.BYTES, indexBuffer, GL3.GL_STATIC_DRAW);

      // Generate and configure VAO
      int[] vao = new int[1];
      gl.glGenVertexArrays(1, vao, 0);
      gl.glBindVertexArray(vao[0]);

      gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
      gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, ebo[0]);

      gl.glEnableVertexAttribArray(0);
      gl.glVertexAttribPointer(0, 2, GL3.GL_FLOAT, false, 4 * Float.BYTES, 0);

      // Texture coordinate attribute (layout = 1 in the shader)
      gl.glEnableVertexAttribArray(1);
      gl.glVertexAttribPointer(1, 2, GL3.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);

      gl.glBindVertexArray(0); // Unbind VAO

      // Step 2: Prepare line data
      float[] lineData = new float[scene.getLines().size() * 7];
      int index = 0;
      for (Line line : scene.getLines()) {
         lineData[index++] = (float) line.p1.x;
         lineData[index++] = (float) line.p1.y;
         lineData[index++] = (float) line.p2.x;
         lineData[index++] = (float) line.p2.y;
         lineData[index++] = (float) line.color.getRed() / 255.0f;
         lineData[index++] = (float) line.color.getGreen() / 255.0f;
         lineData[index++] = (float) line.color.getBlue() / 255.0f;
      }

      int[] ssbo = new int[1];
      gl.glGenBuffers(1, ssbo, 0);
      gl.glBindBuffer(GL3.GL_SHADER_STORAGE_BUFFER, ssbo[0]);
      gl.glBufferData(GL3.GL_SHADER_STORAGE_BUFFER, lineData.length * Float.BYTES, FloatBuffer.wrap(lineData), GL3.GL_STATIC_DRAW);
      gl.glBindBufferBase(GL3.GL_SHADER_STORAGE_BUFFER, 0, ssbo[0]);

      // Store buffer IDs
      this.vao = vao;
      this.vbo = vbo;
      this.ebo = ebo;
      this.ssbo = ssbo;

      // Log completion
      System.out.println("Buffers initialized successfully");
   }

   @Override
   public void init(GLAutoDrawable drawable) {
      GL3 gl = drawable.getGL().getGL3();
      gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // White background
      gl.glEnable(GL3.GL_DEPTH_TEST);          // Enable depth testing
      try {
         shaderProgram = shaderUtil.initShaders(gl, "vertex_shader.glsl", "fragment_shader.glsl");
      } catch (IOException e) {
         e.printStackTrace();
      }
      initBuffers(scene, gl);
   }

   @Override
   public void dispose(GLAutoDrawable drawable) {
      GL3 gl = drawable.getGL().getGL3();
      gl.glDeleteVertexArrays(1, vao, 0);
      gl.glDeleteBuffers(1, vbo, 0);
   }

   @Override
   public void display(GLAutoDrawable drawable) {
      camera.updateCamera(inputHandler.getKeyStates());
      //System.out.println("Rendering a frame...");
      GL3 gl = drawable.getGL().getGL3();
      gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

      // Use the shader program
      gl.glUseProgram(shaderProgram);

      // Bind the VAO
      gl.glBindVertexArray(vao[0]);
      // Set uniforms for Camera
      int cameraPositionLocation = gl.glGetUniformLocation(shaderProgram, "cameraPosition");
      int cameraRotationLocation = gl.glGetUniformLocation(shaderProgram, "cameraRotation");
      int cameraFovLocation = gl.glGetUniformLocation(shaderProgram, "cameraFov");
      int flippedLocation = gl.glGetUniformLocation(shaderProgram, "imageFlipped");
      int lineRenderLocation = gl.glGetUniformLocation(shaderProgram, "lineRendering");

      if (cameraPositionLocation == -1 || cameraRotationLocation == -1 ||
            cameraFovLocation == -1 || flippedLocation == -1 || lineRenderLocation == -1) {
         System.err.println("Uniform not found in shader");
      }

      gl.glUniform2f(cameraPositionLocation, (float) camera.getPosition().x, (float) camera.getPosition().y);
      gl.glUniform1f(cameraRotationLocation, (float) camera.getRotation());
      gl.glUniform1f(cameraFovLocation, (float) camera.getFov());
      gl.glUniform1i(flippedLocation, camera.getFlipState() ? 1 : 0);
      gl.glUniform1i(lineRenderLocation, camera.get1DRendering() ? 1 : 0);

      // Draw
      gl.glDrawElements(GL3.GL_TRIANGLES, 6, GL3.GL_UNSIGNED_INT, 0);

      gl.glBindVertexArray(0); // Unbind the VAO
      gl.glUseProgram(0);
   }


   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL3 gl = drawable.getGL().getGL3();
      gl.glViewport(0, 0, width, height);
   }
}
