package trs.type;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.EnumSet;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Log;
import arc.util.Structs;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.environment.OverlayFloor;
import mindustry.world.consumers.Consume;
import mindustry.world.meta.*;

import java.lang.reflect.Constructor;

import static mindustry.Vars.content;
import static mindustry.Vars.tilesize;

public class Wire extends OverlayFloor {

    public Color emptyLightColor = Color.valueOf("f8c266");
    public Color fullLightColor = Color.valueOf("fb9567");

    public float buildTime = -1f;

    public Wire(String name) {
        super(name);
        inEditor = false;
        breakable = true;
        buildVisibility = BuildVisibility.shown;
        variants = 0;
        solid = false;
        hasPower = true;
        group = BlockGroup.power;
        outputsPower = true;
        consumesPower = false;
        canOverdrive = false;
        flags = EnumSet.of(BlockFlag.battery);
        envEnabled |= Env.space;
        destructible = true;
        update = true;
        floating = true;
    }

    @Override
    public void init(){
        //disable standard shadow
        if(customShadow){
            hasShadow = false;
        }

        if(underBullets){
            priority = -2f;
        }

        if(fogRadius > 0){
            flags = flags.with(BlockFlag.hasFogRadius);
        }

        //initialize default health based on size
        if(health == -1){
            boolean round = false;
            if(scaledHealth < 0){
                scaledHealth = 40;

                float scaling = 1f;
                for(var stack : requirements){
                    scaling += stack.item.healthScaling;
                }

                scaledHealth *= scaling;
                round = true;
            }

            health = round ?
                    Mathf.round(size * size * scaledHealth, 5) :
                    (int)(size * size * scaledHealth);
        }

        clipSize = Math.max(clipSize, size * tilesize);



        if(hasLiquids && drawLiquidLight){
            emitLight = true;

        }

        if(emitLight){

        }

        if(group == BlockGroup.transportation || category == Category.distribution){
            acceptsItems = true;
        }

        offset = ((size + 1) % 2) * tilesize / 2f;
        sizeOffset = -((size - 1) / 2);

        if(requirements.length > 0 && buildTime < 0){
            buildTime = 0f;
            for(ItemStack stack : requirements){
                buildTime += stack.amount * stack.item.cost;
            }
        }

        if(buildTime < 0){
            buildTime = 20f;
        }

        buildTime *= buildCostMultiplier;

        consumers = consumeBuilder.toArray(Consume.class);
        optionalConsumers = consumeBuilder.select(consume -> consume.optional && !consume.ignore()).toArray(Consume.class);
        nonOptionalConsumers = consumeBuilder.select(consume -> !consume.optional && !consume.ignore()).toArray(Consume.class);
        updateConsumers = consumeBuilder.select(consume -> consume.update && !consume.ignore()).toArray(Consume.class);
        hasConsumers = consumers.length > 0;
        itemFilter = new boolean[content.items().size];
        liquidFilter = new boolean[content.liquids().size];

        for(Consume cons : consumers){
            cons.apply(this);
        }

        setBars();

        stats.useCategories = true;

        //TODO check for double power consumption

        if(!logicConfigurable){
            configurations.each((key, val) -> {
                if(UnlockableContent.class.isAssignableFrom(key)){
                    logicConfigurable = true;
                }
            });
        }

        if(!outputsPower && consPower != null && consPower.buffered){
            Log.warn("Consumer using buffered power: @. Disabling buffered power.", name);
            consPower.buffered = false;
        }

        if(buildVisibility == BuildVisibility.sandboxOnly){
            hideDetails = false;
        }
    }

    protected void initBuilding(){
        //attempt to find the first declared class and use it as the entity type
        try{
            Class<?> current = getClass();

            if(current.isAnonymousClass()){
                current = current.getSuperclass();
            }

            subclass = current;

            while(buildType == null && Block.class.isAssignableFrom(current)){
                //first class that is subclass of Building
                Class<?> type = Structs.find(current.getDeclaredClasses(), t -> Building.class.isAssignableFrom(t) && !t.isInterface());
                if(type != null){
                    //these are inner classes, so they have an implicit parameter generated
                    Constructor<? extends Building> cons = (Constructor<? extends Building>)type.getDeclaredConstructor(type.getDeclaringClass());
                    buildType = () -> {
                        try{
                            return cons.newInstance(this);
                        }catch(Exception e){
                            throw new RuntimeException(e);
                        }
                    };
                }

                //scan through every superclass looking for it
                current = current.getSuperclass();
            }

        }catch(Throwable ignored){
        }

        if(buildType == null){
            //assign default value
            buildType = Building::create;
        }
    }



    @Override
    public void load(){
        super.load();
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){

    }

    public class WireBuild extends Building {


        @Override
        public void drawLight(){
            super.drawLight();
        }

        @Override
        public float warmup(){
            return power.status;
        }

        @Override
        public void overwrote(Seq<Building> previous){
            for(Building other : previous){
                if(other.power != null && other.block.consPower != null && other.block.consPower.buffered){
                    float amount = other.block.consPower.capacity * other.power.status;
                    power.status = Mathf.clamp(power.status + amount / consPower.capacity);
                }
            }
        }

        @Override
        public BlockStatus status(){
            if(Mathf.equal(power.status, 0f, 0.001f)) return BlockStatus.noInput;
            if(Mathf.equal(power.status, 1f, 0.001f)) return BlockStatus.active;
            return BlockStatus.noOutput;
        }
    }
}
