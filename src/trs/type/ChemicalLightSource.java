package trs.type;

import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.input.Placement;
import mindustry.logic.LAccess;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.meta.Env;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.ui;

public class ChemicalLightSource extends Block {
    public float brightness = 0.9f;
    public float radius = 200f;
    public Color sourceLightColor = Color.valueOf("ffffff");

    public ChemicalLightSource(String name){
        super(name);
        hasPower = false;
        update = true;
        configurable = false;
        saveConfig = true;
        envEnabled |= Env.space;
        swapDiagonalPlacement = true;

        config(Integer.class, (ChemicalLightSourceBuild tile, Integer value) -> tile.color = value);
    }

    @Override
    public void init(){
        lightRadius = radius*2.5f;
        clipSize = Math.max(clipSize, lightRadius * 3f);
        emitLight = true;

        super.init();
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);

        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, radius * 0.75f, Pal.placing);
    }

    @Override
    public void changePlacementPath(Seq<Point2> points, int rotation){
        var placeRadius2 = Mathf.pow(radius * 0.7f / tilesize, 2f) * 3;
        Placement.calculateNodes(points, this, rotation, (point, other) -> point.dst2(other) <= placeRadius2);
    }

    public class ChemicalLightSourceBuild extends Building {
        public int color = Pal.accent.rgba();
        public float smoothTime = 1f;

        @Override
        public void draw(){
            super.draw();
            Draw.blend(Blending.additive);
            Draw.color(Tmp.c1.set(color), efficiency * 0.3f);
            Draw.color();
            Draw.blend();
        }

        @Override
        public void updateTile(){
            smoothTime = Mathf.lerpDelta(smoothTime, timeScale, 0.1f);
        }
        @Override
        public void drawLight(){
            Drawf.light(x, y, lightRadius * Math.min(smoothTime, 2f), sourceLightColor, brightness * efficiency);
        }

        @Override
        public Integer config(){
            return color;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.i(color);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            color = read.i();
        }
    }
}
