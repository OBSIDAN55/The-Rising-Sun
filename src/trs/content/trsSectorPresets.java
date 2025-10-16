package trs.content;

import mindustry.type.SectorPreset;

public class trsSectorPresets {
    public static SectorPreset
            startSector;

    public static void load(){
        startSector = new SectorPreset("1sector", Planets.fulgera, 0){{
            alwaysUnlocked = true;
            addStartingItems = true;
            captureWave = 10;
            difficulty = 1;
            overrideLaunchDefaults = true;
            noLighting = true;
            startWaveTimeMultiplier = 3f;
        }};

    }
}
