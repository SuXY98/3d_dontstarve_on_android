precision mediump float;

uniform sampler2D u_TextureUnit;
uniform float factor;
varying vec2 v_tex_c;

void main()
{
    vec4 color = texture2D(u_TextureUnit, v_tex_c);
    gl_FragColor = vec4((factor/2.0 + 0.5)*color.x,(factor/2.0 + 0.5)*color.y,(factor/2.0 + 0.5)*color.z,1.0);
}
