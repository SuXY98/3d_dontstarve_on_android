precision mediump float;
uniform sampler2D vTexture;
uniform int hasTexture;

varying vec2 tCoord;
varying vec4 vDiffuse;
varying vec4 vAmbient;
varying vec4 vSpecular;
varying  vec4 vColor;


void main() {
    vec4 finalColor=vColor;
//    vec4 finalColor=hasTexture == 1?texture2D(vTexture,tCoord):vColor;
    //gl_FragColor = vDiffuse;
    gl_FragColor=finalColor*vAmbient+finalColor*vSpecular+ finalColor*vDiffuse;
}

