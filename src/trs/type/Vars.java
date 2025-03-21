package trs.type;

import arc.Core;
import arc.Events;
import arc.assets.Loadable;
import arc.files.Fi;
import arc.struct.Seq;
import arc.struct.StringMap;
import arc.util.Log;
import arc.util.OS;
import arc.util.Structs;
import mindustry.ai.*;
import mindustry.async.AsyncCore;
import mindustry.core.ContentLoader;
import mindustry.core.FileTree;
import mindustry.core.GameState;
import mindustry.core.Version;
import mindustry.editor.MapEditor;
import mindustry.entities.EntityCollisions;
import mindustry.game.EventType;
import mindustry.game.FogControl;
import mindustry.game.Universe;
import mindustry.game.Waves;
import mindustry.gen.Groups;
import mindustry.graphics.CacheLayer;
import mindustry.logic.GlobalVars;
import mindustry.maps.Map;
import mindustry.maps.Maps;
import mindustry.mod.Mods;
import mindustry.net.BeControl;
import mindustry.ui.dialogs.LanguageDialog;
import mindustry.world.Tile;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;

import java.util.Arrays;
import java.util.Locale;

import static arc.Core.settings;

public class Vars extends mindustry.Vars implements Loadable {

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

    public static void load() {
        loadSettings();

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
    }

}
