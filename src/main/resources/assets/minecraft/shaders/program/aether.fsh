#version 120

// The current rendered image
// (name is hardcoded into the minecraft code,
// so if you want it to be updated properly,
// you must name it DiffuseSampler)
uniform sampler2D DiffuseSampler;

// texCoord is the current xy position of this fragment(pixel) on the screen.
// It ranges from 0 to 1.
// It is interpolated from between the positions of the positions sent to the vertex shader (that's what varying's do)
varying vec2 texCoord;

void main() {
    // This is the current colour of the rendering
    vec4 CurrTexel = texture2D(DiffuseSampler, texCoord);

    // Average the colours, and use that as the brightness value
    float brightness = (CurrTexel.x + CurrTexel.y + CurrTexel.z)/3.0;
    CurrTexel.x = brightness;
    CurrTexel.y = brightness*4;
    CurrTexel.z = brightness;

    gl_FragColor = CurrTexel;
}
