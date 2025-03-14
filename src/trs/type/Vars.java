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
    public static Stat powerUsePhase;

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
        powerUsePhase = new Stat("powerusephase",StatCat.power);
    }

}
