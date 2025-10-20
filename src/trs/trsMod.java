package trs;

import mindustry.mod.Mod;
import trs.content.*;
import trs.type.TrsBulletTypes;
import trs.type.Vars;

public class trsMod extends Mod {
    public trsMod(){}
    public void loadContent(){
        mindustry.Vars.maxTextureSize = 2048*10;
        mindustry.Vars.maxSchematicSize = 13000;
        Vars.load();
        Sounds.load();
        trsStatusEffects.load();
        trsLiquids.load();
        trsItems.load();
        trsWeathers.load();
        TrsBulletTypes.load();
        trsUnits.load();
        trsEnv.load();
        trsBlocks.load();
        Planets.load();
        trsSectorPresets.load();
        FulgeraTechTree.load();
    }
}
