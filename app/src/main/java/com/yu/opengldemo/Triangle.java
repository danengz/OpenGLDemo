package com.yu.opengldemo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Triangle {

    int mProgram; //存放顶点着色器和片元着色器的程序的地址

    // 顶点
    static float triangleCoords[] = {
            0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
    };

    // 颜色
    float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    private FloatBuffer vertexBuffer; // 存放顶点的缓冲器，因为OpenGL接受参数为FloatBuffer，所以需要把triangleCoords[]中的数据copy到vertexBuffer



    private float[] mViewMatrix=new float[16];
    private float[] mProjectMatrix=new float[16];
    private float[] mMVPMatrix=new float[16];

    /**
     * 构造函数
     * @param context
     */
    public Triangle(Context context) {

        //float占4个字节
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        // position归位
        vertexBuffer.position(0);

        //创建顶点着色器  并且在GPU进行编译
        int shader= GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        //        String str = "attribute vec4 vPosition;\n" +
        //                "void main(){\n" +
        //                "    gl_Position=vPosition;\n" +
        //                "}";
        //        GLES20.glShaderSource(shader, str);
        // 上面注释的和下面这句一样
        GLES20.glShaderSource(shader, Utils.readRawTextFile(context, R.raw.base_vertex));
        GLES20.glCompileShader(shader);

        //创建片元着色器  并且在GPU进行编译
        int fragmentShader=GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, Utils.readRawTextFile(context, R.raw.base_frag));
        GLES20.glCompileShader(fragmentShader);

        //将片元着色器和顶点着色器放到统一程序中
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, shader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        //连接到着色器程序
        GLES20.glLinkProgram(mProgram);
    }


    public void onSurfaceChanged(int width, int height) {

        float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;

        if (width > height) {
            //横屏。需要设置的就是左右。
            Matrix.orthoM(mProjectMatrix, 0, -aspectRatio, aspectRatio, -1, 1f, -1f, 1f);
        } else {
            //竖屏。需要设置的就是上下
            Matrix.orthoM(mProjectMatrix, 0, -1, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }

//        // 设置投影面位置，near和far距离太小的话可能会导致部分被剪掉
//        Matrix.frustumM(mProjectMatrix, 0, -1, 1, -ratio, ratio, 10f, 100f);
//
//        // 设置相机位置，Z轴的距离要在投影面的near和far之间， eyeZ/near是缩放倍数，但是不能相等
//        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 10.1f,//摄像机的坐标
//                0f, 0f, 0f,//目标物的中心坐标
//                0f, 1f, 0f);//相机方向
//
//        //计算变换矩阵，这是OpenGL顶点着色器的程序里需要的格式
//        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }


    /**
     * 开始渲染
     */
    public void onDrawFrame() {
        GLES20.glUseProgram(mProgram);

        int mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false, mProjectMatrix,0);

        // 获取顶点着色器中的vPosition变量
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // 允许对变量读写
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // 这里参数有点多
        // 最后一个vertexBuffer没什么说的，顶点位置buffer；
        // normalized意思是是否希望数据被标准化（归一化），只表示方向不表示大小
        // index、size、stride 这三个参数跟从vertexBuffer里取数据有关系
        // index是开始位置
        // size是每次取几个
        // stride是步长,也就指定在连续的顶点属性之间的间隔，打个比方：如果传1取值方式为0123、1234、2345……
        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        // 获取片元着色器中的vColor变量
        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // 片元着色器设置颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // 绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        // 禁止变量读写，对应上面的glEnableVertexAttribArray
        // 注意这句要放在最后，不然有时候不显示
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

}
