package com.example.a3d_dontstarve_on_android.Nurbs;


import com.example.a3d_dontstarve_on_android.Terrain.TerrainShader;

import data.IndexBuffer;
import data.VertexBuffer;

import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;

public class Nurbssurf {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int UVCOOD_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            + UVCOOD_COMPONENT_COUNT) *4;
    static NurbsHelper nurbsHelper = new NurbsHelper();
    private final int width = 12;
    private final int height = 12;
    private final int numElements = (width - 1) * (height - 1) * 2 * 3;
    private final VertexBuffer vertexBuffer;
    private final IndexBuffer indexBuffer;

    public Nurbssurf(){
        int i;
        int npts,mpts;
        int k,l;
        int p1,p2;
        float[] b=new float [66];
        float []q=new float [701];
        char[] header = new char [80];

/*
	Data for the standard test control net.
	Comment out to use file input.
*/
        npts = 4;
        mpts = 4;
        k = 3;
        l = 3;

        p1 = 12;
        p2 = 12;

        for (i = 1; i <= 4*npts; i++){
            b[i] = 0.f;
        }

        for (i = 1; i <= 3*p1*p2; i++){
            q[i] = 0.f;
        }

/*
	Data is in the form x=b[1], y=b[2], z=b[3], h=b[4], etc. All h are 1.0.
*/
        b[1] = -15.f;
        b[2] = -1.f;
        b[3] = 15.f;
        b[4] = 1f;
        b[5] = -15.f;
        b[6] = -2.f;
        b[7] = 5.f;
        b[8] = 1;
        b[9] = -15.f;
        b[10] = -1.f;
        b[11] = -5.f;
        b[12] = 1;
        b[13] = -15.f;
        b[14] = 0.f;
        b[15] = -15.f;
        b[16] = 1;

        b[17] = -5.f;
        b[18] = -1.f;
        b[19] = 15.f;
        b[20] = 1;
        b[21] = -5.f;
        b[22] = 10.f;
        b[23] = 5.f;
        b[24] = 5;
        b[25] = -5.f;
        b[26] = 10.f;
        b[27] = -5.f;
        b[28] = 1;
        b[29] = -5.f;
        b[30] = -3.f;
        b[31] = -15.f;
        b[32] = 1;

        b[33] = 5.f;
        b[34] = -2.f;
        b[35] = 15.f;
        b[36] = 1;
        b[37] = 5.f;
        b[38] = 10.f;
        b[39] = 5.f;
        b[40] = 1;
        b[41] = 5.f;
        b[42] = 10.f;
        b[43] = -5.f;
        b[44] = 1;
        b[45] = 5.f;
        b[46] = 0.f;
        b[47] = -15.f;
        b[48] = 1;

        b[49] = 15.f;
        b[50] = -1.9f;
        b[51] = 15.f;
        b[52] = 1;
        b[53] = 15.f;
        b[54] = -0.3f;
        b[55] = 5.f;
        b[56] = 1;
        b[57] = 15.f;
        b[58] = -2.f;
        b[59] = -5.f;
        b[60] = 1;
        b[61] = 15.f;
        b[62] = 0.f;
        b[63] = -15.f;
        b[64] = 1;

        nurbsHelper.nrbssurf(b,k,l,npts,mpts,p1,p2,q);
        final float[] heightmapVertices =
                new float[width * height * POSITION_COMPONENT_COUNT];
        int offset = 0;
        for (i = 1; i <= 3*p1*p2; i++){
            heightmapVertices[offset++] = q[i];
        }

        vertexBuffer = new VertexBuffer(heightmapVertices);
        indexBuffer = new IndexBuffer(createIndexData());
    }
    private short[] createIndexData() {
        final short[] indexData = new short[numElements];
        int offset = 0;

        for (int row = 0; row < height - 1; row++) {
            for (int col = 0; col < width - 1; col++) {

                short topLeftIndexNum = (short) (row * width + col);
                short topRightIndexNum = (short) (row * width + col + 1);
                short bottomLeftIndexNum = (short) ((row + 1) * width + col);
                short bottomRightIndexNum = (short) ((row + 1) * width + col + 1);

                // Write out two triangles.
                indexData[offset++] = topLeftIndexNum;
                indexData[offset++] = bottomLeftIndexNum;
                indexData[offset++] = topRightIndexNum;

                indexData[offset++] = topRightIndexNum;
                indexData[offset++] = bottomLeftIndexNum;
                indexData[offset++] = bottomRightIndexNum;
            }
        }

        return indexData;
    }

    public void draw(NurbsShader shader) {
        vertexBuffer.setVertexAttribPointer(0,
                shader.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                0
        );
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer.getBufferId());
        glDrawElements(GL_TRIANGLES, numElements, GL_UNSIGNED_SHORT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
