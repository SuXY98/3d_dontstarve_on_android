package com.example.a3d_dontstarve_on_android.World.Objs;

import android.graphics.Rect;
import android.opengl.GLES20;

import com.example.a3d_dontstarve_on_android.ObjLoader.MtlInfo;
import com.example.a3d_dontstarve_on_android.Character.Obj3D;
import com.example.a3d_dontstarve_on_android.Vector2f;
import com.example.a3d_dontstarve_on_android.Vector3f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Vector;

public class BaseModel {
    protected Vector<Vector3f> points;
    protected Vector<Vector3f> normals;
    protected Vector<Vector2f> texture;
    protected Vector3f [] K;

    public static final int MaterialSize = 3;
    private IntBuffer indexBuffer;
    private FloatBuffer vertexBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer textureBuffer;
    private float shininess;
    private int mProgram;
    private static final int indexLength = 3;
    protected class RenderPoints{
        public int pointIndex;
        public int normalIndex;
        public int textureIndex;

        public RenderPoints(int pointIndex, int normalIndex, int textureIndex){
            this.normalIndex = normalIndex;
            this.pointIndex = pointIndex;
            this.textureIndex = textureIndex;
        }
    }
    private boolean textureFlag;
    private boolean isGenerated;
    private int tID;
    public boolean openTexture;
    protected Vector<RenderPoints []> planes;
    public boolean hasTexture() {
        return textureFlag && openTexture;
    }
    public int textureID(){return tID;}
    public BaseModel(){
        this.isGenerated = false;
        this.openTexture = true;
        //Ka, Ks, Kd
        this.K = new Vector3f[MaterialSize];
        this.points = new Vector<>();
        this.normals = new Vector<>();
        this.texture = new Vector<>();
        this.planes = new Vector<>();
        K[2] = new Vector3f(0.8f, 0.8f, 0.8f);
        K[0] = new Vector3f(0.2f, 0.2f, 0.2f);
        K[1] = new Vector3f(0f, 0f, 0f);
        shininess = 10.0f;
        this.isCalculated = false;
        this.isBack = false;
    }

    private int getHandlerLocation(String name) throws Exception{
        int id = GLES20.glGetAttribLocation(mProgram, name);
        if(id < 0)
            throw new Exception("Get Location fails!");
        return id;
    }

    //property of specific objects not saved in this class
    //before call this function, all uniform parameter must called
    public void drawSelf(int mProgram) throws Exception {
        this.mProgram = mProgram;
        if(!isGenerated){
            genBuffer();
            if(!isGenerated)
                return;
        }

        // 获取顶点着色器的位置的句柄
        int mPositionHandler = getHandlerLocation("vPosition");
        int mNormalHandler = getHandlerLocation("normal");
        int mTextureHandler = getHandlerLocation("textureCoord");
        // 启用三角形顶点位置的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandler);
        //准备三角形坐标数据
        GLES20.glVertexAttribPointer(mPositionHandler, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        // 获取顶点法向量句柄
        GLES20.glEnableVertexAttribArray(mNormalHandler);
        GLES20.glVertexAttribPointer(mNormalHandler, 3, GLES20.GL_FLOAT, true, 0, normalBuffer);
        if(this.hasTexture()){
            GLES20.glEnableVertexAttribArray(mTextureHandler);
            GLES20.glVertexAttribPointer(mTextureHandler, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
        }
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, planes.size() * 3, GLES20.GL_UNSIGNED_INT, indexBuffer);
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, points.size());
        GLES20.glDisableVertexAttribArray(mPositionHandler);
        GLES20.glDisableVertexAttribArray(mNormalHandler);
        GLES20.glDisableVertexAttribArray(mTextureHandler);
    }

    private void genBuffer(){
        if(isGenerated)
            return;
        float [] vertexData = new float[3 * planes.size() * 3]; //vec3
        float [] normalData = new float[3 * planes.size() * 3]; //vec3
        float [] textureData = new float[3 * planes.size() * 2]; //vec2
        int [] indexData = new int [planes.size() * 3];
        int counter = 0;
        for(int i = 0; i < planes.size(); i++){
            //read normals, vertex and texture coordinate of each plane
            if(planes.elementAt(i).length != 3)
                return;
            RenderPoints [] plane = planes.elementAt(i);
            for(int j = 0;j < indexLength; j++){
                Vector3f v = points.elementAt(plane[j].pointIndex);
                Vector3f n = normals.elementAt(plane[j].normalIndex);
                Vector2f vt = texture.elementAt(plane[j].textureIndex);

                vertexData[3 * counter    ] = v.x;
                vertexData[3 * counter + 1] = v.y;
                vertexData[3 * counter + 2] = v.z;

                normalData[3 * counter    ] = n.x;
                normalData[3 * counter + 1] = n.y;
                normalData[3 * counter + 2] = n.z;

                textureData[2 * counter   ] = vt.x;
                textureData[2 * counter+ 1] = vt.y;
                counter ++;
            }
            indexData[3 * i    ] = 3 * i;
            indexData[3 * i + 1] = 3 * i + 1;
            indexData[3 * i + 2] = 3 * i + 2;
        }
        vertexBuffer = floatBufferUtil(vertexData);
        normalBuffer = floatBufferUtil(normalData);
        textureBuffer = floatBufferUtil(textureData);
        indexBuffer = intBufferUtil(indexData);
        isGenerated = true;
    }

    private static IntBuffer intBufferUtil(int[] arr)
    {
        IntBuffer mBuffer;
        // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个int占4个字节
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 4);
        // 数组排列用nativeOrder
        qbb.order(ByteOrder.nativeOrder());
        mBuffer = qbb.asIntBuffer();
        mBuffer.put(arr);
        mBuffer.position(0);
        return mBuffer;
    }

    private static FloatBuffer floatBufferUtil(float[] arr)
    {
        FloatBuffer mBuffer;
        // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个int占4个字节
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 4);
        // 数组排列用nativeOrder
        qbb.order(ByteOrder.nativeOrder());
        mBuffer = qbb.asFloatBuffer();
        mBuffer.put(arr);
        mBuffer.position(0);
        return mBuffer;
    }

    public Vector3f[] getK() {
        return K;
    }

    public void setTextureID(int textureID) {
        this.tID = textureID;
        this.textureFlag = true;
    }

    public void setK(Vector3f [] K){
        if(K.length != 3)
            return;
        this.K = K;
    }

    public BaseModel(Obj3D obj3d, MtlInfo mtlInfo){

    }

    private boolean isCalculated;
    private Vector3f []box;

    public Vector3f []getBox(){
        //find max delta vector
        Vector3f min = points.elementAt(0);
        Vector3f max = points.elementAt(0);

        if(!isCalculated){
            for(Vector3f v: points){
                min.x = v.x < min.x?v.x:min.x;
                min.y = v.y < min.y?v.y:min.y;
                min.z = v.z < min.z?v.z:min.z;
                max.x = v.x > max.x?v.x:max.x;
                max.y = v.y > max.y?v.y:max.y;
                max.z = v.z > max.z?v.z:max.z;
            }
            box = new Vector3f[]{max, min};
            isCalculated = true;
        }

        return box;
    }

    public boolean isBack;
}
