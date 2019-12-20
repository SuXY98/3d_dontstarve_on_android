package com.example.a3d_dontstarve_on_android.BoardImg;

import com.example.a3d_dontstarve_on_android.Vector3f;

import java.nio.ByteBuffer;

import data.Constants;
import data.VertexAarray;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glDrawElements;

public class BoardImg {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int UVCOOD_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            + UVCOOD_COMPONENT_COUNT) *4;
    private final ByteBuffer indexArray;
    private final VertexAarray vertexAarray;
    public Vector3f boardPos = new Vector3f(0.5f,0,0);

    public BoardImg(){

        // Create a unit cube.
        vertexAarray = new VertexAarray(new float[]{
                // positions         // texture Coords (swapped y coordinates because texture is flipped upside down)
                -0.5f,  0.5f,  0.0f,  0.0f,  0.0f,
                -0.5f, -0.5f,  0.0f,  0.0f,  1.0f,
                0.5f, -0.5f,  0.0f,  1.0f,  1.0f,

                -0.5f,  0.5f,  0.0f,  0.0f,  0.0f,
                0.5f, -0.5f,  0.0f,  1.0f,  1.0f,
                0.5f,  0.5f,  0.0f,  1.0f,  0.0f
        });

        // 6 indices per cube side
        indexArray =  ByteBuffer.allocateDirect(6)
                .put(new byte[] {
                        0, 1, 2,
                        3, 4, 5
                });
        indexArray.position(0);
    }
    public void draw(BoardImgShader shader) {
        vertexAarray.setVertexAttribPointer(0,
                shader.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
                );
        vertexAarray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,
                shader.getTextureCoodAttributeLocation(),
                UVCOOD_COMPONENT_COUNT,
                STRIDE);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, indexArray);
    }
}
