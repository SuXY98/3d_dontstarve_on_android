package com.example.a3d_dontstarve_on_android.Terrain;

import java.nio.ByteBuffer;

import data.VertexAarray;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glDrawElements;

public class Terrain {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int UVCOOD_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            + UVCOOD_COMPONENT_COUNT) *4;
    private final ByteBuffer indexArray;
    private final VertexAarray vertexAarray;

    public Terrain(){

        vertexAarray = new VertexAarray(new     float[] {
            // positions          // texture Coords (note we set these higher than 1 (together with GL_REPEAT as texture wrapping mode). this will cause the floor texture to repeat)
                200.0f, 0.001f,  200.0f,  50.0f, 0.0f,
                -200.0f, 0.001f,  200.0f,  0.0f, 0.0f,
                -200.0f, 0.001f, -200.0f,  0.0f, 50.0f,
                200.0f, 0.001f,  200.0f,  50.0f, 0.0f,
                -200.0f, 0.001f, -200.0f,  0.0f, 50.0f,
                200.0f, 0.001f, -200.0f,  50.0f, 50.0f
        });

        // 6 indices per cube side
        indexArray =  ByteBuffer.allocateDirect(6)
                .put(new byte[] {
                        0, 1, 2,
                        3, 4, 5
                });
        indexArray.position(0);
    }

    public void draw(TerrainShader shader) {
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
