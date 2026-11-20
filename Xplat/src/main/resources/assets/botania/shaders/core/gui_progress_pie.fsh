#version 330

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec2 point = texCoord0 * 2.0 - 1.0;
    float radius = length(point);
    vec2 encoded = round(vertexColor.rg * 255.0);
    float progress = encoded.x * 256.0 + encoded.y;
    float angle = dot(point, point) == 0.0 ? 0.0 : mod(degrees(atan(point.x, -point.y)), 360.0);

    if (radius > 1.0 || angle > progress) {
        discard;
    }

    fragColor = vec4(0.0, mix(0.5, 1.0, radius), 0.5, vertexColor.a);
}
