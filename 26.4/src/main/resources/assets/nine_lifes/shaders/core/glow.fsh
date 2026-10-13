#version 330
#extension GL_ARB_separate_shader_objects : require

layout(location = 0) in vec2 texCoord;
layout(location = 1) in vec4 vertexColor;

layout(location = 0) out vec4 fragColor;

void main() {
    vec2 centered = texCoord - vec2(0.5);
    float distance = length(centered) * 2.0;
    float glow = 1.0 - smoothstep(0.0, 1.0, distance);
    glow = pow(glow, 1.35);
    float alpha = vertexColor.a * glow;
    if (alpha <= 0.001) {
        discard;
    }
    fragColor = vec4(vertexColor.rgb, alpha);
}