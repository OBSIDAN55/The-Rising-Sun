package trs.content;

import arc.graphics.Color;
import mindustry.type.Liquid;

public class trsLiquids {
    public static Liquid
    metan,argon;

    public static void load(){
        metan = new Liquid("metan",Color.valueOf("62392CFF")){{
           gas = true;
        }};
        argon = new Liquid("argon", Color.valueOf("263CAFFF")){{
           gas = true;
        }};
    }
}
