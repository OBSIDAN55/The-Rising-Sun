package trs;
import mindustry.mod.Mod;
import trs.content.*;
import trs.type.Vars;

public class trsMod extends Mod {
    public trsMod(){
    }
    public void loadContent(){
        Vars.load();
        trsLiquids.load();
        trsItems.load();
        trsWeathers.load();
        trsUnits.load();
        trsBlocks.load();
        Planets.load();
    }
}
