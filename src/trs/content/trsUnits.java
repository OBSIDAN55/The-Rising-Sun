package trs.content;

import arc.graphics.Color;
import arc.math.geom.Rect;
import mindustry.entities.part.RegionPart;
import mindustry.gen.TankUnit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.unit.TankUnitType;

public class trsUnits {
    public static UnitType
            disaster,massacre;
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
    }
}
