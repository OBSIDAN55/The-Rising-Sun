package trs;
import mindustry.mod.Mod;
import trs.content.*;

public class trsMod extends Mod {
    public trsMod(){
    }
    public void loadContent(){
        trsWeathers.load();
        trsBlocks.load();
    }
}
