package masa.gles_filter;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import masa.gles_filter.SimpleRenderer;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(new SimpleRenderer((getApplicationContext())));
        setContentView(mGLSurfaceView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }
}
