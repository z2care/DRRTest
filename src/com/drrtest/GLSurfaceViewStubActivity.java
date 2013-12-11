/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drrtest;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * A minimal activity for testing {@link android.opengl.GLSurfaceView}.
 * Also accepts non-blank renderers to allow its use for more complex tests.
 */
public class GLSurfaceViewStubActivity extends Activity {
    private static final float FPS_TOLERANCE = 2.0f;

    private static final String TAG = "GLSurfaceViewStubActivity";
    
    private static class Renderer implements GLSurfaceView.Renderer {

        // Measurement knobs.
        // NB: Some devices need a surprisingly long warmup period before the
        // framerate becomes stable.
        private static final float WARMUP_SECONDS = 2.0f;
        private static final float TEST_SECONDS   = 8.0f;
    	
        // Test states
        private static final int STATE_START  = 0;
        private static final int STATE_WARMUP = 1;
        private static final int STATE_TEST   = 2;
        private static final int STATE_DONE   = 3;
        
        private int       mState     = STATE_START;
        private float     mStartTime = 0.0f;
        private int       mNumFrames = 0;
    	
        public void onDrawFrame(GL10 gl) {
            float t = (float)System.nanoTime() * 1.0e-9f;
            switch (mState) {
                case STATE_START:
                    mStartTime = t;
                    mState = STATE_WARMUP;
                    break;

                case STATE_WARMUP:
                    if ((t - mStartTime) >= WARMUP_SECONDS) {
                        mStartTime = t;
                        mNumFrames = 0;
                        mState = STATE_TEST;
                    }
                    break;

                case STATE_TEST:
                    mNumFrames++;
                    float elapsed = t - mStartTime;
                    if (elapsed >= TEST_SECONDS) {
                        //mResult.notifyResult((float)mNumFrames / elapsed);//handle msg
                        mState = STATE_DONE;
                    }
                    break;

                case STATE_DONE:
                    //if (mResult.restartNecessary()) {
                        //mResult.ackRestart();
                        mState = STATE_START;
                        Log.d(TAG, "restarting");
                    //}
                    break;
            }

            // prevent unwanted optimizations or hidden costs (e.g. reading
            // previous frame on tilers).
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            gl.glClear(gl.GL_COLOR_BUFFER_BIT);
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // Do nothing.
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // Do nothing.
        }
    }

    private GLSurfaceView mView;

    /** To override the blank renderer, or other settings, these
     * static set* methods must be called before onCreate() is called.
     * If using ActivityInstrumentationTestCase2, that means the set
     * methods need to be called before calling getActivity in the
     * test setUp().
     */
    private static GLSurfaceView.Renderer mRenderer = null;
    public static void setRenderer(GLSurfaceView.Renderer renderer) {
        mRenderer = renderer;
    }
    public static void resetRenderer() {
        mRenderer = null;
    }

    private static int mRenderMode = 0;
    private static boolean mRenderModeSet = false;
    public static void setRenderMode(int renderMode) {
        mRenderModeSet = true;
        mRenderMode = renderMode;
    }
    public static void resetRenderMode() {
        mRenderModeSet = false;
        mRenderMode = 0;
    }

    private static int mGlVersion = 0;
    private static boolean mGlVersionSet = false;
    public static void setGlVersion(int glVersion) {
        mGlVersionSet = true;
        mGlVersion = glVersion;
    }
    public static void resetGlVersion() {
        mGlVersionSet = false;
        mGlVersion = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = new GLSurfaceView(this);
        // Only set this if explicitly asked for
        if (mGlVersionSet) {
            mView.setEGLContextClientVersion(mGlVersion);
        }
        // Use no-op renderer by default
        if (mRenderer == null) {
            mView.setRenderer(new Renderer());
        } else {
            mView.setRenderer(mRenderer);
        }
        // Only set this if explicitly asked for
        if (mRenderModeSet) {
            mView.setRenderMode(mRenderMode);
        }
        setContentView(mView);
        
        GLSurfaceViewStubActivity.setRenderer(new Renderer());
        GLSurfaceViewStubActivity.setRenderMode(
                GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        
        
    }

    public void testRefreshRate() throws java.lang.InterruptedException {
        boolean fpsOk = false;
        //GLSurfaceViewStubActivity activity = getActivity();

        WindowManager wm = (WindowManager)this
                .getView()
                .getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display dpy = wm.getDefaultDisplay();
        float claimedFps = dpy.getRefreshRate();

        //for (int i = 0; i < 3; i++) {
            //float achievedFps = mResult.waitResult();
            //Log.d(TAG, "claimed " + claimedFps + " fps, " + "achieved " + achievedFps + " fps");
            //fpsOk = Math.abs(claimedFps - achievedFps) <= FPS_TOLERANCE;
            //if (fpsOk) {
                //break;
            //} else {
                // it could be other sctivity like bug report capturing for other failures
                // sleep for a while and re-try
                //Thread.sleep(10000);
                //mResult.restart();
            //}
        //}
        //activity.finish();
        //assertTrue(fpsOk);
    }
    
    public GLSurfaceView getView() {
        return mView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mView.onPause();
    }
}