package com.arron_yeoman.engine.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.arron_yeoman.engine.gameobjects.GameObject;
import com.arron_yeoman.engine.gameobjects.components.SpriteRenderer;

public class BatchRenderer {
    private static final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public BatchRenderer(){
        this.batches = new ArrayList<>();
    }

    public void add(GameObject go){
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if (spr != null){
            add(spr);
        }
    }

    public void add(SpriteRenderer spr){
        boolean added = false;
        for (RenderBatch batch : batches){
            if (batch.hasRoom() && spr.gameObject.getLayer() == batch.getLayer()){
                Material mat = spr.getMaterial();
                if(mat == null || batch.hasMaterial(mat) || batch.hasMaterialRoom()){
                    batch.addSprite(spr);
                    added = true;
                    break;
                }
            }
        }
        if (!added){
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, spr.gameObject.getLayer());
            newBatch.start();
            newBatch.addSprite(spr);
            batches.add(newBatch);
            Collections.sort(batches);
        }
    }

    public void render(){
        for (RenderBatch batch : batches){
            batch.render();
        }
    }

    
}
