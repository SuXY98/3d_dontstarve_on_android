package data;

import static android.opengl.GLES20.glEnableVertexAttribArray;
import static data.Constants.BYTES_PER_FLOAT;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.GL_FLOAT;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class VertexAarray {
    private final FloatBuffer floatBuffer;

    public VertexAarray(float[] vertexData){
        floatBuffer = ByteBuffer
                .allocateDirect((vertexData.length*BYTES_PER_FLOAT))
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    }

    public void setVertexAttribPointer(int dataOffset,int attributeLocation,
                                       int componentCount, int stride){
        floatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation,componentCount,
               GL_FLOAT,false,stride,floatBuffer );
        glEnableVertexAttribArray(attributeLocation);

        floatBuffer.position(0);
    }

    public void updateBuffer(float[] vertexData,int start, int count){
        floatBuffer.position(start);
        floatBuffer.put(vertexData,start,count);
        floatBuffer.position(0);
    }
}
