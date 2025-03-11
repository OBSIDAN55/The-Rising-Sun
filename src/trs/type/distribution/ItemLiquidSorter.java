package trs.type.distribution;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureRegion;
import arc.util.Tmp;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.type.Liquid;
import mindustry.world.blocks.distribution.Sorter;
import mindustry.world.meta.BlockGroup;

import static mindustry.world.blocks.liquid.LiquidBlock.drawTiledFrames;


public class ItemLiquidSorter extends Sorter {
    public TextureRegion bottomRegion;
    public TextureRegion borderRegion;
    public TextureRegion cross;
    public TextureRegion liquidRegion;


    public ItemLiquidSorter(String name) {
        super(name);
        hasLiquids = true;
        outputsLiquid = true;
        update = true;
        destructible = true;
        underBullets = true;
        instantTransfer = true;
        group = BlockGroup.transportation;
        configurable = true;
        unloadable = false;
        saveConfig = true;
        clearOnDoubleTap = true;
    }
    @Override
    public void load(){
        super.load();
        bottomRegion = Core.atlas.find(name+"-bottom");
        borderRegion = Core.atlas.find(name+"-border");
        cross = Core.atlas.find(name+"-cross");
        liquidRegion = Core.atlas.find(name+"-liquid");
    }
    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{Core.atlas.find(name+"-icon")};
    }

    public class ItemLiquidSorterBuild extends SorterBuild {

        @Override
                public void updateTile() {
                dumpLiquid(liquids.current());
            }
        @Override
        public void draw(){
            Building l = left(), r = right(), f = front(), b = back();

            if(sortItem == null){
                Draw.rect(cross, x, y);
            }else{
                Draw.color(sortItem.color);
                Fill.square(x, y, 1.6f);
                Draw.color();
            }
            if (l != null){
                Draw.rect(bottomRegion, x, y, rotation+90);
                if(liquids.currentAmount() > 0.001f) Drawf.liquid(liquidRegion,x, y, liquids.currentAmount(), liquids.current().color.write(Tmp.c1).a(1f),rotation+90);
                Draw.rect(borderRegion,x,y,rotation+90);
            }
            if (r != null){
                Draw.rect(bottomRegion, x, y, rotation-90);
                if(liquids.currentAmount() > 0.001f) Drawf.liquid(liquidRegion,x, y, liquids.currentAmount(), liquids.current().color.write(Tmp.c1).a(1f),rotation-90);
                Draw.rect(borderRegion,x,y,rotation-90);
            }
            if (f != null){
                Draw.rect(bottomRegion, x, y, rotation);
                if(liquids.currentAmount() > 0.001f) Drawf.liquid(liquidRegion,x, y, liquids.currentAmount(), liquids.current().color.write(Tmp.c1).a(1f),rotation);
                Draw.rect(borderRegion,x,y, rotation);
            }
            if (b != null){
                Draw.rect(bottomRegion, x, y, rotation-180);
                if(liquids.currentAmount() > 0.001f) Drawf.liquid(liquidRegion,x, y, liquids.currentAmount(), liquids.current().color.write(Tmp.c1).a(1f),rotation-180);
                Draw.rect(borderRegion,x,y,rotation-180);
            }
            Draw.rect(region,x,y);
        }
        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            return (liquids.current() == liquid || liquids.currentAmount() < 0.2f);
        }
    }
}
