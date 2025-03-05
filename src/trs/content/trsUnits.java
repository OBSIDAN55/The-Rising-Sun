package trs.content;

import arc.graphics.Color;
import arc.math.geom.Rect;
import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.entities.abilities.ShieldArcAbility;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.part.HaloPart;
import mindustry.entities.part.RegionPart;
import mindustry.gen.*;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.unit.TankUnitType;
import trs.type.BuildTurretRegenGeneratorCoreBlock;

public class trsUnits {
    public static UnitType
            disaster,massacre,apocalypse,unitType;
    public static void load() {
        massacre = new TankUnitType("massacre"){{
            this.constructor = TankUnit::create;
            outlineColor = Color.valueOf("332C2CFF");
            hitSize = 46f;
            treadPullOffset = 1;
            speed = 0.48f;
            health = 22000;
            armor = 26f;
            crushDamage = 25f / 5f;
            rotateSpeed = 0.8f;
            float xo = 128f/2f, yo = 128f/2f;
            treadRects = new Rect[]{new Rect(12-xo, 40-yo , 29, 31),new Rect(15-xo, 117-yo , 28, 31)};

            weapons.add(new Weapon(name+"-weapon"){{
                reload = 100f;
                minWarmup = 0.9f;
                layerOffset = 0.1f;
                rotate = true;
                mirror = false;
                x = 0f;
                y = 0f;
                parts.add(new RegionPart("-blade"){{
                    progress = PartProgress.warmup;
                    mirror = true;
                    moveX = -3f/4;
                    moveY = -3f/4;
                    moveRot = 15f;
                    layerOffset = -0.01f;
                    outline = false;

                }});
            }});
        }};
        disaster = new TankUnitType("disaster"){{
            this.constructor = TankUnit::create;
            outlineColor = Color.valueOf("332C2CFF");
            hitSize = 46f;
            treadPullOffset = 1;
            speed = 0.48f;
            health = 22000;
            armor = 26f;
            crushDamage = 25f / 5f;
            rotateSpeed = 0.8f;
            float xo = 128f/2f, yo = 128f/2f;
            treadRects = new Rect[]{new Rect(12-xo, 40-yo , 29, 31),new Rect(15-xo, 117-yo , 28, 31)};


            weapons.add(new Weapon(name+"-weapon"){{
                            reload = 100f;
                            minWarmup = 0.9f;
                            layerOffset = 0.1f;
                            rotate = true;
                            mirror = false;
                            x = 0f;
                            y = 0f;
                            parts.addAll(new RegionPart("-part1"){{
                                progress = PartProgress.warmup;
                                mirror = true;
                                moveX = -5f/4;
                                moveY = -7f/4;
                                moveRot = 15f;
                                layerOffset = -0.01f;
                                outline = true;
                            }},new RegionPart("-part2"){{
                                progress = PartProgress.warmup;
                                mirror = true;
                                moveX = -25f/4;
                                moveY = -15f/4;
                                moveRot = -15f;
                                layerOffset = -0.02f;
                                outline = true;
                            }});
            }}


            );


            parts.addAll(
                    new RegionPart("-part0"),
                    new RegionPart("-part1"),
                    new RegionPart("-part2"),
                    new RegionPart("-part3")
            );
        }};
        apocalypse = new TankUnitType("apocalypse"){{
            this.constructor = TankUnit::create;
            outlineColor = Color.valueOf("332C2CFF");
            hitSize = 30f;
            treadPullOffset = 1;
            speed = 0.48f;
            health = 22000;
            armor = 26f;
            crushDamage = 25f / 5f;
            rotateSpeed = 0.8f;

            abilities.add(new ShieldArcAbility(){{
                region = "tecta-shield";
                radius = 36f;
                angle = 82f;
                regen = 0.6f;
                cooldown = 60f * 8f;
                max = 2000f;
                y = -15f;
                width = 6f;
                whenShooting = true;
            }}
);

            float xo = 170/2f, yo = 180f/2f-9f;
            treadRects = new Rect[]{new Rect(22-xo, 22-yo , 32, 35),new Rect(16-xo, 68-yo , 32, 35), new Rect(23-xo, 115-yo,32,35)};

            weapons.add( new Weapon(name+"-weapon"){{
                reload = 100f;
                layerOffset = 0.1f;
                mirror = false;
                x = 0f;
                y = 0f;
                shootY = 5f;
                recoil = 2f;
                minWarmup = 0.9f;
                rotate = true;
                rotateSpeed = 0.8f;

                shootSound = Sounds.railgun;


                bullet = new BasicBulletType(60,20){{
                    pierce = true;
                    pierceCap = 3;
                    drag=  0.01f;
                    smokeEffect = Fx.shootBigSmoke;
                    shootEffect = Fx.shootBigColor;
                    width = 10;
                    height = 60;
                    lifetime = 10;
                    hitSize = 4;
                    trailWidth = 4f;
                    trailLength = 13;
                    despawnEffect = Fx.hitBulletColor;
                    trailColor = backColor = frontColor = Color.cyan;
                }};
                for(int i = 1; i <= 3; i++){
                    int fi = i;
                    parts.add(new RegionPart("-blades-"+i){{
                        progress = PartProgress.warmup.delay((3 - fi) * 0.3f).blend(PartProgress.reload, 0.3f);
                        heatColor = new Color(1f, 0.1f, 0.1f);
                        mirror = true;
                        under = true;
                        moveY = -10f * fi/4f;
                        moveX = -5f/4f;
                        layerOffset = -0.002f;

                    }});
                }
                for(int i = 1; i <= 3; i++){
                    int fi = i;
                    parts.add(new RegionPart("-charge-"+i){{
                        progress = PartProgress.warmup.delay((3 - fi) * 0.3f);
                        heatColor = new Color(1f, 0.1f, 0.1f);
                        mirror = true;
                        under = true;
                        moveX = -35f/4f+fi/1.3f;
                        moveY = 13f/4f;
                        moveRot = 30f;
                        layerOffset = -0.002f;

                    }});
                }
                parts.addAll(new RegionPart("-top"){{
                    mirror = false;
                    progress = PartProgress.life;
                    layerOffset = 0.00001f;
                }},new HaloPart(){{
                    tri = false;
                    sides = 4;
                    shapes = 8;
                    shapeRotation = 45f;

                    haloRotateSpeed = 7f;
                    hollow = true;
                    mirror = false;
                    progress = PartProgress.life;
                    color = Color.cyan;
                    radius = 0.4f;
                    haloRadius = 3.5f;
                    y = -7f;
                    stroke = 1f;
                }},new HaloPart(){{
                    tri = false;
                    sides = 4;
                    shapes = 8;
                    shapeRotation = 45f;

                    haloRotateSpeed = 7f;
                    hollow = true;
                    mirror = false;
                    progress = PartProgress.life;
                    color = Color.cyan;
                    radius = 0.4f;
                    haloRadius = 4f;
                    y = -7f;
                    stroke = 1f;
                }});
            }});
        }};
    }
}
