package trs.content;

import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.blocks.environment.TreeBlock;

public class trsEnv {
    public static Block
            oreClinovalve,oreTin,oreZinc,quartzSand,steelOre,
            darkSlate,darkSlateWall,
            biomassFloor,
            whiteRockFloor,whiteRockWall,
            aquamarineFloor,aquamarineWall,
            ashFloor,ashWall,
            obsidianFloor,obsidianWall,
            clinovalveFloor,clinovalveClusterWall,
            petals,
            crushedStone,
                    //trees
            heavenlyTree
            ;

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
        clinovalveFloor = new Floor("clinovalve-floor",6);
        clinovalveClusterWall = new StaticWall("clinovalve-cluster-wall"){{
            variants = 7;
        }};
        petals = new Floor("petals",0);
        crushedStone = new Floor("crushed-stone",3);


        steelOre = new StaticWall("steel-ore");

        // trees

        heavenlyTree = new TreeBlock("heavenly-tree");
    }
}
