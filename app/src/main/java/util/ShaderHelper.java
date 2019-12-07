package util;


import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode){
        return compileShader(GL_VERTEX_SHADER,shaderCode);
    }

    public static int compileFragmentShader(String shaderCode){
        return compileShader(GL_FRAGMENT_SHADER,shaderCode);
    }

    private static int compileShader(int type, String shaderCode){
        //create a shader program
        final int shaderObjectId = glCreateShader(type);
        if(shaderObjectId == 0){
            System.out.print("Can't create a shader\n");
            return 0;
        }
        //传递glsl代码
        glShaderSource(shaderObjectId,shaderCode);
        glCompileShader(shaderObjectId);

        final int [] compileStatus = new int [1];
        glGetShaderiv(shaderObjectId,GL_COMPILE_STATUS,compileStatus,0);;
        if(compileStatus[0]==0){
            glDeleteShader(shaderObjectId);
            System.out.print("Compiliation of shader failed");
            return 0;
        }
        return shaderObjectId;
    }

    public static int linkProgram(int vShaderId,int fShaderId){
        final int programId = glCreateProgram();
        if(programId == 0){
            System.out.print("Failed to create shader program\n");
            return 0;
        }
        glAttachShader(programId,vShaderId);
        glAttachShader(programId,fShaderId);
        glLinkProgram(programId);
        //测试连接状态
        final int [] linkStatus = new int [1];
        glGetProgramiv(programId,GL_LINK_STATUS,
                linkStatus,0);
        if(linkStatus[0] == 0){
            glDeleteProgram(programId);
            System.out.print("Failed to link Program\n");
            return 0;
        }
        return programId;
    }

    public static boolean validateProgram(int programId){
        glValidateProgram(programId);
        final int [] validateStatus = new int [1];
        glGetProgramiv(programId, GL_VALIDATE_STATUS,
                validateStatus, 0);
        return validateStatus[0] != 0;
    }

    public static int buildProgram(String vertexS,String fragmentS){
        int program;
        int vShader = compileVertexShader(vertexS);
        int fShader = compileFragmentShader(fragmentS);

        program = linkProgram(vShader,fShader);

        return program;
    }
}