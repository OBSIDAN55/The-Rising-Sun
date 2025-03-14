package trs.type;

import arc.Core;
import arc.math.Mathf;
import arc.util.Nullable;
import arc.util.Time;
import mindustry.content.Items;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumePower;
import trs.type.consumers.ConsumePowerPhase;

public class OverclockGenericCrafter extends GenericCrafter {

    public @Nullable ConsumePowerPhase consPowerPhase;

    public float updateEffectSpread = 4f;
    public float speedBoost = 5.5f;
    public float speedBoostPhase = 0f;

    public float baseCraftTime = 60f;

    public OverclockGenericCrafter(String name) {
        super(name);
    }
    @Override
    public void setBars(){
        super.setBars();
        addBar("boost", (OverclockGenericCrafterBuild entity) -> new Bar(() -> Core.bundle.format("bar.boost", Mathf.round(Math.max((entity.realBoost() * 100 - 100), 0))), () -> Pal.accent, () -> entity.realBoost() / (true ? speedBoost + speedBoostPhase : speedBoost)));
    }

    public void consumePowerPhase(float powerPerTick){
        consume(new ConsumePowerPhase(powerPerTick, 0.0f, false));
    }
    public <T extends Consume> T consume(T consume){
        if(consume instanceof ConsumePower){
            //there can only be one power consumer
            consumeBuilder.removeAll(b -> b instanceof ConsumePower);
            consPower = (ConsumePower)consume;
        }
        if(consume instanceof ConsumePowerPhase){
            //there can only be one power consumer
            consumeBuilder.removeAll(b -> b instanceof ConsumePowerPhase);
            consPowerPhase = (ConsumePowerPhase)consume;
        }
        consumeBuilder.add(consume);
        return consume;
    }

    public class OverclockGenericCrafterBuild extends GenericCrafterBuild {
        public float phaseHeat;
        public float craftTime = baseCraftTime;

        @Override
        public void updateTile() {

            if(efficiency > 0){

                progress += getProgressIncrease(craftTime);
                warmup = Mathf.approachDelta(warmup, warmupTarget(), warmupSpeed);

                //continuously output based on efficiency
                if(outputLiquids != null){
                    float inc = getProgressIncrease(1f);
                    for(var output : outputLiquids){
                        handleLiquid(this, output.liquid, Math.min(output.amount * inc, liquidCapacity - liquids.get(output.liquid)));
                    }
                }

                if(wasVisible && Mathf.chanceDelta(updateEffectChance)){
                    updateEffect.at(x + Mathf.range(size * updateEffectSpread), y + Mathf.range(size * updateEffectSpread));
                }
            }else{
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }

            totalProgress += warmup * Time.delta;

            if(progress >= 1f) {
                craft();
            }
            dumpOutputs();
            if (items.has(Items.thorium, 3)){
               this.craftTime/=2f;
            }else {
                this.craftTime = baseCraftTime;
            }

        }
        public void craft(){
            consume();

            if(outputItems != null){
                for(var output : outputItems){
                    for(int i = 0; i < output.amount; i++){
                        offload(output.item);
                    }
                }
            }
            if(wasVisible){
                craftEffect.at(x, y);
            }
            progress %= 1f;
        }

        public float realBoost(){
            return speedBoost * efficiency;
        }
    }
}
