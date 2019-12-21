precision mediump float;
uniform sampler2D vTexture;

//varying bool hTexture;
varying vec2 tCoord;
varying vec4 vDiffuse;
varying vec4 vAmbient;
varying vec4 vSpecular;
varying  vec4 vColor;


void main() {
    //vec4 finalColor=hasTexture?texture2D(vTexture,tCoord):vColor;
    vec4 finalColor = vColor;
    //if(hTexture){
        finalColor=texture2D(vTexture,tCoord);
    //}
    gl_FragColor=finalColor*vAmbient+finalColor*vSpecular+ finalColor*vDiffuse;
}

