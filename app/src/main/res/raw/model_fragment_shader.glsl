precision mediump float;
uniform vec3 lightColor;
uniform vec4 lightLocation;
uniform vec3 camera;
uniform vec3 vKa;
uniform vec3 vKd;
uniform vec3 vKs;
uniform bool hasShadow;

uniform sampler2D vTexture;
uniform sampler2D shadowMap;

uniform float shininess;
uniform bool hasTexture;

varying vec2 tCoord;
varying vec4 vColor;
varying vec3 pNormal;
varying vec3 FragPos;

vec4 shadowFilter(vec4 color){
    return vec4(0,0,0,0);
}

void main() {
    vec4 finalColor = vColor;
    if(hasTexture){
        finalColor=texture2D(vTexture,tCoord);
    }
    if(hasShadow){
        finalColor=shadowFilter(finalColor);
    }
    if(lightLocation.w > 0.0){
        vec3 lightPos = lightLocation.xyz / lightLocation.w;
        vec3 ambient = vKa * lightColor;
        vec3 norm = normalize(pNormal);
        vec3 lightDir = normalize(lightPos - FragPos);
        float diff = max(dot(norm, lightDir), 0.0);
        vec3 diffuse = diff * lightColor * vKd;

        vec3 viewDir = normalize(camera - FragPos);
        vec3 reflectDir = reflect(-lightDir, norm);
        float spec = pow(max(dot(viewDir, reflectDir), 0.0), shininess);
        vec3 specular = vKs * spec * lightColor;

        vec3 result = (ambient + diffuse + specular) * finalColor.xyz;
        gl_FragColor = vec4(result, 1.0);
    }else{
        //if w < 0, it is a parallel light
        vec3 ambient = vKa * lightColor;
        vec3 norm = normalize(pNormal);
        vec3 lightDir = normalize(lightLocation.xyz);
        float diff = max(dot(norm, lightDir), 0.0);
        vec3 diffuse = diff * lightColor * vKd;

        vec3 viewDir = normalize(camera - FragPos);
        vec3 reflectDir = reflect(-lightDir, norm);
        float spec = pow(max(dot(viewDir, reflectDir), 0.0), shininess);
        vec3 specular = vKs * spec * lightColor;

        vec3 result = (ambient + diffuse + specular) * finalColor.xyz;
        gl_FragColor = vec4(result, 1.0);
    }
}

