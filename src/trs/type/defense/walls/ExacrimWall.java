package trs.type.defense.walls;

import mindustry.world.blocks.defense.Wall;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;

public class ExacrimWall extends Wall {

    public DrawBlock drawer = new DrawDefault();

    public ExacrimWall(String name) {
        super(name);
    }
    public class ExacrimWallBuild extends WallBuild {


        public void draw(){
            super.draw();
        }
    }
}
