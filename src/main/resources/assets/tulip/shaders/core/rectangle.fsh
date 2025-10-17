#version 150

uniform vec4 color1;
uniform vec4 color2;
uniform vec4 color3;
uniform vec4 color4;

uniform vec2 uSize;
uniform vec2 uLocation;
uniform float radius;
uniform float smoothness;

out vec4 fragColor;

float rdist(vec2 pos, vec2 size, float radius) {
    vec2 d = abs(pos) - size + radius;
    return min(max(d.x, d.y), 0.0) + length(max(d, 0.0)) - radius;
}

float ralpha(vec2 coord, vec2 size, float radius, float smoothness) {
    vec2 center = size * 0.5;
    float dist = rdist(coord - center, center, radius);
    return 1.0 - smoothstep(0.0, smoothness, dist);
}

vec3 createGradient(vec2 coords, vec3 c1, vec3 c2, vec3 c3, vec3 c4) {
    return mix(mix(c1, c3, coords.x), mix(c2, c4, coords.x), coords.y);
}

void main() {
    vec2 fragPos = (gl_FragCoord.xy - uLocation) / uSize;
    float alpha = ralpha(gl_FragCoord.xy - uLocation, uSize, radius, smoothness);

    vec4 color = vec4(createGradient(fragPos, color1.rgb, color2.rgb, color3.rgb, color4.rgb), color1.a * alpha);

    if (color.a < 0.01) discard;

    fragColor = color;
}
