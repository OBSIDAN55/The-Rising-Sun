package trs.type.weather;

import java.util.Random;
import mindustry.entities.Damage;
import mindustry.game.Team;
import mindustry.gen.WeatherState;
import mindustry.type.weather.RainWeather;

public class thunderstorm extends RainWeather {

    public thunderstorm(String name) {
        super(name);
    }
    public int time;
    public float damage = 500f;
    Random random = new Random();
    public int x0 = random.nextInt(501);
    public int y0 = random.nextInt(501);

    @Override
    public void update(WeatherState state) {

        time++;
        if(time> 30){
            time = 0;
            x0 = random.nextInt(501);
            y0 = random.nextInt(501);
            Damage.tileDamage(Team.crux,x0,y0,5f,damage);

        }
    }
}
