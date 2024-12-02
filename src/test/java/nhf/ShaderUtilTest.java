package nhf;

import com.jogamp.opengl.GL3;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.io.IOException;

public class ShaderUtilTest {

    @Test
    void testShaderInitShadersCalled() throws Exception {
        GL3 glMock = mock(GL3.class); 
        // Sadly this doesn't work that well and some things that work with the real thing just don't with this
        ShaderUtil shaderUtil = new ShaderUtil();

        // Call initShaders with mock OpenGL object
        int shaderProgram = shaderUtil.initShaders(glMock, "vertex_shader.glsl", "fragment_shader.glsl");

        // Verify that glCreateProgram, glAttachShader, and other OpenGL methods are called
        verify(glMock, times(1)).glCreateProgram();
        verify(glMock, times(2)).glAttachShader(anyInt(), anyInt());
        verify(glMock, times(1)).glLinkProgram(anyInt());
    }

    @Test
    void testInitShadersWithInvalidFile() {
        ShaderUtil shaderUtil = new ShaderUtil();
        
        // Assuming loadShaderSource() or initShaders() would throw an exception if file is invalid
        assertThrows(IOException.class, () -> {
            shaderUtil.initShaders(null, "nonexistentVertexShader.glsl", "nonexistentFragmentShader.glsl");
        });
    }
}
