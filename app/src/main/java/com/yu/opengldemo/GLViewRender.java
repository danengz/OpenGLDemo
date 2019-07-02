package com.yu.opengldemo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLViewRender implements GLSurfaceView.Renderer {

    protected Context mContext;
    Triangle triangle;

    public GLViewRender(Context context) {
        this.mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //设置背景颜色
        GLES20.glClearColor(0, 0, 0, 0);
        triangle = new Triangle(mContext);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        triangle.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        triangle.onDrawFrame();
    }
}
