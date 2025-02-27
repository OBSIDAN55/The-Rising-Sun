package trs.type.entities;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import mindustry.gen.TankUnit;


public class TRSTankUnit extends TankUnit {

    public float health() {
        return health;
    }
    public int details = 4;
    public TextureRegion region = Core.atlas.find("trs-disaster-part0");
    public void load(){
        TextureRegion region = Core.atlas.find("trs-disaster-part0");
    }
    @Override
    public void update() {
        //if (maxHealth == health()){
        Draw.rect(region, x, y, rotation - 90);
        Draw.reset();
        //}
    }
}
