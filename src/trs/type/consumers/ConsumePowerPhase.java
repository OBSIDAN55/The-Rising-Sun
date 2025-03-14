package trs.type.consumers;

import mindustry.gen.Building;
import mindustry.world.consumers.Consume;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.Stats;
import trs.type.OverclockGenericCrafter;
import trs.type.Vars;

public class ConsumePowerPhase extends Consume {
    public float usage;
    /** The maximum power capacity in power units. */
    public float capacity;
    /** True if the module can store power. */
    public boolean buffered;

    public ConsumePowerPhase(float usage, float capacity, boolean buffered){
        this.usage = usage;
        this.capacity = capacity;
        this.buffered = buffered;
    }

    public ConsumePowerPhase(){
        this(0f, 0f, false);
    }

    public void apply(OverclockGenericCrafter block){
        block.hasPower = true;
        block.consPowerPhase = this;
    }

    @Override
    public boolean ignore(){
        return buffered;
    }

    @Override
    public float efficiency(Building build){
        return build.power.status;
    }

    @Override
    public void display(Stats stats){
        if(buffered){
            stats.add(Stat.powerCapacity, capacity, StatUnit.none);
        }else if(usage > 0f){
            stats.add(Vars.powerUsePhase, usage * 60f, StatUnit.powerSecond);
        }
    }

    /**
     * Retrieves the amount of power which is requested for the given block and entity.
     * @param entity The entity which contains the power module.
     * @return The amount of power which is requested per tick.
     */
    public float requestedPower(Building entity){
        return buffered ?
                (1f - entity.power.status) * capacity :
                usage * (entity.shouldConsume() ? 1f : 0f);
    }
}
