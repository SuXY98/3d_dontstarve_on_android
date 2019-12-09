precision mediump float;
uniform sampler2D vTexture;

varying vec2 tCoord;
varying vec4 vDiffuse;
varying vec4 vAmbient;
varying vec4 vSpecular;

void main() {
    vec4 finalColor=texture2D(vTexture,tCoord);
    gl_FragColor=finalColor*vAmbient+finalColor*vSpecular+finalColor*vDiffuse;
}