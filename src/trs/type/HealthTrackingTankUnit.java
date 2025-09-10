package trs.type;

import mindustry.gen.TankUnit;
import mindustry.gen.Unitc;
import mindustry.gen.Unit;
import arc.graphics.g2d.Draw;
import mindustry.graphics.Layer;
import arc.graphics.g2d.TextureRegion;
import arc.Core;
import arc.graphics.Color;
import arc.struct.ObjectMap;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.Vars;

/**
 * Утилитный класс для работы с частями танка в зависимости от здоровья
 */
public class HealthTrackingTankUnit {
    
    private static Color debugColor = Color.red; // Начальный цвет - красный
    
    /**
     * Отрисовка отладочного текста на экране
     */
    private static void debugDraw(String message) {
        // Меняем цвет в зависимости от сообщения
        if (message.contains("drawPartsBasedOnHealth called")) {
            debugColor = Color.orange; // Оранжевый - метод вызван
        } else if (message.contains("Unit health:")) {
            debugColor = Color.yellow; // Желтый - здоровье получено
        } else if (message.contains("Creating new UnitAnimState")) {
            debugColor = Color.green; // Зеленый - состояние создано
        } else if (message.contains("parts, health:")) {
            debugColor = Color.cyan; // Голубой - части найдены
        } else if (message.contains("Processing") && message.contains("parts for unit")) {
            debugColor = Color.purple; // Фиолетовый - начинаем обработку частей
        } else if (message.contains("Drawing") || message.contains("Drew part")) {
            debugColor = Color.lime; // Лайм - части отрисовываются
        } else if (message.contains("Successfully found region")) {
            debugColor = Color.white; // Белый - регион найден
        } else if (message.contains("Part region not found")) {
            debugColor = Color.pink; // Розовый - регион не найден
        } else if (message.contains("Attempting to draw part")) {
            debugColor = Color.magenta; // Пурпурный - пытаемся нарисовать часть
        } else if (message.contains("Part ") && message.contains(": ")) {
            debugColor = Color.gray; // Серый - информация о конкретной части
        } else if (message.contains("Found body part:")) {
            debugColor = Color.orange; // Оранжевый - найдена часть корпуса
        } else if (message.contains("Checking part (null unit):")) {
            debugColor = Color.yellow; // Желтый - проверяем часть
        } else if (message.contains("Trying alt")) {
            debugColor = Color.cyan; // Голубой - пробуем альтернативные имена
        } else if (message.contains("Core.atlas loaded:") || message.contains("Atlas size:")) {
            debugColor = Color.white; // Белый - информация об атласе
        } else if (message.contains("No parts found for unit")) {
            debugColor = Color.red; // Красный - части не найдены
        } else if (message.contains("Atlas candidate:")) {
            debugColor = Color.lime; // Лайм - найден кандидат региона в атласе
        }
    }
    
    /**
     * Получить текущий цвет отладки
     */
    public static Color getDebugColor() {
        return debugColor;
    }
    
    /**
     * Статический метод для создания экземпляра юнита
     */
    public static TankUnit create() {
        if (Vars.state != null) {
            debugDraw("Creating HealthTrackingTankUnit!");
            TankUnit unit = TankUnit.create();
            debugDraw("Created TankUnit: " + unit.getClass().getSimpleName());
            return unit;
        }
        return TankUnit.create();
    }
    
    /**
     * Отрисовка частей танка в зависимости от текущего здоровья
     */
    public static void drawPartsBasedOnHealth(Unitc unit) {
        debugDraw("drawPartsBasedOnHealth called for unit: " + unit.type().name);
        
        if (Core.atlas == null) {
            debugDraw("Core.atlas is null, skipping");
            return;
        }
        
        // Проверим, что атлас загружен
        debugDraw("Core.atlas loaded: " + (Core.atlas != null));
        debugDraw("Atlas size: " + (Core.atlas != null ? Core.atlas.getRegions().size : "null"));
        
        if (!(unit instanceof TankUnit)) {
            debugDraw("Unit is not TankUnit, skipping");
            return;
        }

        float healthPercent = unit.healthf();
        debugDraw("Unit health: " + healthPercent);

        // Инициализируем/получаем состояние анимации для конкретного юнита
        UnitAnimState state = animStates.get(unit);
        if (state == null) {
            debugDraw("Creating new UnitAnimState for unit " + unit.type().name);
            state = new UnitAnimState(healthPercent, 0); // partCount не используется в новом конструкторе
            animStates.put(unit, state);
        }

        // Получаем реальное количество частей из состояния
        int partCount = state.parts.length;
        debugDraw("Unit " + unit.type().name + " has " + partCount + " parts, health: " + healthPercent);
        if (partCount == 0) {
            debugDraw("No parts found for unit " + unit.type().name);
            // Fallback: scan atlas for any regions that look like disaster parts and draw a few
            int drawn = 0;
            for (TextureRegion reg : Core.atlas.getRegions()) {
                if (reg != null && reg.found()) {
                    String rn = reg.toString();
                    if (rn != null && rn.toLowerCase().contains("disaster") && rn.toLowerCase().contains("part")) {
                        debugDraw("Atlas candidate: " + rn);
                        float offx = (drawn - 1) * 40f;
                        float offy = 40f;
                        Draw.z(Layer.groundUnit + 0.13f);
                        Draw.rect(reg, unit.x() + offx, unit.y() + offy, unit.rotation() - 90f);
                        drawn++;
                        if (drawn >= 3) break;
                    }
                }
            }
            return;
        }
        
        // Дополнительная отладка - проверим каждую часть
        for (int i = 0; i < partCount; i++) {
            String partName = state.partNames[i];
            boolean isWeaponPart = state.isWeaponPart[i];
            debugDraw("Part " + i + ": " + partName + " (weapon: " + isWeaponPart + ")");
        }

        // Рисуем на слое поверх корпуса
        Draw.z(Layer.groundUnit + 0.11f);

        // Система отваливания частей по порогам здоровья (случайно)
        // Проверяем пороги: 80%, 60%, 40%, 20%
        int expectedDetached = 0;
        if (healthPercent < 0.8f) expectedDetached = 1;
        if (healthPercent < 0.6f) expectedDetached = 2;
        if (healthPercent < 0.4f) expectedDetached = 3;
        if (healthPercent < 0.2f) expectedDetached = 4;
        
        // Отваливаем части до достижения нужного количества
        while (state.detachedCount < expectedDetached && state.detachedCount < partCount) {
            // Выбираем случайную неотвалившуюся часть
            int[] availableParts = new int[partCount - state.detachedCount];
            int availableIndex = 0;
            
            for (int i = 0; i < partCount; i++) {
                if (!state.parts[i].detached) {
                    availableParts[availableIndex] = i;
                    availableIndex++;
                }
            }
            
            if (availableIndex > 0) {
                int randomIndex = Mathf.random(availableIndex - 1);
                int partIndex = availableParts[randomIndex];
                
                // Отваливаем выбранную часть
                PartAnim anim = state.parts[partIndex];
                anim.detached = true;
                anim.t = 0f;
                anim.offx = anim.offy = 0f;
                
                // Случайные параметры разлёта (медленно, но на 2-4 блока)
                long seed = (unit.id() * 37L) ^ (partIndex * 127L) ^ (System.currentTimeMillis() & 0xFFFF);
                float angle = Mathf.randomSeed(seed, 0f, 360f);
                float speed = Mathf.randomSeed(seed + 19, 3f, 6f); // скорость для полета на 2-4 блока
                anim.vx = Mathf.cosDeg(angle) * speed;
                anim.vy = Mathf.sinDeg(angle) * speed;
                anim.spinVel = Mathf.randomSeed(seed + 11, -15f, 15f); // очень медленное вращение
                
                state.detachedCount++;
                debugDraw("Detached part at " + (int)(healthPercent * 100) + "% HP: " + state.partNames[partIndex]);
            } else {
                break; // нет доступных частей
            }
        }
        
        // Обрабатываем все части
        debugDraw("Processing " + partCount + " parts for unit " + unit.type().name);
        for (int i = 0; i < partCount; i++) {
            String partName = state.partNames[i];
            boolean isWeaponPart = state.isWeaponPart[i];
            PartAnim anim = state.parts[i];
            
            // Дополнительная защита от null
            if (partName == null || anim == null) {
                debugDraw("Warning: null part at index " + i);
                continue;
            }
            
            debugDraw("Processing part " + i + ": " + partName + " (weapon: " + isWeaponPart + ", detached: " + anim.detached + ")");
            
            if (!anim.detached) {
                // Обычная привязанная часть
                if (isWeaponPart) {
                    // Части оружия поворачиваются с башней
                    debugDraw("Drawing weapon part: " + partName);
                    drawWeaponPart(unit, partName);
                } else {
                    // Части корпуса не поворачиваются
                    debugDraw("Drawing body part: " + partName);
                    drawPart(unit, partName);
                }
            } else if (anim.t < detachDuration) {
                // Анимация отлёта (очень медленно и контролируемо)
                float dt = Time.delta;
                anim.t += dt;
                float damp = 0.95f; // сильное замедление (части быстро останавливаются)
                anim.vx *= Mathf.pow(damp, dt);
                anim.vy *= Mathf.pow(damp, dt);
                anim.offx += anim.vx * dt * 0.3f; // очень медленное движение
                anim.offy += anim.vy * dt * 0.3f;
                anim.spin += anim.spinVel * dt;
                anim.spinVel *= Mathf.pow(0.95f, dt); // быстрое замедление вращения

                float life = Mathf.clamp(anim.t / detachDuration);
                float alpha = 1f - life;
                // Поднимаем слой отрисовки выше корпуса
                Draw.z(Layer.groundUnit + 0.2f);
                Color prev = Draw.getColor().cpy();
                Draw.color(prev.r, prev.g, prev.b, alpha);
                float rot = unit.rotation() - 90f + anim.spin;
                drawPartOffset(unit, partName, anim.offx, anim.offy, rot);
                Draw.color(prev);
            }
        }

        Draw.reset();
    }
    
    /**
     * Отрисовка конкретной части танка
     */
    private static void drawPart(Unitc unit, String partName) {
        try {
            debugDraw("Attempting to draw part: " + partName);
            
            // Попробуем разные способы получения региона
            TextureRegion region = null;
            
            // Список вариантов имён региона
            String[] candidates = new String[]{
                unit.type().name + partName,
                "disaster" + partName,
                unit.type().name + partName + "-outline",
                (partName.startsWith("-") ? partName.substring(1) : partName),
                (partName.startsWith("-") ? partName.substring(1) : partName) + "-outline"
            };

            for (String candidate : candidates) {
                region = Core.atlas.find(candidate);
                debugDraw("Trying region: " + candidate + " = " + (region != null && region.found()));
                if (region != null && region.found()) break;
            }
            
            if (region != null && region.found()) {
                debugDraw("Successfully found region for: " + partName);
                // 2) Анимация попадания как у корпуса: используем hitTime юнита для mixcol
                float flash = 0f;
                if (unit instanceof Unit u) {
                    flash = u.hitTime;
                }
                // clamp флэша
                if (flash < 0f) flash = 0f; if (flash > 1f) flash = 1f;

                // Рисуем основную часть с учётом флэша
                Draw.z(Layer.groundUnit + 0.11f);
                if (flash > 0f) Draw.mixcol(Color.white, flash);
                Draw.rect(region, unit.x(), unit.y(), unit.rotation() - 90);
                if (flash > 0f) Draw.mixcol();
                debugDraw("Drew part: " + partName + " at (" + unit.x() + ", " + unit.y() + ")");
            } else {
                debugDraw("Part region not found for: " + partName);
            }
        } catch (Exception e) {
            debugDraw("Error drawing part " + partName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Отрисовка части оружия (поворачивается с башней)
     */
    private static void drawWeaponPart(Unitc unit, String partName) {
        try {
            TextureRegion region = Core.atlas.find(unit.type().name + partName);
            if (region == null || !region.found()) {
                region = Core.atlas.find("disaster" + partName);
            }
            
            if (region != null && region.found()) {
                // Анимация попадания как у корпуса
                float flash = 0f;
                if (unit instanceof Unit u) {
                    flash = u.hitTime;
                }
                if (flash < 0f) flash = 0f; 
                if (flash > 1f) flash = 1f;

                Draw.z(Layer.groundUnit + 0.11f);
                if (flash > 0f) Draw.mixcol(Color.white, flash);
                
                // Части оружия поворачиваются с башней (используем rotation юнита)
                Draw.rect(region, unit.x(), unit.y(), unit.rotation() - 90);
                
                if (flash > 0f) Draw.mixcol();
            } else {
                System.out.println("Weapon part region not found for: " + partName);
            }
        } catch (Exception e) {
            System.out.println("Error drawing weapon part " + partName + ": " + e.getMessage());
        }
    }

    // Вспомогательное рисование части со смещением и произвольным поворотом (для отлёта)
    private static void drawPartOffset(Unitc unit, String partName, float offx, float offy, float rotation) {
        try {
            // Используем расширенный список кандидатов, как и в drawPart
            String[] candidates = new String[]{
                unit.type().name + partName,
                "disaster" + partName,
                unit.type().name + partName + "-outline",
                (partName.startsWith("-") ? partName.substring(1) : partName),
                (partName.startsWith("-") ? partName.substring(1) : partName) + "-outline"
            };
            TextureRegion region = null;
            for (String candidate : candidates) {
                region = Core.atlas.find(candidate);
                if (region != null && region.found()) break;
            }
            if (region != null && region.found()) {
                Draw.z(Layer.groundUnit + 0.11f);
                Draw.rect(region, unit.x() + offx, unit.y() + offy, rotation);
            }
        } catch (Exception ignored) {}
    }

    // ===== Хранение состояния анимации отлёта частей =====
    private static final float detachDuration = 300f; // около 5с при 60fps (увеличено время)
    private static final ObjectMap<Unitc, UnitAnimState> animStates = new ObjectMap<>();
    
    
    /**
     * Проверяет существование части у юнита
     */
    private static boolean isPartExists(Unitc unit, String partName) {
        if (unit == null) {
            // Если unit null, проверяем только через disaster
            String fullName = "disaster" + partName;
            TextureRegion region = Core.atlas.find(fullName);
            boolean found = region != null && region.found();
            debugDraw("Checking part (null unit): " + fullName + " = " + found);
            if (!found) {
                // Попробуем другие варианты
                String altName1 = "disaster" + partName + ".png";
                String altName2 = partName.substring(1); // убираем "-"
                debugDraw("Trying alt1: " + altName1 + " = " + (Core.atlas.find(altName1) != null && Core.atlas.find(altName1).found()));
                debugDraw("Trying alt2: " + altName2 + " = " + (Core.atlas.find(altName2) != null && Core.atlas.find(altName2).found()));
            }
            return found;
        }
        
        String base1 = unit.type().name + partName;            // e.g. disaster-part0
        String base2 = "disaster" + partName;                 // fallback
        String base3 = unit.type().name + partName + "-outline"; // if outline suffix packed
        String base4 = partName.startsWith("-") ? partName.substring(1) : partName; // part0
        String base5 = base4 + "-outline";

        TextureRegion region = Core.atlas.find(base1);
        if (region == null || !region.found()) region = Core.atlas.find(base2);
        if (region == null || !region.found()) region = Core.atlas.find(base3);
        if (region == null || !region.found()) region = Core.atlas.find(base4);
        if (region == null || !region.found()) region = Core.atlas.find(base5);
        boolean found = region != null && region.found();
        debugDraw("Checking part: [" + base1 + ", " + base2 + ", " + base3 + ", " + base4 + ", " + base5 + "] => " + found);
        return found;
    }
    

    private static class UnitAnimState {
        final PartAnim[] parts;
        final String[] partNames; // Имена всех частей
        final boolean[] isWeaponPart; // Является ли часть частью оружия
        int detachedCount = 0; // Количество отвалившихся частей
        
        UnitAnimState(float initialHealth, int partCount) { 
            // Динамически собираем все части из атласа по именам
            java.util.ArrayList<String> body = new java.util.ArrayList<>();
            java.util.ArrayList<String> weapon = new java.util.ArrayList<>();

            for (TextureRegion reg : Core.atlas.getRegions()) {
                if (reg == null || !reg.found()) continue;
                String rn = reg.toString();
                if (rn == null) continue;
                String lower = rn.toLowerCase();
                if (!lower.contains("disaster") || !lower.contains("part")) continue;
                if (lower.endsWith("-outline")) continue;

                int idx = lower.indexOf("disaster");
                String suffix = rn.substring(idx + "disaster".length());
                if (!suffix.startsWith("-")) continue; // ожидаем формат disaster-...

                if (suffix.contains("-weapon")) weapon.add(suffix);
                else body.add(suffix);
            }

            // Если есть базовое оружие без -part
            TextureRegion wepBase = Core.atlas.find("disaster-weapon");
            if (wepBase != null && wepBase.found() && !weapon.contains("-weapon")) weapon.add("-weapon");

            // Сортируем для детерминированности
            java.util.Collections.sort(body);
            java.util.Collections.sort(weapon);

            int actualPartCount = body.size() + weapon.size();

            this.parts = new PartAnim[actualPartCount];
            this.partNames = new String[actualPartCount];
            this.isWeaponPart = new boolean[actualPartCount];

            int index = 0;
            for (String s : body) {
                this.partNames[index] = s;
                this.isWeaponPart[index] = false;
                this.parts[index] = new PartAnim();
                index++;
                debugDraw("Found body part: " + s);
            }
            for (String s : weapon) {
                this.partNames[index] = s;
                this.isWeaponPart[index] = true;
                this.parts[index] = new PartAnim();
                index++;
                debugDraw("Found weapon part: " + s);
            }

            debugDraw("UnitAnimState initialized with " + actualPartCount + " actual parts");
        }
    }

    private static class PartAnim {
        boolean detached;
        float t;            // прошедшее время
        float offx, offy;   // смещение
        float vx, vy;       // скорость
        float spin, spinVel;// вращение и его скорость
    }
}
