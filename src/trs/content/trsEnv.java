package trs.content;

import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.meta.BuildVisibility;

public class trsEnv {
    public static Block
            oreClinovalve,oreTin,oreZinc,quartzSand,darkSlate,darkSlateWall,steelOre,biomassFloor,whiteRockFloor,whiteRockWall,
            aquamarineFloor,aquamarineWall,ashFloor,ashWall,obsidianFloor,obsidianWall;

    public static void load(){
        //env
        oreClinovalve = new OreBlock("ore-clinovalve",trsItems.clinovalve){{
            oreDefault = true;
            variants = 3;
        }};
        oreTin = new OreBlock("ore-tin",trsItems.tin){{
            oreDefault = true;
            variants = 3;
        }};
        oreZinc = new OreBlock("zinc-ore",trsItems.zinc){{
            oreDefault = true;
            variants = 3;
        }};
        quartzSand = new Floor("quartz-sand",5){{
            itemDrop = trsItems.quartz;
            inEditor = false;
            breakable = true;
            buildVisibility = BuildVisibility.shown;

        }};
        darkSlate = new Floor("dark-slate",3);
        darkSlateWall = new StaticWall("dark-slate-wall");
        whiteRockFloor = new Floor("white-rock",8);
        whiteRockWall = new StaticWall("white-rock-wall");
        ashFloor = new Floor("ash-floor",4);
        ashWall = new StaticWall("ash-wall");
        aquamarineFloor = new Floor("aquamarine-floor",4);
        aquamarineWall = new StaticWall("aquamarine-wall");
        obsidianFloor = new Floor("obsidian-floor",8);
        obsidianWall = new StaticWall("obsidian-wall");
        biomassFloor = new Floor("biomass-floor",0);


        steelOre = new StaticWall("steel-ore");
    }
}
