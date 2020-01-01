precision mediump float;

uniform samplerCube u_TextureUnit;
uniform samplerCube u_TextureUnit_n;
uniform float factor;
varying vec3 v_Position;

void main()
{
	vec4 color = textureCube(u_TextureUnit, v_Position);
	vec4 colorn = textureCube(u_TextureUnit_n, v_Position);
	gl_FragColor = mix(color,colorn,factor);
}