package trs.content;

import mindustry.type.StatusEffect;

public class trsStatusEffects {

    public static StatusEffect brokenMechanism, repair, intoTheFlames, deathOfSystem, rotting, protocolBerserker;

    public static void load(){
        brokenMechanism = new StatusEffect("broken-mechanism") {{
            healthMultiplier = 0.9f;
            speedMultiplier = 0.7f;
        }};
        repair = new StatusEffect("repair") {{
            damage = -0.45f;
        }};
        intoTheFlames = new StatusEffect("into-the-flames") {{
            healthMultiplier = 0.95f;
            speedMultiplier = 0.9f;
            damage = 29;
        }};
        deathOfSystem = new StatusEffect("death-of-system") {{
            disarm = true;
            speedMultiplier = 0f;
            damageMultiplier = 0f;
            healthMultiplier = 0.9f;
        }};
        rotting = new StatusEffect("rotting") {{
            speedMultiplier = 0.8f;
            damage = 36;
        }};
        protocolBerserker = new StatusEffect("protocol-berserker") {{
            speedMultiplier = 1.3f;
            damageMultiplier = 1.2f;
        }};
    }

}
