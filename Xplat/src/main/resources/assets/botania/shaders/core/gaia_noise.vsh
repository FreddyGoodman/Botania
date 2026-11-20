#version 330
// Based on the entity shader, with Gaia's positional distortion.

#moj_import <minecraft:light.glsl>
#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <minecraft:sample_lightmap.glsl>
#moj_import <minecraft:globals.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV1;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler1;
uniform sampler2D Sampler2;

out float sphericalVertexDistance;
out float cylindricalVertexDistance;
out vec4 vertexColor;
out vec4 lightMapColor;
out vec4 overlayColor;
out vec2 texCoord0;
flat out float grainIntensity;

float rand(vec2 co) {
    return (fract(sin(dot(co.xy, vec2(12.9898,78.233))) * 43758.5453) - 0.5) * 2;
}

void main() {
#ifdef DYNAMIC_NOISE
    // The guardian stores its individual shader settings in the model tint.
    grainIntensity = Color.r;
    vec2 encodedDisfiguration = round(Color.gb * 255.0);
    float disfiguration = (encodedDisfiguration.x * 256.0 + encodedDisfiguration.y) / 65535.0;
    vec4 tint = vec4(1.0, 1.0, 1.0, Color.a);
#else
    grainIntensity = 0.05;
    float disfiguration = 0.025;
    vec4 tint = Color;
#endif

    float seed = rand(vec2(GameTime, GameTime));
    vec3 offset = disfiguration * vec3(
        rand(seed * Position.yz),
        rand(seed * Position.xz),
        rand(seed * Position.xy)
    );
    gl_Position = ProjMat * ModelViewMat * vec4(Position + offset, 1.0);

    sphericalVertexDistance = fog_spherical_distance(Position);
    cylindricalVertexDistance = fog_cylindrical_distance(Position);
    vertexColor = minecraft_mix_light(Light0_Direction, Light1_Direction, Normal, tint);
    lightMapColor = sample_lightmap(Sampler2, UV2);
    overlayColor = texelFetch(Sampler1, UV1, 0);
    texCoord0 = UV0;
}
