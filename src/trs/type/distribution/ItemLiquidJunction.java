package trs.type.distribution;

import static mindustry.Vars.content;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.util.Nullable;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.BufferItem;
import mindustry.gen.Building;
import mindustry.gen.Teamc;
import mindustry.graphics.Drawf;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.world.DirectionalItemBuffer;
import mindustry.world.blocks.liquid.LiquidJunction;
import mindustry.world.meta.BlockGroup;

public class ItemLiquidJunction extends LiquidJunction {

    public float speed = 26; //frames taken to go through this junction
    public int capacity = 6;

    public TextureRegion bottomRegion;
    public TextureRegion borderRegion;
    public TextureRegion liquidRegion;

    public ItemLiquidJunction(String name) {
        super(name);
        update = true;
        solid = false;
        underBullets = true;
        group = BlockGroup.transportation;
        unloadable = false;
        floating = true;
        noUpdateDisabled = true;
    }

    @Override
    public void load(){
        super.load();
        bottomRegion = Core.atlas.find(name+"-bottom");
        borderRegion = Core.atlas.find(name+"-border");
        liquidRegion = Core.atlas.find(name+"-liquid");
    }
    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{Core.atlas.find(name+"-icon")};
    }
    @Override
    public boolean outputsItems(){
        return true;
    }
    public class ItemLiquidJunctionBuild extends LiquidJunctionBuild {
        public DirectionalItemBuffer buffer = new DirectionalItemBuffer(capacity);

        @Override
        public int acceptStack(Item item, int amount, Teamc source){
            return 0;
        }

        @Override
        public void updateTile(){

            for(int i = 0; i < 4; i++){
                if(buffer.indexes[i] > 0){
                    if(buffer.indexes[i] > capacity) buffer.indexes[i] = capacity;
                    long l = buffer.buffers[i][0];
                    float time = BufferItem.time(l);

                    if(Time.time >= time + speed / timeScale || Time.time < time){

                        Item item = content.item(BufferItem.item(l));
                        Building dest = nearby(i);

                        //skip blocks that don't want the item, keep waiting until they do
                        if(item == null || dest == null || !dest.acceptItem(this, item) || dest.team != team){
                            continue;
                        }

                        dest.handleItem(this, item);
                        System.arraycopy(buffer.buffers[i], 1, buffer.buffers[i], 0, buffer.indexes[i] - 1);
                        buffer.indexes[i] --;
                    }
                }
            }
        }
        @Override
        public void draw(){
            @Nullable Liquid liquid1,liquid2,liquid3,liquid4;
            Building l = left(), r = right(), f = front(), b = back();
            if (l != null){
                Draw.rect(bottomRegion, x, y, rotation+90);
                if(l.liquids != null && l.liquids.currentAmount() > 0.001f){
                    liquid1 = l.liquids.current();
                    Drawf.liquid(liquidRegion,x, y, l.liquids.currentAmount(), liquid1.color.write(Tmp.c1).a(1f),rotation+90);
                }
                Draw.rect(borderRegion,x,y,rotation+90);
            }
            if (r != null){
                Draw.rect(bottomRegion, x, y, rotation-90);
                if(r.liquids != null && r.liquids.currentAmount()> 0.001f){
                    liquid2 = r.liquids.current();
                    Drawf.liquid(liquidRegion,x, y, r.liquids.currentAmount(), liquid2.color.write(Tmp.c1).a(1f),rotation-90);
                }
                Draw.rect(borderRegion,x,y,rotation-90);
            }
            if (f != null){
                Draw.rect(bottomRegion, x, y, rotation);
                if(f.liquids != null && f.liquids.currentAmount()> 0.001f){
                    liquid3 = f.liquids.current();
                    Drawf.liquid(liquidRegion,x, y, f.liquids.currentAmount(), liquid3.color.write(Tmp.c1).a(1f),rotation);
                }
                Draw.rect(borderRegion,x,y, rotation);
            }
            if (b != null){
                Draw.rect(bottomRegion, x, y, rotation-180);
                if(b.liquids != null && b.liquids.currentAmount()> 0.001f) {
                    liquid4 = b.liquids.current();
                    Drawf.liquid(liquidRegion,x, y, b.liquids.currentAmount(), liquid4.color.write(Tmp.c1).a(1f),rotation-180);
                }
                Draw.rect(borderRegion,x,y,rotation-180);

            }
            Draw.rect(region,x,y);
        }

        @Override
        public void handleItem(Building source, Item item){
            int relative = source.relativeTo(tile);
            buffer.accept(relative, item);
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            int relative = source.relativeTo(tile);

            if(relative == -1 || !buffer.accepts(relative)) return false;
            Building to = nearby(relative);
            return to != null && to.team == team;
        }

        @Override
        public byte version(){
            return 1;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            buffer.write(write);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            buffer.read(read);
        }
    }

}
