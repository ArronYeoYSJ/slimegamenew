package com.arron_yeoman.engine.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ResourceLoader {

    private static String fileSeperator = File.separator;

    public static String loadResourceAsString(String filepath){

        StringBuilder output = new StringBuilder();
        //System.out.println("Loading resource at: " + filepath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))){
            String line ="";
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading resource at: " + filepath);
        }
        
        return output.toString();
        
    }

    public static String resourcesAudioString(String filename){
        if (fileSeperator.equals("\\")){
            return "src\\resources\\audio\\"+filename;
        }else {
            return "src/resources/audio/"+filename;
        }
    }

    public static String resourcesTextureString(String filename){
        if (fileSeperator.equals("\\")){
            return "src\\resources\\textures\\"+filename;
        }else {
            return "src/resources/textures/"+filename;
        }
    }

    public static String resourcesShaderString(String filename){
        if (fileSeperator.equals("\\")){
            return "src\\resources\\shaders\\"+filename;
        }else {
            return "src/resources/shaders/"+filename;
        }
    }
}



