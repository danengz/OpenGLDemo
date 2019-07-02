attribute vec4 vPosition;
uniform mat4 vMatrix;
// 传递给片元着色器的代码
varying  vec4 vColor;
attribute vec4 aColor;
void main() {
  gl_Position = vMatrix*vPosition;
  vColor=aColor;
}