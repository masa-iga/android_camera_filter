package masa.gles_filter;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import masa.gles_filter.SimpleRenderer;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView0;
    private GLSurfaceView mGLSurfaceView1;
    private GLSurfaceView mGLSurfaceView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGLSurfaceView0 = (GLSurfaceView)findViewById(R.id.glview0);
        mGLSurfaceView0.setEGLContextClientVersion(2);
        SimpleRenderer renderer0 = new SimpleRenderer(getApplicationContext());
        renderer0.setFragmentShaderType(SimpleRenderer.fragmentShaderType.GRAYAVERAGE);
        mGLSurfaceView0.setRenderer(renderer0);

        mGLSurfaceView1 = (GLSurfaceView)findViewById(R.id.glview1);
        mGLSurfaceView1.setEGLContextClientVersion(2);
        SimpleRenderer renderer1 = new SimpleRenderer(getApplicationContext());
        renderer1.setFragmentShaderType(SimpleRenderer.fragmentShaderType.MOSAIC);
        mGLSurfaceView1.setRenderer(renderer1);

        mGLSurfaceView2 = (GLSurfaceView)findViewById(R.id.glview2);
        mGLSurfaceView2.setEGLContextClientVersion(2);
        SimpleRenderer renderer2 = new SimpleRenderer(getApplicationContext());
        renderer2.setFragmentShaderType(SimpleRenderer.fragmentShaderType.WATER);
        mGLSurfaceView2.setRenderer(renderer2);
    }

    @Override
    public void onResume() {
        super.onResume();
        mGLSurfaceView0.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGLSurfaceView0.onPause();
    }
}
