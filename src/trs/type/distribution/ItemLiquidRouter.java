package trs.type.distribution;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Nullable;
import arc.util.Tmp;
import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.gen.BlockUnitc;
import mindustry.gen.Building;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.type.Item;
import mindustry.world.Tile;
import mindustry.world.blocks.ControlBlock;
import mindustry.world.blocks.distribution.Router;
import mindustry.world.blocks.liquid.LiquidRouter;
import mindustry.world.meta.BlockGroup;

public class ItemLiquidRouter extends LiquidRouter {
    public float speed = 8f;

    public TextureRegion bottomRegion;
    public TextureRegion bottomCenterRegion;
    public TextureRegion borderRegion;
    public TextureRegion liquidRegion;
    public TextureRegion liquidCenterRegion;

    public ItemLiquidRouter(String name) {
        super(name);
        solid = false;
        underBullets = true;
        update = true;
        hasItems = true;
        itemCapacity = 1;
        group = BlockGroup.transportation;
        unloadable = false;
        noUpdateDisabled = true;
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{Core.atlas.find(name+"-icon")};
    }

    @Override
    public void load(){
        super.load();
        bottomCenterRegion = Core.atlas.find(name+"-bottom-center");
        bottomRegion = Core.atlas.find(name+"-bottom");
        borderRegion = Core.atlas.find(name+"-border");
        liquidRegion = Core.atlas.find(name+"-liquid");
        liquidCenterRegion = Core.atlas.find(name+"-liquid-center");
    }

    public class ItemLiquidRouterBuild extends LiquidRouterBuild implements ControlBlock {
        public Item lastItem;
        public Tile lastInput;
        public float time;
        public @Nullable BlockUnitc unit;

        @Override
        public Unit unit(){
            if(unit == null){
                unit = (BlockUnitc) UnitTypes.block.create(team);
                unit.tile(this);
            }
            return (Unit)unit;
        }

        @Override
        public boolean canControl(){
            return size == 1;
        }

        @Override
        public boolean shouldAutoTarget(){
            return false;
        }

        @Override
        public void updateTile(){
            dumpLiquid(liquids.current());
            if(lastItem == null && items.any()){
                lastItem = items.first();
            }

            if(lastItem != null){
                time += 1f / speed * delta();
                Building target = getTileTarget(lastItem, lastInput, false);

                if(target != null && (time >= 1f || !(target.block instanceof Router || target.block.instantTransfer))){
                    getTileTarget(lastItem, lastInput, true);
                    target.handleItem(this, lastItem);
                    items.remove(lastItem, 1);
                    lastItem = null;
                }
            }
        }
        @Override
        public void draw(){
            Building l = left(), r = right(), f = front(), b = back();
            Draw.rect(bottomCenterRegion, x, y);
            if(liquids.currentAmount() > 0.001f) Drawf.liquid(liquidCenterRegion,x, y, liquids.currentAmount(), liquids.current().color.write(Tmp.c1).a(1f));
            if (l != null){
                Draw.rect(bottomRegion, x, y, rotdeg()+90);
                if(liquids.currentAmount() > 0.001f) Drawf.liquid(liquidRegion,x, y, liquids.currentAmount(), liquids.current().color.write(Tmp.c1).a(1f),rotdeg()+90);
                Draw.rect(borderRegion,x,y,rotdeg()+90);
            }
            if (r != null){
                Draw.rect(bottomRegion, x, y, rotdeg()-90);
                if(liquids.currentAmount() > 0.001f) Drawf.liquid(liquidRegion,x, y, liquids.currentAmount(), liquids.current().color.write(Tmp.c1).a(1f),rotdeg()-90);
                Draw.rect(borderRegion,x,y,rotdeg()-90);
            }
            if (f != null){
                Draw.rect(bottomRegion, x, y, rotdeg());
                if(liquids.currentAmount() > 0.001f) Drawf.liquid(liquidRegion,x, y, liquids.currentAmount(), liquids.current().color.write(Tmp.c1).a(1f),rotdeg());
                Draw.rect(borderRegion,x,y, rotdeg());
            }
            if (b != null){
                Draw.rect(bottomRegion, x, y, rotdeg()-180);
                if(liquids.currentAmount() > 0.001f) Drawf.liquid(liquidRegion,x, y, liquids.currentAmount(), liquids.current().color.write(Tmp.c1).a(1f),rotdeg()-180);
                Draw.rect(borderRegion,x,y,rotdeg()-180);

            }
            Draw.rect(region,x,y);
        }

        @Override
        public int acceptStack(Item item, int amount, Teamc source){
            return 0;
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            return team == source.team && lastItem == null && items.total() == 0;
        }

        @Override
        public void handleItem(Building source, Item item){
            items.add(item, 1);
            lastItem = item;
            time = 0f;
            lastInput = source.tileOn();
        }

        @Override
        public int removeStack(Item item, int amount){
            int result = super.removeStack(item, amount);
            if(result != 0 && item == lastItem){
                lastItem = null;
            }
            return result;
        }

        public Building getTileTarget(Item item, Tile from, boolean set){
            if(unit != null && isControlled()){
                unit.health(health);
                unit.ammo(unit.type().ammoCapacity * (items.total() > 0 ? 1f : 0f));
                unit.team(team);
                unit.set(x, y);

                int angle = Mathf.mod((int)((angleTo(unit.aimX(), unit.aimY()) + 45) / 90), 4);

                if(unit.isShooting()){
                    Building other = nearby(rotation = angle);
                    if(other != null && other.acceptItem(this, item)){
                        return other;
                    }
                }

                return null;
            }

            int counter = rotation;
            for(int i = 0; i < proximity.size; i++){
                Building other = proximity.get((i + counter) % proximity.size);
                if(set) rotation = ((byte)((rotation + 1) % proximity.size));
                if(other.tile == from && from.block() == Blocks.overflowGate) continue;
                if(other.acceptItem(this, item)){
                    return other;
                }
            }
            return null;
        }
    }
}
