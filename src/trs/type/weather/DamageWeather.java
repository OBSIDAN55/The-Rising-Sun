package trs.type.weather;

import mindustry.entities.Damage;
import mindustry.game.Team;
import mindustry.gen.WeatherState;
import mindustry.type.weather.ParticleWeather;

import java.util.Random;

public class DamageWeather extends ParticleWeather {
    public float damage = 50f;
    Random random = new Random();
    public int x0 = random.nextInt(501);
    public int y0 = random.nextInt(501);
    public int x1 = random.nextInt(501);
    public int y1 = random.nextInt(501);
    public int x2 = random.nextInt(501);
    public int y2 = random.nextInt(501);
    public int x3 = random.nextInt(501);
    public int y3 = random.nextInt(501);
    public int x4 = random.nextInt(501);
    public int y4 = random.nextInt(501);
    public DamageWeather(String name){
        super(name);
    }
    @Override
    public void update(WeatherState state){

        x0 = random.nextInt(501);
        y0 = random.nextInt(501);
        x1 = random.nextInt(501);
        y1 = random.nextInt(501);
        x2 = random.nextInt(501);
        y2 = random.nextInt(501);
        x3 = random.nextInt(501);
        y3 = random.nextInt(501);
        x4 = random.nextInt(501);
        y4 = random.nextInt(501);
        Damage.tileDamage(Team.crux,x0,y0,5f,damage);
        Damage.tileDamage(Team.crux,x1,y1,5f,damage);
        Damage.tileDamage(Team.crux,x2,y2,5f,damage);
        Damage.tileDamage(Team.crux,x3,y3,5f,damage);
        Damage.tileDamage(Team.crux,x4,y4,5f,damage);



    }

}
