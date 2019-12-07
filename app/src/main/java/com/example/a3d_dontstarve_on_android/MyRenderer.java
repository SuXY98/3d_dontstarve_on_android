package com.example.a3d_dontstarve_on_android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import Skybox.SkyboxShaderProgram;
import Skybox.Skybox;
import util.MatrixHelper;
import util.TextureHelper;

/*
 *use static import to avoid long and boring thing
 * like GLES20.glClear（GLES20.GL_COLOR_BUFFER_BIT);
*/

public class MyRenderer implements GLSurfaceView.Renderer {
    private final Context context;
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];

    private SkyboxShaderProgram skyboxProgram;
    private Skybox skybox;

    private int skyboxTexture;
    public MyRenderer(Context context){
        this.context = context;
    }
    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        skyboxProgram = new SkyboxShaderProgram(context);
        skybox = new Skybox();
        skyboxTexture = TextureHelper.loadCubeMap(context,
                new int []{R.drawable.left, R.drawable.right,
                    R.drawable.bottom, R.drawable.top,
                    R.drawable.front, R.drawable.back});

    }
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0,0,width,height);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
                / (float) height, 1f, 10f);
    }
    @Override
    public void onDrawFrame(GL10 glUnused) {
        glClear(GL_COLOR_BUFFER_BIT);
        drawSkybox();
    }
    public void drawSkybox(){
        setIdentityM(viewMatrix,0);
        /*
        * TODO:
        *       if there is camera move yaw and pitch
        *       rotate viewProjection
        * */
        multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);
        skyboxProgram.useProgram();
        skyboxProgram.setUniforms(viewProjectionMatrix,skyboxTexture);
        skybox.bindData(skyboxProgram);
        skybox.draw();
    }
}
