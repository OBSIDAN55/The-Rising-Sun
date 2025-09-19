package trs.type.Drills;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.world.blocks.production.BurstDrill;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;

public class ClusterDrill extends BurstDrill {

    public DrawBlock drawer = new DrawDefault();

    public TextureRegion region1, region2, bottom;
    public String suffix = "-piston";

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{Core.atlas.find(name+"-icon")};
    }

    public ClusterDrill(String name) {
        super(name);
    }
    @Override
    public void load(){
        super.load();
        region1 = Core.atlas.find(name + suffix + "0", name + suffix);
        region2 = Core.atlas.find(name + suffix + "1", name + suffix);
        bottom = Core.atlas.find(name +"-bottom");
        drawer.load(this);
    }
    public class ClusterDrillBuild extends BurstDrillBuild {
        float pistonProgress;
        float pistonProgress1 = 0.95f*size;
        @Override
        public void draw(){
            drawer.draw(this);
            Draw.rect(bottom,x,y);

            /*if(dominantItem != null && drawMineItem){
                    Draw.color(dominantItem.color);
                    Draw.rect(itemRegion, x, y);
                    Draw.color();
                }*/
            if (progress/drillTime>0.95f){
                pistonProgress1 = Mathf.approach(pistonProgress1,0,progress/drillTime/5);
                Draw.rect(region1,x-this.pistonProgress1,y+this.pistonProgress1);
                Draw.rect(region2,x+this.pistonProgress1,y-this.pistonProgress1);
            }else{
                pistonProgress = progress/drillTime*size;
                Draw.rect(region1,x-this.pistonProgress,y+this.pistonProgress);
                Draw.rect(region2,x+this.pistonProgress,y-this.pistonProgress);
                pistonProgress1 = 0.95f*size;
            }
            Draw.rect(region, x, y);
            drawDefaultCracks();
        }

        @Override
        public void drawLight() {
            super.drawLight();
            drawer.drawLight(this);


        }
    }
}
