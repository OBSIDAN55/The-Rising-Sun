package trs.type.Drills;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import mindustry.world.blocks.production.Drill;

public class MultiBlockDrill extends Drill {

    TextureRegion[] multiRegions = new TextureRegion[5];

    public MultiBlockDrill(String name) {
        super(name);
    }
    @Override
    public void load(){
        super.load();
        multiRegions[0] = Core.atlas.find(name+"-left0");
        multiRegions[1] = Core.atlas.find(name+"-left1");
        multiRegions[2] = Core.atlas.find(name+"-right0");
        multiRegions[3] = Core.atlas.find(name+"-right1");
        multiRegions[4] = Core.atlas.find(name+"-center");
    }

    public class MultiBlockDrillBuild extends DrillBuild {

        @Override
        public void updateTile() {
            super.updateTile();
        }
        @Override
        public void draw(){
            super.draw();
            for (int i = 0; multiRegions.length > i;i++){
                if (i == 4) {
                    Draw.rect(multiRegions[i], x, y);
                }else {
                    Draw.rect(multiRegions[i], x, y);

                }
            }

        }
    }
}
