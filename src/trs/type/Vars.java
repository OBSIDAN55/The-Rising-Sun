package trs.type;

import arc.Core;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;


public class Vars {

    public static String ChronosName;
    public static String ArchName;
    public static String AkronixName;
    public static String PhoenixName;

    public static String ChronosDesc;
    public static String ArchDesc;
    public static String AkronixDesc;
    public static String PhoenixDesc;

    public static Stat fractionName;
    public static Stat fractionDescription;
    public static Stat healRadius;
    public static Stat buildRadius;
    public static Stat heatRange;
    public static Stat heatDamage;
    public static Stat powerUsePhase;

    public static Stat basePowerRange;
    public static Stat basePowerConnections;
    public static Stat farPowerRange;
    public static Stat farPowerConnections;
    public static Stat farModePowerGeneration;
    public static Stat closePowerRange;
    public static Stat closePowerConnections;

    public static StatCat baseMode;
    public static StatCat farMode;
    public static StatCat closeMode;




    public static void load() {
        ChronosName = Core.bundle.get("ChronosName");
        ArchName = Core.bundle.get("ArchName");
        AkronixName = Core.bundle.get("AkronixName");
        PhoenixName = Core.bundle.get("PhoenixName");

        ChronosDesc = Core.bundle.get("ChronosDesc");
        ArchDesc = Core.bundle.get("ArchDesc");
        AkronixDesc = Core.bundle.get("AkronixDesc");
        PhoenixDesc = Core.bundle.get("PhoenixDesc");

        fractionName = new Stat("fractionname", StatCat.general);
        fractionDescription = new Stat("fractiondescription",StatCat.general);
        healRadius = new Stat("healradius", StatCat.function);
        buildRadius = new Stat("buildradius",StatCat.function);
        heatRange = new Stat("heatrange", StatCat.function);
        heatDamage = new Stat("heatdamage",StatCat.function);
        powerUsePhase = new Stat("powerusephase",StatCat.power);





        baseMode = new StatCat("baseMode");
        basePowerRange = new Stat("basePowerRange", baseMode);
        basePowerConnections = new Stat("basePowerConnections", baseMode);

        farMode = new StatCat("farMode");
        farPowerRange = new Stat("farPowerRange", farMode);
        farPowerConnections = new Stat("farPowerConnections", farMode);
        farModePowerGeneration = new Stat("farModePowerGeneration", farMode);

        closeMode = new StatCat("closeMode");
        closePowerRange = new Stat("closePowerRange", closeMode);
        closePowerConnections = new Stat("closePowerConnections", closeMode);


    }
}