package masa.gles_filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by masa on 2017/02/21.
 */

public final class SimpleRenderer implements GLSurfaceView.Renderer {

    private final Context mContext;

    private int mProgram;
    private int mPosition;
    private int mTexcoord;
    private int mTexture;
    private int mTextureId;

    private final FloatBuffer mVertexBuffer = GLES20Utils.createBuffer(vertexs);
    private final FloatBuffer mTexcoordBuffer = GLES20Utils.createBuffer(texcoords);

    private static final float vertexs[] = {
            -1.0f,  1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
             1.0f,  1.0f, 0.0f,
             1.0f, -1.0f, 0.0f
    };
    private static final float texcoords[] = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f
    };

    private static final String vertex_shader =
            "attribute vec4 position;" +
            "attribute vec2 texcoord;" +
            "varying vec2 texcoordVarying;" +
                "void main() {" +
                "gl_Position = position;" +
                "texcoordVarying = texcoord;" +
            "}";
    private static final String fragment_shader =
            "precision mediump float;" +
            "varying vec2 texcoordVarying;" +
            "uniform sampler2D texture;" +
            "void main() {" +
                "gl_FragColor = texture2D(texture, texcoordVarying);" +
            "}";

    public SimpleRenderer(final Context context) {
        mContext = context;
    }

    // The system calls this method once, when creating the GLSurfaceView.
    // Use this method to perform actions that need to happen only once,
    // such as setting OpenGL environment parameters or initializing OpenGL graphic objects.
    @Override
    public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
        mProgram = GLES20Utils.createProgram(vertex_shader, fragment_shader);
        if (mProgram == 0) {
            throw new IllegalStateException();
        }
        GLES20.glUseProgram(mProgram);
        GLES20Utils.checkGlError("glUserProgram");

        mPosition = GLES20.glGetAttribLocation(mProgram, "position");
        GLES20Utils.checkGlError("glGetAttribLocation position");
        if (mPosition == -1) {
            throw new IllegalStateException("Could not get attrib location for position");
        }
        GLES20.glEnableVertexAttribArray(mPosition);

        mTexcoord = GLES20.glGetAttribLocation(mProgram, "texcoord");
        GLES20Utils.checkGlError("glGetAttribLocation texcoord");
        if (mPosition == -1) {
            throw new IllegalStateException("Could not get texcoord");
        }
        GLES20.glEnableVertexAttribArray(mTexcoord);

        mTexture = GLES20.glGetUniformLocation(mProgram, "texture");
        GLES20Utils.checkGlError("glGetUniformLocation texture");
        if (mTexture == -1) {
            throw new IllegalStateException("Could not get uniform location for texture");
        }

        final Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.lena);
        mTextureId = GLES20Utils.loadTexture(bitmap);
        bitmap.recycle();
    }

    // The system calls this method on each redraw of the GLSurfaceView.
    // Use this method as the primary execution point for drawing (and re-drawing) graphic objects.
    @Override
    public void onSurfaceChanged(final GL10 gl, final int width, final int height) {
        final int x = 0, y = 0;
        GLES20.glViewport(x, y, width, height);
        GLES20Utils.checkGlError("glViewport");
    }

    // The system calls this method when the GLSurfaceView geometry changes,
    // including changes in size of the GLSurfaceView or orientation of the device screen.
    // For example, the system calls this method when the device changes from portrait to
    // landscape orientation. Use this method to respond to changes in the GLSurfaceView container.
    @Override
    public void onDrawFrame(final GL10 gl) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glUniform1i(mTexture, 0);
        GLES20.glVertexAttribPointer(mTexcoord, 2, GLES20.GL_FLOAT, false, 0, mTexcoordBuffer);
        GLES20.glVertexAttribPointer(mPosition, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glDisable(GLES20.GL_TEXTURE_2D);
    }
}
