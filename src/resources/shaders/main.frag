#version 330

in vec4 passColor;
in vec2 passTexCoord;
in float passTexID;

out vec4 outColor;

uniform sampler2D uTextures[8];

void main()
{
    if (passTexID > 0){
        vec4 texColor = texture(uTextures[int(passTexID)], passTexCoord);
        outColor = passColor * texColor;
    } else {
        outColor = passColor;
    }
}
