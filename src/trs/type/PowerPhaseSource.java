package trs.type;

import mindustry.world.blocks.power.*;
import mindustry.world.meta.*;

public class PowerPhaseSource extends PowerNode{
    public float powerPhaseProduction = 10000f;

    public PowerPhaseSource(String name){
        super(name);
        maxNodes = 100;
        outputsPower = true;
        consumesPower = false;
        drawDisabled = true;
        //TODO maybe don't?
        envEnabled = Env.any;
    }

    public class PowerSourceBuild extends PowerNodeBuild{
        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();
            if(!allowUpdate()){
                enabled = false;
            }
        }

       //@Override
       //        public float getPowerProduction(){
       //        return enabled ? powerProduction : 0f;
       //    }
    }

}