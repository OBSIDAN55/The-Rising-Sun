package trs.type;

import arc.math.Mathf;
import arc.struct.EnumSet;
import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.gen.Building;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.world.Edges;
import mindustry.world.Tile;
import mindustry.world.blocks.liquid.LiquidRouter;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;

public class ItemLiquidContainer extends LiquidRouter {
    public boolean coreMerge = true;

    public ItemLiquidContainer(String name) {
        super(name);
        hasItems = true;
        hasLiquids = true;
        solid = true;
        update = false;
        destructible = true;
        separateItemCapacity = true;
        group = BlockGroup.transportation;
        flags = EnumSet.of(BlockFlag.storage);
        allowResupply = true;
        envEnabled = Env.any;
    }
    @Override
    public boolean outputsItems(){
        return false;
    }
    public static void incinerateEffect(Building self, Building source){
        if(Mathf.chance(0.3)){
            Tile edge = Edges.getFacingEdge(source, self);
            Tile edge2 = Edges.getFacingEdge(self, source);
            if(edge != null && edge2 != null && self.wasVisible){
                Fx.coreBurn.at((edge.worldx() + edge2.worldx())/2f, (edge.worldy() + edge2.worldy())/2f);
            }
        }
    }
    public class ItemLiquidContainerBuild extends LiquidRouterBuild {
        public @Nullable Building linkedCore;
        @Override
        public boolean acceptItem(Building source, Item item){
            return linkedCore != null ? linkedCore.acceptItem(source, item) : items.get(item) < getMaximumAccepted(item);
        }

        @Override
        public void handleItem(Building source, Item item){
            if(linkedCore != null){
                if(linkedCore.items.get(item) >= ((CoreBlock.CoreBuild)linkedCore).storageCapacity){
                    incinerateEffect(this, source);
                }
                ((CoreBlock.CoreBuild)linkedCore).noEffect = true;
                linkedCore.handleItem(source, item);
            }else{
                super.handleItem(source, item);
            }
        }

        @Override
        public void itemTaken(Item item){
            if(linkedCore != null){
                linkedCore.itemTaken(item);
            }
        }

        @Override
        public int removeStack(Item item, int amount){
            int result = super.removeStack(item, amount);

            if(linkedCore != null && team == Vars.state.rules.defaultTeam && Vars.state.isCampaign()){
                Vars.state.rules.sector.info.handleCoreItem(item, -result);
            }

            return result;
        }

        @Override
        public int getMaximumAccepted(Item item){
            return linkedCore != null ? linkedCore.getMaximumAccepted(item) : itemCapacity;
        }

        @Override
        public int explosionItemCap(){
            //when linked to a core, containers/vaults are made significantly less explosive.
            return linkedCore != null ? Math.min(itemCapacity/60, 6) : itemCapacity;
        }

        @Override
        public void drawSelect(){
            if(linkedCore != null){
                linkedCore.drawSelect();
            }
        }

        @Override
        public double sense(LAccess sensor){
            if(sensor == LAccess.itemCapacity && linkedCore != null) return linkedCore.sense(sensor);
            return super.sense(sensor);
        }

        @Override
        public void overwrote(Seq<Building> previous){
            //only add prev items when core is not linked
            if(linkedCore == null){
                for(Building other : previous){
                    if(other.items != null && other.items != items){
                        items.add(other.items);
                    }
                }

                items.each((i, a) -> items.set(i, Math.min(a, itemCapacity)));
            }
        }

        @Override
        public boolean canPickup(){
            return linkedCore == null;
        }
    }
}
