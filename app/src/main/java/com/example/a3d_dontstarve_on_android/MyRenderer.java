package com.example.a3d_dontstarve_on_android;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.renderscript.Matrix4f;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.a3d_dontstarve_on_android.Interface.ArrowButton;
import com.example.a3d_dontstarve_on_android.Skybox.SkyboxShaderProgram;
import com.example.a3d_dontstarve_on_android.Skybox.Skybox;
import com.example.a3d_dontstarve_on_android.World.World;
import com.example.a3d_dontstarve_on_android.World.WorldShaderProgram;

import util.MatrixHelper;
import util.TextureHelper;

/*
 *use static import to avoid long and boring thing
 * like GLES20.glClear（GLES20.GL_COLOR_BUFFER_BIT);
*/

public class MyRenderer implements GLSurfaceView.Renderer {
    static float[] identMat = {1, 0, 0, 0,
                                0, 1, 0, 0,
                                0, 0, 1, 0,
                                0, 0, 0, 1};

    private final Context context;
    private final float[] projectionMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];

    private float[] viewMatrix = new float[16];
    private Vector3f lightLocation;
    private SkyboxShaderProgram skyboxProgram;
    private Skybox skybox;

    private WorldShaderProgram worldShader;
    private World world;

    private int skyboxTexture;

    public Camera mCamera;
    private ArrowButton arrowButton;

    private int wWidth;
    private int wHeight;

    public MyRenderer(Context context){
        this.context = context;
    }
    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        arrowButton = new ArrowButton(context);

        skyboxProgram = new SkyboxShaderProgram(context);
        skybox = new Skybox();
        skyboxTexture = TextureHelper.loadCubeMap(context,
                new int []{R.drawable.left, R.drawable.right,
                    R.drawable.bottom, R.drawable.top,
                    R.drawable.front, R.drawable.back});
        mCamera = new Camera();
        mCamera.set(0,0f,0,0.5f,0,0,0,1,0);
        worldShader = new WorldShaderProgram(context);
        world = new World(context);
        lightLocation = new Vector3f(2, 2, -2);
    }
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        wWidth = width;
        wHeight = height;
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0,0,width,height);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
                / (float) height, 0.1f, 10f);
    }
    @Override
    public void onDrawFrame(GL10 glUnused) {
        glClear(GL_COLOR_BUFFER_BIT);

        /*
         * TODO:
         *       if there is camera move yaw and pitch
         *       rotate viewProjection
         * */
        viewMatrix = mCamera.getViewMatrix();
        multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);

        drawSkybox();
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        InitialWorldParam();
        world.renderWorld(worldShader, new Matrix4f(viewProjectionMatrix));
        glDisable(GL_DEPTH_TEST);
    }
    public void drawSkybox(){
        skyboxProgram.useProgram();
        skyboxProgram.setUniforms(viewProjectionMatrix,skyboxTexture);
        skybox.bindData(skyboxProgram);
        skybox.draw();
    }
    private void InitialWorldParam(){
        glClear(GLES20.GL_DEPTH_BUFFER_BIT);
        worldShader.useProgram();
        worldShader.setLightModel(lightLocation, mCamera.getPosition());
    }
}
