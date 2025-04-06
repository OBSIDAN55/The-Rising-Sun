package trs.type.distribution;

import arc.Core;
import mindustry.world.blocks.Autotiler;
import mindustry.world.blocks.distribution.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.meta.*;

import static mindustry.Vars.*;
import static mindustry.type.Liquid.animationFrames;

public class ItemLiquidDuct extends Conduit implements Autotiler {

    static final float rotatePad = 6, hpad = rotatePad / 2f / 4f;

    public float speed = 5f;
    public float maxPressure = 2.5f;
    public float pressureIntake = 0.05f;
    public boolean armored = false;
    public boolean hasPressure;
    public Color transparentColor = new Color(0.4f, 0.4f, 0.4f, 0.1f);

    public TextureRegion liquidr;
    public TextureRegion borderRegion;
    public TextureRegion BuildRegion;

    public @Nullable Block bridgeReplacement;

    public ItemLiquidDuct(String name) {
        super(name);
        group = BlockGroup.transportation;
        update = true;
        solid = false;
        hasItems = true;
        hasPressure = true;
        conveyorPlacement = true;
        unloadable = false;
        itemCapacity = 1;
        noUpdateDisabled = true;
        underBullets = true;
        rotate = true;
        noSideBlend = true;
        isDuct = true;
        priority = TargetPriority.transport;
        envEnabled = Env.space | Env.terrestrial | Env.underwater;
    }
    @Override
    public void load(){
        super.load();

        liquidr = liquidRegion;
        borderRegion = Core.atlas.find(name+"-border");
        rotateRegions = new TextureRegion[4][2][animationFrames];
        BuildRegion = Core.atlas.find("trs-DrawBuild"+this.size+"x"+this.size);

        if(renderer != null){
            float pad = rotatePad;
            var frames = renderer.getFluidFrames();

            for(int rot = 0; rot < 4; rot++){
                for(int fluid = 0; fluid < 2; fluid++){
                    for(int frame = 0; frame < animationFrames; frame++){
                        TextureRegion base = frames[fluid][frame];
                        TextureRegion result = new TextureRegion();
                        result.set(base);

                        if(rot == 0){
                            result.setX(result.getX() + pad);
                            result.setHeight(result.height - pad);
                        }else if(rot == 1){
                            result.setWidth(result.width - pad);
                            result.setHeight(result.height - pad);
                        }else if(rot == 2){
                            result.setWidth(result.width - pad);
                            result.setY(result.getY() + pad);
                        }else{
                            result.setX(result.getX() + pad);
                            result.setY(result.getY() + pad);
                        }

                        rotateRegions[rot][fluid][frame] = result;
                    }
                }
            }
        }

    }
    @Override
    public void setStats(){
        super.setStats();
        stats.add(Stat.itemsMoved, 60f / speed, StatUnit.itemsSecond);
    }

    @Override
    public void init(){
        super.init();

        if(bridgeReplacement == null || !(bridgeReplacement instanceof DuctBridge || bridgeReplacement instanceof ItemBridge)) bridgeReplacement = Blocks.ductBridge;


    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        int[] bits = getTiling(plan, list);

        if(bits == null) return;

        Draw.scl(bits[1], bits[2]);
        Draw.alpha(0.5f);
        Draw.rect(botRegions[bits[0]], plan.drawx(), plan.drawy(), plan.rotation * 90);
        Draw.color();
        Draw.rect(topRegions[bits[0]], plan.drawx(), plan.drawy(), plan.rotation * 90);
        Draw.scl();
    }

    @Override
    public boolean blends(Tile tile, int rotation, int otherx, int othery, int otherrot, Block otherblock){
        if(!armored){
            return (otherblock.outputsItems() || (lookingAt(tile, rotation, otherx, othery, otherblock) && otherblock.hasItems))
                    && lookingAtEither(tile, rotation, otherx, othery, otherrot, otherblock);
        }else{
            return (otherblock.outputsItems() && blendsArmored(tile, rotation, otherx, othery, otherrot, otherblock)) || (lookingAt(tile, rotation, otherx, othery, otherblock) && otherblock.hasItems);
        }
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{botRegions[0], topRegions[0]};
    }

    @Override
    public void handlePlacementLine(Seq<BuildPlan> plans){
        if(bridgeReplacement == null) return;
        if(bridgeReplacement instanceof ItemBridge bridge) Placement.calculateBridges(plans, bridge);
        if(bridgeReplacement instanceof DuctBridge bridge) Placement.calculateBridges(plans, bridge, false, b -> b instanceof Duct || b instanceof StackConveyor || b instanceof Conveyor);
    }
    public class ItemLiquidDuctBuild extends ConduitBuild {
        public float pressure;
        public float progress;
        public @Nullable Item current;
        public int recDir = 0;
        public int blendbits, xscl, yscl, blending;
        public @Nullable Building next;
        public @Nullable Building nextc;
        public float buildOffset;


        @Override
        public void draw(){
            /*buildOffset = Mathf.approachDelta(buildOffset,3f,0.02f);
                if (this.buildOffset != 3) {
                    Draw.z(Layer.blockUnder - 0.01f);
                    Draw.rect(BuildRegion, x - buildOffset, y - buildOffset);
                    Draw.rect(BuildRegion, x + buildOffset, y - buildOffset, 90);
                    Draw.rect(BuildRegion, x + buildOffset, y + buildOffset, 180);
                    Draw.rect(BuildRegion, x - buildOffset, y + buildOffset, 270);
                }*/
            Building l = left(), ri = right(), f = front(), b = back();
            float rotation = rotdeg();
            int r = this.rotation;

            //draw extra ducts facing this one for tiling purposes
            for(int i = 0; i < 4; i++){
                if((blending & (1 << i)) != 0){
                    int dir = r - i;
                    float rot = i == 0 ? rotation : (dir)*90;
                    drawAt(x + Geometry.d4x(dir) * tilesize*0.75f, y + Geometry.d4y(dir) * tilesize*0.75f, 0, rot, i != 0 ? SliceMode.bottom : SliceMode.top);
                }
            }

            //draw item
            if(current != null){
                Draw.z(Layer.blockUnder + 0.2f);
                Tmp.v1.set(Geometry.d4x(recDir) * tilesize / 2f, Geometry.d4y(recDir) * tilesize / 2f)
                        .lerp(Geometry.d4x(r) * tilesize / 2f, Geometry.d4y(r) * tilesize / 2f,
                                Mathf.clamp((progress + 1f) / 2f));

                Draw.rect(current.fullIcon, x + Tmp.v1.x, y + Tmp.v1.y, itemSize, itemSize);
            }

            Draw.scl(xscl, yscl);
            drawAt(x, y, blendbits, rotation, SliceMode.none);
            if (f==null) Draw.rect(borderRegion,x,y,rotdeg());
            if (b==null && l == null && ri == null) Draw.rect(borderRegion,x,y,rotdeg()-180);
            Draw.reset();
        }

        @Override
        public void payloadDraw(){
            Draw.rect(fullIcon, x, y);
        }

        protected void drawAt(float x, float y, int bits, float rotation, SliceMode slice){
            Draw.z(Layer.blockUnder);
            Draw.rect(sliced(botRegions[bits], slice), x, y, rotation);

            Draw.z(Layer.blockUnder +0.0001f);
            Draw.color(transparentColor);

            int offset = yscl == -1 ? 3 : 0;

            int frame = liquids.current().getAnimationFrame();
            int gas = liquids.current().gas ? 1 : 0;
            float ox = 0f, oy = 0f;
            int wrapRot = (int)(rotation + offset) % 4;

            //the drawing state machine sure was a great design choice with no downsides or hidden behavior!!!
            float xscl = Draw.xscl, yscl = Draw.yscl;
            Draw.scl(1f, 1f);
            Drawf.liquid(sliced(liquidr, slice), x + ox, y + oy, smoothLiquid, liquids.current().color.write(Tmp.c1).a(1f));
            Draw.scl(xscl, yscl);

            Draw.color();
            Draw.z(Layer.block-0.01f);

            Draw.rect(sliced(topRegions[bits], slice), x, y, rotation);
        }

        @Override
        public void updateTile() {
            @Nullable Building l = left(), ri = right(), f = front(), b = back();
            smoothLiquid = Mathf.lerpDelta(smoothLiquid, liquids.currentAmount() / liquidCapacity, 0.05f);
            progress += edelta() / speed * 2f;
            if (liquids.currentAmount() > 0.0001f && timer(timerFlow, 1) && (pressure>0 && pressure <= maxPressure)) {
                moveLiquidForward(leaks, liquids.current());
            }
            if (current != null && next != null) {
                if (progress >= (1f - 1f / speed) && moveForward(current)) {
                    items.remove(current, 1);
                    current = null;
                    progress %= (1f - 1f / speed);
                }
            } else {
                progress = 0;
            }
            if (current == null && items.total() > 0) {
                current = items.first();
            }
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            return current == null && items.total() == 0 &&
                    (armored ?
                            //armored acceptance
                            ((source.block.rotate && source.front() == this && source.block.hasItems && source.block.isDuct) ||
                                    Edges.getFacingEdge(source.tile(), tile).relativeTo(tile) == rotation) :
                            //standard acceptance - do not accept from front
                            !(source.block.rotate && next == source) && Edges.getFacingEdge(source.tile, tile) != null && Math.abs(Edges.getFacingEdge(source.tile, tile).relativeTo(tile.x, tile.y) - rotation) != 2
                    );
        }

        @Override
        public int removeStack(Item item, int amount){
            int removed = super.removeStack(item, amount);
            if(item == current) current = null;
            return removed;
        }

        @Override
        public void handleStack(Item item, int amount, Teamc source){
            super.handleStack(item, amount, source);
            current = item;
        }

        @Override
        public void handleItem(Building source, Item item){
            current = item;
            progress = -1f;
            recDir = relativeToEdge(source.tile);
            items.add(item, 1);
            noSleep();
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();

            int[] bits = buildBlending(tile, rotation, null, true);
            blendbits = bits[0];
            xscl = bits[1];
            yscl = bits[2];
            blending = bits[4];
            next = front();
            nextc = next;

            Building next = front(), prev = back();
            capped = next == null || next.team != team || !next.block.hasLiquids;
            backCapped = blendbits == 0 && (prev == null || prev.team != team || !prev.block.hasLiquids);
        }

        @Override
        public byte version(){
            return 1;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.b(recDir);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            if(revision >= 1){
                recDir = read.b();
            }
            current = items.first();
        }
    }
}
