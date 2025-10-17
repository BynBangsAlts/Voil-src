#version 150

uniform vec4 color1;
uniform vec4 color2;
uniform vec4 color3;
uniform vec4 color4;

uniform vec2 uSize;
uniform vec2 uLocation;
uniform float radius;
uniform float smoothness;
uniform float thickness;

out vec4 fragColor;

float roundedBox(vec2 pos, vec2 size, float rad) {
    vec2 d = abs(pos) - size + rad;
    return length(max(d, 0.0)) + min(max(d.x, d.y), 0.0) - rad;
}

vec3 createGradient(vec2 coords, vec3 c1, vec3 c2, vec3 c3, vec3 c4) {
    return mix(mix(c1, c3, coords.x), mix(c2, c4, coords.x), coords.y);
}

void main() {
    vec2 center = uLocation + uSize * 0.5;
    vec2 position = gl_FragCoord.xy - center;
    vec2 halfSize = uSize * 0.5 - thickness * 0.5;

    float outerDist = roundedBox(position, halfSize + thickness, radius);
    float innerDist = roundedBox(position, halfSize - thickness, max(radius - thickness, 0.0));

    float combined = max(outerDist, -innerDist);

    float alpha = 1.0 - smoothstep(-smoothness, smoothness, combined);

    vec2 fragPos = (gl_FragCoord.xy - uLocation) / uSize;
    vec4 color = vec4(createGradient(fragPos, color1.rgb, color2.rgb, color3.rgb, color4.rgb), color1.a * alpha);

    if (color.a < 0.01) discard;

    fragColor = color;
}