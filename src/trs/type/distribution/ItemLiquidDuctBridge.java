package trs.type.distribution;

import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.util.Nullable;
import mindustry.entities.TargetPriority;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.world.ItemBuffer;
import mindustry.world.Tile;
import mindustry.world.blocks.distribution.BufferedItemBridge;
import mindustry.world.meta.BlockGroup;

import static mindustry.Vars.world;

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
