package trs.type;

import arc.func.Prov;
import arc.graphics.Color;
import arc.math.geom.Geometry;
import arc.math.geom.Rect;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.entities.Effect;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.Tile;

import static mindustry.Vars.*;

public class ExplosiveCharge extends Block {

    public Color baseColor = Color.red;

    public float areaSize = 5f;

    public float explosionShake = 0f, explosionShakeDuration = 6f;

    public ExplosiveCharge(String name) {
        super(name);
        update = true;
        solid = true;
        hasLiquids = true;
        sync = true;
    }
    @Override
    public void setBars(){
        super.setBars();
        addBar("heat", (ExplosiveChargeBuild entity) -> new Bar("bar.heat", Pal.lightOrange, () -> entity.heat));
        addBar("health1", entity -> new Bar(
                String.valueOf((Prov<CharSequence>) () -> "rot"),
                Pal.lightOrange,
                () -> (float) entity.rotation / 3
        ));
        addBar("health2", entity -> new Bar("stat.health2", Pal.health, entity::x).blink(Color.white));
    }
    public class ExplosiveChargeBuild extends Building {
        public float heat;

        public float startX;
        public float startY;

        public float endX;
        public float endY;


        @Override
        public void updateTile() {
            if(liquids.currentAmount() == 0 && heat > 0){
                heat-= 0.005f;
            }else heating();
        }
        public void heating(){
            heat+=0.01f;
            if(heat >=0.9999f){
                createExplosion();
            }
        }
        @Override
        public void onDestroyed(){
            super.onDestroyed();
            createExplosion();
        }
        public void createExplosion(){
                    if(rotation == 0){
                        startX = x + tilesize;
                        startY = y + ((areaSize/2)-(areaSize%2))*tilesize;
                        endX = startX + 5*tilesize;
                        endY = startY -5*tilesize;
                    } else if (rotation == 1) {
                        startX = x -((areaSize/2)-(areaSize%2))*tilesize;
                        startY = y + areaSize*tilesize;
                        endX = startX + 5*tilesize;
                        endY = startY -5*tilesize;
                    }else if(rotation ==2){
                        startX = x - areaSize*tilesize;
                        startY = y + ((areaSize/2)-(areaSize%2))*tilesize;
                        endX = startX + 5*tilesize;
                        endY = startY -5*tilesize;
                    } else {
                        startX = x -((areaSize/2)-(areaSize%2))*tilesize;
                        startY = y - tilesize;
                        endX = startX + 5*tilesize;
                        endY = startY -5*tilesize;
                    }
                    for (float x = startX; x < endX; x+=tilesize){
                        for (float y = startY; y > endY; y-=tilesize){
                            Vars.world.tileWorld((int) x, (int) y).setBlock(Blocks.beamNode);
                        }
                    }
                //explodeEffect.at(this);
                //explodeSound.at(this);
                if(explosionShake > 0){
                    Effect.shake(explosionShake, explosionShakeDuration, this);
                }
        }
        public Rect getRect(Rect rect, float x, float y, int rotation){
            rect.setCentered(x, y, areaSize * tilesize);
            float len = tilesize * (areaSize + size)/2f;

            rect.x += Geometry.d4x(rotation) * len;
            rect.y += Geometry.d4y(rotation) * len;

            return rect;
        }
        @Override
        public void drawSelect(){
            Drawf.dashRect(Tmp.c1.set(baseColor).lerp(Pal.remove, areaSize+5), getRect(Tmp.r1, x, y, rotation));
        }
    }
}
