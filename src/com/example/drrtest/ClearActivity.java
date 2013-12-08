package com.example.drrtest;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
 
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
 
public class ClearActivity extends Activity {
	private static String TAG = "ClearActivity";
	private static String TAG2 = "MyResult";
	
    private class FpsResult {
        private float mFps;
        private boolean mValid = false;

        public final synchronized void notifyResult(float fps) {
            if (!mValid) {
                mFps = fps;
                mValid = true;
                notifyAll();
            }
        }

        public final synchronized float waitResult() {
            while (!mValid) {
                try {
                    wait();
                } catch (InterruptedException e) {/* ignore and retry */}
            }
            return mFps;
        }
    }
	private FpsResult mResult = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView = new GLSurfaceView(this);
        mGLView.setRenderer(new ClearRenderer());
        setContentView(mGLView);
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//нч╧ь
        //FpsResult mResult = new FpsResult();
        mResult = new FpsResult();
        Log.i(TAG2,"before achivedFps");
        new MyThread().start();        
        Log.i(TAG2,"after achivedFps");
    }
    
    class MyThread extends Thread{
    	public void run(){
    		float achievedFps = mResult.waitResult();
    	}
    }
 
    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }
 
    private GLSurfaceView mGLView;
    
    class ClearRenderer implements GLSurfaceView.Renderer {
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // Do nothing special.
        }
     
        public void onSurfaceChanged(GL10 gl, int w, int h) {
            gl.glViewport(0, 0, w, h);
        }
     
        public void onDrawFrame(GL10 gl) {
        	Log.i(TAG,"onDrawFrame");
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        }
    }
}
 
