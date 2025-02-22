#version 330

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 texCoord;
layout(location = 3) in float texID;

out vec4 passColor;
out vec2 passTexCoord;
out float passTexID;


uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    //gl_Position = projection * view *  model * position;
    passColor = color;
    passTexCoord = texCoord;
    passTexID = texID;

    gl_Position = projection * view * vec4(position.xyz, 1.0);
}


