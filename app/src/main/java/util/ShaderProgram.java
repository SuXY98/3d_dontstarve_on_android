package util;

import android.content.Context;
import static android.opengl.GLES20.glUseProgram;

public abstract class ShaderProgram {
    // Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_COLOR = "u_Color";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    // Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    protected static final String A_DIRECTION_VECTOR = "a_DirectionVector";

    protected final int program;

    protected ShaderProgram(Context context, int vertexShaderResourceId,
                            int fragmentShaderResourceID){
        program = ShaderHelper.buildProgram(
                TextResourceReader
                        .readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader
                        .readTextFileFromResource(context, fragmentShaderResourceID));
    }

    public void useProgram(){
        glUseProgram(program);
    }

}
