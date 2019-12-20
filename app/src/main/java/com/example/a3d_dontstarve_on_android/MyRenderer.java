package com.example.a3d_dontstarve_on_android;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.renderscript.Matrix4f;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static javax.microedition.khronos.opengles.GL10.GL_ONE_MINUS_SRC_ALPHA;

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

    public Camera mCamera;
    private ArrowButton arrowButton;

    public int wWidth;
    public int wHeight;
    public float wRation;
    public int moveDirection;
    private float moveSpeed;
    private float mMiddleX;
    private float mMiddleY;
    public float arrowButtonCentreX;
    public float arrowButtonCentreY;
    public float arrowButtonR2;

    public MyRenderer(Context context){
        this.context = context;
    }
    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        arrowButton = new ArrowButton(context);

        skyboxProgram = new SkyboxShaderProgram(context);
        skybox = new Skybox();

        mCamera = new Camera();
        mCamera.set(0,0f,0,0.5f,0,0,0,1,0);
        worldShader = new WorldShaderProgram(context);world = new World(context);
        lightLocation = new Vector3f(2, 2, -2);
        moveDirection = 0;
        moveSpeed = 0.01f;
    }
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        wWidth = width;
        wHeight = height;
        wRation = (float) width / (float) height;
        changeInterfaceParas();
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0,0,width,height);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
                / (float) height, 0.1f, 10f);
    }
    @Override
    public void onDrawFrame(GL10 glUnused) {
        if (moveDirection == 1) {
            mCamera.moveVector(mCamera.n.x*moveSpeed, 0, mCamera.n.z*moveSpeed);
        } else if (moveDirection == 2) {
            mCamera.moveVector(-mCamera.n.z*moveSpeed, 0, mCamera.n.x*moveSpeed);
        } else if (moveDirection == 3) {
            mCamera.moveVector(-mCamera.n.x*moveSpeed, 0, -mCamera.n.z*moveSpeed);
        } else if (moveDirection == 4) {
            mCamera.moveVector(mCamera.n.z*moveSpeed, 0, -mCamera.n.x*moveSpeed);
        }

        glViewport(0,0,wWidth,wHeight);
        glClear(GL_COLOR_BUFFER_BIT);

        /*
         * TODO:
         *       if there is camera move yaw and pitch
         *       rotate viewProjection
         * */
        viewMatrix = mCamera.getViewMatrix();
        multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);

        drawSkybox();

        glEnable(GLES20.GL_DEPTH_TEST);

        InitialWorldParam();
        world.renderWorld(worldShader, new Matrix4f(viewProjectionMatrix));
        glDisable(GL_DEPTH_TEST);

        drawArrowBottons();
    }
    public void drawSkybox(){
        skyboxProgram.useProgram();
        skyboxProgram.setUniforms(viewMatrix,projectionMatrix);
        skybox.bindData(skyboxProgram);
        skybox.draw();
    }
    private void InitialWorldParam(){
        glClear(GLES20.GL_DEPTH_BUFFER_BIT);
        worldShader.useProgram();
        worldShader.setLightModel(lightLocation, mCamera.getPosition());
    }

    private void drawArrowBottons() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glViewport(0,0,wWidth,wHeight);

        float[] tempProjection = new float[16];
        float[] mMatrixCurrent=identMat.clone();
        Matrix.translateM(mMatrixCurrent, 0, -0.75f * wRation, -0.55f, 0.0f);
        Matrix.scaleM(mMatrixCurrent, 0, 0.3f, 0.3f, 0.3f);
        Matrix.orthoM(tempProjection,0,- wRation,wRation,-1.0f,1.0f,100,-100);
        Matrix.multiplyMM(mMatrixCurrent,0,tempProjection,0,mMatrixCurrent,0);
        arrowButton.draw(mMatrixCurrent);

        mMatrixCurrent=identMat.clone();
        Matrix.rotateM(mMatrixCurrent, 0, 90, 0, 0, 1);
        Matrix.translateM(mMatrixCurrent, 0, -0.55f, 0.75f * wRation, 0.0f);
        Matrix.scaleM(mMatrixCurrent, 0, 0.3f, 0.3f, 0.3f);
        Matrix.orthoM(tempProjection,0,- wRation,wRation,-1.0f,1.0f,100,-100);
        Matrix.multiplyMM(mMatrixCurrent,0,tempProjection,0,mMatrixCurrent,0);
        arrowButton.draw(mMatrixCurrent);

        mMatrixCurrent=identMat.clone();
        Matrix.rotateM(mMatrixCurrent, 0, 180, 0, 0, 1);
        Matrix.translateM(mMatrixCurrent, 0, 0.75f * wRation, 0.55f, 0.0f);
        Matrix.scaleM(mMatrixCurrent, 0, 0.3f, 0.3f, 0.3f);
        Matrix.orthoM(tempProjection,0,- wRation,wRation,-1.0f,1.0f,100,-100);
        Matrix.multiplyMM(mMatrixCurrent,0,tempProjection,0,mMatrixCurrent,0);
        arrowButton.draw(mMatrixCurrent);

        mMatrixCurrent=identMat.clone();
        Matrix.rotateM(mMatrixCurrent, 0, 270, 0, 0, 1);
        Matrix.translateM(mMatrixCurrent, 0, 0.55f, -0.75f * wRation, 0.0f);
        Matrix.scaleM(mMatrixCurrent, 0, 0.3f, 0.3f, 0.3f);
        Matrix.orthoM(tempProjection,0,- wRation,wRation,-1.0f,1.0f,100,-100);
        Matrix.multiplyMM(mMatrixCurrent,0,tempProjection,0,mMatrixCurrent,0);
        arrowButton.draw(mMatrixCurrent);

        glDisable(GL_BLEND);
    }

    void changeInterfaceParas() {
        mMiddleX = (float)wWidth/2;
        mMiddleY = (float)wHeight/2;
        arrowButtonCentreX = (float)0.25*mMiddleX;
        arrowButtonCentreY = (float)1.55*mMiddleY;
        arrowButtonR2 = (float)0.39*mMiddleY*(float)0.39*mMiddleY;
    }
}
