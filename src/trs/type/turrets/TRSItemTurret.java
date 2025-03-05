package trs.type.turrets;

import arc.util.Nullable;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import trs.type.Vars;

import java.util.Objects;

public class TRSItemTurret extends ItemTurret {

    public String fraction;
    @Nullable public String fractionDescription;

    public TRSItemTurret(String name) {
        super(name);
    }
    @Override
    public void setStats() {
        super.setStats();
        if (Objects.equals(fraction, "Chronos")) {
            this.stats.add(Vars.fractionName, Vars.ChronosName);
            this.stats.add(Vars.fractionDescription, Vars.ChronosDesc);
        } else if (Objects.equals(fraction, "Arch")) {
            this.stats.add(Vars.fractionName, Vars.ArchName);
            this.stats.add(Vars.fractionDescription, Vars.ArchDesc);
        } else if (Objects.equals(fraction, "Akronix")) {
            this.stats.add(Vars.fractionName, Vars.AkronixName);
            this.stats.add(Vars.fractionDescription, Vars.AkronixDesc);
        } else if (Objects.equals(fraction, "Phoenix")) {
            this.stats.add(Vars.fractionName, Vars.PhoenixName);
            this.stats.add(Vars.fractionDescription, Vars.PhoenixDesc);
        } else {
            this.stats.add(Vars.fractionName, fraction);
            this.stats.add(Vars.fractionDescription, fractionDescription);
        }
    }
}

