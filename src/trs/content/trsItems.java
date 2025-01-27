package trs.content;

import arc.graphics.Color;
import mindustry.type.Item;

public class trsItems {
    public static Item
            exacrim, tin, clinovalve, rubidium, barium, zinc, chrome, steel, biomass, carbon,carbonDust,quartz,quartzDust, carbonGlass;
    public static void load(){
        tin = new Item("tin"){{
            hardness = 1;
            cost = 0.7f;
            radioactivity = 0f;
            explosiveness = 0f;
            charge = 0;
        }};
        clinovalve = new Item("clinovalve", Color.valueOf("C48372FF")){{
            hardness = 1;
            cost = 0.85f;
            radioactivity = 0f;
            explosiveness = 0f;
            charge = 0.1f;
        }};
        quartz = new Item("quartz"){{
            hardness = 1;
            cost = 0.5f;
            radioactivity = 0f;
            explosiveness = 0f;
            charge = 0f;
        }};
        quartzDust = new Item("quartz-dust"){{
            hardness = 1;
            cost = 0.35f;
            radioactivity = 0f;
            explosiveness = 0f;
            charge = 0f;
        }};
        biomass = new Item("biomass"){{
            hardness = 1f;
            cost = 0,55f;
            radioactivity = 0f;
            explosiveness = 0.3f;
            charge = 0f;
            flammability = 0.85f;
        }};
        carbon = new Item("carbon"){{
            hardness = 1;
            cost = 0.75f;
            radioactivity = 0f;
            explosiveness = 0.5f;
            charge = 0f;
            flammability = 1f;
        }};
        carbonDust = new Item("carbon-dust"){{
            hardness = 1;
            cost = 0.65f;
            radioactivity = 0f;
            explosiveness = 0.9f;
            charge = 0f;
            flammability = 1.6f;
        }};
        zinc = new Item("zinc"){{
            hardness = 2;
            cost = 1f;
            radioactivity = 0f;
            explosiveness = 0.2f;
            charge = 0.5f;
        }};
        rubidium = new Item("rubidium", Color.valueOf("AC676BFF")){{
            hardness = 2;
            cost = 1.2f;
            radioactivity = 0f;
            explosiveness = 0f;
            charge = 0f;
        }};
        barium = new Item("barium"){{
            hardness = 3;
            cost = 1.7f;
            radioactivity = 0f;
            explosiveness = 0f;
            charge = 0f;
        }};
        steel = new Item("steel"){{
            hardness = 3;
            cost = 1.4f;
            radioactivity = 0f;
            explosiveness = 0f;
            charge = 0f;
        }};
        chrome = new Item("chrome"){{
            hardness = 3;
            cost = 1.3f;
            radioactivity = 0f;
            explosiveness = 0f;
            charge = 0f;
        }};
        exacrim = new Item("exacrim"){{
            hardness = 3;
            cost = 1.6f;
            radioactivity = 0.15f;
            explosiveness = 3f;
            charge = 4.5f;
        }};

    }
}
