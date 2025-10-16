package trs.type.defense.turrets;

import static mindustry.Vars.tilesize;
import static mindustry.graphics.Drawf.circles;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Nullable;
import arc.util.Time;
import java.util.HashMap;
import java.util.Map;
import mindustry.entities.Units;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import trs.type.Vars;

public class TRSItemTurret extends ItemTurret {

    static final float refreshInterval = 6f;

    public float baseReload = reload;
    public float heating = 1f;
    public float heatRange = 100f;
    public float heatDamage = 1f;
    public boolean isHeating = false;
    public boolean reloadWhileCharging = true;

    public TextureRegion heatRegion;

    public String fraction;
    @Nullable public String fractionDescription;

    private static final Map<String, String> FRACTION_NAMES = new HashMap<>();

    static {
        FRACTION_NAMES.put("Chronos", Vars.ChronosName);
        FRACTION_NAMES.put("Arch", Vars.ArchName);
        FRACTION_NAMES.put("Akronix", Vars.AkronixName);
        FRACTION_NAMES.put("Phoenix", Vars.PhoenixName);
    }

    public TRSItemTurret(String name) {
        super(name);
    }
    @Override
    public void load(){
        super.load();
        heatRegion = Core.atlas.find(name+"-heat");
    }
    @Override
    public void setBars(){
        super.setBars();
        if(isHeating) addBar("heat", (TRSItemTurretBuild entity) -> new Bar("bar.heat", Pal.lightOrange, () -> entity.turretHeat));
    }
    @Override
    public void setStats() {
        super.setStats();

        stats.remove(Stat.reload);
        stats.add(Stat.reload, 60f / (baseReload + (!reloadWhileCharging ? shoot.firstShotDelay : 0f)) * shoot.shots, StatUnit.perSecond);
        if (isHeating) {
            stats.add(Vars.heatRange, heatRange/tilesize, StatUnit.blocks);
            stats.add(Vars.heatDamage, heatDamage, StatUnit.perSecond);
        }
        String name = FRACTION_NAMES.getOrDefault(fraction, fraction);
        this.stats.add(Vars.fractionName, name);
    }
    public class TRSItemTurretBuild extends ItemTurretBuild{
        public float turretHeat;
        public float refresh = Mathf.random(refreshInterval);
        public Seq<Unit> targets = new Seq<>();

        @Override
        public void updateTile(){
            refresh +=Time.delta;
            super.updateTile();
            if (isHeating && this.isShooting() && turretHeat <1f){
                turretHeat +=(heating/100f);
                reload=baseReload+turretHeat*baseReload/2;
            }else if (turretHeat >0) turretHeat -=0.001f;
            if (refresh >= refreshInterval && isHeating) {
                targets.clear();
                refresh = 0f;
                Units.nearbyEnemies(team, x, y, heatRange * turretHeat, u -> {
                    if (u != null) {
                        targets.add(u);
                        for (var target : targets) {
                            if (target.isValid()) {
                                target.damage(heatDamage * turretHeat);
                            }
                        }
                    }
                });
            }
        }
        @Override
        public void draw(){
            super.draw();
            if(isHeating && turretHeat>0.01f) circles(x,y,heatRange*turretHeat, Pal.lightOrange);
        }
    }
}
