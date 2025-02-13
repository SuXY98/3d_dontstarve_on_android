precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;

void main()
{
    vec4 color = texture2D(u_TextureUnit, v_TextureCoordinates);
    if(color.a < 0.1 )
        discard;
    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);
}