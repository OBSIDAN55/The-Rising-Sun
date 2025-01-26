package trs.type;

import arc.func.Boolf;
import mindustry.gen.Building;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.consumers.ConsumePower;

public class BoostItemDuct extends Duct {
    public float boost = 1.5f;
    public float speed = 4f;

    public BoostItemDuct(String name) {
        super(name);
        hasPower = true;
        outputsPower = true;
        consumesPower = true;
    }

    @Override
    public <T extends Building> ConsumePower consumePowerCond(float usage, Boolf<T> cons) {
        return super.consumePowerCond(usage, cons);
    }

    public class BoostItemDuctBuild extends DuctBuild{
        @Override
        public void updateTile() {
            super.updateTile();
            //if(){

            //}
        }
    }
}
