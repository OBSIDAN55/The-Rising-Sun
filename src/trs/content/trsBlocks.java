package trs.content;

import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.*;
import trs.type.*;
import trs.type.distribution.ItemLiquidDuct;
import trs.type.distribution.ItemLiquidJunction;
import trs.type.distribution.ItemLiquidRouter;
import trs.type.multicraft.IOEntry;
import trs.type.multicraft.MultiCrafter;
import trs.type.multicraft.Recipe;
import mindustry.world.blocks.distribution.*;

import static mindustry.type.ItemStack.with;

public class trsBlocks {
    public static Block
            //env
            oreClinovalve,oreTin,quartzSand,darkSlate,
            Case,incedent,Signal,
            perseverance,fortitude,stability,
            bariumLightSource,rubidiumSmelter,melter,crusher,atmosphericCondenser,clinovalveDuct,clinovalveJunction,
            clinovalveRouter,
            clinovalveSorter,
            clinovalveInvertedSorter,
            clinovalveDuctBridge,
            clinovalveOverflowGate,
            clinovalveUnderflowGate,
            clinovalveWall,clinovalveWallLarge,zincWall,zincWallLarge,steelWall,steelWallLarge,carbonWall,carbonWallLarge,exacrimWall,exacrimWallLarge;

    public static void load(){
        perseverance = new RegenGeneratorCoreBlock("perseverance"){{
            requirements(Category.effect, with(Items.copper, 15));

            size = 4;

            unitType = UnitTypes.evoke;
            health = 4500;
            itemCapacity = 2000;
            thrusterLength = 34/4f;
            armor = 5f;
            alwaysUnlocked = true;
            incinerateNonBuildable = true;
            requiresCoreZone = true;

            buildCostMultiplier = 0.7f;

            unitCapModifier = 15;
            researchCostMultiplier = 0.07f;
            powerProduction = 10f;


            drawer = new DrawMulti(
                new DrawRegion("-bottom"){{
                    layer = 29.7f;
                }},
                    new DrawRegion("-rotator"){{
                        spinSprite = true;
                        rotateSpeed = 3;
                        layer = 29.8f;
                    }},
                    new DrawGlowRegion("-rotator-glow"){{
                        rotateSpeed = 3;
                        layer = 29.9f;
                        color = Color.cyan;
                        glowScale = 5f;
                        glowIntensity = 2f;
                    }},
                    new DrawRegion("-r"){{
                        layer = 29.99f;
                    }}
            );
        }};
        fortitude = new RegenGeneratorCoreBlock("fortitude"){{
            requirements(Category.effect, with(Items.copper, 15));

            size = 5;

            unitType = UnitTypes.evoke;
            health = 4500;
            itemCapacity = 3000;
            thrusterLength = 34/4f;
            armor = 5f;
            alwaysUnlocked = true;
            incinerateNonBuildable = true;
            requiresCoreZone = true;

            buildCostMultiplier = 0.7f;

            unitCapModifier = 15;
            researchCostMultiplier = 0.07f;
            powerProduction = 10f;


            drawer = new DrawMulti(
                    new DrawRegion("-bottom"){{
                        layer = 29.7f;
                    }},
                    new DrawRegion("-rotator"){{
                        spinSprite = true;
                        rotateSpeed = 3;
                        layer = 29.8f;
                    }},
                    new DrawGlowRegion("-rotator-glow"){{
                        rotateSpeed = 3;
                        layer = 29.9f;
                        color = Color.cyan;
                        glowScale = 5f;
                        glowIntensity = 2f;
                    }},
                    new DrawRegion("-r"){{
                            layer = 29.99f;
                        }}
            );
        }};
        stability = new RegenGeneratorCoreBlock("stability"){{
            requirements(Category.effect, with(Items.copper, 15));

            size = 6;

            unitType = UnitTypes.evoke;
            health = 4500;
            itemCapacity = 3000;
            thrusterLength = 34/4f;
            armor = 5f;
            alwaysUnlocked = true;
            incinerateNonBuildable = true;
            requiresCoreZone = true;

            buildCostMultiplier = 0.7f;

            unitCapModifier = 15;
            researchCostMultiplier = 0.07f;
            powerProduction = 10f;


            drawer = new DrawMulti(
                    new DrawRegion("-bottom"){{
                        layer = 29.7f;
                    }},
                    new DrawRegion("-rotator1"){{
                        spinSprite = true;
                        rotateSpeed = 3;
                        layer = 29.8f;
                    }},
                    new DrawGlowRegion("-rotator1-glow"){{
                        rotateSpeed = 3;
                        layer = 29.9f;
                        color = Color.cyan;
                        glowScale = 5f;
                        glowIntensity = 2f;
                    }},
                    new DrawRegion("-rotator2"){{
                        spinSprite = true;
                        rotateSpeed = -3;
                        layer = 29.8f;
                    }},
                    new DrawGlowRegion("-rotator2-glow"){{
                        rotateSpeed = -3;
                        layer = 29.9f;
                        color = Color.cyan;
                        glowScale = 5f;
                        glowIntensity = 2f;
                    }},
                    new DrawRegion("-r"){{
                        layer = 29.99f;
                    }}
            );
        }};
        bariumLightSource = new ChemicalLightSource("bariumLightSource"){{
            requirements(Category.effect, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            brightness = 0.75f;
            radius = 140f;
            sourceLightColor = Color.valueOf("96037c");
        }};
        rubidiumSmelter = new GenericCrafter("rubidium-smelter"){{
            requirements(Category.crafting, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            size = 2;

            squareSprite = false;

            outputItem = new ItemStack(trsItems.rubidium, 1);
            craftTime = 40f;
            hasPower = true;
            hasLiquids = false;

            consumeItems(with(trsItems.clinovalve, 2, trsItems.tin, 1));
            consumePower(0.50f);

            drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffef99")));
        }};
        melter = new MultiCrafter("melter"){{
            requirements(Category.crafting, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            size = 3;
            itemCapacity = 10;
            drawer = new DrawMulti(new DrawDefault(), new DrawGlowRegion(){{color = Pal.engine.cpy();}});
            resolvedRecipes = Seq.with(
                    new Recipe(){{
                        input = new IOEntry() {{
                            items = ItemStack.with(
                                    trsItems.clinovalve, 4,
                                    trsItems.tin, 2
                            );
                        }};
                        output = new IOEntry(){{
                            items = ItemStack.with(
                                    trsItems.rubidium, 3
                            );
                        }};
                        craftTime = 60f;
                    }},
                    new Recipe(){{
                        input = new IOEntry() {{
                            items = ItemStack.with(
                                    trsItems.chrome, 1,
                                    trsItems.barium,1
                            );
                        }};
                        output = new IOEntry(){{
                            items = ItemStack.with(
                                    // trsItems.carbonDust, 0
                            );
                        }};
                        craftTime = 60f;
                    }},
                    new Recipe(){{
                        input = new IOEntry() {{
                            items = ItemStack.with(
                                    trsItems.chrome, 1,
                                    trsItems.zinc,1
                            );
                        }};
                        output = new IOEntry(){{
                            items = ItemStack.with(
                                    // trsItems.carbonDust, 0
                            );
                        }};
                        craftTime = 60f;
                    }},
                    new Recipe(){{
                        input = new IOEntry() {{
                            items = ItemStack.with(
                                    trsItems.biomass, 1
                            );
                        }};
                        output = new IOEntry(){{
                            items = ItemStack.with(
                                    trsItems.carbon, 1
                            );
                        }};
                        craftTime = 60f;
                    }},
                    new Recipe(){{
                        input = new IOEntry() {{
                            items = ItemStack.with(
                                    trsItems.carbonDust, 2,
                                    trsItems.quartzDust,2
                            );
                        }};
                        output = new IOEntry(){{
                            items = ItemStack.with(
                                    trsItems.carbonGlass, 1
                            );
                        }};
                        craftTime = 60f;
                    }}
            );
        }};
        crusher = new MultiCrafter("crusher"){{
            requirements(Category.crafting, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            size = 3;
            itemCapacity = 10;
            resolvedRecipes = Seq.with(
                    new Recipe(){{
                        input = new IOEntry() {{
                           items = ItemStack.with(
                                   trsItems.quartz, 5
                           );
                        }};
                        output = new IOEntry(){{
                           items = ItemStack.with(
                                   trsItems.quartzDust, 4
                           );
                        }};
                        craftTime = 150f;
                    }},
                    new Recipe(){{
                    input = new IOEntry() {{
                        items = ItemStack.with(
                                trsItems.carbon, 6
                        );
                    }};
                    output = new IOEntry(){{
                        items = ItemStack.with(
                                trsItems.carbonDust, 4
                        );
                    }};
                    craftTime = 120f;
                    }}
            );
        }};
        atmosphericCondenser = new MultiCrafter("atmospheric-condenser"){{
            requirements(Category.crafting, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            size = 3;
            liquidCapacity = 20f;
            drawer = new DrawMulti(new DrawRegion("-bottom"),new DrawLiquidTile(trsLiquids.argon), new DrawLiquidTile(trsLiquids.metan), new DrawDefault());
                resolvedRecipes = Seq.with(
                    new Recipe(){{
                        input = new IOEntry() {{
                           power = 6f;
                        }};
                        output = new IOEntry() {{
                            fluids = LiquidStack.with(trsLiquids.metan, 1f);
                        }};
                    }},
                    new Recipe(){{
                        input = new IOEntry() {{
                            power = 8f;
                        }};
                        output = new IOEntry() {{
                            fluids = LiquidStack.with(trsLiquids.argon, 1f);
                        }};
                    }}
                );

        }};
        clinovalveDuct = new ItemLiquidDuct("clinovalve-duct"){{
            requirements(Category.distribution, with(Items.copper, 1));
            speed = 4f;
            liquidCapacity = 10f;
            leaks = false;

        }};
        clinovalveRouter = new ItemLiquidRouter("clinovalve-router"){{
            requirements(Category.distribution, with(Items.copper, 3));
            buildCostMultiplier = 4f;
        }};
        clinovalveJunction = new ItemLiquidJunction("clinovalve-junction"){{
            requirements(Category.distribution, with(Items.copper, 2));
            speed = 10;
            capacity = 3;
            health = 30;
            buildCostMultiplier = 6f;
        }};
        clinovalveSorter = new Sorter("clinovalve-sorter"){{
            requirements(Category.distribution, with(Items.lead, 2, Items.copper, 2));
            buildCostMultiplier = 3f;
        }};
        clinovalveInvertedSorter = new Sorter("clinovalve-inverted-sorter"){{
            requirements(Category.distribution, with(Items.lead, 2, Items.copper, 2));
            buildCostMultiplier = 3f;
            invert = true;
        }};
        clinovalveDuctBridge = new DuctBridge("clinovalve-duct-bridge"){{
            requirements(Category.distribution, with(Items.beryllium, 20));
            health = 90;
            speed = 4f;
            buildCostMultiplier = 2f;
            researchCostMultiplier = 0.3f;
        }};
        clinovalveOverflowGate = new OverflowGate("clinovalve-overflow-gate"){{
            requirements(Category.distribution, with(Items.lead, 2, Items.copper, 4));
            buildCostMultiplier = 3f;
        }};

        clinovalveUnderflowGate = new OverflowGate("clinovalve-underflow-gate"){{
            requirements(Category.distribution, with(Items.lead, 2, Items.copper, 4));
            buildCostMultiplier = 3f;
            invert = true;
        }};
        //walls

        //env
        oreClinovalve = new OreBlock("ore-clinovalve",trsItems.clinovalve){{
            oreDefault = true;
            variants = 3;
        }};
        oreTin = new OreBlock("ore-tin",trsItems.tin){{
            oreDefault = true;
            variants = 3;
        }};
        quartzSand = new Floor("quartz-sand",4){{
            itemDrop = trsItems.quartz;

        }};
        darkSlate = new Floor("dark-slate",0);
        clinovalveWall = new Wall("clinovalve-wall"){{
            requirements(Category.defense, with(Items.lead, 2, Items.copper, 4));
        }};
        clinovalveWallLarge = new Wall("clinovalve-wall-large"){{
            size = 2;
            requirements(Category.defense, with(Items.lead, 2, Items.copper, 4));
        }};
        zincWall = new Wall("zinc-wall"){{
            requirements(Category.defense, with(Items.lead, 2, Items.copper, 4));
        }};
        zincWallLarge = new Wall("zinc-wall-large"){{
            requirements(Category.defense, with(Items.lead, 2, Items.copper, 4));
            size = 2;
        }};
        steelWall = new Wall("steel-wall"){{
            requirements(Category.defense, with(Items.lead, 2, Items.copper, 4));
        }};
        steelWallLarge = new Wall("steel-wall-large"){{
            requirements(Category.defense, with(Items.lead, 2, Items.copper, 4));
            size = 2;
        }};
        carbonWall = new Wall("carbon-wall"){{
            requirements(Category.defense, with(Items.lead, 2, Items.copper, 4));
        }};
        carbonWallLarge = new Wall("carbon-wall-large"){{
            requirements(Category.defense, with(Items.lead, 2, Items.copper, 4));
            size = 2;
        }};
        exacrimWall = new Wall("exacrim-wall"){{
            requirements(Category.defense, with(Items.lead, 2, Items.copper, 4));
            size = 2;
        }};
        exacrimWallLarge = new Wall("exacrim-wall-large"){{
            requirements(Category.defense, with(Items.lead, 2, Items.copper, 4));
            size = 3;
        }};

    }
}
