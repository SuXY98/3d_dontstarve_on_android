uniform mat4 mMatrix;
uniform mat4 vpMatrix;
uniform vec4 aColor;
uniform bool hasTexture;
uniform bool isBackGround;

varying vec3 FragPos;
varying vec2 tCoord;
varying vec4 vColor;
varying vec3 pNormal;

attribute vec3 vPosition;
attribute vec3 normal;
attribute vec2 textureCoord;

mat4 inverse(mat4 m)
{
  float Coef00 = m[2][2] * m[3][3] - m[3][2] * m[2][3];
  float Coef02 = m[1][2] * m[3][3] - m[3][2] * m[1][3];
  float Coef03 = m[1][2] * m[2][3] - m[2][2] * m[1][3];

  float Coef04 = m[2][1] * m[3][3] - m[3][1] * m[2][3];
  float Coef06 = m[1][1] * m[3][3] - m[3][1] * m[1][3];
  float Coef07 = m[1][1] * m[2][3] - m[2][1] * m[1][3];

  float Coef08 = m[2][1] * m[3][2] - m[3][1] * m[2][2];
  float Coef10 = m[1][1] * m[3][2] - m[3][1] * m[1][2];
  float Coef11 = m[1][1] * m[2][2] - m[2][1] * m[1][2];

  float Coef12 = m[2][0] * m[3][3] - m[3][0] * m[2][3];
  float Coef14 = m[1][0] * m[3][3] - m[3][0] * m[1][3];
  float Coef15 = m[1][0] * m[2][3] - m[2][0] * m[1][3];

  float Coef16 = m[2][0] * m[3][2] - m[3][0] * m[2][2];
  float Coef18 = m[1][0] * m[3][2] - m[3][0] * m[1][2];
  float Coef19 = m[1][0] * m[2][2] - m[2][0] * m[1][2];

  float Coef20 = m[2][0] * m[3][1] - m[3][0] * m[2][1];
  float Coef22 = m[1][0] * m[3][1] - m[3][0] * m[1][1];
  float Coef23 = m[1][0] * m[2][1] - m[2][0] * m[1][1];

  const vec4 SignA = vec4( 1.0, -1.0,  1.0, -1.0);
  const vec4 SignB = vec4(-1.0,  1.0, -1.0,  1.0);

  vec4 Fac0 = vec4(Coef00, Coef00, Coef02, Coef03);
  vec4 Fac1 = vec4(Coef04, Coef04, Coef06, Coef07);
  vec4 Fac2 = vec4(Coef08, Coef08, Coef10, Coef11);
  vec4 Fac3 = vec4(Coef12, Coef12, Coef14, Coef15);
  vec4 Fac4 = vec4(Coef16, Coef16, Coef18, Coef19);
  vec4 Fac5 = vec4(Coef20, Coef20, Coef22, Coef23);

  vec4 Vec0 = vec4(m[1][0], m[0][0], m[0][0], m[0][0]);
  vec4 Vec1 = vec4(m[1][1], m[0][1], m[0][1], m[0][1]);
  vec4 Vec2 = vec4(m[1][2], m[0][2], m[0][2], m[0][2]);
  vec4 Vec3 = vec4(m[1][3], m[0][3], m[0][3], m[0][3]);

  vec4 Inv0 = SignA * (Vec1 * Fac0 - Vec2 * Fac1 + Vec3 * Fac2);
  vec4 Inv1 = SignB * (Vec0 * Fac0 - Vec2 * Fac3 + Vec3 * Fac4);
  vec4 Inv2 = SignA * (Vec0 * Fac1 - Vec1 * Fac3 + Vec3 * Fac5);
  vec4 Inv3 = SignB * (Vec0 * Fac2 - Vec1 * Fac4 + Vec2 * Fac5);

  mat4 Inverse = mat4(Inv0, Inv1, Inv2, Inv3);

  vec4 Row0 = vec4(Inverse[0][0], Inverse[1][0], Inverse[2][0], Inverse[3][0]);

  float Determinant = dot(m[0], Row0);

  Inverse /= Determinant;

  return Inverse;
}

mat4 transpose(mat4 inMatrix) {
  vec4 i0 = inMatrix[0];
  vec4 i1 = inMatrix[1];
  vec4 i2 = inMatrix[2];
  vec4 i3 = inMatrix[3];

  mat4 outMatrix = mat4(
    vec4(i0.x, i1.x, i2.x, i3.x),
    vec4(i0.y, i1.y, i2.y, i3.y),
    vec4(i0.z, i1.z, i2.z, i3.z),
    vec4(i0.w, i1.w, i2.w, i3.w)
  );

  return outMatrix;
}

mat4 genZeroLocVP(mat4 inMatrix){
  vec4 i0 = inMatrix[0];
  i0 = vec4(i0.xyz, 0);
  vec4 i1 = inMatrix[1];
  i1 = vec4(i1.xyz, 0);
  vec4 i2 = inMatrix[2];
  i2 = vec4(i2.xyz, 0);
  vec4 i3 = vec4(0, 0, 0, 1);
  mat4 outMatrix = mat4(i0,i1,i2,i3);

  return outMatrix;
}

void main() {

  vColor=aColor;
  if(!isBackGround){
    gl_Position = vpMatrix* mMatrix *vec4(vPosition,1);
  }else{
    vec4 tmp = genZeroLocVP(vpMatrix) * mMatrix * vec4(vPosition, 1);
    tmp[2] = 0.99 * tmp[3];
    gl_Position = tmp;
  }

  FragPos = vec3(mMatrix * vec4(vPosition, 1.0));
  pNormal = mat3(mMatrix) * normal;

  //texture set
  if(hasTexture){
    tCoord=textureCoord;
  }
}



