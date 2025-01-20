package trs.content;

import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;

import static mindustry.type.ItemStack.with;

public class trsBlocks {
    public static Block
    a;

    public static void load(){
        a = new Wall("a"){{
            requirements(Category.crafting, with(Items.copper, 15));
        }};
    }
}
