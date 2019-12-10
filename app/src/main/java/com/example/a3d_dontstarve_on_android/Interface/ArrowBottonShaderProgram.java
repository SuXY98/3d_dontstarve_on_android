package com.example.a3d_dontstarve_on_android.Interface;

import android.content.Context;

import com.example.a3d_dontstarve_on_android.R;

import util.ShaderProgram;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class ArrowBottonShaderProgram extends ShaderProgram {
    private int mMatrix;
    private int mColor;
    private int mVertex;

    public ArrowBottonShaderProgram(Context context) {
        super(context, R.raw.arrowbutton_vertex_shader, R.raw.arrowbutton_fragment_shader);
        mMatrix = glGetUniformLocation(program, "vMatrix");
        mColor = glGetUniformLocation(program, "vColor");
        mVertex = glGetAttribLocation(program, "vPosition");
    }

    public void setMVPMatrix(float[] matrix){
        glUniformMatrix4fv(mMatrix, 1, false, matrix, 0);
    }

    public void setColor(float[] color) {
        glUniform4fv(mColor, 1, color, 0);
    }

    public int getVertexAttrLocation() {
        return mVertex;
    }


}
