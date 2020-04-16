package com.vincentzhang.androidfluid.Utilities

import android.opengl.GLES20

class ShaderProgram(vertexShaderCode: String, fragmentShaderCode: String) {
    private var programId: Int = -1

    init {
        val vertexShader: Int =
            loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int =
            loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        programId = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    public fun enable() {
        GLES20.glUseProgram(programId)
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }
}