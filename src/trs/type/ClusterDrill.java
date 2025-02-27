package trs.type;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Interp;
import arc.math.Mathf;
import arc.util.Tmp;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.world.blocks.production.BurstDrill;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;

public class ClusterDrill extends BurstDrill {

    public DrawBlock drawer = new DrawDefault();

    public TextureRegion region1, region2, regiont, iconRegion;
    public float sinMag = 4f, sinScl = 6f, sinOffset = 50f, sideOffset = 180f, lenOffset = -1f, horiOffset = 0f, angleOffset = 135f;
    public int sides = 2;
    public String suffix = "-piston";

    public ClusterDrill(String name) {
        super(name);
    }
    @Override
    public void load(){
        super.load();
        region1 = Core.atlas.find(name + suffix + "0", name + suffix);
        region2 = Core.atlas.find(name + suffix + "1", name + suffix);
        regiont = Core.atlas.find(name + suffix + "-t");
        iconRegion = Core.atlas.find(name + suffix + "-icon");

        drawer.load(this);
    }
    public class ClusterDrillBuild extends BurstDrillBuild {
        @Override
        public void draw(){
            drawer.draw(this);
            Draw.rect(region, x, y);
            drawDefaultCracks();

            if(dominantItem != null && drawMineItem){
                Draw.color(dominantItem.color);
                Draw.rect(itemRegion, x, y);
                Draw.color();
            }
            float fract = smoothProgress;
            for(int i = 0; i < sides; i++){
                float len = Mathf.absin(totalProgress() + sinOffset + sideOffset * sinScl * i, sinScl, sinMag) + lenOffset;
                float angle = angleOffset + i * 360f / sides;
                TextureRegion reg =
                        regiont.found() && (Mathf.equal(angle, 315) || Mathf.equal(angle, 135)) ? regiont :
                                angle >= 135 && angle < 315 ? region2 : region1;

                if(Mathf.equal(angle, 315)){
                    Draw.yscl = -1f;
                }

                Tmp.v1.trns(angle, len, -horiOffset);
                Draw.rect(reg, x + Tmp.v1.x, y + Tmp.v1.y, angle);

                Draw.yscl = 1f;
            }



        }

        @Override
        public void drawLight() {
            super.drawLight();
            drawer.drawLight(this);


        }
    }
}

