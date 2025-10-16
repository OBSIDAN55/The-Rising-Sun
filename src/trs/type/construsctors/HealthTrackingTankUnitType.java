package trs.type.construsctors;

import arc.graphics.Color;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.type.unit.TankUnitType;

/**
 * Кастомный TankUnitType с поддержкой отслеживания здоровья для управления частями
 */
public class HealthTrackingTankUnitType extends TankUnitType {
    
    public HealthTrackingTankUnitType(String name) {
        super(name);
        this.constructor = HealthTrackingTankUnit::create;
    }
    
    @Override
    public void draw(mindustry.gen.Unit unit) {
        // Вызываем стандартную отрисовку для корпуса, гусениц и оружия
        super.draw(unit);
        
        // Затем рисуем только части корпуса поверх
        drawBodyPartsOnly(unit);
        
        // Отладочная информация (только один квадрат)
        Color debugColor = HealthTrackingTankUnit.getDebugColor();
        Drawf.square(unit.x, unit.y, 100f, debugColor);
    }
    
    private void drawBodyPartsOnly(Unit unit) {
        // Рисуем ТОЛЬКО части корпуса с анимацией отваливания, НЕ части оружия
        if (unit instanceof mindustry.gen.TankUnit) {
            float healthPercent = unit.healthf();
            
            // Инициализируем состояние анимации если его нет
            var state = HealthTrackingTankUnit.getAnimState(unit);
            if (state == null) {
                // Создаем состояние анимации
                HealthTrackingTankUnit.initAnimState(unit);
                state = HealthTrackingTankUnit.getAnimState(unit);
                if (state == null) return;
            }
            
            int partCount = state.parts.length;
            if (partCount == 0) return;
            
            // Рисуем на слое под башней
            arc.graphics.g2d.Draw.z(mindustry.graphics.Layer.groundUnit + 0.05f);
            
            // Адаптивная система отваливания частей
            int expectedDetached = arc.math.Mathf.clamp((int)arc.math.Mathf.floor((1f - healthPercent) * partCount + 1e-4f), 0, partCount);
            
            // Обрабатываем все части корпуса
            for (int i = 0; i < partCount; i++) {
                String partName = state.partNames[i];
                var anim = state.parts[i];
                
                // Проверяем нужно ли отвалить эту часть
                if (expectedDetached > 0 && !anim.detached) {
                    anim.detached = true;
                    expectedDetached--;
                }
                
                if (!anim.detached) {
                    // Обычная привязанная часть корпуса
                    drawBodyPart(unit, partName);
                } else if (anim.t < HealthTrackingTankUnit.getDetachDuration()) {
                    // Анимация отлёта
                    float dt = arc.util.Time.delta;
                    anim.t += dt;
                    float damp = 0.95f;
                    anim.vx *= arc.math.Mathf.pow(damp, dt);
                    anim.vy *= arc.math.Mathf.pow(damp, dt);
                    anim.offx += anim.vx * dt * 0.3f;
                    anim.offy += anim.vy * dt * 0.3f;
                    anim.spin += anim.spinVel * dt;
                    anim.spinVel *= arc.math.Mathf.pow(0.95f, dt);

                    float life = arc.math.Mathf.clamp(anim.t / HealthTrackingTankUnit.getDetachDuration());
                    float alpha = 1f - life;
                    arc.graphics.g2d.Draw.z(mindustry.graphics.Layer.groundUnit + 0.1f);
                    arc.graphics.Color prev = arc.graphics.g2d.Draw.getColor().cpy();
                    arc.graphics.g2d.Draw.color(prev.r, prev.g, prev.b, alpha);
                    float rot = unit.rotation() - 90f + anim.spin;
                    drawBodyPartOffset(unit, partName, anim.offx, anim.offy, rot);
                    arc.graphics.g2d.Draw.color(prev);
                }
            }
        }
    }
    
    private void drawBodyPart(Unit unit, String partName) {
        try {
            var region = arc.Core.atlas.find(unit.type().name + partName);
            if (region == null || !region.found()) {
                region = arc.Core.atlas.find("disaster" + partName);
            }
            
            if (region != null && region.found()) {
                float flash = 0f;
                if (unit instanceof Unit) {
                    flash = ((Unit)unit).hitTime;
                }
                if (flash < 0f) flash = 0f;
                if (flash > 1f) flash = 1f;

                arc.graphics.g2d.Draw.z(mindustry.graphics.Layer.groundUnit + 0.05f);
                if (flash > 0f) arc.graphics.g2d.Draw.mixcol(arc.graphics.Color.white, flash);
                arc.graphics.g2d.Draw.rect(region, unit.x(), unit.y(), unit.rotation() - 90);
                if (flash > 0f) arc.graphics.g2d.Draw.mixcol();
            }
        } catch (Exception e) {
            // Игнорируем ошибки
        }
    }
    
    private void drawBodyPartOffset(Unit unit, String partName, float offx, float offy, float rotation) {
        try {
            var region = arc.Core.atlas.find(unit.type().name + partName);
            if (region == null || !region.found()) {
                region = arc.Core.atlas.find("disaster" + partName);
            }
            if (region != null && region.found()) {
                arc.graphics.g2d.Draw.z(mindustry.graphics.Layer.groundUnit + 0.1f);
                arc.graphics.g2d.Draw.rect(region, unit.x() + offx, unit.y() + offy, rotation);
            }
        } catch (Exception ignored) {}
    }
    
    
    // Убираем drawWeapon - его нет в TankUnitType
}