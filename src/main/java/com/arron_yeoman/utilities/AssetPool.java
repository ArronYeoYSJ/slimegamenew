package com.arron_yeoman.utilities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.arron_yeoman.engine.audio.Sound;
import com.arron_yeoman.engine.gameobjects.components.SpriteSheet;
import com.arron_yeoman.engine.graphics.Material;
import com.arron_yeoman.engine.graphics.Shader;
import com.arron_yeoman.engine.io.ResourceLoader;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Material> materials = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();

    public static Shader getShader(String fileName){
        if (AssetPool.shaders.containsKey(fileName)){
            AssetPool.shaders.get(fileName).bind();;
            return AssetPool.shaders.get(fileName);
        } else {
            Shader s = new Shader(ResourceLoader.resourcesShaderString(fileName+ ".vert"),
            ResourceLoader.resourcesShaderString(fileName+ ".frag"));
            s.create();
            s.bind();
            AssetPool.shaders.put(fileName , s);
            return s;

        }
    }

    public static Material getMaterial(String fileName){
        if (AssetPool.materials.containsKey(fileName)){
            return AssetPool.materials.get(fileName);
        } else {
            Material m = new Material(fileName);
            AssetPool.materials.put(fileName, m);
            return m;
        }
    }

    public static void addSpriteSheet(String fileName, SpriteSheet spriteSheet){
        if (!AssetPool.spriteSheets.containsKey(fileName)){
            AssetPool.spriteSheets.put(fileName, spriteSheet);
        }
        
    }

    public static SpriteSheet getSpriteSheet(String fileName){
        if (AssetPool.spriteSheets.containsKey(fileName)){
            return AssetPool.spriteSheets.get(fileName);
        } else {
            assert false : "Error: Sprite sheet " + fileName + " not found";
            return null;
        }
    }

    public static Collection<Sound> getSounds(){
        return AssetPool.sounds.values();
    }

    public static Sound getSound(String fileName){
        if (AssetPool.sounds.containsKey(fileName)){
            return AssetPool.sounds.get(fileName);
        } else {
            assert false : "Error: Sound " + fileName + " not found";
        }
        return null;
    }

    public static Sound addSound(String fileName, Sound sound){
        if (AssetPool.sounds.containsKey(fileName)){
            return AssetPool.sounds.get(fileName);
        } else{
            AssetPool.sounds.put(fileName, sound);
            return sound;
        }
    }
}
