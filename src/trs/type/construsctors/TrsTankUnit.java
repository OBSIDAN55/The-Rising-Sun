package trs.type.construsctors;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import mindustry.gen.TankUnit;
import mindustry.graphics.Drawf;

public class TrsTankUnit extends TankUnit {

    int segmentCount = 4;

    @Override
    public void update() {


        float hp = healthf();
        float maxHp = maxHealth();
        float segHp = maxHp/4;
        Drawf.square(this.x()+2,this.y(), hp, Color.red);
        if (segHp*3 < hp){
            Drawf.circles(this.x(),this.y(), hp);
        }
    }


    public static TrsTankUnit create() {
        return new TrsTankUnit();
    }
}
