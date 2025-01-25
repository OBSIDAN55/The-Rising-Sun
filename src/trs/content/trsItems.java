package trs.content;

import mindustry.type.Item;

public class trsItems {
    public static Item
            exacrim, tin, clinovalve, rubidium, barium, zinc, chrome, steel, biomass, carbon,carbonDust,quartz,quartzDust;
    public static void load(){
        exacrim = new Item("exacrim"){{
            hardness = 3;
            cost = 1.6f;
            radioactivity = 0.15f;
            explosiveness = 3f;
            charge = 4.5f;
        }};
    }
}
