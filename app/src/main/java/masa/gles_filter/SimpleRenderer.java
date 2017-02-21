package masa.gles_filter;

import android.content.Context;
import android.opengl.GLSurfaceView;

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
            "attribute vec4 positions;" +
            "attribute vec2 texcoord;" +
            "varying vec2 texcoordVarying;" +
                "void main() {" +
                "gl_Position = position;" +
                "texcorrdVarying = texcoord;" +
            "}";
    private static final String fragment_shader =
            "";

    public SimpleRenderer(final Context context) {
        mContext = context;
    }

    // The system calls this method once, when creating the GLSurfaceView.
    // Use this method to perform actions that need to happen only once,
    // such as setting OpenGL environment parameters or initializing OpenGL graphic objects.
    @Override
    public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
        mProgram = GLES20Utils.createProgram(vertex_shader, fragment_shader);
    }

    // The system calls this method on each redraw of the GLSurfaceView.
    // Use this method as the primary execution point for drawing (and re-drawing) graphic objects.
    @Override
    public void onSurfaceChanged(final GL10 gl, final int width, final int height) {
    }

    // The system calls this method when the GLSurfaceView geometry changes,
    // including changes in size of the GLSurfaceView or orientation of the device screen.
    // For example, the system calls this method when the device changes from portrait to
    // landscape orientation. Use this method to respond to changes in the GLSurfaceView container.
    @Override
    public void onDrawFrame(final GL10 gl) {
    }
}
