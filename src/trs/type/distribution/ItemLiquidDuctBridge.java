package trs.type.distribution;

import arc.util.Nullable;
import mindustry.gen.Building;
import mindustry.world.ItemBuffer;
import mindustry.world.blocks.distribution.BufferedItemBridge;


public class ItemLiquidDuctBridge extends BufferedItemBridge {

    public float speed = 40f;
    public int bufferCapacity = 50;

    public float transportTime = 2f;

    public @Nullable ItemBridgeBuild lastBuild;

    public ItemLiquidDuctBridge(String name) {
        super(name);
        hasLiquids = true;
        hasItems = true;
    }
    public class ItemLiquidDuctBridgeBuild extends BufferedItemBridgeBuild {
        ItemBuffer buffer = new ItemBuffer(bufferCapacity);

        @Override
        public void updateTransport(Building other){
            if(warmup >= 0.25f){
                moved |= moveLiquid(other, liquids.current()) > 0.05f;
            }
        }
        @Override
        public void doDump(){
            dumpLiquid(liquids.current(), 1f);
        }
    }

}
