package com.yu.opengldemo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GLView extends GLSurfaceView {

    public GLView(Context context) {
        this(context, null);
    }

    public GLView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //设置OpenGL版本
        setEGLContextClientVersion(2);

        //设置Renderer（GLSurfaceView展示依赖于GLSurfaceView.Render）
        setRenderer(new GLViewRender(context));

        //设置刷新模式，RENDERMODE_WHEN_DIRTY：手动刷新
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
