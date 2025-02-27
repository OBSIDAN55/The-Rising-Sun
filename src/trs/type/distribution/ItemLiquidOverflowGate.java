package trs.type.distribution;

import mindustry.gen.Building;
import mindustry.type.Liquid;
import mindustry.world.blocks.distribution.OverflowGate;

public class ItemLiquidOverflowGate extends OverflowGate {

    public ItemLiquidOverflowGate(String name) {
        super(name);
        hasLiquids = true;
        outputsLiquid = true;
    }
    public class ItemLiquidOverflowGateBuild extends OverflowGateBuild {
        @Override
        public void updateTile() {
            dumpLiquid(liquids.current());
        }
        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            return (liquids.current() == liquid || liquids.currentAmount() < 0.2f);
        }
    }
}
