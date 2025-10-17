#version 150

uniform sampler2D InputSampler;
uniform vec2 InputResolution;
uniform vec2 uSize;
uniform vec2 uLocation;

uniform float radius;
uniform float Brightness;
uniform float Quality;
in vec2 texCoord;

out vec4 fragColor;

float roundedBoxSDF(vec2 pos, vec2 size, float radius) {
    vec2 d = abs(pos) - size + radius;
    return length(max(d, 0.0)) - radius;
}

vec4 blur(vec2 uv) {
    const float TAU = 6.28318530718;
    vec2 radius = Quality / InputResolution;
    vec4 colorSum = vec4(0.0);
    float weightSum = 0.0;

    float angleStep = TAU / 16.0;
    float radialStep = 0.2;

    for (float angle = 0.0; angle < TAU; angle += angleStep) {
        vec2 dir = vec2(cos(angle), sin(angle));
        for (float scale = radialStep; scale <= 1.0; scale += radialStep) {
            vec2 offset = dir * radius * scale;
            colorSum += texture(InputSampler, uv + offset);
            weightSum += 1.0;
        }
    }

    return colorSum / weightSum;
}


void main() {
    vec2 uv = gl_FragCoord.xy / InputResolution;

    vec2 halfSize = uSize * 0.5;

    float sdfAlpha = 1.0 - smoothstep(0.0, 1.0, roundedBoxSDF(gl_FragCoord.xy - uLocation - halfSize, halfSize, radius));

    vec4 blurredColor = blur(uv);

    fragColor = vec4(blurredColor.rgb, sdfAlpha);
}