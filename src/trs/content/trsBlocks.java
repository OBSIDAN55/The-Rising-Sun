package trs.content;

import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.*;
import trs.type.ChemicalLightSource;
import trs.type.RegenGeneratorCoreBlock;
import trs.type.multicraft.IOEntry;
import trs.type.multicraft.MultiCrafter;
import trs.type.multicraft.Recipe;
import mindustry.world.blocks.distribution.*;

import static mindustry.type.ItemStack.with;

public class trsBlocks {
    public static Block
    perseverance,fortitude,stability, bariumLightSource,rubidiumSmelter,melter,crusher,atmosphericCondenser,tinDuct,tinJunction,tinRouter,tinSorter,tinInvertSorter,tinDuctBridge,tinOverflowGate,tinUnderflowGate;

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
        melter = new Wall("melter"){{
            requirements(Category.crafting, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            size = 3;
        }};
        crusher = new MultiCrafter("crusher"){{
            requirements(Category.crafting, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            size = 3;
            itemCapacity = 10;
            drawer = new DrawMulti(new DrawRegion("-bottom"),new DrawLiquidTile(trsLiquids.argon), new DrawLiquidTile(trsLiquids.metan), new DrawDefault());
            resolvedRecipes = Seq.with(
                    new Recipe(){{
                        input = new IOEntry() {{
                           items = ItemStack.with(
                                   trsItems.quartz, 1
                           );
                        }};
                        output = new IOEntry(){{
                           items = ItemStack.with(
                                   trsItems.quartzDust, 2
                           );
                        }};
                        craftTime = 1f;
                    }},
                    new Recipe(){{
                    input = new IOEntry() {{
                        items = ItemStack.with(
                                trsItems.carbon, 1
                        );
                    }};
                    output = new IOEntry(){{
                        items = ItemStack.with(
                                trsItems.carbonDust, 2
                        );
                    }};
                    craftTime = 1f;
                    }}
            );
        }};
        atmosphericCondenser = new MultiCrafter("atmospheric-condenser"){{
            requirements(Category.crafting, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            size = 3;
            liquidCapacity = 20f;
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
        tinDuct = new Duct("tin-duct"){{
            requirements(Category.distribution, with(Items.copper, 1));
            speed = 4f;
        }};
        tinRouter = new Router("tin-router"){{
            requirements(Category.distribution, with(Items.copper, 3));
            buildCostMultiplier = 4f;
        }};
        tinJunction = new Junction("tin-junction‎"){{
            requirements(Category.distribution, with(Items.copper, 2));
            speed = 15;
            capacity = 3;
            health = 30;
            buildCostMultiplier = 6f;
        }};
        tinSorter = new Sorter("tin-sorter"){{
            requirements(Category.distribution, with(Items.lead, 2, Items.copper, 2));
            buildCostMultiplier = 3f;
        }};
        tinInvertSorter = new Sorter("tin-invert-sorter"){{
            requirements(Category.distribution, with(Items.lead, 2, Items.copper, 2));
            buildCostMultiplier = 3f;
            invert = true;
        }};
        tinDuctBridge = new DuctBridge("tin-duct-bridge"){{
            requirements(Category.distribution, with(Items.beryllium, 20));
            health = 90;
            speed = 4f;
            buildCostMultiplier = 2f;
            researchCostMultiplier = 0.3f;
        }};
        tinOverflowGate = new OverflowGate("tin-overflow-gate"){{
            requirements(Category.distribution, with(Items.lead, 2, Items.copper, 4));
            buildCostMultiplier = 3f;
        }};

        tinUnderflowGate = new OverflowGate("tin-underflow-gate"){{
            requirements(Category.distribution, with(Items.lead, 2, Items.copper, 4));
            buildCostMultiplier = 3f;
            invert = true;
        }};
    }
}
