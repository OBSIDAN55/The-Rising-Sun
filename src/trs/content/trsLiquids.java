package trs.content;

import arc.graphics.Color;
import mindustry.type.Liquid;

public class trsLiquids {
    public static Liquid
    metan,argon;

    public static void load(){
        metan = new Liquid("metan",Color.valueOf("62392CFF")){{
		   	gas = true;
            barColor = Color.valueOf("ffffffff");
            explosiveness = 0.85f;
            flammability = 0.78f;	   
        }};
        argon = new Liquid("argon", Color.valueOf("263CAFFF")){{
           gas = true;
            barColor = Color.valueOf("ffffffff");
            explosiveness = 0.4f;
            flammability = 0.65f;
        }};
    }
}
