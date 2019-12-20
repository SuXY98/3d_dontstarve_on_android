uniform vec3 lightLocation;
uniform vec3 camera;
uniform vec3 vKa;
uniform vec3 vKd;
uniform vec3 vKs;
uniform mat4 vMatrix;
uniform vec4 aColor;
//uniform int hasTexture;



varying vec2 tCoord;
varying vec4 vDiffuse;          //片元着色器散射光
varying vec4 vAmbient;          //片元着色器环境光
varying vec4 vSpecular;          //片元着色器镜面光
varying vec4 vColor;

attribute vec3 vPosition;
attribute vec3 normal;
attribute vec2 textureCoord;

void main() {
  gl_Position = vMatrix * vec4(vPosition,1);
  vColor=aColor;
//  if(hasTexture == 1){
//    tCoord=textureCoord;
//  }
  float shininess=10.0;             //粗糙度，越小越光滑

  vec3 newNormal=normalize((vMatrix*vec4(normal+vPosition,1)).xyz-(vMatrix*vec4(vPosition,1)).xyz);
  vec3 vp=normalize(lightLocation-(vMatrix*vec4(vPosition,1)).xyz);
  vDiffuse=vec4(vKd,1.0)*max(0.0,dot(newNormal,vp));                //计算散射光的最终强度
  vec3 eye= normalize(camera-(vMatrix*vec4(vPosition,1)).xyz);
  vec3 halfVector=normalize(vp+eye);    //求视线与光线的半向量
  float nDotViewHalfVector=dot(newNormal,halfVector);   //法线与半向量的点积
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess));     //镜面反射光强度因子

  vSpecular=vec4(vKs,1.0)*powerFactor;
  vAmbient=vec4(vKa,1.0);
}