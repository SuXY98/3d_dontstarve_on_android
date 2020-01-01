precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
uniform float factor;

void main()
{
    vec4 color = texture2D(u_TextureUnit, v_TextureCoordinates);
    gl_FragColor = vec4((factor/2.0 + 0.5)*color.x,(factor/2.0 + 0.5)*color.y,(factor/2.0 + 0.5)*color.z,1.0);
}