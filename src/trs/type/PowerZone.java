package trs.type;

import static mindustry.Vars.tilesize;

import arc.graphics.Color;
import java.awt.*;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;

public class PowerZone extends PowerNode {



    public int zoneRange = 5;
    public PowerZone(String name) {
        super(name);
        update = true;
        solid = true;
        hasPower = true;
        group = BlockGroup.power;

        configurable = true;
        consumesPower = false;
        outputsPower = false;
        canOverdrive = false;
        swapDiagonalPlacement = true;
        schematicPriority = -10;
        drawDisabled = false;
        envEnabled |= Env.space;
        destructible = true;

        laserRange = 10;
        maxNodes = 99999999;
    }

    public class PowerZoneBuild extends PowerNodeBuild {

        @Override
        public void updateTile(){
            getPotentialLinks(tile, team, other -> {

                if(!power.links.contains(other.pos())){
                    for (int i = 0;i<power.links.size;i++){
                        //if(power.links.contains())
                    }
                    configureAny(other.pos());
                }
            });
        }

        @Override
        public boolean onConfigureBuildTapped(Building other){
                deselect();
                return false;
        }

        @Override
        public void drawSelect(){
            Drawf.dashRect(Color.cyan,this.x + (float) tilesize /2,this.y + (float) tilesize /2,zoneRange*tilesize,zoneRange*tilesize);
        }
    }
}
