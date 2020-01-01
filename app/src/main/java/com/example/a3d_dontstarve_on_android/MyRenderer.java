package com.example.a3d_dontstarve_on_android;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.renderscript.Matrix4f;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glFinish;
import static android.opengl.GLES20.glGetError;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.translateM;

import javax.microedition.khronos.egl.EGL;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.a3d_dontstarve_on_android.Character.Pikachu;
import com.example.a3d_dontstarve_on_android.Interface.ArrowButton;
import com.example.a3d_dontstarve_on_android.Interface.PikachuState;
import com.example.a3d_dontstarve_on_android.Skybox.SkyboxShaderProgram;
import com.example.a3d_dontstarve_on_android.Skybox.Skybox;
import com.example.a3d_dontstarve_on_android.Terrain.Terrain;
import com.example.a3d_dontstarve_on_android.Terrain.TerrainShader;
import com.example.a3d_dontstarve_on_android.World.World;
import com.example.a3d_dontstarve_on_android.World.WorldShaderProgram;

import java.util.Vector;

import util.MatrixHelper;

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

    private float[] boarimgMMatrix = new float[]{
            1,0,0,0,
            0,1,0,0,
            0,0,1,0,
            0,0,0,1
    };

    private Terrain terrain;
    private TerrainShader terrainShader;
    private float[] terrainMMatrix = new float[]{
            1,0,0,0,
            0,1,0,0,
            0,0,1,0,
            0,0,0,1
    };

    private WorldShaderProgram worldShader;
    private World world;

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

    private PikachuState pikachuState;

    public Pikachu pikachu;

    public MyRenderer(Context context){
        this.context = context;
    }
    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        arrowButton = new ArrowButton(context);

        skyboxProgram = new SkyboxShaderProgram(context);
        skybox = new Skybox();
        //todo: 在地形上构造一个NURBS曲面山
        terrainShader = new TerrainShader(context,R.drawable.grass);
        terrain = new Terrain();

        worldShader = new WorldShaderProgram(context);world = new World(context);
        lightLocation = new Vector3f(2, -2, 0);
        moveDirection = 0;

        pikachu = new Pikachu(this.context);
        moveSpeed = 0.1f;

        pikachuState = new PikachuState(this.context);

        GlobalTimer.initializeTimer();
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
                / (float) height, 0.1f, 200f);
    }
    @Override
    public void onDrawFrame(GL10 glUnused) {
        //checkHP();

        GlobalTimer.updateTimer();
        if (moveDirection>0) {
            pikachu.mCamera.move(moveDirection, (float)GlobalTimer.getDeltaTime()/20);
            pikachu.changeDisplayAngle(moveDirection);
        }
        GlobalTimer.resetLastUpdateTime();

        glViewport(0,0,wWidth,wHeight);
        glClear(GL_COLOR_BUFFER_BIT);

        /*
         * TODO:
         *       if there is camera move yaw and pitch
         *       rotate viewProjection
         * */
        viewMatrix = pikachu.mCamera.getViewMatrix();
        multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);;
        glEnable(GLES20.GL_DEPTH_TEST);
        glDisable(GL_DEPTH_TEST);

//        drawSkybox();
//        drawBoardImg();
//        drawTerrain();

        glEnable(GLES20.GL_DEPTH_TEST);

        InitialWorldParam(true);
        world.renderWorld(worldShader, viewProjectionMatrix);
//        pikachu.draw(viewProjectionMatrix);

        glDisable(GL_DEPTH_TEST);

        drawArrowBottons();
        drawPikachuState();


    }

    private void drawTerrain(){
        terrainShader.useProgram();
        terrainShader.setUniforms(viewMatrix,projectionMatrix,terrainMMatrix);
        terrain.draw(terrainShader);
    }


    private void drawSkybox(){
        skyboxProgram.useProgram();
        skyboxProgram.setUniforms(viewMatrix,projectionMatrix);
        skybox.bindData(skyboxProgram);
        skybox.draw();
    }

    private void InitialWorldParam(boolean initShadow){
        glClear(GLES20.GL_DEPTH_BUFFER_BIT);
        worldShader.useProgram();
        worldShader.setLightModel(lightLocation, pikachu.mCamera.position, true);
        worldShader.setShininess(25);
        worldShader.setLightColor(new Vector3f(1, 1, 1));
        if(initShadow)
            world.InitShadow(worldShader, wWidth, wHeight);
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

    private void drawPikachuState() {
        float[] tempProjection = new float[16];
        float[] mMatrixCurrent=identMat.clone();
        Matrix.translateM(mMatrixCurrent, 0, 0.6f * wRation, 0.75f, 0.0f);
        Matrix.scaleM(mMatrixCurrent, 0, 0.15f, 0.15f, 0.15f);
        Matrix.orthoM(tempProjection,0,- wRation,wRation,-1.0f,1.0f,100,-100);
        Matrix.multiplyMM(mMatrixCurrent,0,tempProjection,0,mMatrixCurrent,0);
        pikachuState.draw(mMatrixCurrent);
    }

    void changeInterfaceParas() {
        mMiddleX = (float)wWidth/2;
        mMiddleY = (float)wHeight/2;
        arrowButtonCentreX = (float)0.25*mMiddleX;
        arrowButtonCentreY = (float)1.55*mMiddleY;
        arrowButtonR2 = (float)0.39*mMiddleY*(float)0.39*mMiddleY;
    }

    private void checkHP() {
        if (!pikachuState.isAlive()) {
            Intent intent=new Intent(context,GameOver.class);
            context.startActivity(intent);
        }
    }

    public Vector<Integer> isCollided(Vector3f model, Vector<objAttri> attris){
        //因为是球,设置碰撞半径
        float length = 1;
        Vector<Integer> result = new Vector<>();
        for(objAttri attr: attris){
            if(model.distance(attr.pos) < length){
                switch(attr.type){
                    case TREE:
                        result.add(1);
                        break;
                    case FRUIT:
                        result.add(3);
                        break;
                    case MONSTER:
                        result.add(2);
                        break;
                }
            }else{
                result.add(0);
            }
        }
        //0 没撞到, 1树/山 2怪物 3 水果
        return result;
    }
}
