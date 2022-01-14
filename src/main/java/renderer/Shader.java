package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;
    private String filepath;

    public Shader(String filepath) {
        this.filepath = filepath;
        try{
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+[a-zA-Z]+");

            // Find first type of shader
            int index = source.indexOf("#type"); // Find the index of the word after #type
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring((index+6), eol).trim();

            // Find second type of shader
            index = source.indexOf("#type", eol);
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring((index+6), eol).trim();

            if (firstPattern.equals("vertex")){
                vertexSource = splitString[1];
            } else if(firstPattern.equals("fragment")){
                fragmentSource = splitString[1];
            } else {
                throw  new IOException("Unexpected token '"+ firstPattern + "'");
            }

            if (secondPattern.equals("vertex")){
                vertexSource = splitString[2];
            } else if(secondPattern.equals("fragment")){
                fragmentSource = splitString[2];
            } else {
                throw new IOException("Unexpected token '"+ secondPattern + "'");
            }

        } catch(IOException e){
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }
    }

    public void compile() {
        int vertexID, fragmentID;

        // Compile and link shaders!
        // ------------------------

        // First load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // pass shader source to the GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);
        // check for any errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false: "";
        }

        // load and compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // pass shader source to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);
        // check for any errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false: "";
        }

        // link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);
        // check for errors in linking
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tLinking of shaders failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false: "";
        }
    }

    public void use() {
        // Bind shader program
        glUseProgram(shaderProgramID);
    }

    public void detach() {
        glUseProgram(0);
    }
}
