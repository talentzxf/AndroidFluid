package com.vincentzhang.androidfluid.geometry

import android.graphics.Shader
import android.opengl.GLES20
import com.vincentzhang.androidfluid.Utilities.ShaderProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

// number of coordinates per vertex in this array
const val COORDS_PER_VERTEX = 3
var triangleCoords = floatArrayOf(     // in counterclockwise order:
    0.0f, 0.622008459f, 0.0f,      // top
    -0.5f, -0.311004243f, 0.0f,    // bottom left
    0.5f, -0.311004243f, 0.0f      // bottom right
)

class Triangle {

    private val vertextShaderCode =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
                "void main(){" +
                "gl_Position = uMVPMatrix * vPosition;" +
                "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main(){" +
                "gl_FragColor = vColor;" +
                "}"

    private var mProgram = ShaderProgram(vertextShaderCode, fragmentShaderCode)
    private var vPMatrixHandler: Int = 0

    // Set color with red, green, blue and alpha (opacity) values
    val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

    private var vertexBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(triangleCoords)
                // set the buffer to read the first coordinate
                position(0)
            }
        }


    private var positionHandler: Int = 0
    private var mColorHandler: Int = 0
    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4
    fun draw(mvpMatrix: FloatArray) {
        mProgram.enable()
        vPMatrixHandler = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        GLES20.glUniformMatrix4fv(vPMatrixHandler, 1, false, mvpMatrix, 0)

        positionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition").also {
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )
            mColorHandler = GLES20.glGetUniformLocation(mProgram, "vColor")
                .also { colorHandle -> GLES20.glUniform4fv(colorHandle, 1, color, 0) }

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
            GLES20.glDisableVertexAttribArray(it)
        }
    }
}