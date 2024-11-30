#version 430 core

layout(location = 0) in vec2 vertexPosition;
layout(location = 1) in vec2 texCoord;

out vec2 fragTexCoord; // Passed to the fragment shader for potential use

void main() {
    fragTexCoord = texCoord;
    gl_Position = vec4(vertexPosition, 0.0, 1.0); // Clip space position
}
