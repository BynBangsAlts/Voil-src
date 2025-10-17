#version 150

uniform vec4 color1;
uniform vec2 uLocation;
uniform vec2 size;
uniform float radius;
uniform float smoothness;
uniform vec2 resolution;

out vec4 fragColor;

float rdist(vec2 pos, vec2 size, float radius) {
    vec2 d = abs(pos) - size + radius;
    return min(max(d.x, d.y), 0.0) + length(max(d, 0.0)) - radius;
}

float ralpha(vec2 coord, vec2 size, float radius, float smoothness) {
    vec2 center = size * 0.5;
    float dist = rdist(coord - center, size, radius);
    return 1.0 - smoothstep(0.0, smoothness, dist);
}

void main() {
    vec2 fragPos = (gl_FragCoord.xy - uLocation) / size;  // Normalized position
    float alpha = ralpha(gl_FragCoord.xy - uLocation, size, radius, smoothness);

    vec4 color = vec4(color1.rgb, color1.a * alpha);

    if (color.a < 0.01) discard;

    fragColor = color;
}