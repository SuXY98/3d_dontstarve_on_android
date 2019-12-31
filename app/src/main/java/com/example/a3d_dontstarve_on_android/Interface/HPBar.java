package com.example.a3d_dontstarve_on_android.Interface;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;

import data.VertexAarray;

import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glDrawElements;

public class HPBar {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private final ByteBuffer indexArray;
    private final VertexAarray vertexAarray;
    private float color[] = {0.7f, 0.1f, 0.1f, 1.0f};
    private InterfaceShaderProgram mProgram;

    public HPBar(Context context) {
        mProgram = new InterfaceShaderProgram(context);
        vertexAarray = new VertexAarray(new float[] {
                0.3f,  0.2f,  0.0f,
                0.2f,  0.3f,  0.0f,
                0.2f,  0.6f,  0.0f,
                0.3f,  0.7f,  0.0f,
                0.6f,  0.7f,  0.0f,
                0.7f,  0.6f,  0.0f,
                0.7f,  0.3f,  0.0f,
                0.6f,  0.2f,  0.0f,

                0.9f,  0.2f,  0.0f,
                0.8f,  0.3f,  0.0f,
                0.8f,  0.6f,  0.0f,
                0.9f,  0.7f,  0.0f,
                1.2f,  0.7f,  0.0f,
                1.3f,  0.6f,  0.0f,
                1.3f,  0.3f,  0.0f,
                1.2f,  0.2f,  0.0f,

                1.5f,  0.2f,  0.0f,
                1.4f,  0.3f,  0.0f,
                1.4f,  0.6f,  0.0f,
                1.5f,  0.7f,  0.0f,
                1.8f,  0.7f,  0.0f,
                1.9f,  0.6f,  0.0f,
                1.9f,  0.3f,  0.0f,
                1.8f,  0.2f,  0.0f,

                2.1f,  0.2f,  0.0f,
                2.0f,  0.3f,  0.0f,
                2.0f,  0.6f,  0.0f,
                2.1f,  0.7f,  0.0f,
                2.4f,  0.7f,  0.0f,
                2.5f,  0.6f,  0.0f,
                2.5f,  0.3f,  0.0f,
                2.4f,  0.2f,  0.0f,

                2.7f,  0.2f,  0.0f,
                2.6f,  0.3f,  0.0f,
                2.6f,  0.6f,  0.0f,
                2.7f,  0.7f,  0.0f,
                3.0f,  0.7f,  0.0f,
                3.1f,  0.6f,  0.0f,
                3.1f,  0.3f,  0.0f,
                3.0f,  0.2f,  0.0f,

                3.3f,  0.2f,  0.0f,
                3.2f,  0.3f,  0.0f,
                3.2f,  0.6f,  0.0f,
                3.3f,  0.7f,  0.0f,
                3.6f,  0.7f,  0.0f,
                3.7f,  0.6f,  0.0f,
                3.7f,  0.3f,  0.0f,
                3.6f,  0.2f,  0.0f,

                3.9f,  0.2f,  0.0f,
                3.8f,  0.3f,  0.0f,
                3.8f,  0.6f,  0.0f,
                3.9f,  0.7f,  0.0f,
                4.2f,  0.7f,  0.0f,
                4.3f,  0.6f,  0.0f,
                4.3f,  0.3f,  0.0f,
                4.2f,  0.2f,  0.0f,
        });
        // 6 indices per cube side
        indexArray =  ByteBuffer.allocateDirect(126)
                .put(new byte[] {
                        0, 1, 2,
                        0, 2, 3,
                        0, 3, 7,
                        3, 4, 7,
                        4, 5, 6,
                        4, 6, 7,

                        8, 9, 10,
                        8, 10, 11,
                        8, 11, 15,
                        11, 12, 15,
                        12, 13, 14,
                        12, 14, 15,

                        16, 17, 18,
                        16, 18, 19,
                        16, 19, 23,
                        19, 20, 23,
                        20, 21, 22,
                        20, 22, 23,

                        24, 25, 26,
                        24, 26, 27,
                        24, 27, 31,
                        27, 28, 31,
                        28, 29, 30,
                        28, 30, 31,

                        32, 33, 34,
                        32, 34, 35,
                        32, 35, 39,
                        35, 36, 39,
                        36, 37, 38,
                        36, 38, 39,

                        40, 41, 42,
                        40, 42, 43,
                        40, 43, 47,
                        43, 44, 47,
                        44, 45, 46,
                        44, 46, 47,

                        48, 49, 50,
                        48, 50, 51,
                        48, 51, 55,
                        51, 52, 55,
                        52, 53, 54,
                        52, 54, 55
                });
        indexArray.position(0);
    }

    public void bindData() {
        vertexAarray.setVertexAttribPointer(0,
                mProgram.getVertexAttrLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    public void draw(float[] mvpMatrix, int hpLeft) {
        mProgram.useProgram();
        mProgram.setColor(color);
        mProgram.setMVPMatrix(mvpMatrix);
        vertexAarray.setVertexAttribPointer(0,
                mProgram.getVertexAttrLocation(),
                POSITION_COMPONENT_COUNT, 0);
        glDrawElements(GLES20.GL_TRIANGLES, 18*hpLeft, GL_UNSIGNED_BYTE, indexArray);
    }
}
