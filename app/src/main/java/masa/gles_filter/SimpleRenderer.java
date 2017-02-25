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
    private fragmentShaderType mFragmentShaderType;

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
            "varying vec2 v_texcoord;" +
            "  void main() {" +
            "  gl_Position = position;" +
            "  v_texcoord = texcoord;" +
            "}";
    private static final String fragment_shader_through =
            "precision mediump float;" +
            "varying vec2 v_texcoord;" +
            "uniform sampler2D s_texture;" +
            "void main() {" +
            "  gl_FragColor = texture2D(s_texture, v_texcoord);" +
            "}";

    public static final String fragment_shader_grayAverage =
            "precision mediump float;" +
            "varying vec2 v_texcoord;" +
            "uniform sampler2D s_texture;" +
            "void main() {" +
            "  vec4 tex = texture2D(s_texture, v_texcoord);" +
            "  float c;" +
            "  c = (tex.r + tex.g + tex.b) / 3.0;" +
            "  vec4 pixel = vec4(c, c, c, tex.a);" +
            "  gl_FragColor = vec4(pixel);" +
            "}";

    public static final String fragment_shader_mosaic =
            "precision mediump float;" +
            "varying vec2 v_texcoord;" +
            "uniform sampler2D s_texture;" +
            "void main() {" +
            "  vec2 uv = floor(v_texcoord * 20.0) / 20.0;" +
            "  vec4 tex = texture2D(s_texture, uv);" +
            "  vec4 pixel = vec4(tex.rgb, 1.0);" +
            "  gl_FragColor = vec4(pixel);" +
            "}";

    public static final String fragment_shader_water =
            "precision mediump float;" +
            "varying vec2 v_texcoord;" +
            "uniform sampler2D s_texture;" +
            "void main() {" +
            "  vec2 uv = vec2(pow(v_texcoord.x, 2.0), pow(v_texcoord.y, 2.0));" +
            "  vec4 tex = texture2D(s_texture, uv);" +
            "  vec4 pixel = vec4(vec3(" +
            "       tex.r *   0.0 / 255.0," +
            "       tex.g * 153.0 / 255.0," +
            "       tex.b * 204.0 / 255.0" +
            "       ), 1.0);" +
            "  gl_FragColor = vec4(pixel);" +
            "}";

    public enum fragmentShaderType {
        THROUGH, GRAYAVERAGE, MOSAIC, WATER
    }

    public final void setFragmentShaderType(fragmentShaderType type) {
         mFragmentShaderType = type;
    }

    public SimpleRenderer(final Context context) {
        mContext = context;
        mFragmentShaderType = fragmentShaderType.THROUGH;
    }

    // The system calls this method once, when creating the GLSurfaceView.
    // Use this method to perform actions that need to happen only once,
    // such as setting OpenGL environment parameters or initializing OpenGL graphic objects.
    @Override
    public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
        String fragment_shader;
        switch (mFragmentShaderType) {
            case THROUGH:
                fragment_shader = fragment_shader_through; break;
            case GRAYAVERAGE:
                fragment_shader = fragment_shader_grayAverage; break;
            case MOSAIC:
                fragment_shader = fragment_shader_mosaic; break;
            case WATER:
                fragment_shader = fragment_shader_water; break;
            default :
                fragment_shader = fragment_shader_through; break;
        }
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

        mTexture = GLES20.glGetUniformLocation(mProgram, "s_texture");
        GLES20Utils.checkGlError("glGetUniformLocation s_texture");
        if (mTexture == -1) {
            throw new IllegalStateException("Could not get uniform location for s_texture");
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
