package trs;
import mindustry.mod.Mod;
import trs.content.*;

public class trsMod extends Mod {
    public trsMod(){
    }
    public void loadContent(){
        trsLiquids.load();
        trsItems.load();
        trsWeathers.load();
        trsBlocks.load();
        Planets.load();
    }
}
