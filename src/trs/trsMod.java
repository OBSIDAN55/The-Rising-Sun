package trs;
import mindustry.mod.Mod;
import trs.content.*;
import trs.type.Vars;

public class trsMod extends Mod {
    public trsMod(){
    }
    public void loadContent(){
        Vars.load();
        trsStatusEffects.load();
        trsLiquids.load();
        trsItems.load();
        trsWeathers.load();
        trsUnits.load();
        trsEnv.load();
        trsBlocks.load();
        Planets.load();
    }
}
