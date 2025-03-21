package trs.type.defense.turrets;

import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.logic.LExecutor;
import mindustry.ui.Bar;
import mindustry.world.blocks.defense.ForceProjector;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

import static mindustry.Vars.renderer;

public class CountForceProjector extends ForceProjector {

    public int damageCount = 100, realDamageCount = damageCount;
    public float refreshInterval = 60f;

    public CountForceProjector(String name) {
        super(name);

    }
    @Override
    public void setBars(){
        super.setBars();
        removeBar("shield");
        addBar("shield", (ForceBuild entity) -> new Bar("stat.shieldhealth", Pal.accent, () -> (float) realDamageCount /damageCount).blink(Color.white));
    }
    @Override
    public void setStats(){
        super.setStats();
        stats.remove(Stat.shieldHealth);
        stats.add(Stat.shieldHealth, damageCount, StatUnit.none);

    }
    public class CountForceProjectorBuild extends ForceBuild {
        public float refresh = Mathf.random(0,refreshInterval);

        @Override
        public void updateTile(){
            super.updateTile();
            refresh += Time.delta;
            if (realDamageCount <= 0){
                broken = true;
                shieldBreakEffect.at(x, y, realRadius(), team.color);
                if(team != Vars.state.rules.defaultTeam){
                    Events.fire(EventType.Trigger.forceProjectorBreak);
                }
            }
        }

        public void deflectBullets(){
            float realRadius = realRadius();

            if(realRadius > 0 && !broken){
                paramEntity = this;
                paramEffect = absorbEffect;
                Groups.bullet.intersect(x - realRadius, y - realRadius, realRadius * 2f, realRadius * 2f, shieldConsumer);
                realDamageCount -= 1;
                refreshCount();
            }
        }
        public void refreshCount(){
            if (realDamageCount != damageCount && refresh >= refreshInterval){
                realDamageCount = damageCount;
            }
        }
    }
}
