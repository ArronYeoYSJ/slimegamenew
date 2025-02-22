package com.arron_yeoman.engine.gameobjects.components;

import java.util.ArrayList;
import java.util.List;

import com.arron_yeoman.engine.graphics.Material;
import com.arron_yeoman.maths.Vector2;

public class SpriteSheet {

    private Material material;
    private int sheetWidth, sheetHeight;
    private int spriteWidth, spriteHeight;
    private int spriteCount, padding;

    private List<Sprite> sprites;

    public SpriteSheet(Material material, int spriteCount, int spriteWidth, int spriteHeight, int padding){
        this.material = material;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.spriteCount = spriteCount;
        this.padding = padding;

        this.sheetWidth = (int) material.getWidth();
        this.sheetHeight = (int) material.getHeight();

        int currentX = 0;
        int currentY = 0;

        this.sprites = new ArrayList<>();

        //start from bottom left of top left sprite
        
        for (int i = 0; i < spriteCount; i++){
            
            float topY = (float)(currentY + spriteHeight) / sheetHeight;
            float leftX = (float)(currentX) / sheetWidth;
            float bottomY = (float)(currentY) / sheetHeight;
            float rightX = (float)(currentX + spriteWidth) / sheetWidth;

            Vector2[] texCoords = {
                new Vector2(leftX,bottomY),
                new Vector2(rightX,bottomY),
                new Vector2(rightX,topY),
                new Vector2(leftX,topY)
            };

            Sprite sprite = new Sprite(material, texCoords);
            this.sprites.add(sprite);

            currentX += spriteWidth + padding;
            if (currentX >= sheetWidth) {
                currentX = 0;
                currentY -= spriteHeight + padding;
            }
        }
    }

    public Sprite getSprite(int index){
        return sprites.get(index);
    }
    
}
