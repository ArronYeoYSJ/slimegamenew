package com.arron_yeoman.engine.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceLoader {

    public static String loadResourceAsString(String filepath) {
        StringBuilder output = new StringBuilder();

        // Debugging output
        System.out.println("Trying to load resource: " + filepath);

        // Load resource using ClassLoader (Works in JAR and Development)
        try (InputStream inputStream = ResourceLoader.class.getClassLoader().getResourceAsStream(filepath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                System.err.println("ERROR: Resource not found - " + filepath);
                return null;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.err.println("Error loading resource: " + filepath);
        }

        return output.toString();
    }

    public static String resourcesAudioString(String filename) {
        return "audio/" + filename; // Returns a relative resource path
    }

    public static String resourcesTextureString(String filename) {
        return "textures/" + filename;
    }

    public static String resourcesShaderString(String filename) {
        return "shaders/" + filename;
    }
}
