package trs.type;

import arc.Core;
import arc.graphics.g2d.TextureRegion;

public class DamagePart {

    public final String name;
    public TextureRegion part;
    public String suffix = "-part";

    public DamagePart(String name) {
        this.name = name;
    }
    public void load() {
        part = Core.atlas.find("trs-trs-disaster-part0");
    }
}
