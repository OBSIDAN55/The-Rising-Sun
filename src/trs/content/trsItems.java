package trs.content;

import arc.graphics.Color;
import mindustry.type.Item;

public class trsItems {
    public static Item
            exacrim, tin, clinovalve, rubidium, barium, zinc, chrome, steel, biomass, carbon,carbonDust,quartz,quartzDust, carbonGlass;
    public static void load(){
        tin = new Item("tin", Color.valueOf("54554CFF")){{
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
        quartz = new Item("quartz", Color.valueOf("948BA5FF")){{
            hardness = 1;
            cost = 0.5f;
            radioactivity = 0f;
            explosiveness = 0f;
            charge = 0f;
        }};
        quartzDust = new Item("quartz-dust", Color.valueOf("948BA5FF")){{
            hardness = 1;
            cost = 0.35f;
            radioactivity = 0f;
            explosiveness = 0f;
            charge = 0f;
        }};
        biomass = new Item("biomass", Color.valueOf("66608CFF")){{
            hardness = 1;
            cost = 0.55f;
            radioactivity = 0f;
            explosiveness = 0.3f;
            charge = 0f;
            flammability = 0.85f;
        }};
        carbon = new Item("carbon", Color.valueOf("3B3B42FF")){{
            hardness = 1;
            cost = 0.75f;
            radioactivity = 0f;
            explosiveness = 0.5f;
            charge = 0f;
            flammability = 1f;
        }};
        carbonDust = new Item("carbon-dust", Color.valueOf("3B3B42FF")){{
            hardness = 1;
            cost = 0.65f;
            radioactivity = 0f;
            explosiveness = 0.9f;
            charge = 0f;
            flammability = 1.6f;
        }};
        carbonGlass = new Item("carbon-glass"){{

        }};
        zinc = new Item("zinc", Color.valueOf("456264FF")){{
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
        barium = new Item("barium", Color.valueOf("DFC48AFF")){{
            hardness = 3;
            cost = 1.7f;
            radioactivity = 0f;
            explosiveness = 0f;
            charge = 0f;
        }};
        steel = new Item("steel", Color.valueOf("565666FF")){{
            hardness = 3;
            cost = 1.4f;
            radioactivity = 0f;
            explosiveness = 0f;
            charge = 0f;
        }};
        chrome = new Item("chrome", Color.valueOf("AAA8BCFF")){{
            hardness = 3;
            cost = 1.3f;
            radioactivity = 0f;
            explosiveness = 0f;
            charge = 0f;
        }};
        exacrim = new Item("exacrim", Color.valueOf("74C1E8FF")){{
            hardness = 3;
            cost = 1.6f;
            radioactivity = 0.15f;
            explosiveness = 3f;
            charge = 4.5f;
        }};

    }
}
