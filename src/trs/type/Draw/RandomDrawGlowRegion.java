package trs.type.Draw;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;

public class RandomDrawGlowRegion extends DrawBlock {
    public Blending blending = Blending.additive;
    public String suffix = "-glow";
    public float alpha = 0.9f, glowScale = 10f, glowIntensity = 0.5f;
    public float rotateSpeed = 0f;
    public float layer = Layer.blockAdditive;
    public boolean rotate = false;
    public Color color = Color.red.cpy();
    public TextureRegion region;

    public RandomDrawGlowRegion(){
    }
    public RandomDrawGlowRegion(float layer){
        this.layer = layer;
    }

    public RandomDrawGlowRegion(boolean rotate){
        this.rotate = rotate;
    }


    public RandomDrawGlowRegion(String suffix){
        this.suffix = suffix;
    }

    @Override
    public void draw(Building build){
        if(build.warmup() <= 0.001f) return;

        float z = Draw.z();
        if(layer > 0) Draw.z(layer);
        Draw.blend(blending);
        Draw.color(color);
        Draw.alpha((Mathf.absin(build.totalProgress(), glowScale, alpha) * glowIntensity + 1f - glowIntensity) * build.warmup() * alpha);
        Draw.rect(region, build.x, build.y, build.totalProgress() * rotateSpeed + (rotate ? build.rotdeg() : 0f));
        Draw.reset();
        Draw.blend();
        Draw.z(z);
        if(build.progress() >= 0.999f){
            this.glowIntensity = Mathf.random(0.2f, 0.7f);
            this.glowScale = Mathf.random(10f, 15f);
        }

    }

    @Override
    public void load(Block block){
        region = Core.atlas.find(block.name + suffix);
    }
}
