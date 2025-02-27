package trs.type;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import mindustry.gen.Unit;
import mindustry.graphics.MultiPacker;
import mindustry.type.UnitType;
import trs.type.entities.TRSTankUnit;

public class TRSTankUnitType extends UnitType {
    public TextureRegion part;

    public TRSTankUnitType(String name) {
        super(name);
        constructor = TRSTankUnit::new;
    }
    @Override
    public void load(){
        super.load();
        part = Core.atlas.find("trs-disaster-part0");
    }
    //@Override
    //        public void draw(Unit unit){
    //        super.draw(unit);
    //        if(TRSTankUnit.health() == health) {
    //                Draw.rect(part, unit.x, unit.y, unit.rotation - 90);
    //                Draw.reset();
    //            }
    //    }
    @Override
    public void createIcons(MultiPacker packer){

    }
}
