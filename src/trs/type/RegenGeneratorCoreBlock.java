package trs.type;

import arc.Core;
import arc.audio.Sound;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.*;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Puddles;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.logic.Ranged;
import mindustry.type.Liquid;
import mindustry.ui.Bar;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class RegenGeneratorCoreBlock extends CoreBlock{

    public float powerProduction = 10f;
    public Stat generationType = Stat.basePowerGeneration;
    public DrawBlock drawer = new DrawDefault();

    public final int timerUse = timers++;
    public Color baseColor = Color.valueOf("84f491");
    public Color phaseColor = baseColor;
    public float reload = 250f;
    public float range = 60f;
    public float healPercent = 12f;
    public float phaseBoost = 12f;
    public float phaseRangeBoost = 50f;
    public float useTime = 400f;

    public RegenGeneratorCoreBlock(String name) {
        super(name);
        consumesPower = false;
        outputsPower = true;
        hasPower = true;
        sync = true;

        solid = true;
        update = true;
        group = BlockGroup.power;
        hasItems = true;
        emitLight = true;
        lightRadius = 50f;
        suppressable = true;
        envEnabled |= Env.space;
    }
    @Override
    public TextureRegion[] icons(){
        return drawer.finalIcons(this);
    }

    @Override
    public void load(){
        super.load();
        drawer.load(this);
    }

    @Override
    public void setStats(){
        stats.timePeriod = useTime;
        super.setStats();
        stats.add(generationType, powerProduction * 60.0f, StatUnit.powerSecond);
        stats.add(Stat.repairTime, (int)(100f / healPercent * reload / 60f), StatUnit.seconds);
        stats.add(Stat.range, range / tilesize, StatUnit.blocks);

        if(findConsumer(c -> c instanceof ConsumeItems) instanceof ConsumeItems cons){
            stats.remove(Stat.booster);
            stats.add(Stat.booster, StatValues.itemBoosters(
                            "{0}" + StatUnit.timesSpeed.localized(),
                            stats.timePeriod, (phaseBoost + healPercent) / healPercent, phaseRangeBoost,
                            cons.items, this::consumesItem)
            );
        }
    }

    @Override
    public void setBars(){
        super.setBars();

        if(hasPower && outputsPower){
            addBar("power", (RegenGeneratorCoreBlockBuild entity) -> new Bar(() ->
                    Core.bundle.format("bar.poweroutput",
                            Strings.fixed(entity.getPowerProduction() * 60 * entity.timeScale(), 1)),
                    () -> Pal.powerBar,
                    () -> entity.productionEfficiency));
        }
    }
    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);

        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, range*2, baseColor);

        indexer.eachBlock(player.team(), x * tilesize + offset, y * tilesize + offset, range*2, other -> true, other -> Drawf.selected(other, Tmp.c1.set(baseColor).a(Mathf.absin(4f, 1f))));
    }


    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        drawer.drawPlan(this, plan, list);
    }

    public class RegenGeneratorCoreBlockBuild extends CoreBuild implements Ranged{
        public float heat, charge = Mathf.random(reload), phaseHeat, smoothEfficiency;
        public float generateTime;
        public float productionEfficiency = 1f;

        @Override
                public void updateTile(){
                boolean canHeal = !checkSuppression();

                smoothEfficiency = Mathf.lerpDelta(smoothEfficiency, efficiency, 0.08f);
                heat = Mathf.lerpDelta(heat, efficiency > 0 && canHeal ? 1f : 0f, 0.08f);
                charge += heat * delta();

                phaseHeat = Mathf.lerpDelta(phaseHeat, optionalEfficiency, 0.1f);

                if(optionalEfficiency > 0 && timer(timerUse, useTime) && canHeal){
                        consume();
                    }

                if(charge >= reload && canHeal){
                        float realRange = range + phaseHeat * phaseRangeBoost;
                        charge = 0f;

                        indexer.eachBlock(this, realRange, b -> b.damaged() && !b.isHealSuppressed(), other -> {
                                other.heal(other.maxHealth() * (healPercent + phaseHeat * phaseBoost) / 100f * efficiency);
                                other.recentlyHealed();
                                Fx.healBlockFull.at(other.x, other.y, other.block.size, baseColor, other.block);
                            });
                    }
        }
        @Override
        public void drawSelect(){
            float realRange = range*2;
            indexer.eachBlock(this, realRange, other -> true, other -> Drawf.selected(other, Tmp.c1.set(baseColor).a(Mathf.absin(4f, 1f))));
            Drawf.dashCircle(x, y, realRange, baseColor);
        }
        @Override
        public float range(){
            return range;
        }

        @Override
        public void draw() {
            super.draw();
            drawer.draw(this);
        }

        @Override
        public float warmup() {
            return enabled ? productionEfficiency : 10f;
        }

        @Override
        public void onDestroyed(){
            super.onDestroyed();
        }

        @Override
        public void drawLight() {
            super.drawLight();
            drawer.drawLight(this);
        }

        @Override
        public float ambientVolume() {
            return Mathf.clamp(productionEfficiency);
        }

        @Override
        public float getPowerProduction() {
            return enabled ? powerProduction * productionEfficiency : 10f;
        }

        @Override
        public byte version() {
            return 1;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(productionEfficiency);
            write.f(generateTime);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            productionEfficiency = read.f();
            if (revision >= 1) {
                generateTime = read.f();
            }
        }
    }
}
