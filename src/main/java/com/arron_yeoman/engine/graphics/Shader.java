package com.arron_yeoman.engine.graphics;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import com.arron_yeoman.engine.io.ResourceLoader;
import com.arron_yeoman.maths.Matrix4x4;
import com.arron_yeoman.maths.Vector4;



public class Shader {
    
    private String vertexFile, fragmentFile;
    private String vPath;
    public int vertexID, fragmentID, programID;
    private boolean running = false;

    public Shader(String vPath, String fPath) {
        //System.out.println("Shader created");
        this.vPath = vPath;
        vertexFile = ResourceLoader.loadResourceAsString(vPath);
        fragmentFile = ResourceLoader.loadResourceAsString(fPath);
    }

    public void create() {
        //System.out.println("creating shader11");
        programID = GL20.glCreateProgram();
        //System.out.println("Shader program created");
        vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        //System.out.println("Vertex shader created");
        GL20.glShaderSource(vertexID, vertexFile);
        GL20.glCompileShader(vertexID);
        //System.out.println("Vertex shader compiled");
        //check whether the vertex shader compiled successfully
        if (GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            System.err.println("Vertex shader not compiled");
            System.err.println(GL20.glGetShaderInfoLog(vertexID));
            return;
        }


        fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentID, fragmentFile);
        GL20.glCompileShader(fragmentID);

        //check whether the fragment shader compiled successfully
        if (GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            System.err.println("Fragment shader not compiled");
            System.err.println(GL20.glGetShaderInfoLog(fragmentID));
            return;
        }

        GL20.glAttachShader(programID, vertexID);
        GL20.glAttachShader(programID, fragmentID);
        //link shaders to the program
        GL20.glLinkProgram(programID);
        //check whether the program linked successfully, report errors id not
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL20.GL_FALSE) {
            System.err.println("Program not linked");
            System.err.println(GL20.glGetProgramInfoLog(programID));
            return;
        }
        // validate the program creation
        GL20.glValidateProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == GL20.GL_FALSE) {
            System.err.println("SHADER VALIDATION FAILED");
            System.err.println(GL20.glGetProgramInfoLog(programID));
            return;
        }
        else {
            //System.out.println("Shader program created successfully");
        }
    }

    public void bind() {
        //flag prevents the program from being bound multiple times at once
        if(!running) {
            GL20.glUseProgram(programID);
            running = true;
            //System.out.println("Shader " + programID + " bound");
        }
    }

    public void unbind() {
        GL20.glUseProgram(0);
        running = false;
    }

    public void destroy() {
        GL20.glDetachShader(programID, vertexID);
        GL20.glDetachShader(programID, fragmentID);
        GL20.glDeleteShader(vertexID);
        GL20.glDeleteShader(fragmentID);
        GL20.glDeleteProgram(programID);
    }

    public int getUniformLocation(String name) {
        return GL20.glGetUniformLocation(programID, name);
    }

    public void setUniform(String name, int value) {
        GL20.glUniform1i(getUniformLocation(name), value);
    }
    public void setUniformArray(String name, int[] value) {
        GL20.glUniform1iv(getUniformLocation(name), value);
    }
    public void setUniform(String name, float value) {
        GL20.glUniform1f(getUniformLocation(name), value);
    }
    public void setUniform(String name, boolean value) {
        GL20.glUniform1i(getUniformLocation(name), value  ? 1 :0);
    }
    public void setUniform(String name, float[] value) {
        GL20.glUniform1fv(getUniformLocation(name), value);        
        }
   
    public void setUniform(String name, Vector4 value) {
        GL20.glUniform4f(getUniformLocation(name), value.getX(), value.getY(), value.getZ(), value.getW());
    }
    public void setUniform(String name, Matrix4x4 value) {
        FloatBuffer matrix = MemoryUtil.memAllocFloat(16);
        // value = value.transpose();
        matrix.put(value.getAll());
        matrix.flip();
        GL20.glUniformMatrix4fv(getUniformLocation(name), true, matrix);
        }
    public int getProgramID() {
        return programID;
    }
    public String getVPath () {
        return vPath;
    }
}
