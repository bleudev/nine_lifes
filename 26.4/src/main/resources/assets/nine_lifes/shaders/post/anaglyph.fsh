#version 330
#extension GL_ARB_separate_shader_objects : require

uniform sampler2D InSampler;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

layout(std140) uniform AnaglyphConfig {
    vec2 Delta;
};

layout(location = 0) in vec2 texCoord;
layout(location = 0) out vec4 fragColor;

void main() {
    vec3 colR = texture(InSampler, texCoord - Delta).rgb;
    vec3 colB = texture(InSampler, texCoord + Delta).rgb;
    fragColor = vec4(colR.r, colB.g, colB.b, 1.0);
}