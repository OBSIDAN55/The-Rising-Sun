package trs.type;

import arc.graphics.Color;

public class Team extends mindustry.game.Team {

    protected Team(int id, String name, Color color){
        super(id, name, color);
        this.name = name;
        this.color.set(color);

        if(id < 9) baseTeams[id] = this;
        all[id] = this;

    }
    public static final Team[] baseTeams = new Team[9];
    public final static Team
            remaining = new Team(7, "remaining", Color.valueOf("CB7251FF")),
            krizators = new Team(8, "krizators", Color.valueOf("53D0B4FF"));



}
