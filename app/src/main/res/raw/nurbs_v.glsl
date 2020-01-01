uniform mat4 u_Matrix;
attribute vec3 a_Position;
varying vec2 v_tex_c;

void main()
{
    v_tex_c = vec2(a_Position.x / 5.0,a_Position.y / 5.0);

    gl_Position = u_Matrix * vec4(a_Position, 1.0);
}
