package masa.gles_filter;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by masa on 2017/02/22.
 */

public final class GLES20Utils {
    private GLES20Utils() {}

    private static final int FIRST_INDEX = 0;
    private static final int DEFAULT_OFFSET = 0;
    private static final int FLOAT_SIZE_BYTES = 4;

    public static FloatBuffer createBuffer(float[] array) {
        final FloatBuffer buffer =
                ByteBuffer.allocateDirect(array.length * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buffer.put(array).position(FIRST_INDEX);
        return buffer;
    }

    public static int createProgram(final String vertexSource, final String fragmentSource) throws GLException {
        final int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        // TODO
        int program = GLES20.glCreateProgram();
        return program;
    }

    public static int loadShader(final int shaderType, final String source) {
        int shader = GLES20.glCreateShader(shaderType);
        // TODO
        return shader;
    }

    public static void checkGlError(final String op) throws GLException {
        // TODO
    }

    public static int loadTexture(final Bitmap bitmap) {
        // TODO
        return loadTexture(bitmap, GLES20.GL_NEAREST, GLES20.GL_LINEAR);
    }

    public static int loadTexture(final Bitmap bitmap, final int min, final int mag) {
        // TODO
        final int texture = 0;
        return texture;
    }

}
