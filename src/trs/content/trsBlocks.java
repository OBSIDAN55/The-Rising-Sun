package trs.content;

import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.content.*;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.blocks.power.ConsumeGenerator;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.*;
import mindustry.world.meta.Env;
import trs.type.*;
import trs.type.Draw.RandomDrawGlowRegion;
import trs.type.Drills.MultiBlockDrill;
import trs.type.defense.turrets.CountForceProjector;
import trs.type.distribution.*;
import trs.type.Drills.ClusterDrill;
import trs.type.multicraft.IOEntry;
import trs.type.multicraft.MultiCrafter;
import trs.type.multicraft.Recipe;
import trs.type.defense.turrets.TRSItemTurret;

import static mindustry.type.ItemStack.with;

public class trsBlocks {
    public static Block
            //env
            oreClinovalve,oreTin,quartzSand,darkSlate,steelOre,
    //cores
            Case,incedent,Signal,
            perseverance,fortitude,stability,a,b,c,
    //prod
            bariumLightSource,rubidiumSmelter,melter,crusher,atmosphericCondenser,carbonGlassClin,test,brazier,
    //distribution
            clinovalveDuct,
            clinovalveJunction,
            clinovalveRouter,
            clinovalveSorter,
            clinovalveInvertedSorter,
            clinovalveDuctBridge,
            clinovalveOverflowGate,
            clinovalveUnderflowGate,
    //walls
            clinovalveWall,clinovalveWallLarge,zincWall,zincWallLarge,steelWall,steelWallLarge,carbonWall,carbonWallLarge,exacrimWall,exacrimWallLarge,
    //drills
            hydraulicDrill,deepDrill,clusterDrill,hui,huiPart,
    //turrets
        splash, artery,
    //power
        carbonBiomassReactor;

    public static void load(){
        c = new CountForceProjector("shield"){{
            requirements(Category.effect, with(Items.copper,1));
            size = 3;
            sides = 40;
        }};
        b = new ItemLiquidContainer("b"){{
            requirements(Category.effect, with(Items.copper,1));
            liquidCapacity = 100f;
            itemCapacity = 100;
        }};

        test = new OverclockGenericCrafter("test"){{
            requirements(Category.crafting, with(Items.copper,1));
            consumeItem(Items.copper, 2);
            consumeItem(Items.thorium, 0).optional(true,false);
            baseCraftTime = 60f;
            outputItem = new ItemStack(Items.lead, 2);
            consumePower(1f);
            consumePowerPhase(1f);
        }};
        a = new ExplosiveCharge("a"){{
           requirements(Category.effect, with(Items.copper,1));
           consumeLiquid(Liquids.water, 1f);
           rotate = true;
        }};
        Case = new BuildTurretRegenGeneratorCoreBlock("case"){{
            requirements(Category.effect, with(Items.copper, 15));
            outlineColor = Color.valueOf("00000000");

            health = 4500;
            itemCapacity = 2000;
            thrusterLength = 34/4f;
            armor = 5f;
            alwaysUnlocked = true;
            requiresCoreZone = true;

            buildCostMultiplier = 0.7f;

            unitCapModifier = 10;
            researchCostMultiplier = 0.07f;
            powerProduction = 10f;
            incinerateNonBuildable = false;
            isFirstTier = true;
            size = 3;
            isRegen = false;
            isGenerator = false;

            squareSprite = false;

            drawer = new DrawRegion("-r"){{
                layer = 29.99f;
            }};
        }};
        perseverance = new BuildTurretRegenGeneratorCoreBlock("perseverance"){{
            requirements(Category.effect, with(Items.copper, 15));
            outlineColor = Color.valueOf("00000000");

            size = 4;

            health = 4500;
            itemCapacity = 2000;
            thrusterLength = 34/4f;
            armor = 5f;
            alwaysUnlocked = true;
            requiresCoreZone = true;

            buildCostMultiplier = 0.7f;

            unitCapModifier = 15;
            researchCostMultiplier = 0.07f;
            powerProduction = 10f;
            incinerateNonBuildable = false;
            isFirstTier = true;
            squareSprite = false;


            drawer = new DrawMulti(
                new DrawRegion("-bottom"){{
                    layer = 29.7f;
                }},
                    new DrawRegion("-rotator"){{
                        spinSprite = true;
                        rotateSpeed = 3;
                        layer = 29.8f;
                    }},
                    new RandomDrawGlowRegion("-rotator-glow"){{
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
        fortitude = new BuildTurretRegenGeneratorCoreBlock("fortitude"){{
            requirements(Category.effect, with(Items.copper, 15));
            outlineColor = Color.valueOf("00000000");

            size = 5;

            health = 4500;
            itemCapacity = 3000;
            thrusterLength = 34/4f;
            armor = 5f;
            alwaysUnlocked = true;
            incinerateNonBuildable = false;

            buildCostMultiplier = 0.7f;

            unitCapModifier = 15;
            researchCostMultiplier = 0.07f;
            powerProduction = 10f;
            squareSprite = false;


            drawer = new DrawMulti(
                    new DrawRegion("-bottom"){{
                        layer = 29.7f;
                    }},
                    new DrawRegion("-rotator"){{
                        spinSprite = true;
                        rotateSpeed = 3;
                        layer = 29.8f;
                    }},
                    new RandomDrawGlowRegion("-rotator-glow"){{
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
        stability = new BuildTurretRegenGeneratorCoreBlock("stability"){{
            requirements(Category.effect, with(Items.copper, 15));
            outlineColor = Color.valueOf("00000000");

            size = 6;

            health = 4500;
            itemCapacity = 3000;
            thrusterLength = 34/4f;
            armor = 5f;
            alwaysUnlocked = true;
            incinerateNonBuildable = false;

            buildCostMultiplier = 0.7f;

            unitCapModifier = 15;
            researchCostMultiplier = 0.07f;
            powerProduction = 10f;
            squareSprite = false;


            drawer = new DrawMulti(
                    new DrawRegion("-bottom"){{
                        layer = 29.7f;
                    }},
                    new DrawRegion("-rotator1"){{
                        spinSprite = true;
                        rotateSpeed = 3;
                        layer = 29.8f;
                    }},
                    new RandomDrawGlowRegion("-rotator1-glow"){{
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
                    new RandomDrawGlowRegion("-rotator2-glow"){{
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
            squareSprite = true;
            brightness = 0.75f;
            radius = 140f;
            sourceLightColor = Color.valueOf("96037c");
        }};
        rubidiumSmelter = new GenericCrafter("rubidium-smelter"){{
            requirements(Category.crafting, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            size = 4;

            squareSprite = false;
            itemCapacity = 15;

            outputItem = new ItemStack(trsItems.rubidium, 4);
            craftTime = 40f;
            hasPower = true;
            hasLiquids = false;

            consumeItems(with(trsItems.clinovalve, 6, trsItems.tin, 4));
            consumePower(0.50f);

            drawer = new DrawMulti(new DrawDefault(), new RandomDrawGlowRegion(){{
                color = Color.valueOf("b17702");
            }});
        }};
        melter = new MultiCrafter("melter"){{
            squareSprite = false;
            requirements(Category.crafting, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            size = 3;
            itemCapacity = 10;
            drawer = new DrawMulti(new DrawRegion(""){{layer = 31;}}, new RandomDrawGlowRegion(32){{color = Pal.engine.cpy();}});
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
        brazier = new GenericCrafter("brazier"){{
            requirements(Category.crafting, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            size = 3;
            itemCapacity = 10;
            consumeItem(trsItems.biomass,4);
            outputItem = new ItemStack(trsItems.carbon, 3);
            consumePower(240f/60f);
            drawer = new DrawMulti(new DrawDefault(),new RandomDrawGlowRegion(){{color = Color.valueOf("8c583e");}});
        }};
        carbonGlassClin = new GenericCrafter("carbon-glass-clin"){{
            requirements(Category.crafting, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            squareSprite = false;
            size = 3;
            itemCapacity = 10;
            consumeItems(with(trsItems.carbonDust, 5, trsItems.quartzDust, 5));
            outputItem = new ItemStack(trsItems.carbonGlass, 2);
            consumePower(0.50f);

            drawer = new DrawMulti(new DrawDefault(), new RandomDrawGlowRegion(){{
                color = Color.valueOf("56bff1");
            }});
        }};
        crusher = new MultiCrafter("crusher"){{
            requirements(Category.crafting, with(Items.graphite, 12, Items.silicon, 8, Items.lead, 8));
            squareSprite = false;
            size = 3;
            itemCapacity = 10;
            drawer = new DrawMulti(new DrawRegion("-bottom"),new DrawPistons(){{
                sides = 1;
                angleOffset = -90f;
                sinMag = -4f;
                sinScl = 10f;
                lenOffset = 0.1f;
                sinOffset = 30f;

            }},new DrawPistons(){{
                suffix = "-piston0";
                sides = 1;
                angleOffset = -90f;
                sinMag = 4f;
                sinScl = 10f;
                lenOffset = 0.1f;
                sinOffset = 30f;
            }},new DrawDefault(),new DrawGlowRegion(){{
                color = trsItems.quartzDust.color;
                glowIntensity = 0.35f;
                glowScale = 10.5f;
            }});
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
            squareSprite = false;
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
            buildCostMultiplier = 100f;

        }};
        clinovalveRouter = new ItemLiquidRouter("clinovalve-router"){{
            requirements(Category.distribution, with(Items.copper, 3));
            buildCostMultiplier = 4f;
            liquidCapacity = 10f;
        }};
        clinovalveJunction = new ItemLiquidJunction("clinovalve-junction"){{
            requirements(Category.distribution, with(Items.copper, 2));
            speed = 10;
            capacity = 3;
            health = 30;
            buildCostMultiplier = 6f;
        }};
        clinovalveSorter = new ItemLiquidSorter("clinovalve-sorter"){{
            requirements(Category.distribution, with(Items.lead, 2, Items.copper, 2));
            buildCostMultiplier = 3f;
            liquidCapacity = 10f;
        }};
        clinovalveInvertedSorter = new ItemLiquidSorter("clinovalve-inverted-sorter"){{
            requirements(Category.distribution, with(Items.lead, 2, Items.copper, 2));
            buildCostMultiplier = 3f;
            invert = true;
            liquidCapacity = 10f;
        }};
        clinovalveDuctBridge = new ItemLiquidDuctBridge("clinovalve-duct-bridge"){{
            requirements(Category.distribution, with(Items.beryllium, 20));
            health = 90;
            speed = 4f;
            buildCostMultiplier = 2f;
            researchCostMultiplier = 0.3f;
        }};
        clinovalveOverflowGate = new ItemLiquidOverflowGate("clinovalve-overflow-gate"){{
            requirements(Category.distribution, with(Items.lead, 2, Items.copper, 4));
            buildCostMultiplier = 3f;
            liquidCapacity = 10f;
        }};

        clinovalveUnderflowGate = new ItemLiquidOverflowGate("clinovalve-underflow-gate"){{
            requirements(Category.distribution, with(Items.lead, 2, Items.copper, 4));
            buildCostMultiplier = 3f;
            invert = true;
            liquidCapacity = 10f;
        }};

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
        steelOre = new StaticWall("steel-ore");
        //drills
        hydraulicDrill = new Drill("hydraulic-drill"){{
            requirements(Category.production, with(Items.lead, 2, Items.copper, 2));
            tier = 2;
            drillTime = 600;
            size = 2;
            envEnabled ^= Env.space;
            researchCost = with(Items.copper, 10);

            consumeLiquid(Liquids.water, 0.05f).boost();
        }};
        deepDrill = new Drill("deep-drill"){{
            requirements(Category.production, with(Items.lead, 2, Items.copper, 2));
            tier = 3;
            drillTime = 400;
            size = 2;
            //mechanical drill doesn't work in space
            envEnabled ^= Env.space;
            researchCost = with(Items.copper, 10);

            consumeLiquid(Liquids.water, 0.05f).boost();
        }};
        clusterDrill = new ClusterDrill("cluster-drill"){{
            requirements(Category.production, with(Items.lead, 2, Items.copper, 2));
            drillTime = 281.25f;
            size = 3;
            tier = 4;
            consumeCoolant(0.05f);
        }};
        hui = new MultiBlockDrill("hui-drill"){{
            requirements(Category.production, with(Items.lead, 2, Items.copper, 4));
            size = 4;
            customShadow = true;
        }};
        huiPart = new Block("hui-part"){{
            requirements(Category.production, with(Items.lead, 2, Items.copper, 4));
            size = 2;
            customShadow = true;
        }};
        //walls

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

        //turrets
        splash = new TRSItemTurret("splash"){{
            requirements(Category.turret, with(Items.copper,1));
            isHeating = true;
            heating = 0.5f;
            size = 2;
            fraction = "Chronos";
            heatDamage = 1f;
            ammo(
                    trsItems.clinovalve,  new BasicBulletType(2.5f, 9){{
                        width = 7f;
                        height = 9f;
                        lifetime = 60f;
                        ammoMultiplier = 2;
                        frontColor = backColor = Color.valueOf("c52603");
                    }},
                    trsItems.carbonGlass, new BasicBulletType(3.5f, 12){{
                        frontColor = backColor = Color.valueOf("c52603");
                        width = 9f;
                        height = 12f;
                        reloadMultiplier =1.25f;
                        ammoMultiplier = 4;
                        lifetime = 60f;
                        fragBullets = 6;
                        fragBullet = new BasicBulletType(3f, 5){{
                            frontColor = backColor = Color.valueOf("c52603");
                            width = 5f;
                            height = 12f;
                            shrinkY = 1f;
                            lifetime = 20f;
                            despawnEffect = Fx.none;
                            collidesGround = true;
                        }};
                    }},
                    trsItems.carbon, new BasicBulletType(3f, 20){{
                        frontColor = backColor = Color.valueOf("c52603");
                        width = 7f;
                        height = 8f;
                        shrinkY = 0f;
                        homingPower = 0.08f;
                        splashDamageRadius = 20f;
                        splashDamage = 30f * 1.5f;
                        makeFire = true;
                        ammoMultiplier = 5f;
                        hitEffect = Fx.blastExplosion;
                        status = StatusEffects.burning;
                    }}
            );
            shoot = new ShootAlternate(3.5f);

            recoil = 0.5f;
            shootY = 6.5f;
            reload = 7f;
            range = 110;
            shootCone = 15f;
            ammoUseEffect = Fx.casing1;
            health = 250;
            inaccuracy = 2f;
            rotateSpeed = 10f;
            coolant = consumeCoolant(0.1f);

            limitRange();
        }};
        //power
        carbonBiomassReactor = new ConsumeGenerator("carbon-biomass-reactor"){{
            requirements(Category.turret, with(Items.copper,1));
            size = 5;

            powerProduction = 100f;
            itemDuration = 90f;
            consumeLiquid(trsLiquids.crystalWater, 0.1f);
            hasLiquids = true;
            consumeItems(with(trsItems.carbon, 2, trsItems.biomass,2));
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidRegion(trsLiquids.crystalWater),new DrawCultivator(){{
                radius = 2f;
                bubbles = 40;
                sides  =10;
                spread = 10f;
            }},new DrawDefault());
        }};

    }
}
