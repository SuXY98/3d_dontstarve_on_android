package util;

public class MatrixHelper {
    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect,
                                    float n, float f) {
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);
        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));

        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }

    public static void setIdent(float [] m){
        m[0] =1;m[1] =0;m[2] =0;m[3] =0;
        m[4] =0;m[5] =1;m[6] =0;m[7] =0;
        m[8] =0;m[9] =0;m[10] =1;m[11] =0;
        m[12] =0;m[13] =0;m[14] =0;m[15] =1;
    }
}
