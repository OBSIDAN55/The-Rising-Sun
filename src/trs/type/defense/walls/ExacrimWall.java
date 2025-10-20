package trs.type.defense.walls;

import arc.util.Time;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;

public class ExacrimWall extends Wall {

    public DrawBlock drawer = new DrawDefault();

    public ExacrimWall(String name) {
        super(name);
        update = true;
    }
    
    @Override
    public void load(){
        super.load();
        drawer.load(this);
    }

    public class ExacrimWallBuild extends WallBuild {


        public float progress;
        float warmupTimer;

        @Override
        public void updateTile(){
            super.updateTile();
            this.warmupTimer += delta();
            if(this.warmupTimer >= 60f){
                this.progress = 0f;
                this.warmupTimer = 0f;
            }
        }

        public void draw(){
            drawer.draw(this);
        }

        @Override
        public float warmup(){
            return 1;
        }
        @Override
        public float progress(){
            return progress;
        }
        @Override
        public float totalProgress() {
            return Time.time;
        }
    }
}
