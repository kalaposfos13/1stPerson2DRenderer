#version 430 core

#define pi 3.14159265359

layout(std430, binding = 0) buffer LineData {
    float lineData[];
};

uniform vec2 cameraPosition;
uniform float cameraRotation;
uniform float cameraFov;
uniform int imageFlipped;

in vec2 fragTexCoord;

out vec4 fragColor;

vec2 rotate2(vec2 v, float a) {
    float cosR = cos(a);
    float sinR = sin(a);
    return vec2(v.x * cosR - v.y * sinR, v.x * sinR + v.y * cosR);
}
bool intersectRayWithLineSegment(vec2 rayOrigin, vec2 rayDir, vec2 lineA, vec2 lineB, out float t, out vec2 intersectionPoint) {
    vec2 lineDir = lineB - lineA;

    vec2 lineNormal = vec2(-lineDir.y, lineDir.x);

    float denom = dot(lineNormal, rayDir);
    if (abs(denom) < 1e-6) {
        return false; // Parallel, no intersection
    }

    float u = dot(lineNormal, (lineA - rayOrigin)) / denom;

    if (u < 0.0) {
        return false; // No intersection, ray points away
    }

    vec2 hitPoint = rayOrigin + u * rayDir;

    float lineParam = dot(hitPoint - lineA, lineDir) / dot(lineDir, lineDir);
    if (lineParam < 0.0 || lineParam > 1.0) {
        return false; // Outside the segment
    }

    // If we get here, there is an intersection
    t = u;
    intersectionPoint = hitPoint; // Output the intersection point
    return true;
}

float v2Len(vec2 a) {
    return sqrt(a.x * a.x + a.y * a.y);
}

float customDistanceBetweenPoints(vec2 a, vec2 b, vec2 viewNormal) {
    return abs(dot(b - a, normalize(viewNormal)));
    //return v2Len(a - b);
}

void main() {
    vec2 viewPos = (fragTexCoord - vec2(0.5)) * vec2(2.0); // (-1,-1) - (1,1)

    vec3 closestColor = vec3(0.0);

    float minDistance = 1e10;

    vec2 pixelRayStart = cameraPosition;

    float rayRotation = (cameraRotation) + cameraFov * 0.5 -
        atan(((imageFlipped == 0 ? 1 : -1) * cameraFov * 0.5 * viewPos.y));
    vec2 pixelRayDirection = rotate2(vec2(1.0, 0.0), rayRotation);


    vec2 dummy = vec2(cameraRotation, cameraFov) + cameraPosition;

    for (int i = 0; i < lineData.length() / 7; i++) {
        int lineIndex = i * 7;
        vec2 p1 = vec2(lineData[lineIndex], lineData[lineIndex + 1]);
        vec2 p2 = vec2(lineData[lineIndex + 2], lineData[lineIndex + 3]);
        vec3 color = vec3(lineData[lineIndex + 4], lineData[lineIndex + 5], lineData[lineIndex + 6]);

        float distFromRayOrigin  = 0;
        vec2 intersection = vec2(0.0);
        if (intersectRayWithLineSegment(cameraPosition, pixelRayDirection, p1, p2, distFromRayOrigin, intersection)) {
            float customDist = customDistanceBetweenPoints(intersection, cameraPosition, rotate2(vec2(1.0, 0.0), cameraRotation + cameraFov * 0.5));
            if ( 10 / customDist > abs(viewPos.x) && distFromRayOrigin < minDistance) {
                minDistance = distFromRayOrigin;
                closestColor = color;
            }
        }
    }

    fragColor = vec4(closestColor, 1.0);
}
