package trs.type;

//import arc.math.Mathf;
import arc.math.Mathf;
import arc.util.Nullable;
import mindustry.entities.pattern.ShootPattern;


public class ShootAlternateSpread extends ShootPattern {
    /** number of barrels used for shooting. */
    public int barrels = 2;
    /** spread between barrels, in world units - not degrees. */
    public float spread = 5f;
    /** offset of barrel to start on */
    public int barrelOffset = 0;
    /** If true, the shoot order is flipped. */
    public boolean mirror = false;
    /** spread between bullets, in degrees. */
    public float spreadDeg = 5f;

    float index;



    public ShootAlternateSpread(int shots, float spread, float spreadDeg){
        this.spread = spread;
        this.spreadDeg = spreadDeg;
        this.shots = shots;
    }

    public ShootAlternateSpread(){};

    @Override
    public void flip(){
        mirror = !mirror;
    }

    @Override
    public void shoot(int totalShots, BulletHandler handler, @Nullable Runnable barrelIncrementer){
        for(int i = 0; i < 1; i++) {
            index = ((totalShots + i + barrelOffset) % barrels) - (barrels-1)/2f;
            for(int j = 0; j < shots; j++) {
                float angleOffset = j * spreadDeg - (shots - 1) * spreadDeg / 2f;
                handler.shoot(index * spread * -Mathf.sign(mirror), 0, angleOffset, firstShotDelay + shotDelay * j);
            }
            if(barrelIncrementer != null) barrelIncrementer.run();
        }
    }
}
