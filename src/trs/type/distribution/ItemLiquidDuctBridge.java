package trs.type.distribution;

import static mindustry.Vars.tilesize;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.geom.Geometry;
import arc.util.Nullable;
import arc.util.Tmp;
import mindustry.core.Renderer;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.world.blocks.distribution.DirectionBridge;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;


public class ItemLiquidDuctBridge extends DirectionBridge {

    public final int timerFlow = timers++;

    public float speed = 5f;
    public float liquidPadding = 1f;

    public TextureRegion bottomCenterRegion;
    public TextureRegion liquidCenterRegion;
    public TextureRegion bottomRegion;
    public TextureRegion borderRegion;
    public TextureRegion liquidRegion;




    public ItemLiquidDuctBridge(String name) {
        super(name);

        outputsLiquid = true;
        group = BlockGroup.liquids;
        canOverdrive = false;
        liquidCapacity = 20f;
        hasLiquids = true;
        itemCapacity = 4;
        hasItems = true;
        underBullets = true;
        isDuct = true;
    }

    @Override
    public void load(){
        super.load();
        bottomCenterRegion = Core.atlas.find(name+"-bottom-center");
        liquidCenterRegion = Core.atlas.find(name+"-liquid-center");
        bottomRegion = Core.atlas.find(name+"-bottom");
        borderRegion = Core.atlas.find(name+"-border");
        liquidRegion = Core.atlas.find(name+"-liquid");

    }

    @Override
    public void setStats(){
        super.setStats();
        stats.add(Stat.itemsMoved, 60f / speed, StatUnit.itemsSecond);
    }

    public class ItemLiquidDuctBridgeBuild extends DirectionBridgeBuild {

        public float progress = 0f;




        @Override
        public void draw(){
            //Draw.rect(bottomRegion, x, y);

            Draw.rect(bottomCenterRegion, x, y);
            if(liquids.currentAmount() > 0.001f) Drawf.liquid(liquidCenterRegion,x, y, liquids.currentAmount(), liquids.current().color.write(Tmp.c1).a(1f));
            Draw.rect(block.region, x, y);

            Draw.rect(dirRegion, x, y, rotdeg());
            var link = findLink();
            if(link != null){
                Draw.z(Layer.power - 1);
                trsDrawBridge(rotation, x, y, link.x, link.y, Tmp.c1.set(liquids.current().color).a(liquids.currentAmount() / liquidCapacity * liquids.current().color.a));
            }

            @Nullable Liquid liquid1,liquid2,liquid3,liquid4;
            Building l = left(), r = right(), f = front(), b = back();
            float rot = this.rotdeg();
            if (l != null){
                Draw.rect(bottomRegion, x, y, rot+90);
                if(l.liquids != null && l.liquids.currentAmount() > 0.001f){
                    liquid1 = l.liquids.current();
                    Drawf.liquid(liquidRegion,x, y, l.liquids.currentAmount(), liquid1.color.write(Tmp.c1).a(1f),rot+90);
                }
                Draw.rect(borderRegion,x,y,rot+90);
            }
            if (r != null){
                Draw.rect(bottomRegion, x, y, rot-90);
                if(r.liquids != null && r.liquids.currentAmount()> 0.001f){
                    liquid2 = r.liquids.current();
                    Drawf.liquid(liquidRegion,x, y, r.liquids.currentAmount(), liquid2.color.write(Tmp.c1).a(1f),rot-90);
                }
                Draw.rect(borderRegion,x,y,rot-90);
            }
            if (f != null){
                Draw.rect(bottomRegion, x, y, rot);
                if(f.liquids != null && f.liquids.currentAmount()> 0.001f){
                    liquid3 = f.liquids.current();
                    Drawf.liquid(liquidRegion,x, y, f.liquids.currentAmount(), liquid3.color.write(Tmp.c1).a(1f),rot);
                }
                Draw.rect(borderRegion,x,y, rot);
            }
            if (b != null){
                Draw.rect(bottomRegion, x, y, rot-180);
                if(b.liquids != null && b.liquids.currentAmount()> 0.001f) {
                    liquid4 = b.liquids.current();
                    Drawf.liquid(liquidRegion,x, y, b.liquids.currentAmount(), liquid4.color.write(Tmp.c1).a(1f),rot-180);
                }
                Draw.rect(borderRegion,x,y,rot-180);

            }
            Draw.rect(region,x,y);

        }

        @Override
        public void updateTile(){

            var link = lastLink = findLink();
            if(link != null){
                moveLiquid(link, liquids.current());
                link.occupied[rotation % 4] = this;
                if(items.any() && link.items.total() < link.block.itemCapacity){
                    progress += edelta();
                    while(progress > speed){
                        Item next = items.take();
                        if(next != null && link.items.total() < link.block.itemCapacity){
                            link.handleItem(this, next);
                        }
                        progress -= speed;
                    }
                }
            }
            if(link == null && items.any()){
                Item next = items.first();
                if(moveForward(next)){
                    items.remove(next, 1);
                }
            }
            if(link == null){
                if(liquids.currentAmount() > 0.0001f && timer(timerFlow, 1)){
                    moveLiquidForward(false, liquids.current());
                }
            }
            for(int i = 0; i < 4; i++){
                if(occupied[i] == null || occupied[i].rotation != i || !occupied[i].isValid() || occupied[i].lastLink != this){
                    occupied[i] = null;
                }
            }
        }

        public void trsDrawBridge(int rotation, float x1, float y1, float x2, float y2, @Nullable Color liquidColor){
            Draw.alpha(Renderer.bridgeOpacity);
            float
                    angle = Angles.angle(x1, y1, x2, y2),
                    cx = (x1 + x2)/2f,
                    cy = (y1 + y2)/2f,
                    len = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)) - size * tilesize;

            Draw.rect(bridgeRegion, cx, cy, len, bridgeRegion.height * bridgeRegion.scl(), angle);
            if(liquidColor != null){
                Draw.color(liquidColor, liquidColor.a * Renderer.bridgeOpacity);
                Draw.rect(bridgeLiquidRegion, cx, cy, len, bridgeLiquidRegion.height * bridgeLiquidRegion.scl(), angle);
                Draw.color();
                Draw.alpha(Renderer.bridgeOpacity);
            }
            if(bridgeBotRegion.found()){
                Draw.color(0.4f, 0.4f, 0.4f, 0.4f * Renderer.bridgeOpacity);
                Draw.rect(bridgeBotRegion, cx, cy, len, bridgeBotRegion.height * bridgeBotRegion.scl(), angle);
                Draw.reset();
            }
            Draw.alpha(Renderer.bridgeOpacity);

            for(float i = 6f; i <= len + size * tilesize - 5f; i += 5f){
                Draw.rect(arrowRegion, x1 + Geometry.d4x(rotation) * i, y1 + Geometry.d4y(rotation) * i, angle);
            }

            Draw.reset();
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            //only accept if there's an output point.
            if(findLink() == null) return false;

            int rel = this.relativeToEdge(source.tile);
            return items.total() < itemCapacity && rel != rotation && occupied[(rel + 2) % 4] == null;
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            var link = findLink();
            //only accept if there's an output point, or it comes from a link
            if(link == null && !(source instanceof DirectionBridgeBuild b && b.findLink() == this)) return false;

            int rel = this.relativeToEdge(source.tile);

            return
                    hasLiquids && team == source.team &&
                            (liquids.current() == liquid || liquids.get(liquids.current()) < 0.2f) && rel != rotation &&
                            (occupied[(rel + 2) % 4] == null || occupied[(rel + 2) % 4] == source);
        }
    }
}
